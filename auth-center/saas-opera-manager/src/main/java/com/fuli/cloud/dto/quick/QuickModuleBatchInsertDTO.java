package com.fuli.cloud.dto.quick;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:    快捷功能批量插入请求类
 * @Author:         WFZ
 * @CreateDate:     2019/5/22 14:19
 * @Version:        1.0
*/
@Data
public class QuickModuleBatchInsertDTO implements Serializable {

    @ApiModelProperty(name = "list" ,value = "快捷模块数据集合", required = true)
    private List<BatchInsertDTO> list;

}
