package com.fuli.server.exception;

import org.apache.commons.lang.StringUtils;

/**
 * @author xyj
 **/
public class FuliAssert {

   public static void unauthorized(int code,String msg){

        throw new BaseException(new ErrorType() {

            @Override
            public int getCode() {
                return code;
            }

            @Override
            public String getMsg() {
                return msg;
            }
        });
   }

    public static void isNull(String param){
        if (StringUtils.isEmpty(param)){
            throw new BaseException(new ErrorType() {

                @Override
                public int getCode() {
                    return 10501;
                }

                @Override
                public String getMsg() {
                    return "参数"+param+"不能为空";
                }
            });
        }

    }
}
