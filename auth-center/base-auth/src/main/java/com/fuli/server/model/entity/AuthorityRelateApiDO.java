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
@TableName("authority_relate_api")
@Data
public class AuthorityRelateApiDO {
    //权限组ID
    private Long authorityGroupId;
    //权限组标识
    private Long apiId;

}
