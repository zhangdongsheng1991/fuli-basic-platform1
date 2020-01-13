package com.fuli.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuli.user.commons.CodeEnum;
import com.fuli.user.commons.Result;
import com.fuli.user.commons.base.BaseController;
import com.fuli.user.dto.ChangePhoneDTO;
import com.fuli.user.dto.ResetPasswordDTO;
import com.fuli.user.dto.UpdateUserDTO;
import com.fuli.user.dto.UserSwitchCompanyDTO;
import com.fuli.user.model.AppUser;
import com.fuli.user.service.AppUserInfoService;
import com.fuli.user.service.AppUserLoginAndRegisterService;
import com.fuli.user.utils.PublicUtil;
import com.fuli.user.utils.RegexUtil;
import com.fuli.user.vo.AppUserIdVO;
import com.fuli.user.vo.AppUserIdsVO;
import com.fuli.user.vo.JwtVO;
import com.fuli.user.vo.PageResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 用户信息
 * @Author: WS
 * @CreateDate: 2019/4/18 19:16
 * @Version: 1.0
 */
@Slf4j
@RestController
@Api(tags = "用户信息模块api")
@RequestMapping("/userInfo")
public class AppUserInfoController extends BaseController {

    @Autowired
    private AppUserInfoService appUserInfoService;
    @Autowired
    private AppUserLoginAndRegisterService appUserLoginAndRegisterService;


    @PostMapping(value = "/update")
    @ApiOperation(value = "修改用户信息 （头像、用户名等）")
    public Result update(@RequestBody UpdateUserDTO dto) {
        Long userId = getAppUserId();
        AppUser user = new AppUser();
        /** 不为空表示修改用户名*/
        if (StringUtils.isNotBlank(dto.getUsername())){
            if (RegexUtil.match(RegexUtil.NUMBER,dto.getUsername())){
                return Result.failed("用户名不能为纯数字");
            }
            if (! RegexUtil.match(RegexUtil.USER_NAMES,dto.getUsername())){
                return Result.failed("用户名仅支持英文 、数字、_ 或者这3种字符串的组合");
            }

            QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id","phone","username");
            queryWrapper.eq("username",dto.getUsername().trim()).apply("1=1 LIMIT 1");
            AppUser one = appUserInfoService.getOne(queryWrapper);
            if (one != null && ! one.getId().toString().equals(userId.toString())){
                return Result.failed("您输入的用户名已存在，请重新输入");
            }
            user.setUsername(dto.getUsername());
        }
        user.setId(userId);
        user.setHeadImgUrl(dto.getHeadImgUrl());
        return Result.status(appUserInfoService.updateById(user));
    }


    /**
     * APP用户登录后获取用户信息(企业名称+用户姓名)
     * @author      WS
     * @param 	    map-用户手机
     * @return      Result
     * @date        2019/4/17 9:33
     */
    @PostMapping(value = "/getUserInfo")
    @ApiOperation(value = "用户信息(带企业信息)")
    public Result<AppUser> getUserInfo(@RequestBody(required = false) Map<String,Object> map) {
        return appUserInfoService.getUserInfo(getAppUserId());
    }

    /**
     * APP用户登录后获取用户信息(企业名称+用户姓名)
     * @author      WS
     * @param 	    id-主键id
     * @return      Result
     * @date        2019/4/17 9:33
     */
    @ApiIgnore
    @PostMapping(value = "/getUserInfoById")
    @ApiOperation(value = "根据用户主键id获取用户信息(企业)")
    public Result<AppUser> getUserInfoById(@RequestParam("id")Long id) {
        if (PublicUtil.isNotNull(id)){
            return appUserInfoService.getUserInfo(id);
        }
        return Result.failedWith(null, CodeEnum.PARAM_ERROR.getCode(),"id不能为空");
    }

    /**
     * APP用户登录后获取用户信息
     * @author      WS
     * @param 	    id-主键id
     * @return      Result
     * @date        2019/4/17 9:33
     */
    @PostMapping(value = "/getAppUser")
    @ApiOperation(value = "根据用户主键id获取用户信息")
    public Result<AppUser> getUserInfo(@RequestParam("id")Long id) {
        if (! PublicUtil.isNotNull(id)){
            return Result.failedWith(null, CodeEnum.PARAM_ERROR.getCode(),"id不能为空");
        }
        AppUser user = appUserInfoService.getById(id);
        if(null == user){
            return Result.failed(CodeEnum.CHECKED_USER_NONEXISTENT);
        }
        /** 密码不返回*/
        user.setPassword(null);
        user.setPayPassword(null);
        return Result.succeed(user);
    }

    /**
     * 用户切换企业，重新生成token  2019-08-12 修改接口
     * @author      WFZ
     * @param 	    userSwitchCompanyDTO
     * @return      Result
     * @date        2019/8/12 16:33
     */
    @PostMapping(value = "/switchCompany")
    @ApiOperation(value = "用户切换企业，重新生成token")
    public Result<JwtVO> switchCompany(@Validated @RequestBody UserSwitchCompanyDTO userSwitchCompanyDTO) {
        log.info("切换企业参数：{}",userSwitchCompanyDTO);
        return appUserInfoService.switchCompany(userSwitchCompanyDTO,getAppUserInfo());
    }


    /**
     * 根据身份证号获取用户信息
     * @author      fengjing
     * @date        2019/5/16 9:25
     */
    @PostMapping(value = "/idNumGetUser")
    @ApiOperation(value = "根据身份证号获取用户信息")
    public AppUser idNumGetUser(@RequestBody Map<String,Object> map){
        String certificateCard = map.get("certificateCard") == null ? "0" : map.get("certificateCard").toString();
        return appUserInfoService.findByIdCard(certificateCard);
    }

    /**
     * 同步中台openId -- 用户实名后同步给中台，中台返回openId
     * @author      WFZ
     * @param       userId : 用户id
     * @param       openId : 中台openId
     * @return      Result
     * @date        2019/8/7 15:35
     */
    @PostMapping(value = "/sysnOpenId")
    @ApiOperation(value = "同步中台openId")
    public Result syncOpenId(@RequestParam("thirdUserId") String userId,
                             @RequestParam("openId") String openId){
        AppUser user = new AppUser();
        user.setId(Long.valueOf(userId));
        user.setOpenid(openId);
        return Result.status(appUserInfoService.updateById(user));
    }

    /**
     * 更换手机号 - 2019-08-02 修改接口
     * @author      WFZ
     * @param 	    changePhoneDto  : 请求类
     * @return      Result
     * @date        2019/8/2 16:32
     */
    @ApiOperation(value = "修改手机号")
    @PostMapping(value = "/replacePhone")
    public Result replacePhone(@Validated @RequestBody ChangePhoneDTO changePhoneDto){
        return appUserInfoService.replacePhone(changePhoneDto,getAppUserInfo());
    }

    /**
     * @Description:(批量手机号查询批量id)
     * @author      fengjing
     * @date        2019/6/27 9:28
     */
    @ApiIgnore
    @PostMapping(value = "/phoneGetUserId")
    @ApiOperation(value = "批量手机号查询批量id")
    public List<AppUserIdVO> phoneGetUserId(@RequestBody List<String> list){
        return appUserInfoService.phoneGetUserId(list);
    }


    /**
     * 通过账号批量获取用户信息
     * @param accounts 多账号
     * @return
     */
    @ApiIgnore
    @PostMapping("/inner/getAccounts")
    @ApiOperation("通过账号批量获取用户信息")
    public Result getAccounts(@RequestBody Set<String> accounts){
        return Result.succeed(appUserLoginAndRegisterService.getAccounts(accounts));
    }

    /**
     * 获取用户id列表
     * @author pcg
     * @return
     */
    @GetMapping("/getUserIds")
    @ApiOperation("获取用户id列表")
    public Result<PageResultVO<List<AppUserIdsVO>>> getUserIds(Integer currentPage, Integer pageSize){
        return Result.succeed(appUserInfoService.getUserIds(currentPage, pageSize));
    }


    @PostMapping("/resetPassword")
    @ApiOperation("重置密码")
    public Result<Boolean> resetPassword(@Validated @RequestBody ResetPasswordDTO resetPasswordDTO){
        return appUserLoginAndRegisterService.resetPassword(resetPasswordDTO);
    }

}
