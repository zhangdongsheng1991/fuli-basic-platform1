package com.fuli.user.vo;

import lombok.Data;

/**
 * 中台响应数据
 * @author pcg
 * @date 2019-4-22 17:13:13
 * @version 1.0
 */
@Data
public class PlatformResponse {
    private Integer code;
    private Object data;
    private String message;
    private long timestamp;
}
