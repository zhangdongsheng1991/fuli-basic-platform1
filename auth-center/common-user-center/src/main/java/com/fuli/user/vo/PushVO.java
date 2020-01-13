package com.fuli.user.vo;

import lombok.Data;

/**
 * @Author 易煌
 */
@Data
public class PushVO {

    /**
     * 设置通知标题--标题(必填)
     */
    private String title;

    /**
     * 设置通知文字描述--内容(必填)
     */
    private String text;
    /**
     * 点击通知消息退出登录
     */
    private String logout;
    /**
     * 设备Token 当指定设备传送(例如单播)时,需传该值
     */
    private String deviceToken;

    /**
     * 操作人Id(必填)
     */
    private Long operatorId;

}
