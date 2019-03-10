package com.niaoren.eurekaclientuser.controller;

import com.niaoren.eurekaclientuser.Utils.redis.JedisUtil;
import com.niaoren.eurekaclientuser.Utils.redis.UserKey;
import com.niaoren.eurekaclientuser.common.Const;
import com.niaoren.eurekaclientuser.common.ServerResponse;
import com.niaoren.eurekaclientuser.entity.Company;
import com.niaoren.eurekaclientuser.entity.Staff;
import com.niaoren.eurekaclientuser.service.CompanyService;
import com.niaoren.eurekaclientuser.vo.CompanyInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/company/")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 公司注册审核
     * @param company 要审核的公司对象
     * @return
     */
    @RequestMapping("registerCompany")
    @ResponseBody
    public ServerResponse<String> registerCompany(@RequestParam("company") Company company){
        return companyService.registerCompany(company);
    }


    /**
     * 获取cso公司信息列表
     * @return
     */
    @RequestMapping("getcsoComList")
    public ServerResponse<List<CompanyInfoVo>> getcsoComList(){
        return companyService.getComList(3);
    }

    /**
     * 获取fac公司信息列表
     * @return
     */
    @RequestMapping("getfacComList")
    public ServerResponse<List<CompanyInfoVo>> getfacComList(){
        return companyService.getComList(2);
    }

    /**
     * 根据公司名称获取公司信息
     * @param companyName
     * @return
     */
    @RequestMapping("/getCompanyInfo")
    public ServerResponse<CompanyInfoVo> getCompanyInfo(@RequestParam("companyName") String companyName){
        return companyService.getCompanyInfo(companyName);
    }

    /**
     * 获取admin子账号username列表
     * @param request
     * @return
     */
    @RequestMapping("/getAdminChildList")
    public ServerResponse<List<String>> getAdminChildList(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if(currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String myCompanyName = currentStaff.getCompanyName();
                return companyService.getAdminStaffNameList(myCompanyName);
            }
        }
        return ServerResponse.createByErrorMessage("获取失败");
    }

    /**
     * admin分配子账号客服
     * @param companyName
     * @param adminStaffUserName
     * @return
     */
    @RequestMapping("/distributeAdminUser")
    public ServerResponse<String> distributeAdminUser(@RequestParam("cmopanyName") String companyName,@RequestParam("adminStaffUserName") String adminStaffUserName){
        return companyService.distributeAdminUser(companyName,adminStaffUserName);
    }


    /**
     * admin子账号获取cso,fac公司列表
     * @param request
     * @param companyType
     * @return
     */
    @RequestMapping("/adminStaffGetCompanyList")
    public ServerResponse adminStaffGetCompanyList(HttpServletRequest request,@RequestParam("companyType") Integer companyType){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if(currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                Integer myId = currentStaff.getId();
                return companyService.adminStaffGetCompanyList(myId,companyType);
            }
        }
        return ServerResponse.createByErrorMessage("获取失败");
    }
}
