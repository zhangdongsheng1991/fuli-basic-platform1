package com.fuli.cloud.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.model.SystemRoleDO;
import com.fuli.cloud.model.SystemRoleUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 *  角色表
 * @author WFZ 2019-07-29
 */
@Mapper
@Repository
public interface SystemRoleUserMapper extends BaseMapper<SystemRoleUserDO> {



}
