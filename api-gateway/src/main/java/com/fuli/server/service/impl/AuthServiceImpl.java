package com.fuli.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fuli.auth.common.constant.RedisKeyConstant;
import com.fuli.auth.common.model.CustomAuthority;
import com.fuli.auth.common.utils.JsonUtils;
import com.fuli.server.constants.GatewayCommonConstant;
import com.fuli.server.dao.URLWhiteDao;
import com.fuli.server.model.entity.URLWhiteDO;
import com.fuli.server.model.vo.AuthorityApiVO;
import com.fuli.server.service.AuthService;
import com.fuli.server.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Author create by XYJ
 * @Date 2019/5/9
 **/
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired(required = false)
    private URLWhiteDao urlWhiteDao;
    @Autowired
    private RedisUtils redisUtils;

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean hasPermission(OAuth2Authentication authentication, String reqUrl, String method) {
        String grantType = authentication.getOAuth2Request().getGrantType();
        //1、服务api权限验证
        boolean appAuth = hasAPIAuthority(authentication, reqUrl);
        //2、客户端模式，返回api权限验证结果
        if (GatewayCommonConstant.CLIENT_CREDENTIALS.equals(grantType)) {
            log.info("客户端模式验证：{}",appAuth);
            return appAuth;
        }
        //3、密码模式，验证用户权限
        if (appAuth){
            if (GatewayCommonConstant.PASSWORD.equals(grantType)) {
                try {
                    Object principal = authentication.getPrincipal();
                    String clientId = authentication.getOAuth2Request().getClientId();
                    Map user = JsonUtils.jsonToPojo(JSONObject.toJSONString(principal),Map.class);
                    if (GatewayCommonConstant.saAsPORTAL.equals(clientId)){
                        // SaaS门户
                        return saAsPortalPermission(user.get("userId")+"",user.get("currentCompanyId")+"",reqUrl);
                    }else if (GatewayCommonConstant.saAsOperation.equals(clientId)){
                        // SaaS运营
                        return saAsOperationPermission(user.get("userId")+"",reqUrl);
                    }else {
                        return true;
                    }
                }catch (Exception e){
                    log.error("个人鉴权",e);
                    return true;
                }
            }else {
                return true;
            }
        }
        log.info("服务管理无权限");
        return false;
    }

    /**
     * SaaS门户个人鉴权
     * @author      WFZ
     * @param
     * @return      Result
     * @date        2019/9/11 15:36
     */
    public boolean saAsPortalPermission(String userId , String companyId , String url){
        /** 首先判断该url是否需要校验 */
        if (redisUtils.hasKey(GatewayCommonConstant.PORTAL_MENU)){
            // 菜单id
            Set<String> menuId = new HashSet<>();
            List<Map<String, String>> mapList = JSONObject.parseObject(redisUtils.get(GatewayCommonConstant.PORTAL_MENU).toString(), List.class);
            for (Map<String, String> map : mapList){
                if (! StringUtils.isEmpty(map.get(GatewayCommonConstant.URL))){
                    String str[] = map.get(GatewayCommonConstant.URL).split(",");
                    for (String s : str){
                        if ( pathMatcher.match(url , s) ) {
                            menuId.add(String.valueOf(map.get("id")));
                        }
                    }
                }
            }
            if (CollectionUtils.isEmpty(menuId)){
                return true;
            }
            String key =GatewayCommonConstant.PORTAL_USER_ROLE + userId +"|"+ companyId;
            /** 获取用户权限*/
            Object o = redisUtils.get(key);
            if (o == null){
                log.info("用户无角色，鉴权不通过");
                return false;
            }
            // 记录用户拥有的所有菜单id
            Set<String> menuIds = new HashSet<>();
            /** 判断用户有无超级管理员角色*/
            String[] str = o.toString().split(",");
            for (String s : str){
                if (redisUtils.hasKey(GatewayCommonConstant.PORTAL_ADMIN_ROLE + s)){
                    return true;
                }
                Object obj = redisUtils.get(GatewayCommonConstant.PORTAL_ROLE_MENU + s);
                if (obj != null){
                    String[] split = obj.toString().split(",");
                    for (String st : split){
                        menuIds.add(st);
                    }
                }
            }
            /** 无超级管理员角色就对比菜单id*/
            for (String s : menuIds){
                for (String b : menuId){
                    if (b.equals(s)){
                        return true;
                    }
                }
            }
            log.info("用户无此菜单权限，鉴权不通过。菜单id:{}",menuId);
            return false;
        }
        return true;
    }


    /**
     * SaaS运营个人鉴权
     * @author      WFZ
     * @param
     * @return      Result
     * @date        2019/9/11 15:36
     */
    public boolean saAsOperationPermission(String userId , String url){
        /** 首先判断该url是否需要校验 */
        if (redisUtils.hasKey(GatewayCommonConstant.OPERATION_MENU)){
            // 菜单id
            Set<String> menuId = new HashSet<>();
            List<Map<String, String>> mapList = JSONObject.parseObject(redisUtils.get(GatewayCommonConstant.OPERATION_MENU).toString(), List.class);
            for (Map<String, String> map : mapList){
                if (! StringUtils.isEmpty(map.get(GatewayCommonConstant.URL))){
                    String str[] = map.get(GatewayCommonConstant.URL).split(",");
                    for (String s : str){
                        if ( pathMatcher.match(url , s) ) {
                            menuId.add(String.valueOf(map.get("id")));
                        }
                    }
                }
            }
            if (CollectionUtils.isEmpty(menuId)){
                return true;
            }

            /** 获取用户权限*/
            Object object = redisUtils.get(GatewayCommonConstant.OPERATION_USER_ROLE + userId);
            if (object == null){
                log.info("用户无角色，鉴权不通过.");
                return false;
            }
            // 记录用户拥有的所有菜单id
            Set<String> menuIds = new HashSet<>();
            /** 判断用户有无超级管理员角色*/
            String[] str = object.toString().split(",");
            for (String s : str){
                if ("1".equals(s)){
                    return true;
                }
                Object obj = redisUtils.get(GatewayCommonConstant.OPERATION_ROLE_MENU + s);
                if (obj != null){
                    String[] split = obj.toString().split(",");
                    for (String st : split){
                        menuIds.add(st);
                    }
                }
            }

            /** 无超级管理员角色就对比菜单id*/
            for (String st : menuIds){
                for (String b : menuId){
                    if (b.equals(st)){
                        return true;
                    }
                }
            }
            log.info("用户无此菜单权限，鉴权不通过菜单id:{}",menuId);
            return false;
        }
        return true;
    }


    @Override
    public boolean isWhiteURL(String reqUrl) {
        List<URLWhiteDO> whiteUrls = urlWhiteDao.allURLWhite();
        // url匹配器
        for (int i = 0; i < whiteUrls.size(); i++) {
            if (pathMatcher.match(whiteUrls.get(i).getIgnoreUrl(), reqUrl)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Jwt getJwt(String token) {
        // log.info("jwt token is ++++" + token);
        return JwtHelper.decode(token);
    }

    /**
     * 从redis查询客户端权限
     * RedisKeyConstant.BASE_AUTH_AUTHORITY_API_GROUD + groudId
     *
     * @param groupId
     * @return
     */
    private List<AuthorityApiVO> getApisByGroupId(String groupId) {
        List<AuthorityApiVO> apis = null;
        Boolean hasKey = redisUtils.hasKey(RedisKeyConstant.BASE_AUTH_AUTHORITY_API_GROUP + groupId);
        if (hasKey) {
            apis = (List<AuthorityApiVO>) redisUtils.get(RedisKeyConstant.BASE_AUTH_AUTHORITY_API_GROUP + groupId);
        }
        return apis;
    }

    /**
     * 服务api权限验证
     *
     * @param authentication 拥有的权限
     * @param reqUrl         请求rul
     * @return 验证结果
     */
    private boolean hasAPIAuthority(OAuth2Authentication authentication, String reqUrl) {
        //1、先鉴定客户端权限
        Collection collection = authentication.getOAuth2Request().getAuthorities();
        if (collection != null && collection.size()>0){
            try {
                for (Object authorities : collection) {
                    CustomAuthority authority = JSON.parseObject(JSON.toJSONString(authorities), CustomAuthority.class);;
                    List<AuthorityApiVO> authorityApis = getApisByGroupId(authority.getAuthorityGroupId());
                    for (AuthorityApiVO authorityApiVO : authorityApis) {
                        if (pathMatcher.match(authorityApiVO.getApi(), reqUrl)) {
                            return true;
                        }
                    }
                }
                return false;
            }catch (Exception e){
                log.error("服务权限验证",e);
            }
        }
        return true;
    }
}
