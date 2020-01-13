package com.fuli.cloud.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 * Description:
 * </pre>
 *
 * @author chenyi
 * @date 2019/8/2
 */
@Data
@TableName("system_announcement")
public class SystemAnnouncementDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * 消息链接
     */
    private String messageLink;

    public SystemAnnouncementDO() {
    }

}