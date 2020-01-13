package com.fuli.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.model.SystemModuleDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 *  模块菜单表
 * @author xq 2019-07-30
 */
@Mapper
@Repository
public interface SystemModuleMapper extends BaseMapper<SystemModuleDO> {



}
