package com.fuli.cloud.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author chenyi
 * @date 2019/7/30
 */
@Data
public class UserIdDTO {

    @Size(min = 1, max = 5000, message = "一次批量操作不能超过{max}")
    @NotEmpty(message = "员工id不能为空")
    @NotNull(message = "员工id不能为空")
    private Set<Integer> ids;

}
