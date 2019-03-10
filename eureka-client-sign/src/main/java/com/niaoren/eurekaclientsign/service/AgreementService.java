package com.niaoren.eurekaclientsign.service;

import com.niaoren.eurekaclientsign.common.ServerResponse;
import com.niaoren.eurekaclientsign.entity.Agreement;
import com.niaoren.eurekaclientsign.vo.SignPrtcInfoVo;
import com.niaoren.eurekaclientsign.vo.apiVo.CompanyInfoVo;

import javax.servlet.ServletContext;
import java.util.List;

public interface AgreementService {

    /**
     * fac查询cso列表
     * @param myCompanyName
     * @return
     */
    public ServerResponse<List> selectCsoCooperList(String myCompanyName);

    /**
     * cso查询fac列表
     * @param myCompanyName
     * @return
     */
    public ServerResponse<List> selectFacCooperList(String myCompanyName);

    /**
     * 根据公司名称获取staffNameList信息
     * @param companyName
     * @return
     */
    public ServerResponse<List> selectStaffInfoListByCompanyName(String companyName);

    /**
     * 选择联系人之后，返回联系人电话
     * @param companyName
     * @param staffName
     * @return
     */
    public ServerResponse<String> selectCallNumContNum(String companyName,String staffName);
    /**
     * 接受企业承诺书之后返回协议编号
     * @param myCompanyName
     * @return
     */
    public ServerResponse<String> getContNum(String myCompanyName);

    /**
     * cso获取公司详细信息生成协议
     * @param companyName
     * @return
     */
    public ServerResponse<CompanyInfoVo> getCompanyInfo(String companyName,String realName,String userName);

    /**
     *  cso协议提交保存（fac为甲方）
     * @param signPrtcInfoVo
     * @param servletContext
     * @return
     */
    public ServerResponse<String> submitSignPrtc(SignPrtcInfoVo signPrtcInfoVo,
//                                                    MultipartFile csoSignName,
//                                                 MultipartFile facSignName,
                                                    ServletContext servletContext);

    //cso查询签约列表
    public ServerResponse<List<CompanyInfoVo>> csoSelectAllAgreFactory(String myCompanyName);

    //fac查询签约列表
    public ServerResponse<List<CompanyInfoVo>> facSelectAllAgreFactory(String myCompanyName);

    //根据协议编号获取协议内容
    public ServerResponse<SignPrtcInfoVo> validSign(String agreementCode);

    //cso审核签约通过
    public ServerResponse<Agreement> csoPassProtocol(String myCompanyName , String agreementCode, String csoSignName, String csoSignTime);

    //fac审核签约通过
    public ServerResponse<Agreement> facPassProtocol(String myCompanyName ,String agreementCode,String facSignName,String facSignTime);

    //拒绝签约
    public ServerResponse<String> refuseSign(String agreementCode);

    //admin查询cso签约列表
    public ServerResponse<List<CompanyInfoVo>> adminSelectCsoSign(String myCompanyName);

    //admin查询fac签约列表
    public ServerResponse<List<CompanyInfoVo>> adminSelectFacSign(String myCompanyName);

}
