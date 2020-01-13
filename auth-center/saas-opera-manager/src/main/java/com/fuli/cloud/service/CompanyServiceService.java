package com.fuli.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.dto.CompanyMessageDto;
import com.fuli.cloud.dto.service.OpenServiceDTO;
import com.fuli.cloud.model.CompanyMessageDO;
import com.fuli.cloud.model.SaAsCompanyServiceDO;
import com.fuli.cloud.model.SystemUserDO;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:    企业开通的服务
 * @Author:         WFZ
 * @CreateDate:     2019/7/30 10:53
 * @Version:        1.0
*/
public interface CompanyServiceService extends IService<SaAsCompanyServiceDO> {

    /**
     * 展示服务模块，已开通和未开通
     * @author      WFZ
     * @param 	    companyId ： 企业id
     * @return      Result
     * @date        2019/7/30 14:45
     */
    Result listServiceModule(Serializable companyId);

    /**
     * 企业开通或关闭服务
     * @author      WFZ
     * @param 	    dto
     * @return      Result
     * @date        2019/7/30 15:40
     */
    Result onOrOffService(OpenServiceDTO dto);

    /**
     * 查询已开通的有效服务
     * @param companyId 企业id
     * @return
     */
    Result listCompanyEnableService(Serializable companyId);
}
