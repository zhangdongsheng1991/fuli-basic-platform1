package com.fuli.user.controller;

import com.fuli.user.commons.CodeEnum;
import com.fuli.user.commons.Result;
import com.fuli.user.dao.AppUserDao;
import com.fuli.user.dto.AppUserUpdatePasswordRequest;
import com.fuli.user.dto.UpdatePwdDTO;
import com.fuli.user.model.AppUser;
import com.fuli.user.service.AppUserInfoService;
import com.fuli.user.service.AppUserPwdUpdateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* SaaS门户用户密码管理模块Controller层
* @author fengjing
* @date 2019/6/26 11:48
* @version V1.0
*/
@Api(tags = "SaaS门户用户密码管理模块")
@RequestMapping("/saasUser")
@RestController
@Slf4j
public class SaAsUserController {

    @Autowired
    private AppUserDao appUserDao;
    @Autowired
    private AppUserPwdUpdateService appUserPwdUpdateService;
    @Autowired
    private AppUserInfoService appUserInfoService;


    @PostMapping(value = "/updateInitPwd")
    @ApiOperation(value = "修改初始密码")
    public Result updateInitPwd(@Validated @RequestBody UpdatePwdDTO updatePwdDTO){
        /** 原密码校验 */
        AppUser user = appUserDao.findAppUserByPhone(updatePwdDTO.getPhone());
        if (user == null){
            return Result.failed(CodeEnum.ACCOUNT_NO_EXISTS);
        }
        AppUserUpdatePasswordRequest request = new  AppUserUpdatePasswordRequest();
        request.setPhone(user.getPhone());
        request.setUserId(user.getId());
        request.setUsedPwd(updatePwdDTO.getUsedPwd());
        request.setPassword(updatePwdDTO.getPassword());
        request.setRetypePassword(updatePwdDTO.getRetypePassword());
        return appUserPwdUpdateService.updatePasswordByPhone(request,2);
    }




    /**
     * 判断该用户身份证是否实名
     * @author      fengjing
     * @date        2019/7/3 15:15
     */
    @PostMapping(value = "/userIsRealName")
    @ApiOperation(value = "（saas重构）判断该用户身份证是否实名 冯静")
    public Result userIsRealName(@RequestBody String idNum){
        AppUser appUser = appUserInfoService.findByIdCard(idNum);
        if (appUser != null && appUser.getIsIdentification().intValue()==1){
            return Result.succeed();
        }
        return Result.failed();
    }


}
