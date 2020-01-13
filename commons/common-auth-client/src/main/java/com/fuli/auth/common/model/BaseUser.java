package com.fuli.auth.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * @Description:    TOKEN参数基类
 * @Author:         WFZ
 * @CreateDate:     2019/9/5 17:21
 * @Version:        1.0
*/
@Data
public class BaseUser implements UserDetails , Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "用户密码",hidden = true)
    private String password;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "系统帐号")
    private String userAccount;

    @ApiModelProperty(value = "当前企业id")
    private String currentCompanyId;

    @ApiModelProperty(value = "员工id")
    private String employeeId;

    @ApiModelProperty(value = "客户端id")
    private String clientId;

    @ApiModelProperty(value = "社会企业信用代码")
    private String companyCreditCode;

    @ApiModelProperty(value = "当前时间")
    private Date loginTime;

    /*@ApiModelProperty(value = "菜单id集合")
    private Set<String> menuIds;*/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
        /*Collection<GrantedAuthority> collection = new HashSet<>();
        if (! CollectionUtils.isEmpty(menuIds)){
            menuIds.forEach( p -> collection.add(new SimpleGrantedAuthority(p)));
        }
        return collection;*/
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername(){
        return this.phone;
    }
}
