package com.niaoren.eurekaclientuser.controller;


import com.niaoren.eurekaclientuser.Utils.redis.JedisUtil;
import com.niaoren.eurekaclientuser.Utils.redis.UserKey;
import com.niaoren.eurekaclientuser.common.Const;
import com.niaoren.eurekaclientuser.common.ServerResponse;

import com.niaoren.eurekaclientuser.entity.Staff;
import com.niaoren.eurekaclientuser.service.CompanyService;
import com.niaoren.eurekaclientuser.service.StaffService;


import com.niaoren.eurekaclientuser.vo.CompanyInfoVo;
import com.niaoren.eurekaclientuser.vo.LoginTotleVo;
import com.niaoren.eurekaclientuser.vo.StaffInfoVo;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



@RestController
@Slf4j
@RequestMapping("/staff/")
public class StaffController {

    @Autowired
    private StaffService staffService;
    @Autowired
    private JedisUtil jedisUtil;
    @Autowired
    private CompanyService companyService;


    /**
     * 用户注册信息填写，获取验证码
     * @param username 注册用户信息
     * @return
     */
    @RequestMapping("register")
    @ResponseBody
    public ServerResponse<String> register(@RequestParam("username")String username){
        return staffService.register(username);
    }

    /**
     * 用户注册提交验证码验证
     * @param staff 注册用户信息
     * @param code 验证码
     * @return
     */
    @RequestMapping("validatecode")
    @ResponseBody
    public ServerResponse<String> validateCode(Staff staff,@RequestParam("demo-radio")Integer userType,@RequestParam("code")String code){
        return staffService.ValidateCode(staff,userType,code);
    }

    /**
     * OK
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param response
     * @return
     */
    @RequestMapping("login" )
    @ResponseBody
    public ServerResponse<Staff> login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse response){
        return staffService.login(username,password,response);
    }


    /**
     * OK
     * 用户退出
     * @param request
     * @return
     */
    @RequestMapping("logout")
    @ResponseBody
    public ServerResponse<String> logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                jedisUtil.expireSet(UserKey.token,token,null);
            }
        }
        return ServerResponse.createBySuccessMessage("成功退出");
    }



    /**
     * OK
     * 登录状态下更改密码
     * @param request
     * @param passwordOld 旧密码
     * @param passwordNew 新密码
     * @return
     */
    @RequestMapping("rsetPassword")
    @ResponseBody
    public ServerResponse<String> rsetPassword(HttpServletRequest request, @RequestParam("passwordOld")String passwordOld, @RequestParam("passwordNew")String passwordNew){

        return staffService.updatePassword(passwordOld,passwordNew,request);
    }

    /**
     * OK
     * 登状态下获取用户详细信息
     * @param request
     * @return
     */
    @RequestMapping("getStaffInfo")
    @ResponseBody
    public ServerResponse<Staff> getStaffInfo(HttpServletRequest request) {
        return staffService.getInformation(request);
    }

    /**
     * OK
     * 登录状态下更新用户信息
     * @param request
     * @param staff 更新内容
     * @return
     */
    @RequestMapping("updateInformation")
    @ResponseBody
    public ServerResponse<Staff> updateInformation(HttpServletRequest request,@RequestParam("staff")Staff staff){
       return staffService.updateInformation(request,staff);
    }

    /**
     * OK
     * 登录状态获取公司详细信息
     * @param request
     * @return
     */
    @RequestMapping("getCompanyInfo")
    @ResponseBody
    public ServerResponse<CompanyInfoVo> getCompanyInfo(HttpServletRequest request){
        return companyService.getInformation(request);
    }

    /**
     * OK
     * 登录状态下管理员获取子用户
     * @param request
     * @return
     */
    @RequestMapping("getChildList")
    @ResponseBody
    public ServerResponse getChildList(HttpServletRequest request){
        return staffService.getChildStaff(request);
    }



    /**
     * OK
     * 管理员删除员工
     * @param request
     * @param username
     * @return
     */
    @RequestMapping("deleteStaff")
    @ResponseBody
    public ServerResponse<String> deleteStaff(HttpServletRequest request,@RequestParam("username") String username){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if(currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                return staffService.deleteStaff(username);
            }
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }

    /**
     * OK
     * 管理员修改员工信息
     * @param request
     * @return
     */
    @RequestMapping("updateStaff")
    @ResponseBody
    public ServerResponse<String> updateStaff(HttpServletRequest request,StaffInfoVo staffInfoVo,@RequestParam("phoneNumberOld") String phoneNumberOld){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token =  c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if(currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                return staffService.updateStaff(staffInfoVo,phoneNumberOld);
            }
        }
        return ServerResponse.createByErrorMessage("修改失败");
    }

    /**
     * OK
     * 管理员获取员工信息
     * @param request
     * @param username
     * @return
     */
    @RequestMapping("selectAdminChildStaff")
    @ResponseBody
    public ServerResponse<CompanyInfoVo> selectAdminChildStaff(HttpServletRequest request, @RequestParam("username")String username){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token =  c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if(currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                return staffService.selectStaff(username);
            }
        }
        return ServerResponse.createByErrorMessage("获取失败");
    }


    /**
     * OK
     * 管理员重置员工密码
     * @param request
     * @param username 要重置密码的员工姓名
     * @return
     */
    @RequestMapping("resetPassword")
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpServletRequest request,@RequestParam("username")String username){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token  = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if(currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                return staffService.resetPassword(username);
            }
        }
        return ServerResponse.createByErrorMessage("重置失败");
    }

    /**
     * OK
     * 上传头像
     * @param request
     * @param headImg 要上传的头像
     * @return
     */
    @RequestMapping("uploadHeadImg")
    @ResponseBody
    public ServerResponse uploadHeadImg(HttpServletRequest request, @RequestParam(value = "headImg",required = false) MultipartFile headImg){

        return  staffService.updateHeadImg(request,headImg);
    }

    /**
     * OK
     * 提交公司资料
     * @param session
     * @param name 公司名称
     * @param unifyCreditCode 统一社会信用代码
     * @param contactName  联系人姓名
     * @param contactIdNumber 联系人身份证号
     * @param contactPhoneNumber 联系人电话
     * @param synopsis 公司简介
     * @param businessLicense 营业执照
     * @param openingPermit 开户许可证
     * @param officialSeal 公章水印
     * @param qualificationCertificate 资质证书
     * @param enterpriseSystem 企业内控制度
     * @param GPMcertificate GPM证书
     * @param medicineOfficial 药品批文
     * @return
     */
    @RequestMapping("submitFile")
    @ResponseBody
    public ServerResponse submitFile(HttpSession session,
                                     @RequestParam(value = "name",required = false)String name,
                                     @RequestParam(value = "unifyCreditCode",required = false)String unifyCreditCode,
                                     @RequestParam(value = "contactName",required = false)String contactName,
                                     @RequestParam(value = "contactIdNumber",required = false)String contactIdNumber,
                                     @RequestParam(value = "contactPhoneNumber",required=false)String contactPhoneNumber,
                                     @RequestParam(value = "synopsis",required = false)String synopsis,
                                     @RequestParam(value = "email",required = false)String email,
                                     @RequestParam(value = "workAddress",required = false)String workAddress,
                                     @RequestParam(value = "recordCode",required = false)String recordCode,
                                     @RequestParam(value = "website",required = false)String website,
                                     @RequestParam(value = "bank",required = false)String bank,
                                     @RequestParam(value = "bankNumber",required = false)String bankNumber,
                                     @RequestParam(value = "gmpNumber",required = false)String gmpNumber,
                                     @RequestParam(value = "businessLicense",required = false) MultipartFile businessLicense,
                                     @RequestParam(value = "openingPermit",required = false) MultipartFile openingPermit,
                                     @RequestParam(value = "officialSeal",required = false) MultipartFile officialSeal,
                                     @RequestParam(value = "qualificationCertificate[]",required = false) MultipartFile[] qualificationCertificate,
                                     @RequestParam(value = "enterpriseSystem",required = false) MultipartFile enterpriseSystem,
                                     @RequestParam(value = "GPMcertificate",required = false) MultipartFile GPMcertificate,
                                     @RequestParam(value = "medicineOfficial",required = false) MultipartFile medicineOfficial,
                                     @RequestParam(value = "contactIdCard",required = false) MultipartFile contactIdCard
                                     ){
        //服务器项目根目录
        ServletContext servletContext = session.getServletContext();
        return staffService.submitFile(name,
                unifyCreditCode,
                contactName,
                contactIdNumber,
                contactPhoneNumber,
                synopsis,
                email,
                workAddress,
                recordCode,
                website,
                bank,
                bankNumber,
                gmpNumber,
                businessLicense,
                openingPermit,
                officialSeal,
                qualificationCertificate,
                enterpriseSystem,
                GPMcertificate,
                medicineOfficial,
                contactIdCard,
                servletContext);
    }

    /**
     * OK
     * admin审核公司通过
     * @param request
     * @param companyName 被审核的公司名
     * @return
     */
    @RequestMapping("validPassCompany")
    @ResponseBody
    public ServerResponse<String> validPassCompany(HttpServletRequest request,@RequestParam("companyName")String companyName){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())) {
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                //审核此公司的admin成员id
                int adminId = currentStaff.getId();
                String userType = currentStaff.getUserType().toString();
                //如果身份是admin或者代理商，或者admin子账号才可以审核
                if(Const.ADMIN_TYPE.equals(userType)||Const.ADMIN_AGENT_TYPE.equals(userType)||Const.ADMIN_STAFF_TYPE.equals(userType)){
                    return staffService.validPassCompany(companyName,adminId);
                }
            }
        }
        return ServerResponse.createByErrorMessage("无权限操作");
    }

    /**
     * OK
     * admin审核公司未通过
     * @param request
     * @param companyName 被审核的公司名称
     * @return
     */
    @RequestMapping("validNotPassCompany")
    @ResponseBody
    public ServerResponse<String> validNotPassCompany(HttpServletRequest request,@RequestParam("companyName")String companyName){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies) {
            if (Const.COOKIE_NAME_TOKEN.equals(c.getName())) {
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                //审核此公司的admin成员id
                Integer adminId = currentStaff.getId();
                String userType = currentStaff.getUserType().toString();
                if (Const.ADMIN_TYPE.equals(userType) || Const.ADMIN_AGENT_TYPE.equals(userType) || Const.ADMIN_STAFF_TYPE.equals(userType)) {
                    return staffService.validNotPassCompany(companyName,adminId);
                }
            }
        }
        return ServerResponse.createByErrorMessage("无权限操作");
    }

    /**
     * 根据当前时间（小时）统计登录次数
     * @param nowTime  当前小时
     * @return
     */
    @RequestMapping("getLoginTotle")
    public ServerResponse getLoginTotle(@RequestParam("nowTime")Integer nowTime){
        return staffService.getLoginTotle(nowTime);
    }


}
