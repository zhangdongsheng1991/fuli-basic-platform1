package com.fuli.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuli.auth.common.model.BaseUser;
import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.base.BaseServiceImpl;
import com.fuli.cloud.commons.exception.ServiceException;
import com.fuli.cloud.commons.utils.PublicUtil;
import com.fuli.cloud.commons.utils.RsaEncryptUtil;
import com.fuli.cloud.configuration.service.ProductionAccessTokenService;
import com.fuli.cloud.constant.CommonConstant;
import com.fuli.cloud.dto.user.UserLoginDTO;
import com.fuli.cloud.dto.user.UserChangePasswordDTO;
import com.fuli.cloud.feign.BasicServerFeign;
import com.fuli.cloud.mapper.SystemMenuMapper;
import com.fuli.cloud.mapper.SystemRoleMapper;
import com.fuli.cloud.mapper.SystemUserMapper;
import com.fuli.cloud.model.SystemRoleDO;
import com.fuli.cloud.model.SystemUserDO;
import com.fuli.cloud.model.TokenUser;
import com.fuli.cloud.service.UserLoginService;
import com.fuli.cloud.vo.SystemModuleVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description:    用户登录相关
 * @Author:         WFZ
 * @CreateDate:     2019/7/29 12:35
 * @Version:        1.0
*/
@Slf4j
@Service
public class UserLoginServiceImpl extends BaseServiceImpl<SystemUserMapper, SystemUserDO> implements UserLoginService {

    @Autowired
    private BasicServerFeign basicServerFeign;
    @Autowired
    private SystemRoleMapper systemRoleMapper;
    @Autowired
    private SystemMenuMapper systemMenuMapper;
    @Autowired
    private ProductionAccessTokenService productionAccessTokenService;

    /**
     * 用户登录
     * @param dto
     * @return Result
     * @author WFZ
     * @date 2019/7/29 11:33
     */
    @Override
    public Result userLogin(UserLoginDTO dto) {

        /** 校验验证码是否有效 */
        Result resultImageCode = basicServerFeign.verificationImageCode(dto.getVerifyCode());
        if(! PublicUtil.isResultSuccess(resultImageCode)){
            return resultImageCode;
        }

        /** 判断用户是否存在*/
        SystemUserDO userDO = this.findByPhoneOrUsername(dto.getPhone());
        if (userDO == null){
            return Result.failed(CodeEnum.USER_EMPTY_EXCEPTION);
        }
        /** 密码解密*/
        String password = RsaEncryptUtil.getPassword(dto.getPassword());
        if (! password.equals(userDO.getPassword())){
            return Result.failed(CodeEnum.PASSWORD_ERROR_EXCEPTION);
        }
        /** 账号无效*/
        if (userDO.getStatus().intValue() != 1){
            return Result.failed(CodeEnum.ACCOUNT_INVALID_EXCEPTION);
        }

        /** 获取用户角色，如果是超级管理员可以登录任何系统*/
        List<SystemRoleDO> roleList = systemRoleMapper.listSystemRoleByUserId(userDO.getId());
        if (PublicUtil.isEmpty(roleList)){
            log.info("帐号没有可用角色");
            return Result.failed(CodeEnum.ACCOUNT_NO_PERMISSIONS_EXCEPTION);
        }

        /** 记录角色id*/
        boolean rFlag = true;
        Set<Integer> roles = new HashSet<>(4);
        for (SystemRoleDO role : roleList){
            roles.add(role.getId());
            if (role.getAdministrators().intValue()==1){
                rFlag = false;
                break;
            }
        }
        if (rFlag){
            /** SaaS运营登录 */
            if (CommonConstant.LOGIN_OPERATE.equals(dto.getType())){
                /** 账号无权限*/
                if (userDO.getOpenSystem().intValue() != 1){
                    return Result.failed(CodeEnum.ACCOUNT_NO_PERMISSIONS_EXCEPTION);
                }
            }else if (CommonConstant.LOGIN_APP_SYSTEM.equals(dto.getType())){
                /** 渠道运营登录,获取拥有的服务 */
                List<SystemModuleVO> moduleVOS = systemMenuMapper.listSystemModuleByRoles(roles , null);
                boolean flag = true;
                for (SystemModuleVO module : moduleVOS){
                    if (module.getId().intValue()==2){
                        flag = false;
                    }
                }
                if (flag){
                    log.info("没有APP渠道门户的权限");
                    return Result.failed(CodeEnum.ACCOUNT_NO_PERMISSIONS_EXCEPTION);
                }
            }
        }
        /** 是否需要修改初始密码*/
        if (CommonConstant.USER_INIT_PASSWORD.equals(password) || (CommonConstant.ADMIN_INIT_PASSWORD).equals(password)){
            return Result.failed(30108111, "需修改密码");
        }

        return getJwtToken(userDO);
    }

    public Result getJwtToken(SystemUserDO userDO){
        BaseUser dto = new BaseUser();
        dto.setClientId(CommonConstant.CLIENT_ID);
        dto.setUserId(userDO.getId().toString());
        dto.setPhone(userDO.getPhoneNumber());
        dto.setRealName(userDO.getName());
        dto.setUserAccount(userDO.getUsername());
        try{
            return productionAccessTokenService.createAccessToken(CommonConstant.CLIENT_ID, CommonConstant.CLIENT_SECRET, CommonConstant.CLIENT_GRANT_TYPE, dto);
        }catch (Exception e){
            log.error("获取token出错",e);
        }
        return Result.failed(CodeEnum.GLOBAL_EXCEPTION);
    }

    /**
     * 认证服务生成token用
     *
     * @param userAccount : 账户或手机号
     * @return Result
     * @author WFZ
     * @date 2019/7/31 20:08
     */
    @Override
    public SystemUserDO userLoginInfo(String userAccount) {
        /** 判断用户是否存在*/
        SystemUserDO userDO = this.findByPhoneOrUsername(userAccount);
        if (userDO == null){
            throw new ServiceException(CodeEnum.USER_EMPTY_EXCEPTION);
        }
        /** 账号无效*/
        if (userDO.getStatus().intValue() != 1){
            throw new ServiceException(CodeEnum.ACCOUNT_INVALID_EXCEPTION);
        }
        return userDO;
    }

    /**
     * 我的头像框
     *
     * @param user : 登录用户信息
     * @return Result
     * @author WFZ
     * @date 2019/8/1 14:55
     */
    @Override
    public Result pictureFrame(TokenUser user) {
        return Result.succeed(baseMapper.findMyPictureFrame(user.getId()));
    }

    /**
     * 我的档案
     *
     * @param user : 登录用户信息
     * @return Result
     * @author WFZ
     * @date 2019/8/1 14:55
     */
    @Override
    public Result findUserArchivesVO(TokenUser user) {
        return Result.succeed(baseMapper.findUserArchives(user.getId()));
    }

    @Override
    public Result updateInitPwd(UserChangePasswordDTO dto) {
        String oldPwd = RsaEncryptUtil.getPassword(dto.getOldPwd());
        if (StringUtils.isBlank(dto.getMobile())){
            return Result.failed("帐号不能为空");
        }
        /** 数据校验*/
        String password = RsaEncryptUtil.getPassword(dto.getPassword());
        if (PublicUtil.isEmpty(password)){
            return Result.failed("密码非法");
        }
        if(! password.equals(RsaEncryptUtil.getPassword(dto.getConfirmPwd()))){
            return Result.failed("两次输入的密码不一致");
        }

        /** 先判断账户是否存在*/
        SystemUserDO userDO = findByPhoneOrUsername(dto.getMobile());
        if (userDO == null){
            return Result.failed(CodeEnum.USER_EMPTY_EXCEPTION);
        }
        if (!(userDO.getPassword()).equals(oldPwd)){
            log.info("用户原密码password:{}, 传入原密码oldPwd:{}",userDO.getPassword(),oldPwd);
            return Result.failed("原密码输入错误");
        }
        if (userDO.getStatus().intValue() != 1){
            return Result.failed(CodeEnum.ACCOUNT_INVALID_EXCEPTION.getCode(),"该帐号已注销");
        }
        /** 忘记密码*/
        userDO.setPassword(password);
        int i = baseMapper.updateById(userDO);
        if (i != 1){
            return Result.failed(CodeEnum.GLOBAL_EXCEPTION);
        }
        return Result.succeed();
    }


    public SystemUserDO findByPhoneOrUsername(String phone){
        QueryWrapper<SystemUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id,name,username,password,phone_number,status,open_system");
        queryWrapper.apply(" username='"+ phone +"' OR phone_number='"+ phone +"'  LIMIT 1");
        return baseMapper.selectOne(queryWrapper);
    }
}
