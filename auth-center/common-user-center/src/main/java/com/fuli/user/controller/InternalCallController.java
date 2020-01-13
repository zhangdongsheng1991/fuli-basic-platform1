package com.fuli.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuli.auth.common.utils.JsonUtils;
import com.fuli.logtrace.annotation.LogTrace;
import com.fuli.user.commons.CodeEnum;
import com.fuli.user.commons.Result;
import com.fuli.user.commons.base.BaseController;
import com.fuli.user.dto.MiddleTokenDTO;
import com.fuli.user.dto.UpdateEwalletInfoDTO;
import com.fuli.user.dto.UserCertificationDTO;
import com.fuli.user.model.AppUser;
import com.fuli.user.service.AppUserInfoService;
import com.fuli.user.service.impl.AppUserLoginAndRegisterServiceImpl;
import com.fuli.user.utils.PublicUtil;
import com.fuli.user.vo.AppUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:    内部接口调用Controller
 * @Author:         WFZ
 * @CreateDate:     2019/8/5 17:01
 * @Version:        1.0
*/
@Slf4j
@RestController
@Api(tags = "内部接口调用")
@RequestMapping("/internal")
public class InternalCallController extends BaseController {

    @Autowired
    private AppUserInfoService appUserInfoService;
    @Autowired
    private AppUserLoginAndRegisterServiceImpl appUserLoginAndRegisterServiceImpl;

    /**
     * 根据手机号以及身份证号码查询用户信息
     *
     * @return Result
     * @author WFZ
     * @date 2019/8/12 10:41
     */
    @ApiOperation("设置用户是否开通电子钱包信息")
    @PostMapping(value = "/updateEwalletInfo")
    public Result updateEwalletInfo(@Validated @RequestBody UpdateEwalletInfoDTO updateEwalletInfoDTO) {

        Integer haoYiLian = updateEwalletInfoDTO.getHaoYiLian();
        Integer huaXiaPds = updateEwalletInfoDTO.getHuaXiaPds();
        if (null == haoYiLian && null == huaXiaPds) {
            return Result.failed("参数缺失： 需要传入是否开通电子钱包信息");
        }
        if (haoYiLian != null) {
            if (haoYiLian < 0 || haoYiLian > 1) {
                return Result.failed("参数缺失： 参数不合法！haoYiLian[" + haoYiLian + "]");
            }
        }
        if (huaXiaPds != null) {
            if (huaXiaPds < 0 || huaXiaPds > 1) {
                return Result.failed("参数缺失： 参数不合法！haoYiLian[" + huaXiaPds + "]");
            }
        }
        AppUser appUser = appUserInfoService.getById(updateEwalletInfoDTO.getUserId());
        if (appUser == null) {
            return Result.failed("用户不存在");
        }
        AppUser update = new AppUser();
        update.setId(appUser.getId());
        if (haoYiLian != null) {
            update.setHaoYiLian(haoYiLian);
        }
        if (huaXiaPds != null) {
            update.setHuaXiaPds(huaXiaPds);
        }
        boolean b = appUserInfoService.updateById(update);
        if (b) {
            return Result.succeed();
        }
        return Result.failed("更新数据库失败");
    }


    /**
     * 根据手机号以及身份证号码查询用户信息
     * @author      WFZ
     * @param 	    phone : 手机号码
     * @param 	    idCard ： 身份证号码
     * @return      Result
     * @date        2019/8/12 10:41
     */
    @ApiOperation("根据手机号以及身份证号码查询用户信息")
    @PostMapping(value = "/byPhoneAndIdCard")
    public Result byPhoneAndIdCard(@ApiParam(value = "手机号",required = true)@RequestParam("phone")String phone ,
                                   @ApiParam(value = "身份证号",required = true)@RequestParam("idCard")String idCard){
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","phone","real_name","gender","certificate_card","is_identification");
        queryWrapper.eq("phone", phone ).eq("certificate_card",idCard).apply("1=1 LIMIT 1");
        AppUser appUser = appUserInfoService.getOne(queryWrapper);
        if (appUser != null){
            return Result.succeed(appUser);
        }
        return Result.failed("用户不存在");
    }


    @ApiOperation("根据身份证号及姓名查询用户信息")
    @PostMapping(value = "/byIdCardAndName")
    public Result byIdCardAndName(@ApiParam(value = "姓名",required = true)@RequestParam("name")String name,
                                  @ApiParam(value = "身份证号",required = true)@RequestParam("idCard")String idCard){
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","phone","real_name","gender","certificate_card","is_identification");
        queryWrapper.eq("real_name", name ).eq("certificate_card",idCard).apply("1=1 ORDER BY is_identification DESC LIMIT 1");
        AppUser appUser = appUserInfoService.getOne(queryWrapper);
        if (appUser != null){
            return Result.succeed(appUser);
        }
        return Result.failed("用户不存在");
    }

    @ApiOperation("根据身份证号码判断该身份证是否已实名认证")
    @PostMapping(value = "/isRealNameByIdCard")
    public Result<Boolean> isRealNameByIdCard(@RequestParam("idCard")String idCard){
        if (StringUtils.isNotBlank(idCard)){
            return Result.succeed(appUserInfoService.isRealNameByIdCard(idCard));
        }
        return Result.failed(CodeEnum.PARAM_ERROR.getCode(),"身份证号码不能为空");
    }


    /**
     * 修改用户的实名认证状态  2019-08-07 修改，新增用户id为必传参数
     * @author      WFZ
     * @param       userCertificationDto : 实名请求类
     * @return      Result
     * @date        2019/8/7 16:12
     */
    @PostMapping(value = "/updateRealNameStatus")
    @ApiOperation(value = "修改用户的实名认证状态为已认证")
    @LogTrace("修改用户的实名认证状态为已认证")
    public Result updateRealNameStatus(@Validated @RequestBody UserCertificationDTO userCertificationDto) {
        log.info("实名认证参数：{}",userCertificationDto);
        return appUserInfoService.updateRealNameStatus(userCertificationDto);
    }


    @PostMapping(value = "/findByPhone")
    @ApiOperation(value = "根据手机号查询用户")
    public Result<AppUser> findByPhone(@RequestParam("phone") String phone) {
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","phone","openId","username","real_name","is_identification","status","certificate_card","user_from");
        queryWrapper.apply("phone = '"+ phone +"' LIMIT 1");
        AppUser appUser = appUserInfoService.getOne(queryWrapper);
        if (appUser ==null){
            return Result.failed(CodeEnum.CHECKED_USER_NONEXISTENT);
        }
        return Result.succeed();
    }


    @PostMapping(value = "/middle/token")
    @ApiOperation(value = "中台获取token")
    public Map middleToken(@Validated @RequestBody MiddleTokenDTO middleTokenDTO) {
        /** 记录错误信息*/
        Map<String, Object> map = new HashMap<>(4);
        AppUserVo userNews = appUserLoginAndRegisterServiceImpl.getUserNews(middleTokenDTO.getAppUserId(), middleTokenDTO.getCompanyId());
        if (userNews != null){
            Result accessToken = appUserLoginAndRegisterServiceImpl.getAccessToken(userNews, 3, middleTokenDTO.getClientId(), middleTokenDTO.getClientSecret());
            if (PublicUtil.isResultSuccess(accessToken)){
                return JsonUtils.jsonToPojo(JsonUtils.objectToJson(accessToken.getData()),Map.class);
            }else {
                map.put("code",accessToken.getCode());
                map.put("msg", accessToken.getMsg());
                return map;
            }
        }
        map.put("code", 10210003);
        map.put("msg", "无效的用户id");
        return map;
    }
}
