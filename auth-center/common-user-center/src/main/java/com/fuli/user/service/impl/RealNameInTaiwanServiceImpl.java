package com.fuli.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fuli.user.commons.CodeEnum;
import com.fuli.user.commons.Result;
import com.fuli.user.config.PlatformConfig;
import com.fuli.user.constant.CommonConstant;
import com.fuli.user.constant.PlatformApiUriConstant;
import com.fuli.user.constant.TemplateRequest;
import com.fuli.user.dao.AppUserDao;
import com.fuli.user.dao.DataRecordUserMapper;
import com.fuli.user.dto.PlatformPasswordDTO;
import com.fuli.user.dto.RealNameInTaiwanDTO;
import com.fuli.user.feign.MiddleServerFeign;
import com.fuli.user.model.AppUser;
import com.fuli.user.model.DataRecordUserDO;
import com.fuli.user.service.RealNameInTaiwanService;
import com.fuli.user.utils.PublicUtil;
import com.fuli.user.utils.RsaHelper;
import com.fuli.user.vo.PlatformResponse;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @Description: (中台实名认证service逻辑层)
* @author fengjing
* @date 2019/4/17 15:43
* @version V1.0
*/
@Service
@Slf4j
public class RealNameInTaiwanServiceImpl implements RealNameInTaiwanService {

    @Autowired
    private PlatformConfig platformConfig;
    @Autowired
    private TemplateRequest templateRequest;
    @Autowired
    private DataRecordUserMapper dataRecordUserMapper;
    @Autowired
    private AppUserDao appUserDao;
    @Autowired
    private MiddleServerFeign middleServerFeign;

    private RsaHelper rsaHelper = new RsaHelper();

    /**
     * @Description:(中台实名三要素认证逻辑)
     * @author      fengjing
     * No such property: code for class: Script1
     * @return
     * @date        2019/4/17 15:45
     */
    @Override
    public Result obtainRealName(RealNameInTaiwanDTO dto) {
        String url = "http://api.chinadatapay.com/communication/personal/1882?key=06f89f1029b0cde33f7a8709b256118d&name=" + dto.getName() + "&idcard=" + dto.getIdNum();
        log.info("url：{}",url);
        //设置入参
        HashMap<String,Object> postParameters = new HashMap<>(4);
        try {
            JSONObject jsonObject = templateRequest.unifiedTransmission(url, postParameters);
            log.info("实名认证调取中台返回结果：{}",jsonObject);
            if (PublicUtil.isNotEmpty(jsonObject) && PublicUtil.isNotEmpty(jsonObject.get("data"))){
                Map<String, Object> data = (Map<String, Object>) jsonObject.get("data");
                String result = data.get("result") + "";
                if(result.equals(String.valueOf(1))){
                    return Result.succeed();
                }
            }
        }catch (Exception e){
            log.error("实名认证接口",e);
        }
        /** 姓名与身份证不匹配 */
        return Result.failed(CodeEnum.NAME_IDENTITY_MISMATCH);
    }

    @Override
    public Result thirdLogin(String username, String password) {
        log.info("中台登录校验入参：username:{},password:{}",username,password);
        try {
            password = rsaHelper.encryptWithBase64(password, rsaHelper.loadPublicKey(platformConfig.getRsaPublicKey()));
        }catch (Exception e){
            log.error("第三方登录时rsa加密异常:{}", e.getMessage());
            return Result.failed("第三方登录时RSA加密异常");
        }
        // 完整的url
        String url = platformConfig.getUrl() + PlatformApiUriConstant.THIRD_LOGIN;
        // 请求参数
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("username",username);
        params.add("password",password);
        PlatformResponse response;
        try {
            response = templateRequest.postForObject(url,params);
        } catch (RestClientException e) {
            log.error("访问中台===统一账号密码多业务线登录接口失败===url:{}， 错误信息:{}", url, e );
            return Result.failed(CodeEnum.INVOKE_MIDDLE_SERVICE_ERROR);
        }
        log.info("第三方登录中台，请求响应报文：{}", response);
        if ("12016".equals(response.getCode().toString())){
            AppUser byPhone = appUserDao.findUserByPhone(username);
            if (byPhone == null){
                return Result.failed(CodeEnum.ACCOUNT_NO_EXISTS);
            }
            // 转换用户id，如果不成功，则进行补偿
            log.info("12016,通过手机号登录中台失败，进行补偿:{}", byPhone);
            middleServerFeign.findOtherUserID(String.valueOf(byPhone.getId()));
            // 数据补偿之后重新登录校验
            response = templateRequest.postForObject(url,params);
        }
        // 检查返回的accessToken数据是否正常
        if (response.getCode() != platformConfig.getResponseOkCode()){
            return Result.succeedWith(response.getData(), response.getCode(),response.getMessage().equals("密码错误")?"您输入的帐号或密码错误，请重新输入":response.getMessage());
        }
        return Result.succeed(response.getData());
    }

    @Override
    public Result syncPlatformPwd(PlatformPasswordDTO dto) {
        log.info("修改密码入参：dto:{}",dto.toString());
        Result result = new Result();
        /** 原密码不为空就要校验原密码是否错误*/
        if(!StringUtils.isEmpty(dto.getOldPassword())){
            log.info("有传源码密码，调用中台密码校验开始。。。。");
            result = checkPwd(dto);
            if(PublicUtil.isResultSuccess(result)){
                result =  registerLogin(dto);
            }else{
                // 10007是中台返回的用户名密码错误
                if (result.getCode() == 10007){
                    result = Result.failed(CodeEnum.OLDNEW_PWD_ATYPISM);
                }
            }
        }else{
            result =  registerLogin(dto);
        }
        log.info("用户修改密码调用中台密码同步结果：{}",result);
        // 补偿数据重试一次
        if (!PublicUtil.isResultSuccess(result) && "用户未关联到平台".contains(result.getMsg())){
            middleServerFeign.findOtherUserID(dto.getThirdUserId());
            result =  registerLogin(dto);
        }
        try{
            /** 将值保存到中间表, 只有本地用户修改密码并且密码修改失败插入数据*/
            if (CommonConstant.FU_LI_LABEL.equals(dto.getUserFrom()) && ! PublicUtil.isResultSuccess(result)){
                DataRecordUserDO data = new DataRecordUserDO();
                data.setUserId(Long.valueOf(dto.getThirdUserId()));
                data.setPhone(dto.getLoginName());
                data.setPassword(dto.getPassword());
                data.setCreateTime(new Date());
                data.setType(3);
                data.setSyncState(0);
                dataRecordUserMapper.insert(data);
                log.info("修改密码保存到中间表, 只有本地用户修改密码并且密码修改失败插入数据：{}", data);
            }
        }catch (Exception e){
            log.error("修改密码同步中台保存到中间表",e);
        }
        return result;
    }

    @Override
    public Result checkPwd(PlatformPasswordDTO dto) {
        // 设置请求头和请求参数
        HashMap<String,Object> map = new HashMap<String,Object>(8);
        String oldPassword = dto.getOldPassword();
        String url = platformConfig.getUrl() + PlatformApiUriConstant.CHECK_PWD;
        PlatformResponse response;
        try {
            oldPassword = rsaHelper.encryptWithBase64(oldPassword, rsaHelper.loadPublicKey(platformConfig.getRsaPublicKey()));
            map.put("password",oldPassword);
            map.put("thirdUserId",dto.getThirdUserId());
            response = templateRequest.postForObject(url, map);
            log.info("检验密码入参：response===="+response);
            if (ObjectUtils.isEmpty(response) || response.getCode() != platformConfig.getResponseOkCode()) {
                log.error("中台校验密码, 请求参数:{},请求地址：{}，返回结果：{}", map.toString(),url,response);
                return Result.failedWith(response.getData(),response.getCode(),response.getMessage());
            }else{
                return Result.succeed(response.getData(),response.getMessage());
            }
        } catch (Exception e) {
            log.error("===访问中台密码正确性接口失败 :请求url:{},参数：{}, 错误信息：{}", url,oldPassword, e);
            return Result.failed(CodeEnum.INVOKE_MIDDLE_SERVICE_ERROR);
        }
    }

    @Override
    public Result registerLogin(PlatformPasswordDTO dto) {
        // 设置请求头和请求参数
        HashMap<String,Object> map = new HashMap<String,Object>(8);
        String password = dto.getPassword();
        String url = platformConfig.getUrl() + PlatformApiUriConstant.REGISTER_LOGIN;
        PlatformResponse response;
        try {
            password = rsaHelper.encryptWithBase64(password, rsaHelper.loadPublicKey(platformConfig.getRsaPublicKey()));
            map.put("password",password);
            map.put("thirdUserId",dto.getThirdUserId());
            map.put("loginName",dto.getLoginName());
            response = templateRequest.postForObject(url, map);
            log.info("===账户密码同步同步到中台: 请求参数:{},请求地址：{}，返回结果{}", map, url,response);
            if (ObjectUtils.isEmpty(response) || response.getCode() != platformConfig.getResponseOkCode()) {
                return Result.failedWith(response.getData(),response.getCode(),response.getMessage());
            }else{
                return Result.succeed();
            }
        } catch (Exception e) {
            log.error("===访问中台账户密码同步接口失败 :请求url:{},参数：{}, 错误信息：{}", url, dto.toString(), e);
            return Result.failed(CodeEnum.INVOKE_MIDDLE_SERVICE_ERROR);
        }
    }

    /**
     * 组装完整的url
     * @param uri api接口
     * @return 返回完整的url
     */
    private String getFullUrl(String uri){
        return platformConfig.getUrl() + uri;
    }
}
