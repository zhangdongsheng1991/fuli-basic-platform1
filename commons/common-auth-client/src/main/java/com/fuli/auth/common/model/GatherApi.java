package com.fuli.auth.common.model;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * api 实体
 * @Author create by XYJ
 * @Date 2019/8/14 15:24
 **/

public class GatherApi implements Serializable {

    @ApiModelProperty(value = "接口名称")
    private String apiName;

    @ApiModelProperty(value = "接口编码")
    private String apiCode;

    @ApiModelProperty(value = "接口分类:default-默认分类")
    private String apiCategory;

    @ApiModelProperty(value = "资源描述")
    private String apiDesc;

    @ApiModelProperty(value = "请求方式")
    private String requestMethod;

    @ApiModelProperty(value = "响应类型")
    private String contentType;

    @ApiModelProperty(value = "服务ID")
    private String serviceId;

    @ApiModelProperty(value = "请求路径")
    private String path;

    @ApiModelProperty(value = "优先级")
    private Long priority;

    @ApiModelProperty(value = "状态:1-无效 2-有效")
    private Integer status;

    @ApiModelProperty(value = "保留数据1-否 2-是 不允许删除")
    private Integer isPersist;

    @ApiModelProperty(value = "是否需要认证: 1-无认证 2-身份认证 默认:2")
    private Integer isAuth;

    @ApiModelProperty(value = "是否公开: 1-内部的 2-公开的")
    private Integer isOpen;

    @ApiModelProperty(value = "类名")
    private String className;

    @ApiModelProperty(value = "方法名")
    private String methodName;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    public String getApiCode() {
        return apiCode;
    }

    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiCategory() {
        return apiCategory;
    }

    public void setApiCategory(String apiCategory) {
        this.apiCategory = apiCategory;
    }

    public String getApiDesc() {
        return apiDesc;
    }

    public void setApiDesc(String apiDesc) {
        this.apiDesc = apiDesc;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsPersist() {
        return isPersist;
    }

    public void setIsPersist(Integer isPersist) {
        this.isPersist = isPersist;
    }

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }

    public Integer getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "GatherApi{" +
                "apiName='" + apiName + '\'' +
                ", apiCategory='" + apiCategory + '\'' +
                ", apiDesc='" + apiDesc + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", contentType='" + contentType + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", path='" + path + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", isPersist=" + isPersist +
                ", isAuth=" + isAuth +
                ", isOpen=" + isOpen +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                '}';
    }
}
