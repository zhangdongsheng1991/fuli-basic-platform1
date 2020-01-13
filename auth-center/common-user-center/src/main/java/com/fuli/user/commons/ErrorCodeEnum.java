package com.fuli.user.commons;

/**
 * 
 * @Description: 描述
 * @Author: chenyi
 * @CreateDate: 2019/4/16 12:52
 */
public enum ErrorCodeEnum {

	
	/**
	 * 编码 -- 具体服务
	0001    consumer-message-queue ; fuli-cloud-app-message ; provider-message-service-impl
	0002    ewallet-server
	0003    oss-server
	0004    saas-server
	0005    sms-server
	0006    system-server
	0007    upush-message
	0008    user-center
	 */
	
	
	
	ERROR("-1", "操作失败"),
	//===========================system-server 开始================================
	/**参数输入不合法！*/
	SYSTEM_SERVER_20200060001("20200060001", "参数输入不合法！"),
	/**内部异常*/
	SYSTEM_SERVER_20200060002("20200060002", "内部异常！"),
	//===========================system-server 结束================================
	
	
	
    /**
     * 消息的消费队列不能为空
     */
    MESSAGE_SDK10050001("10050001", "消息的消费队列不能为空"),

    /**
     * 数据库中消息数据保存失败
     */
    MESSAGE_SDK10050002("10050002", "数据库中消息数据保存失败,messageId=%s"),

    /**
     * 数据库中消息数据删除失败
     */
    MESSAGE_SDK10050003("10050003", "数据库中消息数据删除失败,messageId=%s"),

    /**
     * 目标接口参数不能为空
     */
    MESSAGE_SDK10050005("10050005", "目标接口参数不能为空"),
    /**
     * 根据消息id查找的消息为空
     */
    MESSAGE_SDK10050006("10050006", "根据消息id查找的消息为空,messageId=%s"),

    /**
     * 消息数据不能为空
     */
    MESSAGE_SDK10050007("10050007", "消息数据不能为空"),

    /**
     * 消息体不能为空
     */
    MESSAGE_SDK10050008("10050008", "消息体不能为空,messageId=%s"),

    /**
     * 消息ID不能为空
     */
    MESSAGE_SDK10050009("10050009", "消息ID不能为空"),

    /**
     * 参数异常
     */
    MESSAGE_SDK10050010("10050010", "参数异常"),

	/** 注解使用错误，方法的第一个参数必须为FuliMqMessage类型*/
	MESSAGE_SDK10050011("10050011", "注解使用错误，方法[%s]的第一个参数必须为FuliMqMessage类型"),

    /**
     * 消息中心接口异常
     */
    MESSAGE_SDK10050004("10050004", "消息中心接口异常,message=%s, messageId=%s"),


    /**
     * 微服务不在线,或者网络超时
     */
    MESSAGE_SDK99990002("99990002", "微服务不在线,或者网络超时"),

    ;

    private String code;
    private String msg;

    /**
     * Msg string.
     *
     * @return the string
     */
    public String msg() {
        return msg;
    }

    /**
     * Code int.
     *
     * @return the int
     */
    public String code() {
        return code;
    }

    ErrorCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * Gets enum.
     *
     * @param code the code
     * @return the enum
     */
    public static ErrorCodeEnum getEnum(String code) {
        for (ErrorCodeEnum ele : ErrorCodeEnum.values()) {
            if (ele.code().equals(code)) {
                return ele;
            }
        }
        return null;
    }
}
