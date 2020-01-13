package com.fuli.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.server.model.entity.URLWhiteDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface URLWhiteDao extends BaseMapper<URLWhiteDO> {

    List<URLWhiteDO> allURLWhite();

}
