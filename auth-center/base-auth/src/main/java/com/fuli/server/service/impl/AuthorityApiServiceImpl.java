package com.fuli.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuli.auth.common.constant.RedisKeyConstant;
import com.fuli.auth.common.model.GatherApi;
import com.fuli.server.exception.FuliAssert;
import com.fuli.server.mapper.AuthorityApiMapper;
import com.fuli.server.mapper.AuthorityRelateApiMapper;
import com.fuli.server.mapper.GatewayRouteMapper;
import com.fuli.server.model.entity.BaseApiDO;
import com.fuli.server.model.entity.GatewayRouteDO;
import com.fuli.server.model.vo.AuthorityApiVO;
import com.fuli.server.service.AuthorityApiService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author create by XYJ
 * @Date 2019/8/16 18:21
 **/
@Service
public class AuthorityApiServiceImpl implements AuthorityApiService {

    @Autowired
    private AuthorityApiMapper authorityApiMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    private GatewayRouteMapper gatewayRouteMapper;
    @Autowired
    private AuthorityRelateApiMapper authorityRelateApiMapper;

    /**
     * 根据groud查询api列表
     *
     * @param groudId
     * @return
     */
    @Override
    public List<AuthorityApiVO> getApisByGroudId(String groudId) {
        List<AuthorityApiVO> apis = null;
        if (redisTemplate.hasKey(RedisKeyConstant.BASE_AUTH_AUTHORITY_API_GROUP + groudId)) {
            apis = (List<AuthorityApiVO>) redisTemplate.opsForValue().get(RedisKeyConstant.BASE_AUTH_AUTHORITY_API_GROUP + groudId);
        } else {
            apis = authorityApiMapper.getApisByGroudId(groudId);
            cacheAuthorityApi(groudId, apis);
        }
        return apis;
    }

    /**
     * 权限组绑定api
     *
     * @param groupId
     * @param apiIds
     */
    @Override
    public Integer authorityBindApi(String groupId, String[] apiIds, boolean firstRemove) {

        if (firstRemove) {
            authorityRelateApiMapper.delete(null);
        }


        //1、更新数据库
        int apisC = authorityApiMapper.authorityBindApi(groupId, apiIds);
        //2、更新redis
        List<AuthorityApiVO> apis = authorityApiMapper.getApisByGroudId(groupId);
        cacheAuthorityApi(groupId, apis);
        return apisC;
    }

    /**
     * 新增采集的api
     *
     * @param apis
     * @return
     */
    @Override
    @Transactional
    public List<BaseApiDO> saveGatherApis(List<GatherApi> apis) throws Exception {

        QueryWrapper qw = new QueryWrapper();
        qw.eq("service_id", apis.get(0).getServiceId());
        //已存在的api
        List<BaseApiDO> serviceApis = authorityApiMapper.selectList(qw);
        //路由
        GatewayRouteDO gatewayRoute = gatewayRouteMapper.selectOne(qw);
        if (null == gatewayRoute) {
            FuliAssert.unauthorized(20010, "请先配置网关路由");
        }

        //GatherApi 转换成BaseApiDO
        List<BaseApiDO> newApis = new ArrayList<>();
        for (GatherApi api : apis) {
            BaseApiDO baseApiDO = new BaseApiDO();
            BeanUtils.copyProperties(api, baseApiDO);
            //加上网关路由前缀
            baseApiDO.setPath(gatewayRoute.getPath().replace("/**", baseApiDO.getPath()));
            newApis.add(baseApiDO);
        }
        if (serviceApis != null && serviceApis.size() > 0) {
            //排除重复api
            for (int i = 0; i < serviceApis.size(); i++) {
                for (int j = 0; j < newApis.size(); j++) {
                    if (serviceApis.get(i).getPath().equals(newApis.get(j).getPath())
                            && serviceApis.get(i).getServiceId().equals(newApis.get(j).getServiceId())) {
                        newApis.remove(j);
                        continue;
                    }
                }
            }
        }

        if (newApis != null && newApis.size() > 0) {
            //批量新增api
            batchInsertApi(newApis);
            String[] apiArr = new String[newApis.size()];
            for (int i = 0; i < apiArr.length; i++) {
                apiArr[i] = newApis.get(i).getApiId().toString();
            }
            //新增的api绑定默认分组original
            authorityBindApi("1400", apiArr, false);
        }
        return newApis;

    }

    /**
     * 缓存权限组api
     *
     * @param groupId
     * @param apis
     */
    private void cacheAuthorityApi(String groupId, List<AuthorityApiVO> apis) {
        redisTemplate.opsForValue().set(RedisKeyConstant.BASE_AUTH_AUTHORITY_API_GROUP + groupId, apis);
    }

    /**
     * 批量插入api
     *
     * @param apis
     */
    public void batchInsertApi(List<BaseApiDO> apis) {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        AuthorityApiMapper mapper = session.getMapper(AuthorityApiMapper.class);
        try {
            for (int i = 0; i < apis.size(); i++) {
                mapper.insert(apis.get(i));
            }
            session.commit();
            session.clearCache();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }
}

