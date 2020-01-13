package com.fuli.cloud.service;

import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.base.BaseService;
import com.fuli.cloud.dto.*;
import com.fuli.cloud.model.SystemUserGrantCompanyDO;
import com.fuli.cloud.model.TokenUser;

/**
 * @author chenyi
 * @date 2019/12/4
 */
public interface SystemUserGrantCompanyService extends BaseService<SystemUserGrantCompanyDO> {

    /**
     * 查询授权企业权限信息
     * @param qryDto 查询参数类
     * @return
     */
    Result getGrantCompanyInfo(SystemUserGrantCompanyQryDto qryDto);

    /**
     * 新增企业管理员
     * @author      WFZ
     * @param 	    dto : 入参
     * @param 	    user : 登录用户信息
     * @return      Result
     * @date        2019/12/4 15:49
     */
    Result addGrantCompany(AddSystemUserGrantCompanyDTO dto, TokenUser user);

    /**
     * 新增企业管理员
     * @author      WFZ
     * @param 	    dto : 入参
     * @param 	    user : 登录用户信息
     * @return      Result
     * @date        2019/12/4 15:49
     */
    Result updateGrantCompany(UpdateSystemUserGrantCompanyDTO dto, TokenUser user);

    /**
     * 根据用户id获取我管理的企业列表
     * @author      WFZ
     * @param 	    dto : 入参
     * @return      Result
     * @date        2019/12/4 15:49
     */
    Result listByUserId(ListCompanyManagerByUserIdDTO dto);

    /**
     * 根据企业id获取已添加的管理人员列表
     * @author      WFZ
     * @param 	    dto : 请求参数
     * @return      Result
     * @date        2019/12/4 15:49
     */
    Result listUserGrantByCompanyId(ListUserGrantByCompanyIdDTO dto);
}
