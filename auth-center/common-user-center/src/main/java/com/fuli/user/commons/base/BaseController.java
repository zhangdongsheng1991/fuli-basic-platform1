package com.fuli.user.commons.base;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuli.user.commons.CodeEnum;
import com.fuli.user.constant.CommonConstant;
import com.fuli.user.exception.BusinessException;
import com.fuli.user.vo.TokenUserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;


/**
 * @Description:    基础控制层
 * @Author:         WFZ
 * @CreateDate:     2019/4/23 19:53
 * @Version:        1.0
 */
@Component
@Slf4j
public class BaseController {

	@Autowired
	HttpServletRequest request;
	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * 获取当前用户的id
	 * @author      FZ
	 * @param
	 * @return		登录状态正常返回用户id,否则返回0
	 * @date        2019/4/23 19:53
	 */
	protected Long getAppUserId(){
		String authorization =  request.getHeader(CommonConstant.HEAD_USER_INFO_KEY);
		try{
			String decode = URLDecoder.decode(authorization, "utf-8");
			if (StringUtils.isNoneBlank(decode)){
				Map map = JSONObject.parseObject(decode);
				if (! map.isEmpty()){
					return Long.valueOf(map.get("id").toString());
				}
			}
		}catch (Exception e){
			log.error("获取当前用户id-----long类型", e.getMessage());
		}
		throw new BusinessException(CodeEnum.ACCESS_TOKEN_NULLITY);
	}

	/**
	 * 获取当前用户信息（APP用户）
	 * @author      WFZ
	 * @param
	 * @return      Result
	 * @date        2019/8/8 10:43
	 */
	protected TokenUserVO getAppUserInfo(){
		String authorization = request.getHeader(CommonConstant.HEAD_USER_INFO_KEY);
		if (StringUtils.isBlank(authorization)){
			log.info("获取当前用户的消息头为：{}",authorization);
			throw new BusinessException(CodeEnum.ACCESS_TOKEN_NULLITY);
		}

		try {
			authorization = URLDecoder.decode(authorization, "utf-8");
			if (StringUtils.isBlank(authorization)){
				throw new BusinessException(CodeEnum.ACCESS_TOKEN_NULLITY);
			}
			TokenUserVO tokenUser = objectMapper.readValue(authorization, TokenUserVO.class);
			if (tokenUser.getId() == null){
				throw new BusinessException(CodeEnum.ACCESS_TOKEN_NULLITY);
			}
			return tokenUser;
		}catch (IOException e){
			log.error("获取token中的数据异常: {}",e.getMessage());
			throw new BusinessException(CodeEnum.ACCESS_TOKEN_NULLITY);
		}
	}

	/**
	 * @Description:(解析消息头的手机号)
	 * @author      fengjing
	 * @date        2019/6/12 11:02
	 */
	public String getLoginUserPhone(){
		/**获取请求头中用户手机号信息*/
		String authorization = request.getHeader(CommonConstant.X_CLIENT_TOKEN_USER);
		if (StringUtils.isNotEmpty(authorization)){
			/** 根据authentication获取登录的账号  在根据手机号查询企业信息 */
			try {
				Map map = (Map)JSONObject.parseObject(URLDecoder.decode(authorization, "utf-8"));
				return map.get("phone").toString();
			}catch (Exception e){
				log.error("获取当前用户手机号："+e.getMessage());
			}
		}
		return "";
	}
}
