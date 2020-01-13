package com.fuli.cloud.vo.service;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:    第三方服务模块响应vo
 * @Author:         WFZ
 * @CreateDate:     2019/5/22 14:19
 * @Version:        1.0
*/
@Data
public class ListServiceVO implements Serializable {

    @ApiModelProperty(name = "openedList" ,value = "已开通服务集合")
    private List<SaAsServiceModuleVO> openedList;

    @ApiModelProperty(name = "notOpenList" ,value = "未开通服务集合")
    private List<SaAsServiceModuleVO> notOpenList;


}
