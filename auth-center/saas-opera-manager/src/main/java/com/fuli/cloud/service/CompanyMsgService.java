package com.fuli.cloud.service;

import com.fuli.cloud.commons.Result;
import com.fuli.cloud.dto.CompanyMessageDto;
import com.fuli.cloud.model.CompanyMessageDO;
import com.fuli.cloud.vo.CompanyMessageVO;

import java.util.Date;

/**
 * 企业留言服务接口
 */
public interface CompanyMsgService {
    /**
     * 获取企业留言信息
     * @param companyMessageDto
     * @return
     */
    Result getCompanyMsgInfo(CompanyMessageDto companyMessageDto);

    /**
     * 查询企业留言详情
     * @param id ID
     * @return
     */
    CompanyMessageVO getCompanyMsgDetail(Long id);

    /**
     * 处理反馈企业留言信息
     * @param companyMessageDO
     * @return
     */
    Result dealCompanyMsg(CompanyMessageDO companyMessageDO);

    /**
     * 获取新增企业留言消息个数
     * @param logoutTime 上次登出时间
     * @return 新增消息个数
     */
    Integer getNewAddMsgCount(Date logoutTime);
}
