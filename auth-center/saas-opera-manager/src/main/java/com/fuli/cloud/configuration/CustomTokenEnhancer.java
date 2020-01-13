package com.fuli.cloud.configuration;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
* @Description:    自定义token生成携带的信息
* @Author:         WFZ
* @CreateDate:     2019/9/10 10:52
* @Version:        1.0
*/
public class CustomTokenEnhancer implements TokenEnhancer {
  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
    /** 获取登录信息*/
    Authentication authentication = oAuth2Authentication.getUserAuthentication();
    final Map<String, Object> additionalInfo = new HashMap<>(16);
    if (authentication != null){
      Object user = authentication.getPrincipal();
      Map userMap = JSONObject.parseObject(JSONObject.toJSONString(user));
      additionalInfo.put("code",0);
      additionalInfo.put("msg","ok");
      additionalInfo.put("id",userMap.get("userId"));
      additionalInfo.put("phone", userMap.get("phone"));
      additionalInfo.put("clientId",userMap.get("clientId"));
      additionalInfo.put("loginTime",System.currentTimeMillis());
      additionalInfo.put("realName",userMap.get("realName"));
      additionalInfo.put("userAccount",userMap.get("userAccount"));
      additionalInfo.put("currentCompanyId",userMap.get("currentCompanyId"));
      additionalInfo.put("employeeId",userMap.get("employeeId"));
      additionalInfo.put("companyCreditCode",userMap.get("companyCreditCode"));
    }else{
      additionalInfo.put("phone", 0);
      additionalInfo.put("id",0);
    }
    ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInfo);
    return oAuth2AccessToken;
  }

}
