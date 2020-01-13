package com.fuli.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.dto.QueryAnnouncementUserListDTO;
import com.fuli.cloud.model.SystemAnnouncementDO;
import com.fuli.cloud.vo.SystemAnnouncementVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <pre>
 * Description:
 * </pre>
 *
 * @author chenyi
 * @date 2019/8/2
 */
@Repository
@Mapper
public interface SystemAnnouncementMapper extends BaseMapper<SystemAnnouncementDO> {

    /**
     * <pre>
     * Description: 根据查询条件返回公告数据集合
     * </pre>
     *
     * @param queryAnnouncementUserListDTO 查询条件
     * @return 公告数据集合
     * @author chenyi
     * @date 11:07 2019/8/2
     **/
    List<SystemAnnouncementVO> getSystemAnnouncementVOList(QueryAnnouncementUserListDTO queryAnnouncementUserListDTO);
}
