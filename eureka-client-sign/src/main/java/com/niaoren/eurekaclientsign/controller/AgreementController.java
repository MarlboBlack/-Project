package com.niaoren.eurekaclientsign.controller;

import com.niaoren.eurekaclientsign.common.Const;
import com.niaoren.eurekaclientsign.common.ServerResponse;
import com.niaoren.eurekaclientsign.entity.Agreement;
import com.niaoren.eurekaclientsign.entity.apiEntity.Staff;
import com.niaoren.eurekaclientsign.service.AgreementService;
import com.niaoren.eurekaclientsign.service.BusCoopService;
import com.niaoren.eurekaclientsign.utils.redis.JedisUtil;
import com.niaoren.eurekaclientsign.utils.redis.UserKey;
import com.niaoren.eurekaclientsign.vo.SignPrtcInfoVo;
import com.niaoren.eurekaclientsign.vo.apiVo.CompanyInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
public class AgreementController {

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private BusCoopService busCoopService;
    @Autowired
    private JedisUtil jedisUtil;
    /**
     * OK
     * cso查询fac列表
     * @param request
     * @return
     */
    @RequestMapping("/selectFacCooperList")
    public ServerResponse<List> selectFacCooperList(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token,token,Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String myCompanyName = currentStaff.getCompanyName();
                return agreementService.selectFacCooperList(myCompanyName);
            }
        }
        return ServerResponse.createByErrorMessage("获取药厂信息失败");
    }

    /**
     * OK
     * fac查询cso列表
     * @param request
     * @return
     */
    @RequestMapping("/selectCsoCooperList")
    public ServerResponse<List> selectCsoCooperList(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token,token,Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String myCompanyName = currentStaff.getCompanyName();
                return agreementService.selectCsoCooperList(myCompanyName);
            }
        }
        return ServerResponse.createByErrorMessage("获取cso信息失败");
    }

    /**
     * OK
     * 根据公司名称获取员工名称列表
     * @param companyName
     * @return
     */
    @RequestMapping("/selectStaffInfoListByCompanyName")
    public ServerResponse<List> selectStaffInfoListByCompanyName(@RequestParam("selectComp") String companyName){
        return agreementService.selectStaffInfoListByCompanyName(companyName);
    }

    /**
     * OK
     * 根据公司名称和员工名称获取员工电话
     * @param companyName
     * @param staffName
     * @return
     */
    @RequestMapping("/selectCallNumContNum")
    public ServerResponse<String> selectCallNumContNum(@RequestParam("selectComp") String companyName,@RequestParam("callName") String staffName){
        return agreementService.selectCallNumContNum(companyName,staffName);
    }

    /**
     * OK
     * 获取本公司的协议编号
     * @param request
     * @return
     */
    @RequestMapping("/getContNum")
    public ServerResponse<String> getContNum(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token,token,Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String myCompanyName = currentStaff.getCompanyName();
                return agreementService.getContNum(myCompanyName);
            }
        }
        return ServerResponse.createByErrorMessage("获取失败");
    }

    /**
     * OK
     * cso获取公司详细信息生成协议
     * @param request
     * @return
     */
    @RequestMapping("/csoGetCompanyInfo")
    public ServerResponse<CompanyInfoVo> csoGetCompanyInfo(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token,token,Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String myCompanyName = currentStaff.getCompanyName();
                String realName = currentStaff.getName();
                String phoneNumber = currentStaff.getUsername();
                return agreementService.getCompanyInfo(myCompanyName,realName,phoneNumber);
            }
        }
        return ServerResponse.createByErrorMessage("获取信息失败");
    }

    /**
     * OK
     * fac获取公司详细信息生成协议
     * @param request
     * @return
     */
    @RequestMapping("/facGetCompanyInfo")
    public ServerResponse<CompanyInfoVo> facGetCompanyInfo(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token,token,Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String myCompanyName = currentStaff.getCompanyName();
                String realName = currentStaff.getName();
                String phoneNumber = currentStaff.getUsername();
                return agreementService.getCompanyInfo(myCompanyName,realName,phoneNumber);
            }
        }
        return ServerResponse.createByErrorMessage("获取信息失败");
    }

    /**
     * OK
     * 根据公司名称获取公司详细信息
     * @param companyName
     * @return
     */
    @RequestMapping("/getCompanyInfo")
    public ServerResponse<CompanyInfoVo> getCompanyInfo(@RequestParam("companyName") String companyName){
        return busCoopService.getCompanyInfo(companyName);
    }

    /**
     * OK
     * 协议提交保存（fac为甲方）
     * @param signPrtcInfoVo
     * @param session
     * @return
     */
    @RequestMapping("/submitSignPrtc")
    public ServerResponse<String> submitSignPrtc(SignPrtcInfoVo signPrtcInfoVo, HttpSession session){
        ServletContext servletContext = session.getServletContext();
        return agreementService.submitSignPrtc(signPrtcInfoVo,servletContext);
    }

    /**
     *OK
     * cso查询签约列表
     * @return
     */
    @RequestMapping("/csoSelectAllAgreFactory")
    public ServerResponse<List<CompanyInfoVo>> csoSelectAllAgreFactory(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token,token,Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String myCompanyName = currentStaff.getCompanyName();
                return agreementService.csoSelectAllAgreFactory(myCompanyName);
            }
        }
        return ServerResponse.createByErrorMessage("获取信息失败");
    }

    /**
     * OK
     * @param request
     * @return
     */
    //fac查询签约列表
    @RequestMapping("/facSelectAllAgreFactory")
    public ServerResponse<List<CompanyInfoVo>> facSelectAllAgreFactory(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token,token,Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String myCompanyName = currentStaff.getCompanyName();
                return agreementService.facSelectAllAgreFactory(myCompanyName);
            }
        }
        return ServerResponse.createByErrorMessage("获取信息失败");
    }

    /**
     * OK
     * 根据协议编号获取协议内容
     * @param agreementCode
     * @return
     */
    @RequestMapping("/validSign")
    public ServerResponse<SignPrtcInfoVo> validSign(@RequestParam("agreementCode") String agreementCode){
        return agreementService.validSign(agreementCode);
    }

    /**
     * OK
     * cso审核签约通过
     * @param request
     * @param agreementCode
     * @param csoSignName
     * @param csoSignTime
     * @return
     */
    @RequestMapping("/csoPassProtocol")
    public ServerResponse<Agreement> csoPassProtocol(HttpServletRequest request,
                                                     @RequestParam("agreementCode") String agreementCode,
                                                     @RequestParam("csoSignName") String csoSignName,
                                                     @RequestParam("csoSignTime") String csoSignTime){
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token,token,Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String myCompanyName = currentStaff.getCompanyName();
                return agreementService.csoPassProtocol(myCompanyName,agreementCode,csoSignName,csoSignTime);

            }
        }
        return ServerResponse.createByErrorMessage("审核失败");
    }

    /**
     * OK
     * fac审核签约通过
     * @param request
     * @param agreementCode
     * @param facSignName
     * @param facSignTime
     * @return
     */
    @RequestMapping("/facPassProtocol")
    public ServerResponse<Agreement> facPassProtocol(HttpServletRequest request,
                                                     @RequestParam("agreementCode") String agreementCode,
                                                     @RequestParam("facSignName")String facSignName,
                                                     @RequestParam("facSignTime")String facSignTime){
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies){
            if(Const.COOKIE_NAME_TOKEN.equals(c.getName())){
                String token = c.getValue();
                Staff currentStaff = jedisUtil.get(UserKey.token,token,Staff.class);
                if (currentStaff == null)
                    return ServerResponse.createByErrorMessage("请重新登录");
                String myCompanyName = currentStaff.getCompanyName();
                return agreementService.facPassProtocol(myCompanyName,agreementCode,facSignName,facSignTime);
            }
        }
        return ServerResponse.createByErrorMessage("审核失败");
    }

    /**
     * OK
     * 拒绝签约
     * @param agreementCode
     * @return
     */
    @RequestMapping("/refuseSign")
    public ServerResponse<String> refuseSign(@RequestParam("agreementCode") String agreementCode){
        return agreementService.refuseSign(agreementCode);
    }

    /**
     * OK
     * admin查询cso签约列表
     * @param myCompanyName
     * @return
     */
    @RequestMapping("/adminSelectCsoSign")
    public ServerResponse<List<CompanyInfoVo>> adminSelectCsoSign(@RequestParam("companyName") String myCompanyName){
        return agreementService.adminSelectCsoSign(myCompanyName);
    }

    /**
     * OK
     * admin查询fac签约列表
     * @param myCompanyName
     * @return
     */
    @RequestMapping("/adminSelectFacSign")
    public ServerResponse<List<CompanyInfoVo>> adminSelectFacSign(@RequestParam("companyName") String myCompanyName){
        return agreementService.adminSelectFacSign(myCompanyName);
    }
}
