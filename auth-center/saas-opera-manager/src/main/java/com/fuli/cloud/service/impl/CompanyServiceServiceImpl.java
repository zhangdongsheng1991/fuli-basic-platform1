package com.fuli.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.base.BaseServiceImpl;
import com.fuli.cloud.commons.utils.OrderInfoUtil;
import com.fuli.cloud.commons.utils.PublicUtil;
import com.fuli.cloud.dto.service.OpenServiceDTO;
import com.fuli.cloud.feign.BasicServerFeign;
import com.fuli.cloud.feign.MiddleServerFeign;
import com.fuli.cloud.feign.SalaryFeign;
import com.fuli.cloud.mapper.SaAsCompanyServiceMapper;
import com.fuli.cloud.mapper.SaAsServiceModuleMapper;
import com.fuli.cloud.model.SaAsCompanyServiceDO;
import com.fuli.cloud.model.SaAsServiceModuleDO;
import com.fuli.cloud.service.CompanyServiceService;
import com.fuli.cloud.vo.service.ListServiceVO;
import com.fuli.cloud.vo.service.SaAsServiceModuleVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * @Description:    企业开通的服务
 * @Author:         WFZ
 * @CreateDate:     2019/7/30 10:53
 * @Version:        1.0
*/
@Slf4j
@Service
public class CompanyServiceServiceImpl extends BaseServiceImpl<SaAsCompanyServiceMapper, SaAsCompanyServiceDO>  implements CompanyServiceService {

    @Autowired
    private BasicServerFeign basicServerFeign;
    @Autowired
    private SaAsServiceModuleMapper saAsServiceModuleMapper;
    @Autowired
    private SalaryFeign salaryFeign;
    @Autowired
    private MiddleServerFeign middleServerFeign;


    /**
     * 展示服务模块，已开通和未开通
     *
     * @param companyId ： 企业id
     * @return Result
     * @author WFZ
     * @date 2019/7/30 14:45
     */
    @Override
    public Result listServiceModule(Serializable companyId) {
        if(PublicUtil.isEmpty(companyId)){
            return Result.failed(CodeEnum.PARAM_ERROR.getCode(),"企业id不能为空");
        }
        /** 获取企业已经开通的服务*/
        List<SaAsServiceModuleVO> voList = baseMapper.listServiceModule(companyId, null,null);
        Set<Integer> ids = new HashSet<>();
        voList.forEach(p -> ids.add(p.getId()));

        /** 获取剩余的未开通的第三方服务*/
        List<SaAsServiceModuleVO> otherList = baseMapper.listServiceModule(null, 1, ids);

        ListServiceVO result = new ListServiceVO();
        result.setOpenedList(voList);
        result.setNotOpenList(otherList);
        return Result.succeed(result);
    }

    /**
     * 企业开通或关闭服务
     *
     * @param dto
     * @return Result
     * @author WFZ
     * @date 2019/7/30 15:40
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result onOrOffService(OpenServiceDTO dto) {
        /** 判断模块id是否有效*/
        SaAsServiceModuleDO moduleDO = saAsServiceModuleMapper.selectById(dto.getId());
        if (moduleDO == null){
            return Result.failed(CodeEnum.PARAM_ERROR.getCode(),"服务不存在");
        }
        /** 判断是否已经开通*/
        SaAsCompanyServiceDO serviceDO = getByCompanyIdAndServiceId(dto.getCompanyId(), dto.getId());
        /** 开通服务*/
        if ("1" .equals(dto.getState())){
            if (serviceDO != null){
                if (serviceDO.getState().intValue() ==1){
                    return Result.failed(CodeEnum.SERVICE_REPEATED_OPENING_EXCEPTION);
                }else{
                    /** 为了保证薪发放那边同步，先调用薪发放那边*/
                    Result result = informSalary(dto.getCompanyId(), moduleDO, dto.getState());
                    if (! PublicUtil.isResultSuccess(result)){
                        return result;
                    }
                    serviceDO.setState(1);
                    serviceDO.setCreateTime(new Date());
                    baseMapper.updateById(serviceDO);
                }
            }else {
                dto.setId(moduleDO.getModuleId().toString());
                try{
                    /** 为了保证薪发放那边同步，先调用薪发放那边*/
                    Result result = informSalary(dto.getCompanyId(), moduleDO, dto.getState());
                    if (! PublicUtil.isResultSuccess(result)){
                        return result;
                    }
                    /** 先开通权限模块*/
                    result = basicServerFeign.openUp(dto);
                    if (PublicUtil.isResultSuccess(result)){
                        /** 开通服务*/
                        serviceDO = new SaAsCompanyServiceDO();
                        serviceDO.setServiceId(moduleDO.getId());
                        serviceDO.setCompanyId(Long.valueOf(dto.getCompanyId()));
                        serviceDO.setCreateTime(new Date());
                        baseMapper.insert(serviceDO);

                        /** 如果是开通薪发放服务， 要默认创建一个空的审批流*/
                        if ("11".equals(moduleDO.getId().toString())){
                            log.info("第一次开通薪发放服务掉middle服务创建一个空审批流companyId:{}",dto.getCompanyId());
                            Result forXFF = middleServerFeign.addApprovalForXFF(Long.valueOf(dto.getCompanyId()));
                            log.info("创建空审批流结果result:{}",forXFF);
                        }
                    }else {
                        return result;
                    }
                }catch (Exception e){
                    log.error("basic-server开通权限模块出错", e);
                    return Result.failed(CodeEnum.GLOBAL_EXCEPTION);
                }
            }
            return Result.succeed();
        }else if("2" .equals(dto.getState())){
            if (serviceDO == null){
                return Result.failed(30211032, "企业未开通该服务不能修改状态");
            }
            /** 为了保证薪发放那边同步，先调用薪发放那边*/
            Result result = informSalary(dto.getCompanyId(), moduleDO, dto.getState());
            if (! PublicUtil.isResultSuccess(result)){
                return result;
            }
            serviceDO.setState(2);
            serviceDO.setShutTime(new Date());
            baseMapper.updateById(serviceDO);
            return Result.succeed();
        }
        return Result.status(false);
    }

    /**
     * 判断企业是否开通服务
     * @author      WFZ
     * @param
     * @return      Result
     * @date        2019/7/30 19:06
     */
    public SaAsCompanyServiceDO getByCompanyIdAndServiceId(String companyId,String serviceId){
        QueryWrapper<SaAsCompanyServiceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply(" company_id="+ companyId +" AND service_id="+ serviceId +"  LIMIT 1");
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 通知薪发放系统
     *      SRYJ 收入预结
     *      XFYJ 消费预结
     *
     * @author      WFZ
     * @param       companyId
     * @param       moduleDO
     * @param       serviceStatus
     * @return      Result
     * @date        2019/8/27 18:05
     */
    public Result informSalary(String companyId , SaAsServiceModuleDO moduleDO , String serviceStatus){
        if (StringUtils.isNotBlank(moduleDO.getServiceType())){
            /** 查询企业信息*/
            Result result = basicServerFeign.getCompanyById(Long.valueOf(companyId));
            if (PublicUtil.isResultSuccess(result) && PublicUtil.isNotEmpty(result.getData())){
                Map data = (Map)result.getData();
                if (PublicUtil.isNotEmpty(data.get("companyId"))){
                    TreeMap<String, Object> salaryMap = new TreeMap<String, Object>();
                    salaryMap.put("appId","100100101");
                    salaryMap.put("companyCreditCode",data.get("businessLicenseCode")+"");
                    salaryMap.put("corpNo",companyId);
                    salaryMap.put("describe",moduleDO.getName());
                    salaryMap.put("serviceStatus",serviceStatus.equals("1")?"1":"0");
                    salaryMap.put("serviceType",moduleDO.getServiceType());
                    String signature = OrderInfoUtil.signMsg(salaryMap);
                    salaryMap.put("signature",signature);
                    log.info("扣减服务通知薪发放参数：salaryMap="+ salaryMap);
                    try{
                        log.info("扣减服务通知薪发放结果：result="+ result);
                        result = salaryFeign.noticeAddConsum(salaryMap);
                        return result;
                    }catch (Exception e){
                        log.error("扣减服务通知薪发放报错",e);
                    }
                }
            }
            return result;
        }
        return Result.succeed();
    }

    /**
     * 查询已开通的有效服务
     * @param companyId 企业id
     * @return
     */
    @Override
    public Result listCompanyEnableService(Serializable companyId){
        List<SaAsServiceModuleVO> voList = baseMapper.listServiceModule(companyId, null,null);
        return Result.succeed(voList);
    }

}
