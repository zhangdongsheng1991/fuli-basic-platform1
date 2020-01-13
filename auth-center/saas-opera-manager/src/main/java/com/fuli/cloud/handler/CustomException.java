package com.fuli.cloud.handler;

import com.fuli.cloud.commons.CodeEnum;
import lombok.Data;

/**
 * 自定义异常类
 * @author pcg
 * @date 2019-6-21 13:59:08
 */
@Data
public class CustomException extends RuntimeException{

    private CodeEnum codeEnum;

    public CustomException(CodeEnum codeEnum){
        super(codeEnum.getMsg());
        this.codeEnum = codeEnum;
    }
}
