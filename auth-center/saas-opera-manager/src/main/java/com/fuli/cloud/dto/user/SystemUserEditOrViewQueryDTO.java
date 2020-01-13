package com.fuli.cloud.dto.user;

import lombok.Data;

/**
 * @author chenyi
 * @date 2019/8/6
 */
@Data
public class SystemUserEditOrViewQueryDTO {

    private String idsStr;

    private String certificateCard;

    private Integer status;

}
