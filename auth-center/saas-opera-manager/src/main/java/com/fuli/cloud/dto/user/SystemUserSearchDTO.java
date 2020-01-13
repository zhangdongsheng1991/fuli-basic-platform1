package com.fuli.cloud.dto.user;

import com.fuli.cloud.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <pre>
 * Description:员工搜索实体类
 * </pre>
 *
 * @author chenyi
 * @date 16:15 2019/7/31
 **/
@ApiModel(value = "员工搜索实体类")
@Data
public class SystemUserSearchDTO extends PageDTO {


    @ApiModelProperty(value = "员工姓名")
    private String name;

    @ApiModelProperty( value = "员工手机号")
    private String phoneNumber;

    @ApiModelProperty( value = "状态(1.在职,0. 离职 ,-1 已删除)")
    private Integer status;

    @ApiModelProperty( value = "是否开通运营权限 0-不开通，1-开通")
    private Integer openSystem;

    @ApiModelProperty( value = "当前登录员工id", hidden = true)
    private Integer id;

    @ApiModelProperty( value = "部门id -- 用来做数据权限判断", hidden = true)
    private Integer deptId;

    @ApiModelProperty( value = "岗位id -- 用来做数据权限判断", hidden = true)
    private Integer positionId;

}
