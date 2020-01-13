package com.fuli.cloud.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * 岗位实体类
 * @author yhm
 * @date 2019/06/25
 */
@Data
@FieldNameConstants
public class PositionAddDto implements Serializable {

    private static final long serialVersionUID = -8279781225638195501L;

    @ApiModelProperty(value = "部门ID",required = true)
    @NotNull(message = "部门不能为空")
    private Integer departmentId;

    @ApiModelProperty(value = "岗位名称",required = true)
    @NotBlank(message = "岗位名称不能为空")
    @Pattern(regexp = "^(?![\\.\\_\\-\\(\\)]+$)[0-9A-Za-z\\u4e00-\\u9fa5\\.\\_\\-\\(\\)]{1,20}$"
            ,message = "岗位名称只支持中文、英文、数字、符号（._-/），不可全部为符号，最长20个字符")
    private String name;

    @ApiModelProperty(value = "岗位编码")
    @Pattern(regexp = "^[0-9a-zA-Z]{0,20}$",message = "岗位编码只支持英文、数字，最长20个字符")
    private String code;

    @ApiModelProperty(value = "父级岗位ID")
    private Integer parentId;

}