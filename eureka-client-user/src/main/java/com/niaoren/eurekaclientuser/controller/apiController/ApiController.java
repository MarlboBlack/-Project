package com.niaoren.eurekaclientuser.controller.apiController;

import com.niaoren.eurekaclientuser.common.ServerResponse;
import com.niaoren.eurekaclientuser.entity.Company;
import com.niaoren.eurekaclientuser.entity.Staff;
import com.niaoren.eurekaclientuser.service.CompanyService;
import com.niaoren.eurekaclientuser.vo.CompanyInfoVo;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;


@RestController
@Slf4j
public class ApiController {

    @Autowired
    private CompanyService companyService;

    /**
     * 获取同一类型公司的列表
     * @param type
     * @return
     */
    @RequestMapping("/getComList")
    public ServerResponse<List<CompanyInfoVo>> getComList(@RequestParam("type") Integer type){
        return companyService.getComList(type);
    }

    /**
     * 根据公司名称获取公司id
     * @param companyName
     * @return
     */
    @RequestMapping("/getCompanyIdBycompanyName")
    public ServerResponse<Integer> getCompanyId(@RequestParam("companyName") String companyName){
        return companyService.getCompanyId(companyName);
    }

    /**
     * 根据公司名称获取公司信息
     * @param companyName
     * @return
     */
    @RequestMapping("/getCompanyInfoBycompanyName")
    public ServerResponse getCompanyInfoBycompanyName(@RequestParam("companyName") String companyName){
        return companyService.getCompanyInfo(companyName);
    }

    /**
     * 根据公司id获取公司名称
     * @param companyId
     * @return
     */
    @RequestMapping("/getCompanyNameByCompanyId")
    public ServerResponse<String> getCompanyInfoByCompanyId(@RequestParam("companyId")Integer companyId){
        return companyService.getCompanyName(companyId);
    }

    /**
     * 根据id列表查询公司信息返回一个公司信息列表
     * @param
     * @return
     */
    @RequestMapping("/getCompanyListBycompanyIds")
    public ServerResponse<List<Company>> getCompanyListBycompanyIds(@RequestParam("idListString") String idListString){
        return companyService.getCompanyInfoByIds(idListString);
    }

    /**
     * 根据公司名称获取该公司员工信息列表
     * @param companyName
     * @return
     */
    @RequestMapping("/getStaffListByCompanyName")
    public ServerResponse<List<Staff>> getStaffListByCompanyName(@RequestParam("companyName") String companyName){
        return companyService.getStaffInfoByCompanyName(companyName);
    }

    /**
     * 根据 公司名称，员工名称获取电话号码
     * @param companyName
     * @param staffName
     * @return
     */
    @RequestMapping("/getUserNameByCompanyNameStaffName")
    public ServerResponse<String> getUserName(@RequestParam("companyName") String companyName,@RequestParam("staffName") String staffName){
        return companyService.getUsername(companyName,staffName);
    }
    /**
     * 根据公司名称，员工名称获取员工id
     * @param companyName
     * @param staffName
     * @return
     */
    @RequestMapping("/getStaffIdByCompanyNameName")
    public ServerResponse<Integer> getStaffIdByCompanyNameName(@RequestParam("companyName") String companyName,@RequestParam("staffName") String staffName){
        return companyService.getStaffIdByCompanyNameName(companyName,staffName);
    }
}
