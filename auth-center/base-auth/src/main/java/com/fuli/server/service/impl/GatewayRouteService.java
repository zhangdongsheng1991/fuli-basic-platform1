package com.fuli.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuli.server.exception.FuliAssert;
import com.fuli.server.mapper.GatewayRouteMapper;
import com.fuli.server.model.dto.GatewayRouteDTO;
import com.fuli.server.model.entity.GatewayRouteDO;
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
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("service_id", gatewayRouteDto.getServiceId());
        GatewayRouteDO gatewayRouteDO = gatewayRouteMapper.selectOne(wrapper);
        if (gatewayRouteDO != null) {
            FuliAssert.unauthorized(20013, "配置失败，该服务路由已配置:"+ JSON.toJSONString(gatewayRouteDO));
        }
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
