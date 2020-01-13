package com.fuli.server.exception;

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
}
