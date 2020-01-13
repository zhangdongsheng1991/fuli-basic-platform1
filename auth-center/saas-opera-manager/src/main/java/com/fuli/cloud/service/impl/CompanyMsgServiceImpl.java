package com.fuli.cloud.service.impl;

import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.PageResult;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.exception.ServiceException;
import com.fuli.cloud.constant.OperateConstant;
import com.fuli.cloud.dto.CompanyMessageDto;
import com.fuli.cloud.mapper.CompanyMessageMapper;
import com.fuli.cloud.model.CompanyMessageDO;
import com.fuli.cloud.service.CompanyMsgService;
import com.fuli.cloud.vo.CompanyMessageVO;
import com.fuli.cloud.vo.DepartmentVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CompanyMsgServiceImpl implements CompanyMsgService {

    @Resource
    CompanyMessageMapper companyMessageMapper;

    /**
     * 获取企业留言信息
     * @param companyMessageDto
     * @return
     */
    @Override
    public Result getCompanyMsgInfo(CompanyMessageDto companyMessageDto){
        log.info("搜索参数companyMessageDto:{},时间startTime:{},endTime:{}",companyMessageDto,companyMessageDto.getStartTime(),companyMessageDto.getEndTime());
        PageHelper.startPage(companyMessageDto.getPageNum(),companyMessageDto.getPageSize());
        List<CompanyMessageVO> msgList = companyMessageMapper.getCompanyMsgInfo(companyMessageDto);
        PageInfo pageInfo = new PageInfo(msgList);
        return Result.succeed(PageResult.getPageResult(pageInfo,null));
    }

    /**
     * 查询企业留言详情
     * @param id ID
     * @return
     */
    @Override
    public CompanyMessageVO getCompanyMsgDetail(Long id){
        CompanyMessageVO msg = companyMessageMapper.getCompanyMsgDetail(id);
        return msg;
    }

    /**
     * 处理反馈企业留言信息
     * @param companyMessageDO
     * @return
     */
    @Transactional
    @Override
    public Result dealCompanyMsg(CompanyMessageDO companyMessageDO){
        //查询该消息
        CompanyMessageDO messageDO = companyMessageMapper.selectById(companyMessageDO.getId());
        if(messageDO == null){
            throw new ServiceException(CodeEnum.MSG_NOTFOUND);
        }

        //校验状态
        if(OperateConstant.MESSAGE_STATE_UNDEAL != messageDO.getState()){
            throw new ServiceException(CodeEnum.MSG_STATUS_ERROR);
        }

        //设置处理时间
        companyMessageDO.setDealTime(new Date());
        int result = companyMessageMapper.updateById(companyMessageDO);
        if(result > 0){
            return Result.succeed();
        }
        return Result.failed();
    }

    /**
     * 获取新增企业留言消息
     * @param logoutTime 上次登出时间
     * @return 新增消息个数
     */
    @Override
    public Integer getNewAddMsgCount(Date logoutTime){
        return companyMessageMapper.getNewAddMsgCount(logoutTime);
    }
}
