package com.fuli.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.server.model.entity.GatewayRouteDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GatewayRouteMapper extends BaseMapper<GatewayRouteDO> {

}
