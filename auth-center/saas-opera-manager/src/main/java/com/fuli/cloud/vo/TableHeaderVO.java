package com.fuli.cloud.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description:    返回给前端的自定义列
 * @Author:         WFZ
 * @CreateDate:     2019/7/5 10:42
 * @Version:        1.0
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableHeaderVO implements Serializable {

    private static final long serialVersionUID = -1669426780638887422L;

    @ApiModelProperty(name = "title",value = "列名")
    private String title;

    @ApiModelProperty(name = "field",value = "列对应的字段")
    private String field;


}
