package com.niaoren.eurekaclientuser.mapper;

import com.niaoren.eurekaclientuser.entity.Staff;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Staff record);

    int insertSelective(Staff record);

    Staff selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Staff record);

    int updateByPrimaryKey(Staff record);

    int checkUsername(String username);

    Staff selectLogin(@Param("username")String username, @Param("password") String password);

    int checkPassword(@Param("password") String password,@Param("id")int id);

    //根据用户名获取用户
    Staff selectByUsername(String username);
    //根据用户名删除用户
    int deleteByUsername(String username);

    //重置密码
    int resetPassword(@Param("username") String username,@Param("rsetPassword")String rsetPassword);

    //根据公司名称查看子账号信息列表
    List<Staff> selectByCompanyName(String companyName);

    //根据旧的账号（手机号）修改用户子账号信息
    int updateByUsernameSelective(@Param("staff")Staff staff,@Param("phoneNumberOld")String phoneNumberOld);

    //根据 公司名和真实姓名获取账号（电话号码）
    String getUsernameByCompNameRealName(@Param("companyName")String companyName,@Param("realName")String realName);

    //根据公司名和真实姓名获取id
    Integer getIdByNameCompanyName(@Param("companyName") String companyName,@Param("realName")String realName);

    //根据用户类型查询用户信息列表
    List<Staff> selectByUserType(Integer userType);
}