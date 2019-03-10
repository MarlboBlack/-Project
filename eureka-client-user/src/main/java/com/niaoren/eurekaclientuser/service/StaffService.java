package com.niaoren.eurekaclientuser.service;

import com.niaoren.eurekaclientuser.common.ServerResponse;
import com.niaoren.eurekaclientuser.entity.Staff;
import com.niaoren.eurekaclientuser.vo.CompanyInfoVo;
import com.niaoren.eurekaclientuser.vo.StaffInfoVo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface StaffService {

    /**
     * 校验校验参数是否已存在方法
     * @param str 参数value
     * @param type 参数类型
     * @return
     */
    public ServerResponse<String> checkVaild(String str,String type);

    /**
     * 用户注册信息填写，获取验证码
     * @param username 用户名
     * @return
     */
    public ServerResponse<String> register(String username);

    /**
     * 用户注册提交验证码验证
     * @param staff 注册用户的信息
     * @param code 验证码
     * @return
     */
    public ServerResponse<String> ValidateCode(Staff staff, Integer userType, String code);

    /**
     * 用户登录
     * @param username
     * @param password
     * @param response
     * @return
     */
    public ServerResponse<Staff> login(String username, String password,HttpServletResponse response);

    /**
     * 登录状态下更改密码
     * @param passwordOld 旧密码
     * @param passwordNew 新密码
     * @param request
     * @return
     */
    public ServerResponse<String> updatePassword(String passwordOld, String passwordNew, HttpServletRequest request);

    /**
     * 登录状态下上传头像
     * @param request
     * @param headImg 上传的头像
     * @return
     */
    public ServerResponse<String> updateHeadImg(HttpServletRequest request,MultipartFile headImg);

    /**
     * 登录状态下获取用户信息
     * @param request
     * @return
     */
    public ServerResponse<Staff> getInformation(HttpServletRequest request);

    /**
     * 登录状态下获取子账号列表
     * @param request
     * @return
     */
    public ServerResponse<List<StaffInfoVo>> getChildStaff(HttpServletRequest request);

    /**
     * 登录状态下更新个人信息
     * @param  request
     * @param staff 要更新的用户
     * @return
     */
    public ServerResponse<Staff> updateInformation(HttpServletRequest request,Staff staff);

    /**
     * 管理员添加员工
     * @param staff 要添加的员工
     * @param companyCode 公司编号
     * @return
     */
    public ServerResponse<String> addStaff(Staff staff,String companyCode);

    /**
     * 管理员删除员工
     * @param username
     * @return
     */
    public ServerResponse<String> deleteStaff(String username);

    /**
     * 管理员修改员工信息
     * @param staffInfoVo
     * @param phoneNumberOld
     * @return
     */
    public ServerResponse<String> updateStaff(StaffInfoVo staffInfoVo,String phoneNumberOld);

    /**
     * 管理员查看员工信息
     * @param username
     * @return
     */
    public ServerResponse<CompanyInfoVo> selectStaff(String username);

    /**
     * 公司管理员重置员工密码
     * @param username 要重置密码的用户
     * @return
     */
    public ServerResponse<String> resetPassword(String username);

    /**
     * 提交公司资料
     * @param name
     * @param unifyCreditCode
     * @param contactName
     * @param contactIdNumber
     * @param contactPhoneNumber
     * @param synopsis
     * @param email
     * @param workAddress
     * @param recordCode
     * @param website
     * @param bank
     * @param bankNumber
     * @param gmpNumber
     * @param businessLicense
     * @param openingPermit
     * @param officialSeal
     * @param qualificationCertificate
     * @param enterpriseSystem
     * @param GPMcertificate
     * @param medicineOfficial
     * @param contactIdCard
     * @param servletContext
     * @return
     */
    public ServerResponse submitFile(String name,
                                     String unifyCreditCode,
                                     String contactName,
                                     String contactIdNumber,
                                     String contactPhoneNumber,
                                     String synopsis,
                                     String email,
                                     String workAddress,
                                     String recordCode,
                                     String website,
                                     String bank,
                                     String bankNumber,
                                     String gmpNumber,
                                     MultipartFile businessLicense,
                                     MultipartFile openingPermit,
                                     MultipartFile officialSeal,
                                     MultipartFile[] qualificationCertificate,
                                     MultipartFile enterpriseSystem,
                                     MultipartFile GPMcertificate,
                                     MultipartFile medicineOfficial,
                                     MultipartFile contactIdCard,
                                     ServletContext servletContext);

    /**
     * admin审核公司通过
     * @param companyName 被审核的公司名
     * @param adminId  审核操作的admin成员id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<String> validPassCompany(String companyName,Integer adminId);

    /**
     * admin审核公司未通过
     * @param companyName 被审核的公司名称
     * @param adminId   审核操作的admin成员id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<String> validNotPassCompany(String companyName,Integer adminId);

    /**
     * 根据当前时间（小时）统计登录次数
     * @param nowTime  当前小时
     * @return
     */
    public ServerResponse getLoginTotle(Integer nowTime);

}
