package com.fuli.user.dto;


import lombok.Data;

/**
 * @Description:    实名校验3要数
 * @Author:         WFZ
 * @CreateDate:     2019/8/7 16:05
 * @Version:        1.0
*/
@Data
public class RealNameInTaiwanDTO {

    /**
     * 用户真实姓名
     */
    private String name;
    /**
     * 身份证号码
     */
    private String idNum;
    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 用户自拍图片链接（图片大小必须小于100k）
     */
    private String image;
}
