package com.fuli.user.controller;

import com.fuli.user.commons.PageResult;
import com.fuli.user.commons.Result;
import com.fuli.user.commons.base.BaseController;
import com.fuli.user.constant.CommonConstant;
import com.fuli.user.dto.*;
import com.fuli.user.service.AppUserLoginAndRegisterService;
import com.fuli.user.utils.RedisService;
import com.fuli.user.vo.AppUserVo;
import com.fuli.user.vo.JwtVO;
import com.fuli.user.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description:    APP用户登录注册控制层
 * @Author:         FZ
 * @CreateDate:     2019/4/15 16:24
 * @Version:        1.0
 */
@Slf4j
@RestController
@RequestMapping("/appUser")
@Api(tags = "用户模块api")
public class AppUserLoginAndRegisterController extends BaseController {

    @Autowired
    private AppUserLoginAndRegisterService appUserLoginAndRegisterService;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private RedisService redisService;

    /**
     * 根据手机号用户名查询用户
     * @author      WFZ
     * @param       phone -用户名
     * @return      AppUserVo
     * @date        2019/4/13 19:56
     */
    @ApiIgnore
    @PostMapping(value = "/oauth/getUser" )
    @ApiOperation(value = "根据用户名查询用户")
    public AppUserVo findAppUserByUser(@RequestParam ("phone") String phone) {
        return appUserLoginAndRegisterService.findAppUserByUser(phone);
    }


    /**
     * SaaS门户登录用
     * @author      WFZ
     * @param       userLoginDTO -用户名
     * @return      AppUserVo
     * @date        2019/8/5 16:56
     */
    @PostMapping(value = "/saasLogin" )
    @ApiOperation(value = "SaaS门户 -- 温福州")
    public Result<JwtVO> saAsLogin(@Validated @RequestBody UserLoginDTO userLoginDTO) {
        return appUserLoginAndRegisterService.saAsLogin(userLoginDTO);
    }


    /**
     * 根据用户id查询用户
     * @author      WFZ
     * @param       id - 用户id
     * @param       companyId - 企业id
     * @return      AppUserVo
     * @date        2019/4/13 19:56
     */
    @ApiIgnore
    @PostMapping(value = "/oauth/getUserNews")
    @ApiOperation(value = "根据用户id查询用户")
    public AppUserVo getUserNews(@ApiParam(value = "用户id",required = true)@RequestParam ("id") String id,
                                 @ApiParam(value = "企业id",required = false)@RequestParam (value = "id",required = false)String companyId ) {
        return appUserLoginAndRegisterService.getUserNews(id,companyId);
    }

    /**
     * APP用户注册
     * @author      FZ
     * @param 	    userLoginVo:用户登录VO
     * @return      Result
     * @date        2019/4/15 9:33
     */
    @PostMapping(value = "/register")
    @ApiOperation(value = "APP用户注册")
    public Result register(@Validated @RequestBody UserRegisterDTO userRegisterDTO) {
        return appUserLoginAndRegisterService.appUserRegister(userRegisterDTO);
    }

    /**
     * APP用户登录
     * @author      FZ
     * @param 	    userLoginVo:用户登录VO
     * @return      Result
     * @date        2019/4/15 9:33
     */
    @PostMapping(value = "/login")
    @ApiOperation(value = "APP用户登录")
    public Result<JwtVO> login(@Validated @RequestBody UserLoginVO userLoginVo) {
        return appUserLoginAndRegisterService.appUserLogin(userLoginVo);
    }

    /**
     * APP用户退出
     * @author      FZ
     * @param
     * @return      Result
     * @date        2019/4/18 9:12
     */
    @PostMapping("/logout")
    @ApiOperation(value = "APP用户退出")
    public Result logout(HttpServletRequest request){
        Long id =  getAppUserId();
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (id != 0) {
            /** 清空设备号*/
            redisService.remove(CommonConstant.FULI_DEVICE_NUM + id);
            if (null != header && header.contains("earer ")){
                header = header.split("earer ")[1];
            }
            try {
                OAuth2AccessToken accessToken = tokenStore.getAccessToken(tokenStore.readAuthentication(header));
                if (accessToken != null){
                    if (accessToken.getRefreshToken() != null){
                        tokenStore.removeRefreshToken(accessToken.getRefreshToken());
                    }
                    /** 清空token*/
                    tokenStore.removeAccessToken(accessToken);
                }
            }catch (Exception e){
                log.error("退出清空token",e);
            }
        }
        return Result.succeed();
    }


    /**
     * 中台用户同步接口
     * @author      fengjing
     * @date        2019/5/16 10:15
     */
    @PostMapping(value = "/platformSynchronized")
    @ApiOperation(value = "中台用户同步接口")
    public Result platformUserSynchronized(@RequestBody AppUserDTO appUserDTO){
        return appUserLoginAndRegisterService.platformUserSynchronized(appUserDTO);
    }

    /**
     * 订阅中台用户修改
     * @author      fengjing
     * @date        2019/5/16 19:59
     */
    @PostMapping(value = "/platformUpdate")
    @ApiOperation(value = "订阅中台用户修改")
    public Result platformUpdate( @Validated @RequestBody PlatformUpdateDTO platformUpdateDto){
        return appUserLoginAndRegisterService.platformUpdate(platformUpdateDto);
    }

    /**
     * @Description:(根据openID获取主键唯一ID)
     * @author      fengjing
     * @date        2019/5/6 9:40
     */
    @ApiOperation(value = "根据openID获取主键唯一ID")
    @PostMapping(value = "/obtainOpenId")
    public Result obtainPrimaryKey(@RequestBody Map<String,Object> map){
        String openId = map.get("openId") == null ? "0" : map.get("openId").toString();
        return appUserLoginAndRegisterService.obtainPrimaryKey(openId);
    }

    /**
     * @Description:(分页查询用户手机号)
     * @author      fengjing
     * @date        2019/5/6 14:12
     */
    @ApiOperation(value = "分页查询用户手机号")
    @PostMapping(value = "/pagePhone")
    public PageResult pageUserPhone(@RequestBody Map<String,Object> map){
        return appUserLoginAndRegisterService.pageUserPhone(map);
    }


    /**
     * 设置手势密码、修改手势密码
     * @author      fengjing
     * @date        2019/6/3 17:34
     */
    @ApiOperation(value = "设置/修改手势密码")
    @PostMapping(value = "/gesturePassword")
    public Result gesturePassword(@Validated @RequestBody ChangeGesturePwdDTO changeGesturePwdDTO){
        return appUserLoginAndRegisterService.gesturePassword(changeGesturePwdDTO , getAppUserInfo());
    }
}
