package com.fuli.user.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.user.model.SaAsDefaultCompanyDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description:    SaaS门户默认企业展示表
 * @Author:         WFZ
 * @CreateDate:     2019/8/5 18:13
 * @Version:        1.0
*/
@Mapper
@Repository
public interface SaAsDefaultCompanyMapper extends BaseMapper<SaAsDefaultCompanyDO> {


}

