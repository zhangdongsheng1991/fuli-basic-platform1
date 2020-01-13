package com.fuli.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuli.cloud.commons.PageResult;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.utils.PublicUtil;
import com.fuli.cloud.dto.*;
import com.fuli.cloud.mapper.SystemUserGrantCompanyMapper;
import com.fuli.cloud.model.SystemUserGrantCompanyDO;
import com.fuli.cloud.model.TokenUser;
import com.fuli.cloud.service.SystemUserGrantCompanyService;
import com.fuli.cloud.vo.ListByUserIdGrantCompanyVO;
import com.fuli.cloud.vo.ListUserGrantByCompanyIdVO;
import com.fuli.cloud.vo.SystemUserGrantCompanyVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import java.util.*;

/**
 * @author chenyi
 * @date 2019/12/4
 */
@Slf4j
@Service
public class SystemUserGrantCompanyServiceImpl extends ServiceImpl<SystemUserGrantCompanyMapper, SystemUserGrantCompanyDO> implements SystemUserGrantCompanyService {


    /**
     * 查询授权企业权限信息
     * @param qryDto 查询参数类
     * @return
     */
    @Override
    public Result getGrantCompanyInfo(SystemUserGrantCompanyQryDto qryDto){
        PageHelper.startPage(qryDto.getPageNum(),qryDto.getPageSize());
        List<SystemUserGrantCompanyVo> grantCompanyList = baseMapper.getGrantCompanyInfo(qryDto);
        PageInfo pageInfo = new PageInfo(grantCompanyList);
        PageResult<SystemUserGrantCompanyVo> pageResult = PageResult.getPageResult(pageInfo);
        return Result.succeed(pageResult);
    }

    @Override
    public Result addGrantCompany(AddSystemUserGrantCompanyDTO dto, TokenUser user) {
        /** 新增管理人，企业与管理人及模块都是相乘的关系*/
        Date date = new Date();
        String operationAccount = user.getUserAccount();
        /** 循环模块*/
        for (ModuleMapDTO mDto : dto.getModuleIds()){
            if (! PublicUtil.isNotNull(mDto.getModuleId())){
                return Result.failed("业务线id不能为空");
            }else if (StringUtils.isBlank(mDto.getModuleName())){
                return Result.failed("业务线名称不能为空");
            }
            /** 循环企业*/
            for (CompanyMapDTO cDto : dto.getCompanyIds()){
                if (! PublicUtil.isNotNull(cDto.getCompanyId())){
                    return Result.failed("企业id不能为空");
                }else if (StringUtils.isBlank(cDto.getCompanyName())){
                    return Result.failed("企业名称不能为空");
                }

                /** 未绑定过的用户id集合*/
                Set<Integer> userIds = new HashSet<>();
                dto.getUserIds().forEach(p -> userIds.add(p));

                /** 批量插入，先找出已存在的*/
                List<SystemUserGrantCompanyDO> list = baseMapper.listByCompanyIdAndModuleIdAndUserIds(cDto.getCompanyId(), mDto.getModuleId(), userIds);
                if (! CollectionUtils.isEmpty(list)){
                    for (Integer uid : dto.getUserIds()){
                        for (SystemUserGrantCompanyDO su : list){
                            if (uid.intValue() == su.getUserId().intValue()){
                                userIds.remove(uid);
                            }
                        }
                    }
                }
                if (! CollectionUtils.isEmpty(userIds)){
                    try {
                        /** 批量插入*/
                        SystemUserGrantCompanyDO sdo = new SystemUserGrantCompanyDO();
                        sdo.setModuleId(mDto.getModuleId());
                        sdo.setModuleName(mDto.getModuleName());
                        sdo.setCompanyId(cDto.getCompanyId());
                        sdo.setCompanyName(cDto.getCompanyName());
                        sdo.setCreateTime(date);
                        sdo.setUpdateTime(date);
                        sdo.setOperationAccount(operationAccount);
                        baseMapper.batchInsert(sdo,userIds);
                    }catch (Exception e){
                        log.error("插入企业管理人重复- 跳过",e);
                        continue;
                    }
                }
            }
        }
        return Result.succeed();

        /** 循环管理人 并最终插入数据*/
        //                for (Integer uid : dto.getUserIds()){
        //                    if (! PublicUtil.isNotNull(uid)){
        //                        return Result.failed("用户id不能为空");
        //                    }
        //                    /** 表中有唯一索引，重复数据插入报错放过继续*/
        //                    try{
        //                        SystemUserGrantCompanyDO sdo = new SystemUserGrantCompanyDO();
        //                        sdo.setModuleId(mDto.getModuleId());
        //                        sdo.setModuleName(mDto.getModuleName());
        //                        sdo.setCompanyId(cDto.getCompanyId());
        //                        sdo.setCompanyName(cDto.getCompanyName());
        //                        sdo.setUserId(uid);
        //                        sdo.setCreateTime(date);
        //                        sdo.setUpdateTime(date);
        //                        sdo.setOperationAccount(operationAccount);
        //                        baseMapper.insert(sdo);
        //                    }catch (Exception e){
        //                        log.error("插入企业管理人重复- 跳过");
        //                        continue;
        //                    }
        //                }
    }

    @Override
    public Result updateGrantCompany(UpdateSystemUserGrantCompanyDTO dto, TokenUser user) {
        /** 编辑管理人，先清空再添加*/
        Date date = new Date();
        String operationAccount = user.getUserAccount();

        /** 清空原企业绑定的所有用户*/
        QueryWrapper<SystemUserGrantCompanyDO> queryWrapper = new QueryWrapper();
        queryWrapper.eq("company_id",dto.getCompanyId()).eq("module_id",dto.getModuleId());
        baseMapper.delete(queryWrapper);

        /** 再新增所有用户为该企业管理员*/
        for (Integer uid : dto.getUserIds()){
            if (! PublicUtil.isNotNull(uid)){
                return Result.failed("用户id非法");
            }
            /** 表中有唯一索引，重复数据插入报错放过继续*/
            try{
                SystemUserGrantCompanyDO sdo = new SystemUserGrantCompanyDO();
                sdo.setCompanyId(dto.getCompanyId());
                sdo.setCompanyName(dto.getCompanyName());
                sdo.setModuleId(dto.getModuleId());
                sdo.setModuleName(dto.getModuleName());
                sdo.setUserId(uid);
                sdo.setUpdateTime(date);
                sdo.setCreateTime(date);
                sdo.setOperationAccount(operationAccount);
                baseMapper.insert(sdo);
            }catch (Exception e){
                log.error("插入企业管理人重复- 跳过");
                continue;
            }
        }
        return Result.succeed("编辑授权成功");
    }

    /**
     * 根据用户id获取我管理的企业列表
     * @author      WFZ
     * @param 	    dto : 入参
     * @return      Result
     * @date        2019/12/4 15:49
     */
    @Override
    public Result listByUserId(ListCompanyManagerByUserIdDTO dto){
        if (0==dto.getPageNum()){
            dto.setPageSize(100000);
        }
        Page page = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        /** type=1 表示查询主体企业*/
        List<ListByUserIdGrantCompanyVO> list =  new ArrayList<>();
        if (dto.getType() != null && 1==dto.getType().intValue()){
            list = baseMapper.listMasterCompanyByUserId(dto);
        }else {
            list = baseMapper.listClientCompanyByMasterCompanyId(dto);
        }
        log.info("薪发放获取我管理的企业列表结果result:{}",list.size());
        return Result.succeed(PageResult.getPageResult(page));
    }

    @Override
    public Result listUserGrantByCompanyId(ListUserGrantByCompanyIdDTO dto) {
        /*Page page = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());*/
        List<ListUserGrantByCompanyIdVO> list = baseMapper.listUserGrantByCompanyId(dto);
        return Result.succeed(list);
    }

    @Override
    public boolean batchDelete(@NotEmpty List<Integer> ids) {
        return false;
    }
}
