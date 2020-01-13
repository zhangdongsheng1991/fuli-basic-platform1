package com.fuli.logtrace.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * @Author create by XYJ
 * @Date 2019/7/3 19:18
 **/

public final class IOUtils {

    public static String inputstream2Str(InputStream in, String encode) {
        String str = "";
        try {
            if (StringUtils.isEmpty(encode)) {
                // 默认以utf-8形式
                encode = "utf-8";
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));
            StringBuffer sb = new StringBuffer();

            while ((str = reader.readLine()) != null) {
                sb.append(str).append("\n");
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 通过reader获取requestbody
     * @param request
     * @return
     */
    public static String getBodyData(HttpServletRequest request) {
        StringBuffer data = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine()))
                data.append(line);
        } catch (IOException e) {
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
        return data.toString();
    }

    /**
     * 获取POST请求中Body参数
     *
     * @return 字符串
     */
    public static String getParm(InputStream inputStream) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try {
            br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }

        return sb.toString();
    }

}
