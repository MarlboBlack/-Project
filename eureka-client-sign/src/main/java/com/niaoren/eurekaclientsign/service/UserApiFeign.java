package com.niaoren.eurekaclientsign.service;

import com.niaoren.eurekaclientsign.common.ServerResponse;
import com.niaoren.eurekaclientsign.entity.apiEntity.Company;
import com.niaoren.eurekaclientsign.entity.apiEntity.Staff;
import com.niaoren.eurekaclientsign.vo.apiVo.CompanyInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name="eureka-client-user")
public interface UserApiFeign {

    @RequestMapping("/staff/logout")
    public String logout() ;

    /**
     * 获取某一类公司列表
     * @param companyType
     * @return
     */
    @RequestMapping(value="/getComList",method = RequestMethod.POST)
    public ServerResponse<List<CompanyInfoVo>> getComList(@RequestParam("type") Integer companyType);

    /**
     * 根据公司名字获取公司id
     * @param companyName
     * @return
     */
    @RequestMapping(value="/getCompanyIdBycompanyName",method =RequestMethod.POST)
    public ServerResponse<Integer> getCompanyId(@RequestParam("companyName") String companyName);

    /**
     * 根据公司id获取公司名称
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getCompanyNameByCompanyId",method = RequestMethod.POST)
    public ServerResponse<String> getCompanyNameByCompanyId(@RequestParam("companyId")Integer companyId);

    /**
     * 根据公司名字获取公司信息
     * @param companyName
     * @return
     */
    @RequestMapping(value="/getCompanyInfoBycompanyName",method =RequestMethod.POST)
    public ServerResponse getCompanyInfoBycompanyName(@RequestParam("companyName") String companyName);

    /**
     * 根据id列表查询公司信息返回一个公司信息列表
     * @param
     * @return
     */
    @RequestMapping(value="/getCompanyListBycompanyIds",method = RequestMethod.POST)
    public ServerResponse<List<Company>> getCompanyListBycompanyIds(@RequestParam("idListString") String idListString);

    /**
     * 根据公司名称获取该公司员工信息列表
     * @param companyName
     * @return
     */
    @RequestMapping(value="/getStaffListByCompanyName",method =RequestMethod.POST)
    public ServerResponse<List<Staff>> getStaffListByCompanyName(@RequestParam("companyName") String companyName);

    /**
     * 根据公司名称，员工名称获取电话号码
     * @param companyName
     * @param staffName
     * @return
     */
    @RequestMapping(value="/getUserNameByCompanyNameStaffName",method = RequestMethod.POST)
    public ServerResponse<String> getUserNameByCompanyNameStaffName(@RequestParam("companyName")String companyName,@RequestParam("staffName") String staffName);

    /**
     * 根据公司名称，员工名称获取员工id
     * @param companyName
     * @param staffName
     * @return
     */
    @RequestMapping(value="/getStaffIdByCompanyNameName",method = RequestMethod.POST)
    public ServerResponse<Integer> getStaffIdByCompanyNameName(@RequestParam("companyName") String companyName,@RequestParam("staffName") String staffName);

}
