package com.niaoren.eurekaclientsign.controller;

import com.niaoren.eurekaclientsign.common.Const;
import com.niaoren.eurekaclientsign.common.ServerResponse;
import com.niaoren.eurekaclientsign.entity.apiEntity.Staff;
import com.niaoren.eurekaclientsign.service.AgreementService;
import com.niaoren.eurekaclientsign.service.BusCoopService;
import com.niaoren.eurekaclientsign.utils.redis.JedisUtil;
import com.niaoren.eurekaclientsign.utils.redis.UserKey;
import com.niaoren.eurekaclientsign.vo.apiVo.CompanyInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class BusCoopController {

    @Autowired
    private BusCoopService busCoopService;
    @Autowired
    private AgreementService agreementService;
    @Autowired
    private JedisUtil jedisUtil;



    /**
     * OK
     * cso获取fac列表
     * @param request
     * @return
     */
    @RequestMapping("/getfacComList")
    public ServerResponse<List<CompanyInfoVo>> getfacComList(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String myCompanyName = currentStaff.getCompanyName();
                return busCoopService.getfacComList(myCompanyName);
            }
        }
        return ServerResponse.createByErrorMessage("获取信息失败");

    }

    /**
     * OK
     * fac获取cso列表
     * @param request
     * @return
     */
    @RequestMapping("/getcsoComList")
    public ServerResponse<List<CompanyInfoVo>> getcsoComList(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String myCompanyName = currentStaff.getCompanyName();
                return busCoopService.getcsoComList(myCompanyName);
            }
        }
        return ServerResponse.createByErrorMessage("获取信息失败");

    }

    /**
     * 根据公司名称获取公司详细信息
     * @param companyName
     * @return
     */
    @RequestMapping("/getComInfo")
    public ServerResponse<CompanyInfoVo> getComInfo(@RequestParam("companyName")String companyName){
        return busCoopService.getCompanyInfo(companyName);
    }

    /**
     * OK
     * cso向工厂申请合作
     * @param request
     * @param facCompanyName
     * @return
     */
    @RequestMapping("/csoApplyCoop")
    public ServerResponse csoApplyCoop(HttpServletRequest request,@RequestParam("facCompanyName") String facCompanyName){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String myCompanyName = currentStaff.getCompanyName();
                return busCoopService.csoApplyCoop(myCompanyName,facCompanyName);
            }
        }
        return ServerResponse.createByErrorMessage("申请失败");

    }

    /**
     * OK
     * fac向cso申请合作
     * @param request
     * @param csoCompanyName
     * @return
     */
    @RequestMapping("/facApplyCoop")
    public ServerResponse facApplyCoop(HttpServletRequest request ,@RequestParam("csoCompanyName") String csoCompanyName){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String myCompanyName = currentStaff.getCompanyName();
                return busCoopService.facApplyCoop(myCompanyName,csoCompanyName);
            }
        }
        return ServerResponse.createByErrorMessage("申请失败");
    }

    /**
     * OK
     * cso审核商务合作
     * @param request
     * @param facCompanyName
     * @param status
     * @return
     */
    @RequestMapping("/csoCooperExamine")
    public ServerResponse csoCooperExamine(HttpServletRequest request,@RequestParam("facCompanyName") String facCompanyName,
                                           @RequestParam("status") Integer status){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String csoCompanyName = currentStaff.getCompanyName();
                return busCoopService.cooperExamine(csoCompanyName,facCompanyName,status);
            }
        }
        return ServerResponse.createByErrorMessage("审核失败");
    }

    /**
     * OK
     * fac审核商务合作
     * @param request
     * @param csoCompanyName
     * @param status
     * @return
     */
    @RequestMapping("/facCooperExamine")
    public ServerResponse facCooperExamine(HttpServletRequest request,@RequestParam("csoCompanyName") String csoCompanyName,
                                           @RequestParam("status") Integer status){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token, token, Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String facCompanyName = currentStaff.getCompanyName();
                return busCoopService.cooperExamine(csoCompanyName,facCompanyName,status);
            }
        }

        return ServerResponse.createByErrorMessage("审核失败");
    }

    /**
     * OK
     * 获取fac公司子账号姓名列表
     * @param request
     * @return
     */
    @RequestMapping("/getFacChildList")
    public ServerResponse getFacChildList(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token =  c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token,token,Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String myCompanyName = currentStaff.getCompanyName();
                return agreementService.selectStaffInfoListByCompanyName(myCompanyName);
            }
        }
        return ServerResponse.createByErrorMessage("获取失败");
    }

    /**
     * OK
     * fac子账号分配服务
     * @param request
     * @param csoCompanyName
     * @param facStaffName
     * @return
     */
    @RequestMapping("/facDistributionService")
    public ServerResponse facDistributionService(HttpServletRequest request,@RequestParam("csoCompanyName") String csoCompanyName,
                                                 @RequestParam("facStaffName") String facStaffName){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token =  c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token,token,Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String myCompanyName = currentStaff.getCompanyName();
                return busCoopService.facDistributionService(myCompanyName,csoCompanyName,facStaffName);
            }
        }
       return ServerResponse.createByErrorMessage("分配失败");

    }


    /**
     * OK
     * fac子账号获取cso列表
     * @param request
     * @return
     */
    @RequestMapping("/facChildGetCsoList")
    public ServerResponse facChildGetCsoList(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token =  c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token,token,Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                Integer facChildId = currentStaff.getId();
                return busCoopService.facChildGetCsoList(facChildId);
            }
        }
        return ServerResponse.createByErrorMessage("获取失败");
    }

    /**
     * OK
     * fac子账号向cso申请合作
     * @param request
     * @param csoCompanyName
     * @return
     */
    @RequestMapping("/facChildApplyCoop")
    public ServerResponse facChildApplyCoop(HttpServletRequest request,@RequestParam("csoCompanyName") String csoCompanyName){
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token =  c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token,token,Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                Integer facChildId = currentStaff.getId();
                return busCoopService.facChildApplyCoop(facChildId,csoCompanyName);
            }
        }
        return ServerResponse.createByErrorMessage("申请失败");
    }

}
