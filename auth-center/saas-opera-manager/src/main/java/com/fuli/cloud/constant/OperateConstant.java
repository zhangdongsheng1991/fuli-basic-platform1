package com.fuli.cloud.constant;

/**
 * 运营模块相关常量
 * @author yhm
 * @date 2019/5/5
 */
public interface OperateConstant {
    /**
     * 试用留言消息状态：待反馈
     */
    public static final short MESSAGE_STATE_UNDEAL = 0;

    /**
     * 试用留言消息状态：有效反馈
     */
    public static final short MESSAGE_STATE_VALID = 1;

    /**
     * 试用留言消息状态：无效反馈
     */
    public static final short MESSAGE_STATE_INVALID = 2;

    /**
     * 新增消息个数
     */
    public static final String ADD_MSG_COUNT = "addMsgCount";

}
