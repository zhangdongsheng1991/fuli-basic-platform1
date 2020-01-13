package com.fuli.user.dao;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.user.model.AppUser;
import com.fuli.user.model.DataRecordEmployeeDO;
import com.fuli.user.model.EmployeeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:    员工表
 * @Author:         WFZ
 * @CreateDate:     2019/8/5 18:13
 * @Version:        1.0
*/
@Mapper
@Repository
public interface EmployeeMapper extends BaseMapper<EmployeeDO> {

    /**
     * 实名认证时根据身份证获取已实名用户数据
     * @author      WFZ
     * @param 	    idCard : 身份证号
     * @param 	    userId : 用户id
     * @return      Result
     * @date        2019/10/7 11:11
     */
    EmployeeDO getEmployeeByIdCard(@Param("idCard") String idCard,@Param("userId") Long userId);

    /**
     * 修改在职员工手机号
     * @author      WFZ
     * @param 	    phone ： 手机号
     * @param 	    userId ： 用户id
     * @return      Result
     * @date        2019/10/18 17:06
     */
    @Update("UPDATE employee SET phone_number=#{phone} , update_time=NOW() WHERE `status`=1 AND user_id=#{userId}")
    int updateEmployeePhoneNumber(@Param("phone") String phone , @Param("userId")Long userId);

    /**
     * 根据用户id获取在职员工列表且openId不为空 -- 同步数据用
     * @author      WFZ
     * @param 	    userId ： 用户id
     * @return      Result
     * @date        2019/10/25 17:02
     */
    List<DataRecordEmployeeDO> listEmployeeByUserId(@Param("userId") Long userId);


    /**
     * 用户实名时修改员工身份证号码 -- 只修改身份证号码为空的数据
     * @author      WFZ
     * @param 	    idCard ： 身份证号码
     * @param 	    gender ： 性别
     * @param 	    name ： 姓名
     * @param 	    userId ： 用户id
     * @return      Result
     * @date        2019/10/18 17:06
     */
    @Update("UPDATE employee SET certificate_type=1, certificate_card=#{idCard} ,gender=#{gender}, name=#{name}, update_time=NOW() WHERE user_id=#{userId} AND certificate_card=''")
    int updateEmployeeIdCard(@Param("idCard") String idCard, @Param("gender")Integer gender, @Param("name")String name  , @Param("userId")Long userId);
}

