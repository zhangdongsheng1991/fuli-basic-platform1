package com.fuli.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuli.cloud.dto.QueryAnnouncementUserListDTO;
import com.fuli.cloud.model.SystemAnnouncementDO;
import com.fuli.cloud.vo.SystemAnnouncementVO;

import java.util.List;

/**
 * @author chenyi
 * @date 2019/8/2
 */
public interface SystemAnnouncementService extends IService<SystemAnnouncementDO> {

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


    /**
     * <pre>
     * Description: 根据查询条件返回一页公告数据集合
     * </pre>
     *
     * @param queryAnnouncementUserListDTO 分页查询参数
     * @return 一页公告数据集合
     * @author chenyi
     * @date 11:47 2019/8/2
     **/
    List<SystemAnnouncementVO> listOnePage(QueryAnnouncementUserListDTO queryAnnouncementUserListDTO);
}
