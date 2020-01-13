package com.fuli.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.dto.ListCompanyManagerByUserIdDTO;
import com.fuli.cloud.dto.ListUserGrantByCompanyIdDTO;
import com.fuli.cloud.dto.SystemUserGrantCompanyQryDto;
import com.fuli.cloud.model.SystemUserGrantCompanyDO;
import com.fuli.cloud.vo.ListByUserIdGrantCompanyVO;
import com.fuli.cloud.vo.ListUserGrantByCompanyIdVO;
import com.fuli.cloud.vo.SystemUserGrantCompanyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 *  运营用户与模块、企业数据权限关联表
 * @author WFZ 2019-12-04
 */
@Mapper
@Repository
public interface SystemUserGrantCompanyMapper extends BaseMapper<SystemUserGrantCompanyDO> {

    /**
     * 查询授权企业权限信息
     * @param qryDto 查询参数类
     * @return
     */
    List<SystemUserGrantCompanyVo> getGrantCompanyInfo(SystemUserGrantCompanyQryDto qryDto);


    /**
     * 获取我管理的主体企业列表
     * @author      WFZ
     * @param 	    dto : 入参
     * @return      Result
     * @date        2019/12/4 15:49
     */
    List<ListByUserIdGrantCompanyVO> listMasterCompanyByUserId(ListCompanyManagerByUserIdDTO dto);

    /**
     * 根据主体企业id获取我管理的客户企业列表
     * @author      WFZ
     * @param 	    dto : 入参
     * @return      Result
     * @date        2019/12/4 15:49
     */
    List<ListByUserIdGrantCompanyVO> listClientCompanyByMasterCompanyId(ListCompanyManagerByUserIdDTO dto);

    /**
     * 根据企业id获取已添加的管理人员列表
     * @author      WFZ
     * @param 	    dto : 请求参数
     * @return      Result
     * @date        2019/12/4 15:49
     */
    List<ListUserGrantByCompanyIdVO> listUserGrantByCompanyId(ListUserGrantByCompanyIdDTO dto);

    /**
     * 批量查询已绑定的用户
     * @author      WFZ
     * @param 	    companyId ： 企业id
     * @param 	    moduleId ： 业务线id
     * @param 	    userIds ： 用户id集合
     * @return      Result
     * @date        2019/12/24 15:32
     */
    List<SystemUserGrantCompanyDO> listByCompanyIdAndModuleIdAndUserIds(@Param("companyId") Long companyId , @Param("moduleId")Integer moduleId , @Param("userIds")Set<Integer> userIds);

    /**
     * 批量插入
     * @author      WFZ
     * @param 	    sdo ： 基础参数
     * @param 	    userIds ： 用户id集合
     * @return      Result
     * @date        2019/12/24 15:32
     */
    int batchInsert(@Param("sdo")SystemUserGrantCompanyDO sdo , @Param("userIds")Set<Integer> userIds);
}
