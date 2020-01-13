package com.fuli.cloud.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.model.SaAsCompanyServiceDO;
import com.fuli.cloud.model.SystemRoleUserDO;
import com.fuli.cloud.vo.service.SaAsServiceModuleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 *  SaaS企业开通的服务中间表
 * @author WFZ 2019-07-29
 */
@Mapper
@Repository
public interface SaAsCompanyServiceMapper extends BaseMapper<SaAsCompanyServiceDO> {

    /**
     * 获取服务（企业已开通 / 第三方服务）
     * @author      WFZ
     * @param 	    companyId : 企业id
     * @param 	    isOther : 是否第三方服务， 不为null表示找第三方服务
     * @param 	    ids : 服务id
     * @return      Result
     * @date        2019/7/30 14:37
     */
    List<SaAsServiceModuleVO> listServiceModule(@Param("companyId")Serializable companyId , @Param("isOther")Integer isOther, @Param("ids") Set<Integer> ids);

}
