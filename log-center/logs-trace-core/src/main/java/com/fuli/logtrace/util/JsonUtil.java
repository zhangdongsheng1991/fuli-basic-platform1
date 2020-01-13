package com.fuli.logtrace.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @Author create by XYJ
 * @Date 2019/7/3 17:19
 **/

public final class JsonUtil {


    public static boolean isjson(String string){
        try {
            JSONObject jsonStr= JSONObject.parseObject(string);
            return  true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean objIsJson(Object obj){
        try {
            String jsonStr= JSON.toJSONString(obj);
            return  true;
        } catch (Exception e) {
            return false;
        }
    }



}
