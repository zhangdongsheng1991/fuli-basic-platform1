package com.fuli.cloud.vo;

import com.fuli.cloud.vo.menu.MenuVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:    快捷功能排序请求类
 * @Author:         WFZ
 * @CreateDate:     2019/5/22 14:19
 * @Version:        1.0
*/
@Data
public class SystemModuleVO implements Serializable {

    @ApiModelProperty(value = "服务id")
    private Integer id;

    @ApiModelProperty(value = "服务名称")
    private String modelName;

    @ApiModelProperty(value = "子集")
    private List<MenuVO> children;

}
