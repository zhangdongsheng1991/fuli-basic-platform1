package com.fuli.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuli.cloud.dto.EmpCountDTO;
import com.fuli.cloud.dto.RoleInfoDTO;
import com.fuli.cloud.dto.user.SystemUserSaveDTO;
import com.fuli.cloud.dto.user.SystemUserSearchDTO;
import com.fuli.cloud.model.SystemUserDO;
import com.fuli.cloud.model.TokenUser;
import com.fuli.cloud.vo.SysUserExportVO;
import com.fuli.cloud.vo.SystemUserEditOrViewVO;
import com.fuli.cloud.vo.SystemUserListVO;

import java.util.List;
import java.util.Set;

/**
 * @author chenyi
 * @date 2019/7/29
 */
public interface SystemUserService extends IService<SystemUserDO> {


    /**
     * <pre>
     * Description:根据身份证号查询员工
     * </pre>
     *
     * @param certificateCard 身份证号码
     * @return SystemUserDO 员工
     * @author chenyi
     * @date 16:46 2019/7/29
     **/
    SystemUserDO getOneByCertificateCard(String certificateCard);


    /**
     * <pre>
     * Description: 根据身份证号码判断一个员工是否存在
     * </pre>
     *
     * @param certificateCard 身份证号码
     * @return 是否存在
     * @author chenyi
     * @date 17:19 2019/8/1
     **/
    boolean exsitByCertificateCard(String certificateCard);

    /**
     * 新增一个员工
     *
     * @param systemUserSaveDTO 员工入参
     * @param tokenUser         tokenUser
     * @return 添加结果
     */
    boolean saveOne(SystemUserSaveDTO systemUserSaveDTO, TokenUser tokenUser);

    /**
     * 根据手机号查询用户信息
     * @author      WFZ
     * @param 	    phone : 手机号
     * @return      Result
     * @date        2019/7/31 9:30
     */
    SystemUserDO getOneByPhone(String phone);


    /**
     * <pre>
     * Description: 根据手机号判断员工是否存在
     * </pre>
     *
     * @param phoneNumber 手机号
     * @return 是否存在
     * @author chenyi
     * @date 17:17 2019/8/1
     **/
    boolean exsitByPhoneNumber(String phoneNumber);

    /**
     * <pre>
     * Description: 根据员工id查询编辑或者查看的VO
     * </pre>
     *
     * @param ids 员工id集合
     * @return 编辑或查看的VO
     * @author chenyi
     * @date 17:44 2019/7/30
     **/
    List<SystemUserEditOrViewVO> getSystemUserEditOrViewVOList(Set<Integer> ids);


    /**
     * <pre>
     * Description: 授权角色保存
     * </pre>
     *
     * @param roleInfoDto 授权角色
     * @param tokenUser   登录用户token
     * @author chenyi
     * @date 20:01 2019/7/30
     **/
    boolean saveRoleInfo(SystemUserDO systemUserDO, RoleInfoDTO roleInfoDto, TokenUser tokenUser);

    /**
     * @param systemUserDO 用户员工
     * @param tokenUser 登录用户Token
     * @author chenyi
     * @date 20:01 2019/7/30
     */
    boolean dimission(SystemUserDO systemUserDO, TokenUser tokenUser);


    /**
     * <pre>
     * Description: 分页查询员工列表
     * </pre>
     *
     * @param systemUserSearchDTO 分页查询参数
     * @return 返回一页的员工列表数据
     * @author chenyi
     * @date 16:42 2019/7/31
     **/
    List<SystemUserListVO> listOnePage(SystemUserSearchDTO systemUserSearchDTO);


    /**
     * <pre>
     * Description:
     * </pre>
     *
     * @param systemUserSearchDTO 导出查询条件
     * @return 导出列表数据
     * @author chenyi
     * @date 18:55 2019/8/1
     **/
    List<SysUserExportVO> getSysUserExportVOList(SystemUserSearchDTO systemUserSearchDTO);


    /**
     * 根据身份证号，查询离职员工
     *
     * @param certificateCard 身份证号
     * @return
     * @author chenyi
     * @date 10:26 2019/8/6
     **/
    SystemUserEditOrViewVO getOneSystemUserEditOrViewVO(String certificateCard);


    /**
     * <pre>
     * Description: 统计本月在职员工人数
     * </pre>
     *
     * @return 本月在职员工人数
     * @author chenyi
     * @date 10:49 2019/8/16
     **/
    int statisticsEmpInCount(EmpCountDTO empCountDTO);


    /**
     * <pre>
     * Description: 统计本月离职员工人数
     * </pre>
     *
     * @return 统计本月离职员工人数
     * @author chenyi
     * @date 10:50 2019/8/16
     **/
    int statisticsEmpOutCount(EmpCountDTO empCountDTO);


    /**
     * <pre>
     * Description: 一个失败全部失败
     * </pre>
     *
     * @author chenyi
     * @date 16:05 2019/8/28
     **/
    void saveList(List<SystemUserDO> toSaveList);
}
