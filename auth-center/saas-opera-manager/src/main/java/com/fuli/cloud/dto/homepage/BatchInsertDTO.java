package com.fuli.cloud.dto.homepage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:    首页服务管理
 * @Author:         WFZ
 * @CreateDate:     2019/6/26 17:39
 * @Version:        1.0
*/
@Data
public class BatchInsertDTO implements Serializable {

    private static final long serialVersionUID = 3251927912339234034L;

    @ApiModelProperty(value = "首页模块数据集合" , required = true)
    private List<HomepageUserDTO> list;

}
