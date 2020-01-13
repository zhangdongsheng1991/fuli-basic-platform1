package com.fuli.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.user.model.AppUser;
import com.fuli.user.vo.AppUserIdVO;
import com.fuli.user.vo.AppUserIdsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: TODO
 * @Author: WS
 * @CreateDate: 2019/4/18 19:26
 * @Version: 1.0
 */
@Mapper
@Repository
public interface AppUserInfoDao extends BaseMapper<AppUser> {

    /**
     * 分页查询用户id信息
     * @return
     */
    @Select("select id from app_user")
    List<AppUserIdsVO> getUserIds();
    /**
     * @Description:(批量手机号查询批量id)
     * @author      fengjing
     * @date        2019/6/27 9:37
     */
    @Select("<script>select id,phone from app_user au where au.phone IN " +
            " <foreach item='item' index='index' collection='list' open='(' separator=',' close=')'> " +
            " #{item} " +
            " </foreach>" +
            "</script>")
    List<AppUserIdVO> phoneGetUserId(List<String> list);


}
