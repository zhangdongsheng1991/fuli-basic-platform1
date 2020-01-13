package com.fuli.cloud.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description:    企业留言
 * @Author:
 * @CreateDate:     2019/8/9 10:41
 * @Version:        1.0
*/
@Data
@TableName("company_message")
public class CompanyMessageDO extends Model<CompanyMessageDO> implements Serializable {

    private static final long serialVersionUID = 2103518867849051057L;

    @TableId(value="id",type= IdType.AUTO)
    private Long id;

    @ApiModelProperty("姓名")
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50,message = "姓名长度不能超过50")
    private String realName;

    @ApiModelProperty("联系电话")
    @NotBlank(message = "联系电话不能为空")
    @Size(max = 11,message = "联系电话长度不能超过15")
    private String telephone;

    @ApiModelProperty("电子邮箱")
    @NotBlank(message = "电子邮箱不能为空")
    @Email(message = "电子邮箱格式错误")
    private String email;

    @ApiModelProperty("留言内容")
    @NotBlank(message = "留言内容不能为空")
    private String content;

    @ApiModelProperty("处理备注，用于管理系统中处理该留言")
    private String remark;

    @TableField(value="deal_time")
    private Date dealTime;

    @TableField(value="deal_user")
    private String dealUser;

    private Short state;

    @TableField(value="create_time")
    private Date createTime;

}
