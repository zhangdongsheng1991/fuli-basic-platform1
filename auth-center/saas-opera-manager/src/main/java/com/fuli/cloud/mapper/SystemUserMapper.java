package com.fuli.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.dto.EmpCountDTO;
import com.fuli.cloud.dto.user.SystemUserEditOrViewQueryDTO;
import com.fuli.cloud.dto.user.SystemUserSearchDTO;
import com.fuli.cloud.model.SystemUserDO;
import com.fuli.cloud.vo.SysUserExportVO;
import com.fuli.cloud.vo.SystemUserEditOrViewVO;
import com.fuli.cloud.vo.SystemUserListVO;
import com.fuli.cloud.vo.employee.UserArchivesVO;
import com.fuli.cloud.vo.employee.UserPictureFrameVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 *  用户表
 * @author WFZ 2019-07-29
 */
@Mapper
@Repository
public interface SystemUserMapper extends BaseMapper<SystemUserDO> {

    /**
     * 头像框 （包含角色名与是否开启悬浮球）
     * @author      WFZ
     * @param 	    userId : 用户id
     * @return      Result
     * @date        2019/8/1 15:31
     */
    UserPictureFrameVO findMyPictureFrame(@Param("userId") Serializable userId);

    /**
     * 获取我的档案（包含角色-部门-岗位等信息）
     * @author      WFZ
     * @param 	    userId : 用户id
     * @return      Result
     * @date        2019/8/1 15:02
     */
    UserArchivesVO findUserArchives(@Param("userId") Serializable userId);
    /**
     * <pre>
     * Description:根据员工id查询编辑或者查看的VO
     * </pre>
     *
     * @param systemUserEditOrViewQueryDTO idsStr 员工id 逗号分隔
     * @return 查询编辑或者查看的VO
     * @author chenyi
     * @date 17:49 2019/7/30
     **/
    List<SystemUserEditOrViewVO> getSystemUserEditOrViewVOList(SystemUserEditOrViewQueryDTO systemUserEditOrViewQueryDTO);


    /**
     * <pre>
     * Description: 分页查询一页员工列表数据
     * </pre>
     *
     * @param systemUserSearchDTO 分页查询参数
     * @return 返回一页员工列表数据
     * @author chenyi
     * @date 16:48 2019/7/31
     **/
    List<SystemUserListVO> listOnePage(SystemUserSearchDTO systemUserSearchDTO);


    /**
     * <pre>
     * Description: 根据参数查询需要导出的员工数据
     * </pre>
     *
     * @param systemUserSearchDTO 导出查询参数
     * @return 需要导出的员工数据
     * @author chenyi
     * @date 19:45 2019/8/1
     **/
    List<SysUserExportVO> getSysUserExportVOList(SystemUserSearchDTO systemUserSearchDTO);



    /**
     * <pre>
     * Description: 统计本月员工人数
     * </pre>
     *
     * @author chenyi
     * @date 11:43 2019/8/16
     **/
    int statisticsEmpCount(EmpCountDTO empCountDTO);
}
