package com.niaoren.eurekaclientuser.service;

import com.niaoren.eurekaclientuser.common.ServerResponse;
import com.niaoren.eurekaclientuser.entity.Company;
import com.niaoren.eurekaclientuser.entity.Staff;
import com.niaoren.eurekaclientuser.vo.CompanyInfoVo;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface CompanyService {

    /**
     * 校验参数是否已存在方法
     * @param str 参数value
     * @param type 参数类型
     * @return
     */
    public ServerResponse<String> checkVaild(String str, String type);

    /**
     * 公司注册 提交审核
     * @param company 注册公司信息
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<String> registerCompany(Company company);

    /**
     * 获取企业主页详细信息
     * @param request
     * @return
     */
    public ServerResponse getInformation(HttpServletRequest request);

    /**
     * 获取cso或fac公司信息列表
     * @param type
     * @return
     */
    public ServerResponse<List<CompanyInfoVo>> getComList(Integer type);

    /**
     * 根据公司名称获取公司id
     * @param companyName
     * @return
     */
    public ServerResponse<Integer> getCompanyId(String companyName);

    /**
     * 根据公司id获取公司名称
     * @param companyId
     * @return
     */
    public ServerResponse<String> getCompanyName(Integer companyId);
    /**
     * 根据公司名称获取公司信息
     * @param companyName
     * @return
     */
    public ServerResponse<CompanyInfoVo> getCompanyInfo(String companyName);

    /**
     * 根据id列表查询公司信息返回一个公司信息列表
     * @param idListString
     * @return
     */
    public ServerResponse<List<Company>> getCompanyInfoByIds(String idListString);


    /**
     * 根据公司名称获取员工信息列表
     * @param compangName
     * @return
     */
    public ServerResponse<List<Staff>> getStaffInfoByCompanyName(String compangName);

    /**
     * 根据 公司名称，员工名称获取电话号码
     * @param companyName
     * @param staffName
     * @return
     */
    public ServerResponse<String> getUsername(String companyName,String staffName);

    /**
     * 根据公司名称，员工名称获取员工id
     * @param companyName
     * @param staffName
     * @return
     */
    public ServerResponse<Integer> getStaffIdByCompanyNameName(String companyName,String staffName);

    /**
     * 获取admin子账号及代理商username列表
     * @param compangName
     * @return
     */
    public ServerResponse<List<String>> getAdminStaffNameList(String compangName);

    //admin分配子账号客服
    public ServerResponse<String> distributeAdminUser(String companyName,String adminStaffUserName);

    //admin子账号获取公司列表
    public ServerResponse adminStaffGetCompanyList(Integer myId,Integer companyType);
}
