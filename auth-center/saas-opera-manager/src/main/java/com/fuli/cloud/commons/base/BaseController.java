package com.fuli.cloud.commons.base;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.exception.ServiceException;
import com.fuli.cloud.constant.CommonConstant;
import com.fuli.cloud.model.TokenUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Map;


/**
 * @Description:    基础控制层
 * @Author:         WFZ
 * @CreateDate:     2019/4/23 19:53
 * @Version:        1.0
*/
@Slf4j
@Component
public class BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

	/** 头部封装用户信息 KEY*/
	private final String HEAD_USER_INFO_KEY= "x-client-token-user";

	@Autowired
	HttpServletRequest request;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * 获取当前用户的id (后台)
	 * @author      FZ
	 * @param
	 * @return		登录状态正常返回用户id,否则返回0
	 * @date        2019/4/23 19:53
	 */
	protected Integer getSystemUserId(){
		String authorization =  request.getHeader(CommonConstant.HEAD_USER_INFO_KEY);
		try{
			if (StringUtils.isNoneBlank(authorization)){
				Map map = JSONObject.parseObject(URLDecoder.decode(authorization,"utf-8"));
				if (! map.isEmpty()){
					return Integer.valueOf(map.get("id").toString());
				}
			}
		}catch (Exception e){
			LOGGER.info("获取当前用户id-----Integer类型");
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取企业saas平台的用户信息
	 */
	protected TokenUser getSystemUser(){
		String authorization = request.getHeader(HEAD_USER_INFO_KEY);
		if (StringUtils.isBlank(authorization)){
			throw new ServiceException(CodeEnum.ILLEGAL_ACCESS_TOKEN);
		}

		try {
			authorization = URLDecoder.decode(authorization, "utf-8");
			if (StringUtils.isBlank(authorization)){
				log.info("authorization**************"+authorization);
				throw new ServiceException(CodeEnum.ILLEGAL_ACCESS_TOKEN);
			}
			TokenUser tokenUser = objectMapper.readValue(authorization, TokenUser.class);
			if (tokenUser.getId() == null){
				throw new ServiceException(CodeEnum.ILLEGAL_ACCESS_TOKEN);
			}
			return tokenUser;
		}catch (IOException e){
			log.error("获取token中的数据异常: {}",e.getMessage());
			throw new ServiceException(CodeEnum.ILLEGAL_ACCESS_TOKEN);
		}
	}

	/**
	 * 获取当前用户名
	 * @return		登录状态正常返回用户userName,否则返回""
	 */
	protected String getUserName(){
		String authorization =  request.getHeader(HEAD_USER_INFO_KEY);
		try{
			if (StringUtils.isNotBlank(authorization)){
				Map map = JSONObject.parseObject(URLDecoder.decode(authorization,"utf-8"));
				if (! map.isEmpty()){
					return String.valueOf(map.get("userAccount"));
				}
			}
		}catch (Exception e){
			log.error("获取当前用户user_name异常:{}",e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * <p>
	 * Description: 数字类型id ，不能为负数
	 * </p>
	 *
	 * @author chenyi
	 * @date 2019年6月3日下午3:12:35
	 */
	protected static void checkNumberIds(Collection<?> col) {

		if (CollectionUtils.isEmpty(col)) {
			// "接口参数不能为空"
			throw new ServiceException(CodeEnum.ILLEGAL_DATA_ERROR);
		}

		col.forEach(o -> {
			if (o instanceof Number) {
				// 包含负数，抛异常
				if (((Number) o).longValue() < 0) {
					throw new ServiceException(CodeEnum.ILLEGAL_DATA_ERROR);
				}
			}
		});

	}

}
