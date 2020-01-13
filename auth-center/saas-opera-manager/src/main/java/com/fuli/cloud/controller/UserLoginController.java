package com.fuli.cloud.controller;

import com.fuli.auth.common.utils.Md5Utils;
import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.base.BaseController;
import com.fuli.cloud.commons.utils.PublicUtil;
import com.fuli.cloud.commons.utils.RsaEncryptUtil;
import com.fuli.cloud.dto.BaseIdDTO;
import com.fuli.cloud.dto.user.UserLoginDTO;
import com.fuli.cloud.dto.user.SmsSendDTO;
import com.fuli.cloud.dto.user.UserChangePasswordDTO;
import com.fuli.cloud.feign.BasicServerFeign;
import com.fuli.cloud.mapper.SystemRoleMapper;
import com.fuli.cloud.model.SystemRoleDO;
import com.fuli.cloud.model.SystemUserDO;
import com.fuli.cloud.model.TokenUser;
import com.fuli.cloud.service.SystemUserService;
import com.fuli.cloud.service.UserLoginService;
import com.fuli.cloud.vo.JwtVO;
import com.fuli.cloud.vo.employee.UserArchivesVO;
import com.fuli.cloud.vo.employee.UserPictureFrameVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.DynamicParameter;
import io.swagger.annotations.DynamicParameters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * @Description:    运营用户登录相关
 * @Author:         WFZ
 * @CreateDate:     2019/7/29 11:17
 * @Version:        1.0
*/
@Slf4j
@RestController
@RequestMapping("/systemUser")
@Api(tags="用户登录、修改密码等 --温福州")
public class UserLoginController extends BaseController {

    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private BasicServerFeign basicServerFeign;
    @Autowired
    private SystemRoleMapper systemRoleMapper;
    @Autowired
    private TokenStore tokenStore;


    @ApiOperation("用户登录 --温福州")
    @PostMapping("/login")
    public Result<JwtVO> userLogin(@Validated @RequestBody UserLoginDTO userLoginDTO){
        return userLoginService.userLogin(userLoginDTO);
    }


    @ApiOperation("用户退出 --温福州")
    @PostMapping("/logout")
    public Result userLogout(HttpServletRequest request){
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (null != header && header.contains("earer ")){
            header = header.split("earer ")[1];
        }
        try {
            OAuth2AccessToken accessToken = tokenStore.getAccessToken(tokenStore.readAuthentication(header));
            if (accessToken != null){
                if (accessToken.getRefreshToken() != null){
                    tokenStore.removeRefreshToken(accessToken.getRefreshToken());
                }
               /* * 清空token*/
                tokenStore.removeAccessToken(accessToken);
            }
        }catch (Exception e){
            log.error("退出清空token",e);
        }
        return Result.succeed();
    }

    @ApiOperation("根据手机号判断帐号是否存在 --温福州")
    @PostMapping("/verifyPhone")
    @DynamicParameters(name = "map", properties = {
            @DynamicParameter(name = "mobile", value = "手机号", required = true) })
    public Result verifyPhone(@RequestBody Map<String,String> map){
        /** 先判断账户是否存在*/
        SystemUserDO userDO = systemUserService.getOneByPhone(map.get("mobile"));
        if (userDO == null){
            return Result.failed(CodeEnum.PHONE_EMPTY_EXCEPTION);
        }
        return Result.succeed();
    }

    @ApiOperation("发送短信验证码 --温福州")
    @PostMapping("/sendSms")
    public Result sendSms(@Validated @RequestBody SmsSendDTO smsSendDTO){
        /** 先判断账户是否存在*/
        SystemUserDO userDO = systemUserService.getOneByPhone(smsSendDTO.getMobile());
        if (userDO == null){
            return Result.failed(CodeEnum.PHONE_EMPTY_EXCEPTION);
        }
        if (userDO.getStatus().intValue() != 1){
            return Result.failed(CodeEnum.ACCOUNT_INVALID_EXCEPTION.getCode(),"该帐号已注销");
        }

        basicServerFeign.sendSmsVerifyCode(smsSendDTO);
        return Result.succeed("验证码已发送");
    }

    @ApiOperation("忘记密码 --温福州")
    @PostMapping("/forgetPwd")
    public Result forgetPwd(@RequestBody UserChangePasswordDTO dto){
        /** 数据校验*/
        String password = RsaEncryptUtil.getPassword(dto.getPassword());
        if (PublicUtil.isEmpty(password)){
            return Result.failed(CodeEnum.PARAM_ERROR.getCode(),"密码非法");
        }
        if(! password.equals(RsaEncryptUtil.getPassword(dto.getConfirmPwd()))){
            return Result.failed(CodeEnum.PARAM_ERROR.getCode(),"两次输入的密码不一致");
        }

        /** 先判断账户是否存在*/
        SystemUserDO userDO = systemUserService.getOneByPhone(dto.getMobile());
        if (userDO == null){
            return Result.failed(CodeEnum.USER_EMPTY_EXCEPTION);
        }
        if (userDO.getStatus().intValue() != 1){
            return Result.failed(CodeEnum.ACCOUNT_INVALID_EXCEPTION.getCode(),"该帐号已注销");
        }

        /** 忘记密码*/
        userDO.setPassword(password);
        return Result.status(userLoginService.updateById(userDO));
    }

    @ApiOperation("修改密码 --温福州")
    @PostMapping("/resetPwd")
    public Result resetPwd(@Validated @RequestBody UserChangePasswordDTO dto){
        /** 数据校验*/
        String password = RsaEncryptUtil.getPassword(dto.getPassword());
        if (PublicUtil.isEmpty(password)){
            return Result.failed(CodeEnum.PARAM_ERROR.getCode(),"密码非法");
        }

        TokenUser user = getSystemUser();
        /** 先判断账户是否存在*/
        SystemUserDO userDO = systemUserService.getById(user.getId());
        if (userDO == null){
            return Result.failed(CodeEnum.USER_ABNORMAL_LOGIN_EXCEPTION);
        }
        if (userDO.getStatus().intValue() != 1){
            return Result.failed(CodeEnum.ACCOUNT_INVALID_EXCEPTION.getCode(),"该帐号已注销");
        }

        /** 修改密码*/
        if(! password.equals(RsaEncryptUtil.getPassword(dto.getConfirmPwd()))){
            return Result.failed(CodeEnum.PARAM_ERROR.getCode(),"两次输入的密码不一致");
        }
        String oldPwd = RsaEncryptUtil.getPassword(dto.getOldPwd());
        if (! userDO.getPassword().equals(oldPwd)){
            return Result.failed(CodeEnum.PARAM_ERROR.getCode(),"原密码输入错误");
        }
        userDO.setPassword(password);

        return Result.status(userLoginService.updateById(userDO));
    }


    @ApiOperation("头像框 --温福州")
    @PostMapping("/pictureFrame")
    public Result<UserPictureFrameVO> pictureFrame(){
        return userLoginService.pictureFrame(getSystemUser());
    }

    @ApiOperation("用户档案 --温福州")
    @PostMapping("/archives")
    public Result<UserArchivesVO> userArchives(){
        return userLoginService.findUserArchivesVO(getSystemUser());
    }


    @ApiOperation("后台重置密码 --温福州")
    @PostMapping("/resetPassword")
    public Result resetPassword(@Validated @RequestBody BaseIdDTO dto){
        /** 先判断用户角色 -- 暂时只能重置管理员密码*/
        List<SystemRoleDO> roleList = systemRoleMapper.listSystemRoleByUserId(dto.getId());
        if (CollectionUtils.isNotEmpty(roleList)) {
            /** 记录角色id*/
            for (SystemRoleDO role : roleList) {
                if (role.getAdministrators().intValue() == 1) {
                    SystemUserDO systemUserDO = new SystemUserDO();
                    systemUserDO.setId(Integer.valueOf(dto.getId()));
                    // 设置默认密码
                    systemUserDO.setPassword(Md5Utils.encode(Md5Utils.DEFAULT_PASSWORD));
                    return Result.status(systemUserService.updateById(systemUserDO));
                }
            }
        }
        return Result.failed("该用户暂不支持重置密码");
    }


    @ApiOperation("修改初始密码 --温福州")
    @PostMapping("/updateInitPwd")
    public Result updateInitPwd(@Validated @RequestBody UserChangePasswordDTO dto){
        return userLoginService.updateInitPwd(dto);
    }

}
