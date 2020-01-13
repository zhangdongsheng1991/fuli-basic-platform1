package com.fuli.cloud.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.model.SaAsCompanyServiceDO;
import com.fuli.cloud.model.SaAsServiceModuleDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 *  现有第三方服务模块记录
 * @author WFZ 2019-07-29
 */
@Mapper
@Repository
public interface SaAsServiceModuleMapper extends BaseMapper<SaAsServiceModuleDO> {



}
