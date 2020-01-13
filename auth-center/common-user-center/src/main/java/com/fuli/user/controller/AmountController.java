package com.fuli.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuli.user.commons.Result;
import com.fuli.user.commons.base.BaseController;
import com.fuli.user.dto.UserAmountDTO;
import com.fuli.user.dto.ValidPayDTO;
import com.fuli.user.model.AppUser;
import com.fuli.user.service.AppUserInfoService;
import com.fuli.user.utils.RSAEncrypt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 金额操作Controller
 * @author      fengjing
 * @date        2019/7/22 20:40
 */
@Slf4j
@RestController
@Api(tags = "金额显示相关")
public class AmountController extends BaseController {

    @Autowired
    private AppUserInfoService appUserInfoService;
    @Value("${FULI_RSA_USER}")
    private String FU_LI_RSA_USER;

    /**
     * 修改金额是否显示标识
     * @author      fengjing
     * @date        2019/7/22 20:50
     */
    @ApiOperation("修改金额是否显示标识")
    @PostMapping(value = "/updateAmountIsDisplaySign")
    public Result updateAmountIsDisplaySign(@RequestBody UserAmountDTO userAmountDto){
        Long id = getAppUserId();
        AppUser user = new AppUser();
        user.setId(id);
        user.setHomeAmountFlag(userAmountDto.getHomeAmountFlag());
        boolean b = appUserInfoService.updateById(user);
        if (b){
            return Result.succeed();
        }
        return Result.failed();
    }

    /**
     * 查询金额是否显示标识
     * @author      fengjing
     * @date        2019/7/22 20:50
     */
    @ApiOperation("查询金额是否显示标识")
    @RequestMapping(value = "/queryAmountIsDisplaySign")
    public Result queryAmountIsDisplaySign(@RequestBody UserAmountDTO userAmountDto){
        AppUser appUser = appUserInfoService.getById(userAmountDto.getUserId());
        if (appUser != null){
            return Result.succeed(appUser.getHomeAmountFlag());
        }
        return Result.failed();
    }

    /**
     * 电子钱包支付密码校验 --2019-08-02 修改接口
     * @author      WFZ
     * @param       validPayDto : 请求实体
     * @return      Result
     * @date        2019/8/2 17:11
     */
    @ApiOperation("电子钱包支付密码校验")
    @RequestMapping(value = "/ewallet/validPayUser")
    public Result validPayUser(@Validated @RequestBody ValidPayDTO validPayDto) {
        /** 密码解密*/
        String password = RSAEncrypt.getPassword(validPayDto.getPayPwd(), FU_LI_RSA_USER);
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","phone");
        queryWrapper.eq("id", validPayDto.getUserId()).eq("pay_password",password);
        AppUser appUser = appUserInfoService.getOne(queryWrapper);
        if (appUser != null){
            return Result.succeed("密码校验通过");
        }
        return Result.failed("密码不对");
    }
}
