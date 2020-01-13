package com.fuli.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.user.model.AppUser;
import com.fuli.user.vo.AppUserVo;
import com.fuli.user.vo.CompanyByUserIdVO;
import com.fuli.user.vo.RoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @Description:    用户DAO
* @Author:         FZ
* @CreateDate:     2019/4/15 17:24
* @Version:        1.0
*/
@Mapper
@Repository
public interface AppUserDao extends BaseMapper<AppUser> {

	/**
	 * 返回 oauth 验证
	 * @param phone
	 * @return
	 */
	@Select("SELECT id, phone, username, password, real_name, user_from, this_status, is_identification  FROM app_user WHERE this_status != -1 AND ( phone = #{phone} or username = #{phone}) LIMIT 1 ")
	AppUserVo findUserOauthByPhone(@Param("phone") String phone);

	/**
	 * APP 登录
	 * @author      WFZ
	 * @param 	    phone
	 * @return      Result
	 * @date        2019/8/23 15:34
	 */
	@Select("SELECT id, phone, username, password, real_name, user_from, this_status,is_identification  FROM app_user WHERE this_status != -1 AND ( phone = #{phone} or username = #{phone}) LIMIT 1 ")
	AppUser findAppUserByPhone(@Param("phone") String phone);

	/**
	 * 根据用户id获取企业列表
	 * @param userId :用户id
	 * @param openSaAs :1-表示只展示有SaaS门户权限的企业
	 * @return List<CompanyByUserIdVO>
	 * @author WFZ
	 * @date 2019/8/5 16:05
	 */
	List<CompanyByUserIdVO> listCompanyByUserIdAndOpenSaAs(@Param("userId") Long userId, @Param("openSaAs") Integer openSaAs);

	/**
	 * 根据用户id获取企业列表 --  APP根据用户id获取
	 * @param userId :用户id
	 * @return List<CompanyByUserIdVO>
	 * @author WFZ
	 * @date 2019/8/5 16:05
	 */
	List<CompanyByUserIdVO> listCompanyByUserId(@Param("userId") Long userId);

	/**
	 * 根据用户id和企业id获取用户信息-- 中台获取token用
	 * @author      WFZ
	 * @param 	    userId ： 用户id
	 * @param 	    companyId ： 企业id
	 * @return      Result
	 * @date        2019/8/12 9:51
	 */
	AppUserVo findCompanyAndUserById(@Param("userId") Serializable userId, @Param("companyId") Serializable companyId);


	/**
	 * 根据用户id和企业openId获取用户信息-- 白条
	 * @author      WFZ
	 * @param 	    userId ： 用户id
	 * @param 	    companyOpenId ： 企业openId
	 * @return      Result
	 * @date        2019/8/12 9:51
	 */
	CompanyByUserIdVO findCompanyAndUserByOpenId(@Param("userId") Long userId, @Param("companyOpenId") String companyOpenId);

    /**
     * 根据用户id和企业id获取用户信息-- 切换token时查询（因为一个用户可能即是员工也是项目人员）
     * @author      WFZ
     * @param 	    userId ： 用户id
     * @param 	    companyId ： 企业id
     * @return      Result
     * @date        2019/8/12 9:51
     */
    AppUserVo listCompanyAndUserById(@Param("userId") Long userId, @Param("companyId") Long companyId);

	/**
	 * 根据手机号获取用户信息
	 * @author      xq
	 * @param 	    phone 手机号
	 * @return      AppUser
	 * @date        2019/4/15 18:05
	 */
	@Select("SELECT id, phone,openId,`password`,user_from,username,`status`,is_identification,real_name,pay_password,pay_password_flag FROM app_user WHERE phone = #{phone} LIMIT 1 ")
	AppUser findUserByPhone(@Param("phone") String phone);


	/**
	 * 根据中台用户id查询用户信息
	 * @param openId 中台用户唯一id
	 * @return AppUser
	 * @author AnJiao
	 * @date 2019/4/24
	 */
	@Select("SELECT id,username,phone,user_from,real_name,is_identification FROM app_user WHERE openId = '${openId}' LIMIT 1 ")
	AppUser findUserByOpenId(@Param("openId") String openId);


	/**
	 * 匹配系统帐号
	 * @param username
	 * @return
	 */
	@Select("select count(*) from app_user where username like concat(#{username},'%')")
	Integer getCountByLikeUserName(@Param("username") String username);


	/**
	 * @Description:(分页查询用户手机号)
	 * @author      fengjing
	 * @date        2019/5/6 14:28
	 */
	@Select("SELECT id,phone from app_user")
	List<AppUser> getAllUser();


	/**
	 * 将员工默认企业状态清空
	 * @author      WFZ
	 * @param        userId : 企业id
	 * @return      Result
	 * @date        2019/8/12 16:20
	 */
	@Update("update employee set is_show = 0 where user_id = #{userId}")
	int  removeEmployeeIsShowByUserId(@Param("userId")Long userId);

	/**
	 *
	 * 新增员工默认企业
	 * @author      WFZ
	 * @param 	    id : 员工id
	 * @return      Result
	 * @date        2019/8/12 16:19
	 */
	@Update("update employee set is_show = 1 where id=#{id}")
	int  updateEmployeeIsShowByUserIdAndCompanyId(@Param("id")Long id);
}
