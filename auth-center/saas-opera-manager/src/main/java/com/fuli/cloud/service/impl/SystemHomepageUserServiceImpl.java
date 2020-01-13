package com.fuli.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.utils.MyBeanUtil;
import com.fuli.cloud.commons.utils.PublicUtil;
import com.fuli.cloud.dto.homepage.HomepageUserDTO;
import com.fuli.cloud.handler.CustomException;
import com.fuli.cloud.mapper.SystemHomepageModuleMapper;
import com.fuli.cloud.mapper.SystemHomepageUserMapper;
import com.fuli.cloud.model.SystemHomepageUserDO;
import com.fuli.cloud.model.TokenUser;
import com.fuli.cloud.service.SystemHomepageUserService;
import com.fuli.cloud.service.SystemRoleService;
import com.fuli.cloud.vo.homepage.HomepageModuleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * @Description:    首页模块管理
 * @Author:         WFZ
 * @CreateDate:     2019/6/26 11:16
 * @Version:        1.0
 */
@Slf4j
@Service
public class SystemHomepageUserServiceImpl implements SystemHomepageUserService {

    @Autowired
    SystemHomepageModuleMapper systemHomepageModuleMapper;
    @Autowired
    SystemHomepageUserMapper systemHomepageUserMapper;
    @Autowired
    SystemRoleService systemRoleService;


    /**
     * 获取拥有的首页模块列表
     *
     * @param userInfo ： 登录用户信息
     * @return Result
     * @author WFZ
     * @date 2019/6/26 11:16
     */
    @Override
    public Result list(TokenUser userInfo) {
        List<HomepageModuleVO> moduleVOList = this.listHomepageModuleByUserId(userInfo.getId());
        if (PublicUtil.isNotEmpty(moduleVOList)){
            /** 记录最终返回数据*/
            Map<String , Object> map = new HashMap<>(8);
            /** 创建3个list记录三列数据*/
            List<HomepageModuleVO> leftList =  new ArrayList<>();
            List<HomepageModuleVO> middleList =  new ArrayList<>();
            List<HomepageModuleVO> rightList =  new ArrayList<>();

            /** 首先判断用户是否已经排序过 ，如果没有用默认的排序*/
            List<SystemHomepageUserDO> saAsHomepageUsers = listSystemHomepageUserByUserId(userInfo.getId());
            if (PublicUtil.isNotEmpty(saAsHomepageUsers)){
                for (SystemHomepageUserDO user : saAsHomepageUsers){
                    for (HomepageModuleVO vo : moduleVOList){
                        if (user.getHomepageModuleId().intValue() == vo.getHomepageModuleId().intValue()){
                            vo.setSort(user.getSort());
                            vo.setType(user.getType());
                            if (user.getType()==1){
                                leftList.add(vo);
                            }else if (user.getType()==2){
                                middleList.add(vo);
                            }else {
                                rightList.add(vo);
                            }
                        }
                    }
                }
            }else{
                /** 用户自己没添加用原始排序*/
                for (HomepageModuleVO vo : moduleVOList){
                    if (vo.getType()==2){
                        middleList.add(vo);
                    }else if (vo.getType()==3){
                        rightList.add(vo);
                    }else {
                        leftList.add(vo);
                    }
                }
            }

            map.put("leftList",leftList);
            map.put("middleList",middleList);
            map.put("rightList",rightList);
            return Result.succeed(map);
        }

        return Result.succeed();
    }


    /**
     * 批量新增
     *
     * @param userInfo ： 登录用户信息
     * @param list
     * @return Result
     * @author WFZ
     * @date 2019/6/25 10:09
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Result batchInsert(TokenUser userInfo, List<HomepageUserDTO> list) {
        if (PublicUtil.isNotEmpty(list)){
            /** 校验数据中是否有重复的HomepageModuleId*/
            Set<String> set = new HashSet<>();
            list.forEach(key -> {
                if(PublicUtil.isNotEmpty(key.getHomepageModuleId())){
                    set.add(key.getHomepageModuleId());
                }
            });
            if (set.size() != list.size()){
                return Result.failedWith(null, CodeEnum.PARAM_ERROR.getCode(),"模块id不能为空且不能重复");
            }
            /** 数据校验*/
            List<HomepageModuleVO> moduleVOList = this.listHomepageModuleByUserId(userInfo.getId());
            if (PublicUtil.isNotEmpty(moduleVOList)){
                /** 记录返回的用户拥有的模块数据*/
                if (list.size() != moduleVOList.size()){
                    return Result.failedWith(null, CodeEnum.PARAM_ERROR.getCode(),"需传入用户所有模块排序");
                }
                boolean flag = true;
                for (HomepageUserDTO dto : list){
                    if (PublicUtil.isEmpty(dto.getSort())){
                        return Result.failedWith(null, CodeEnum.PARAM_ERROR.getCode(),"sort值不能为空");
                    }
                    if (PublicUtil.isEmpty(dto.getType()) || ! "1,2,3".contains(dto.getType())){
                        return Result.failedWith(null, CodeEnum.PARAM_ERROR.getCode(),"type值非法");
                    }
                    for (HomepageModuleVO vo : moduleVOList){
                        if (dto.getHomepageModuleId().equals(vo.getHomepageModuleId().toString())){
                            flag = false;
                        }
                    }
                    if (flag){
                        return Result.failed(CodeEnum.ILLEGAL_DATA_ERROR);
                    }
                }
                /** 先全部清空再添加 */
                Result result = reset(userInfo);
                if (! PublicUtil.isResultSuccess(result)){
                    return result;
                }
                /** 批量新增*/
                systemHomepageUserMapper.batchInsert(list , userInfo.getId());
                return Result.succeed();
            }
        }else {
            return Result.failed(CodeEnum.SELECT_IS_EMPTY.getCode(),"请至少添加一条数据");
        }
        return Result.failedWith(null, CodeEnum.ERROR.getCode(),"用户无模块权限");
    }


    /**
     * 获取当前用户拥有是首页模块
     * @author      WFZ
     * @param
     * @return      Result
     * @date        2019/8/1 14:36
     */
    public List<HomepageModuleVO> listHomepageModuleByUserId(Serializable userId){
        if (PublicUtil.isEmpty(userId)){
            throw new CustomException(CodeEnum.USER_ABNORMAL_LOGIN_EXCEPTION);
        }
        /** 判断是否超级管理员*/
        Boolean administrator = systemRoleService.isAdministratorByUserId(userId);
        if (administrator){
            userId = null;
        }
        return systemHomepageUserMapper.listSystemHomepageByUserId(userId);
    }

    /**
     * 重置（清空用户已加入排序的模块）
     *
     * @param userInfo ： 登录用户信息
     * @return Result
     * @author WFZ
     * @date 2019/6/26 15:10
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Result reset(TokenUser userInfo) {
        if (! PublicUtil.isNotNull(userInfo.getId())){
            return Result.failed(CodeEnum.USER_ABNORMAL_LOGIN_EXCEPTION);
        }
        QueryWrapper<SystemHomepageUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("user_id = " + userInfo.getId() );
        systemHomepageUserMapper.delete(queryWrapper);
        return Result.succeed();
    }



    /**
     * 根据员工id获取自定义排序后的模块
     * @author      WFZ
     * @param
     * @return      Result
     * @date        2019/6/26 11:34
     */
    private List<SystemHomepageUserDO> listSystemHomepageUserByUserId(Serializable userId){
        QueryWrapper<SystemHomepageUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("user_id = " + userId );
        queryWrapper.orderByAsc("sort");
        return systemHomepageUserMapper.selectList(queryWrapper);
    }
}
