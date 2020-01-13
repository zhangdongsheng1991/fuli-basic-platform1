package com.fuli.server.api.model.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: XYJ
 * @Date: 2019/8/5 15:01
 * 实体类父类
 */
@Data
public abstract class AbstractEntity extends Model implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    public String createTime;
    /**
     * 修改时间
     */
    public String updateTime;

}