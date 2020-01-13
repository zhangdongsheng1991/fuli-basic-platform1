package com.fuli.user.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.user.model.DataRecordUserDO;
import com.fuli.user.model.SaAsDefaultCompanyDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description:    用户表 - 同步中台记录
 * @Author:         WFZ
 * @CreateDate:     2019/10/14 11:26
 * @Version:        1.0
*/
@Mapper
@Repository
public interface DataRecordUserMapper extends BaseMapper<DataRecordUserDO> {


}

