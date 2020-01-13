package com.fuli.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fuli.user.vo.CompanyByUserIdVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description:    APP用户实体
 * @Author:         FZ
 * @CreateDate:     2019/4/15 16:30
 * @Version:        1.0
 */
@Data
@TableName("app_user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppUser implements Serializable {

    private static final long serialVersionUID = -5886012896705137070L;

    @ApiModelProperty("主键")
    @TableId(value="id",type= IdType.ID_WORKER)
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /**
     * 电话(唯一)
     */
    @ApiModelProperty("电话")
    private String phone;

    /**
     * 用户在集团平台系统的唯一id
     */
    @ApiModelProperty("用户在集团平台系统的唯一id")
    private String openid;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 用户支付密码
     */
    @ApiModelProperty("支付密码")
    private String payPassword;

    /**
     * 是否设置支付密码（0 未设置，1已设置）
     */
    @ApiModelProperty("是否设置支付密码（0 未设置，1已设置）")
    private Integer payPasswordFlag;

    /**
     * 真实姓名
     */
    @ApiModelProperty("真实姓名")
    private String realName;

    /**
     * 头像
     */
    @ApiModelProperty("头像地址")
    private String headImgUrl;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 性别：0-默认值未知，1-男，2-女，3-保密
     */
    @ApiModelProperty("性别：0-默认值未知，1-男，2-女，3-保密")
    private Integer gender;

    /**
     * 是否实名认证 0:未认证 1:已认证
     */
    @ApiModelProperty("是否实名认证 0:未认证 1:已认证")
    private Integer isIdentification;

    /**
     * 中台用户状态:(1.正常, 0.离职 ,-1.删除)
     */
    @ApiModelProperty("中台用户状态")
    private Integer status;

    /**
     * 本系统用户状态:(1.正常, 0.离职 ,-1.删除)
     */
    @ApiModelProperty("本系统用户状态:(1.正常, 0.离职 ,-1.删除)")
    private Integer thisStatus;

    /**
     * 证件类型1.身份证，2护照号
     */
    @ApiModelProperty("证件类型1.身份证，2护照号")
    private Integer certificateType;

    /**
     * 证件号码
     */
    @ApiModelProperty("证件号码")
    private String certificateCard;

    /**
     * 新用户来源(fljr：本地注册 赋力 )
     */
    @ApiModelProperty("用户来源")
    private String userFrom;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 手势开关(0.未设置，1已设置)
     */
    @ApiModelProperty("手势开关(0.未设置，1已设置)")
    private Integer gestureSwitch;

    /**
     * 是否显示手势轨迹(0.否，1.是)
     */
    @ApiModelProperty("是否显示手势轨迹(0.否，1.是)")
    private Integer isDisplayTrajectory;

    /**
     * 手势密码
     */
    @ApiModelProperty("手势密码")
    private String gesturePassword;

    /**
     * 身份证正面图片
     */
    @ApiModelProperty("身份证正面图片")
    private String imgPositive;

    /**
     * 身份证反面图片
     */
    @ApiModelProperty("身份证反面图片")
    private String imgReverse;

    /**
     * 首页金额是否显示标识 0：不显示；1：显示
     */
    @ApiModelProperty("首页金额是否显示标识 0：不显示；1：显示")
    private Integer homeAmountFlag;

    @TableField(exist=false)
    @ApiModelProperty("企业列表")
    private List<CompanyByUserIdVO> companys;

    /**
     * 中台用户id
     */
    @TableField(exist=false)
    @ApiModelProperty("中台用户id")
    private String middleUserId;

    @ApiModelProperty(name = "haoYiLian", value = "是否开通数科好易联电子钱包 0:未开通 1:已开通")
    private Integer haoYiLian;

    @ApiModelProperty(name = "huaXiaPds", value = "是否开通数科华夏pds电子钱包 0:未开通 1:已开通")
    private Integer huaXiaPds;

    public AppUser() {
    }


}
