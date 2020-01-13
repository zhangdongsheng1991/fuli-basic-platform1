package com.fuli.server.dynaroute.service;

import com.fuli.server.dynaroute.model.dto.GatewayRouteDTO;
import com.fuli.server.dynaroute.model.entity.GatewayRouteDO;
import com.fuli.server.dynaroute.mapper.GatewayRouteMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author create by XYJ
 * @Date 2019/9/3 18:14
 **/
@Service
public class GatewayRouteService {
    @Autowired
    private GatewayRouteMapper gatewayRouteMapper;

    /**
     * 新增路由
     *
     * @param gatewayRouteDto
     * @return
     */
    public Integer add(GatewayRouteDTO gatewayRouteDto) {
        GatewayRouteDO gatewayRoute = new GatewayRouteDO();
        BeanUtils.copyProperties(gatewayRouteDto, gatewayRoute);
        return gatewayRouteMapper.insert(gatewayRoute);
    }

    /**
     * 更新路由
     *
     * @param gatewayRouteDto
     * @return
     */
    public Integer update(GatewayRouteDTO gatewayRouteDto) {
        GatewayRouteDO gatewayRoute = new GatewayRouteDO();
        BeanUtils.copyProperties(gatewayRouteDto, gatewayRoute);
        return gatewayRouteMapper.updateById(gatewayRoute);
    }

    /**
     * 删除路由
     *
     * @param id
     * @return
     */
    public Integer delete(String id) {
        return gatewayRouteMapper.deleteById(Long.parseLong(id));
    }
}
