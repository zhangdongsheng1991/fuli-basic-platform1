package com.fuli.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.dto.CompanyMessageDto;
import com.fuli.cloud.model.CompanyMessageDO;
import com.fuli.cloud.vo.CompanyMessageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description:    企业留言
 * @Author:         yhm
 * @CreateDate:     2019/8/8 10:56
 * @Version:        1.0
*/
@Mapper
@Repository
public interface CompanyMessageMapper extends BaseMapper<CompanyMessageDO> {

    /**
     * 获取企业留言信息
     * @param companyMessageDto
     * @return
     */
    List<CompanyMessageVO> getCompanyMsgInfo(CompanyMessageDto companyMessageDto);

    /**
     * 查询企业留言详情
     * @param id 留言ID
     * @return
     */
    CompanyMessageVO getCompanyMsgDetail(@Param("id") Serializable id);

    /**
     * 获取新增企业留言消息
     * @param logoutTime 上次登出时间
     * @return 新增消息个数
     */
    Integer getNewAddMsgCount(@Param("logoutTime") Date logoutTime);

}
