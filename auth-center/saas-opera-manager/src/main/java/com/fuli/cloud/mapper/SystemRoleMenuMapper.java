package com.fuli.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.model.SystemRoleMenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 *  角色菜单关联表
 * @author xq 2019-07-29
 */
@Mapper
@Repository
public interface SystemRoleMenuMapper extends BaseMapper<SystemRoleMenuDO> {



}
