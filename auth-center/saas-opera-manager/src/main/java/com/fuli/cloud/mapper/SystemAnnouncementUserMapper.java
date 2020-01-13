package com.fuli.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.model.SystemAnnouncementUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author chenyi
 * @date 2019/8/2
 */
@Repository
@Mapper
public interface SystemAnnouncementUserMapper extends BaseMapper<SystemAnnouncementUserDO> {
}
