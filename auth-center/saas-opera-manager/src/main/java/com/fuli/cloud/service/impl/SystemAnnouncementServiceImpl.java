package com.fuli.cloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuli.cloud.dto.QueryAnnouncementUserListDTO;
import com.fuli.cloud.mapper.SystemAnnouncementMapper;
import com.fuli.cloud.model.SystemAnnouncementDO;
import com.fuli.cloud.service.SystemAnnouncementService;
import com.fuli.cloud.vo.SystemAnnouncementVO;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenyi
 * @date 2019/8/2
 */
@Service
public class SystemAnnouncementServiceImpl extends ServiceImpl<SystemAnnouncementMapper, SystemAnnouncementDO> implements SystemAnnouncementService {

    @Override
    public List<SystemAnnouncementVO> getSystemAnnouncementVOList(QueryAnnouncementUserListDTO queryAnnouncementUserListDTO) {
        return baseMapper.getSystemAnnouncementVOList(queryAnnouncementUserListDTO);
    }

    @Override
    public List<SystemAnnouncementVO> listOnePage(QueryAnnouncementUserListDTO queryAnnouncementUserListDTO) {

        PageHelper.startPage(queryAnnouncementUserListDTO.getPageNum(), queryAnnouncementUserListDTO.getPageSize());

        return baseMapper.getSystemAnnouncementVOList(queryAnnouncementUserListDTO);
    }
}
