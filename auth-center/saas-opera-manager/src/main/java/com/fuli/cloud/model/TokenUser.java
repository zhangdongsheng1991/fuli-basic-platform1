package com.fuli.cloud.model;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * 运营人员token信息
 * @author yhm
 * @date 2019/07/29
 */
@Data
public class TokenUser implements Serializable {

    /** 用户id*/
    private Integer id;

    private String phone;

    private String realName;

    private String userAccount;

    //    private Long companyId;
    //
    //    /** 当前企业id*/
    //    private Long currentCompanyId;

    //    public Long getCompanyId() {
    //        return currentCompanyId;
    //    }

    public String getUserAccount() {
        return StringUtils.isEmpty(userAccount) ? " " : userAccount;
    }

}
