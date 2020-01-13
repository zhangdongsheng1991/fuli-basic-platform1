package com.fuli.cloud.commons.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zenglw
 * @description: model基类
 * @date 2019/4/25 14:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class BaseModel<PK extends Serializable> implements Serializable {
	private static final long serialVersionUID = 6644024729399248836L;
	
	private PK id;
    private Long createUser;
    private Date createTime;
    private Long updateUser;
    private Date updateTime;
    private Date deleteTime;
    private Short deleteFlag;
    
}
