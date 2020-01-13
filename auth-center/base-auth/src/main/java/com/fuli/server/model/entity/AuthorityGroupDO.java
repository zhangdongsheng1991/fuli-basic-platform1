package com.fuli.server.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * api 权限组
 *
 * @Author create by XYJ
 * @Date 2019/8/14 15:05
 **/
@TableName("base_authority_group")
@Data
public class AuthorityGroupDO extends AbstractEntity {
    //权限组ID
    private Long groupId;
    //权限组标识
    private String authority;
    //权限组名称
    private String groudName;
    //状态，1-启用 2-禁用
    private Integer status;

}
