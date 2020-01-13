package com.fuli.cloud.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**
 * 运营saas系统公告用户关联表
 *
 * @author chenyi 2019-08-02
 */
@Data
@TableName("system_announcement_user")
@FieldNameConstants
public class SystemAnnouncementUserDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 公告id
     */
    private Integer announcementId;

    /**
     * saas用户id
     */
    private Integer userId;

    /**
     * 后台公告历史id
     */
    private Integer groupSentMessageId;

    /**
     * 是否已读 0:未读，1:已读
     */
    private Integer readFlag;

}
