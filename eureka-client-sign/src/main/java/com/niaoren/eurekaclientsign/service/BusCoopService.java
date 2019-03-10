package com.niaoren.eurekaclientsign.service;

import com.niaoren.eurekaclientsign.common.ServerResponse;
import com.niaoren.eurekaclientsign.vo.apiVo.CompanyInfoVo;

import java.util.List;

public interface BusCoopService {

    /**
     * cso获取可以合作的fac公司列表
     * @param myCompanyName
     * @return
     */
    public ServerResponse<List<CompanyInfoVo>> getfacComList(String myCompanyName);

    /**
     * fac获取可以合作的cso公司列表
     * @param myCompanyName
     * @return
     */
    public ServerResponse<List<CompanyInfoVo>> getcsoComList(String myCompanyName);

    /**
     * 根据公司名称获取公司信息
     * @param companyName
     * @return
     */
    public ServerResponse<CompanyInfoVo> getCompanyInfo(String companyName);

    /**
     * cso向工厂申请合作
     * @param myCompanyName
     * @param facCompanyName
     * @return
     */
    public ServerResponse csoApplyCoop(String myCompanyName,String facCompanyName);

    /**
     * fac向cso申请合作
     * @param myCompanyName
     * @param csoCompanyName
     * @return
     */
    public ServerResponse facApplyCoop(String myCompanyName,String csoCompanyName);

    /**
     * 审核合作请求
     * @param csoCompanyName
     * @param facCompanyName
     * @param status
     * @return
     */
    public ServerResponse cooperExamine(String csoCompanyName,String facCompanyName,Integer status);

    /**
     * fac子账号分配服务
     * @param myCompanyName
     * @param csoCompanyName
     * @param facStaffName
     * @return
     */
    public ServerResponse facDistributionService(String myCompanyName, String csoCompanyName, String facStaffName);

    /**
     * fac子账号huoqu
     * @param facChildId
     * @return
     */
    public ServerResponse facChildGetCsoList(Integer facChildId);

    /**
     * fac子账号向cso申请合作
     * @param facChildId
     * @param csoCompanyName
     * @return
     */
    public ServerResponse facChildApplyCoop(Integer facChildId,String csoCompanyName);
}
