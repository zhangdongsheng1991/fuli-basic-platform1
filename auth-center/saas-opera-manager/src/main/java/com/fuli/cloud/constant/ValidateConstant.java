package com.fuli.cloud.constant;

/**
 * 校验参数常量 包含正则表达式和提示语
 * @author pcg
 * @date 2019-6-21 14:25:28
 */
public interface ValidateConstant {

    /**
     * 角色名称为空提示语
     */
    String ROLE_NAME_NOT_BLANK_MSG = "角色名称不能为空";
    /**
     * 角色id为空提示语
     */
    String ROLE_ID_NOT_NULL_MSG = "角色id不能为空";
    /**
     * 角色名称长度
     */
    int ROLE_NAME_LENGTH_MAX = 20;
    /**
     * 角色名称规则正则表达式
     */
    String ROLE_NAME_RULE = "^(?![\\.\\_\\-]+$)[0-9A-Za-z\\u4e00-\\u9fa5\\.\\_\\-]{1,20}$";
    /**
     * 不满足角色名称规则提示语
     */
    String ROLE_NAME_RULE_MSG = "角色名称最多支持输入20位中文、英文、数字或常见符号，不可全部为符号";
    /**
     * 角色描述长度
     */
    int ROLE_DESC_LENGTH = 200;
    /**
     * 角色描述长度提示语
     */
    String ROLE_DESC_LENGTH_MSG = "角色描述不超过200个字符";
    /**
     * 角色审核退回长度
     */
    int ROLE_APPROVAL_MESSAGE_MAX = 200;
    /**
     * 角色审核退回长度提示语
     */
    String ROLE_APPROVAL_MESSAGE_MSG = "退回原因不超过 {max} 个字符";
    /**
     * 角色审核退回原因不能为空提示语
     */
    String ROLE_APPROVAL_MESSAGE_NOT_BLANK_MSG = "退回原因不能为空";
    /**
     * 角色分配菜单为空提示语
     */
    String MENU_NOT_EMPTY = "请给角色配置权限";

    /**
     * 是否只显示选中的菜单 提示语
     */
    String ONLY_CHECKED_MSG = "是否只显示选中的菜单值只能为 1 或 0";


}
