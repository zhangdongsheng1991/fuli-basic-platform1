package com.fuli.cloud.vo.homepage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:    首页模块响应vo
 * @Author:         WFZ
 * @CreateDate:     2019/6/26 10:22
 * @Version:        1.0
 */
@Data
public class HomepageModuleListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "左列数据集合")
    private List<HomepageModuleVO> leftList;

    @ApiModelProperty(value = "中列数据集合")
    private List<HomepageModuleVO> middleList;

    @ApiModelProperty(value = "右列数据集合")
    private List<HomepageModuleVO> rightList;

}
