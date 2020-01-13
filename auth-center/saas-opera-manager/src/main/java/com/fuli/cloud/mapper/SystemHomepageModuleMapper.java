package com.fuli.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.model.SystemHomepageModuleDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 *  首页服务模块记录 (工作台模块权限)
 * @author xq 2019-07-29
 */
@Repository
@Mapper
public interface SystemHomepageModuleMapper extends BaseMapper<SystemHomepageModuleDO> {



}
