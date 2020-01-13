package com.fuli.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.dto.homepage.HomepageUserDTO;
import com.fuli.cloud.model.SystemHomepageUserDO;
import com.fuli.cloud.vo.homepage.HomepageModuleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 *  首页服务模块与用户中间表
 * @author WFZ 2019-07-31
 */
@Repository
@Mapper
public interface SystemHomepageUserMapper extends BaseMapper<SystemHomepageUserDO> {

    /**
     * 根据用户id获取首页模块 -- 用户id为空表示是超级管理员
     * @author      WFZ
     * @param 	    userId : 用户id
     * @return      Result
     * @date        2019/7/31 17:48
     */
    List<HomepageModuleVO> listSystemHomepageByUserId(@Param("userId") Serializable userId);

    /**
     * 批量插入
     * @author      WFZ
     * @param 	    list: 集合
     * @param 	    userId: 用户id
     * @return      int ：影响的行数
     * @date        2019/6/25 17:08
     */
    int batchInsert(List<HomepageUserDTO> list, Serializable userId);
}
