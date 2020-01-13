package com.fuli.server.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author create by XYJ
 * @Date 2019/8/14 15:24
 **/
@TableName("base_api")
@Data
public class BaseApiDO extends AbstractEntity {
    //雪花算法  id生成策略
    @TableId(value="api_id",type= IdType.ID_WORKER)
    private Long apiId;
    //接口编码
    private String apiCode;
    //接口名称
    private String apiName;
    //接口分类:default-默认分类
    private String apiCategory;
    //资源描述
    private String apiDesc;
    //请求方式
    private String requestMethod;
    //响应类型
    private String contentType;
    //服务ID
    private String serviceId;
    //请求路径
    private String path;
    //优先级
    private Long priority;
    //状态:1-无效 2-有效
    private Integer status;
    //保留数据1-否 2-是 不允许删除
    private Integer isPersist;
    //是否需要认证: 1-无认证 2-身份认证 默认:2
    private Integer isAuth;
    //是否公开: 1-内部的 2-公开的
    private Integer isOpen;
    //类名
    private String className;
    //方法名
    private String methodName;

}
