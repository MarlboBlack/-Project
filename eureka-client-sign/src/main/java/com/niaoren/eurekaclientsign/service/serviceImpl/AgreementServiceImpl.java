package com.niaoren.eurekaclientsign.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.niaoren.eurekaclientsign.common.ServerResponse;
import com.niaoren.eurekaclientsign.entity.Agreement;
import com.niaoren.eurekaclientsign.entity.ProtocolservicePrice;
import com.niaoren.eurekaclientsign.entity.apiEntity.Company;
import com.niaoren.eurekaclientsign.entity.apiEntity.Staff;
import com.niaoren.eurekaclientsign.mapper.AgreementMapper;
import com.niaoren.eurekaclientsign.mapper.BuscooperationMapper;
import com.niaoren.eurekaclientsign.mapper.ProtocolservicePriceMapper;
import com.niaoren.eurekaclientsign.service.AgreementService;
import com.niaoren.eurekaclientsign.service.UserApiFeign;
import com.niaoren.eurekaclientsign.vo.CooperName;
import com.niaoren.eurekaclientsign.vo.SignPrtcInfoVo;
import com.niaoren.eurekaclientsign.vo.apiVo.CompanyInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.ServletContext;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AgreementServiceImpl implements AgreementService {

    private Logger logger = LoggerFactory.getLogger(AgreementServiceImpl.class);

    @Autowired(required = false)
    private UserApiFeign userApiFeign;

    @Autowired
    private BuscooperationMapper buscooperationMapper;

    @Autowired
    private AgreementMapper agreementMapper;

    @Autowired
    private ProtocolservicePriceMapper protocolservicePriceMapper;

    /**
     * 获取一个2位的随机数
     * @return
     */
    public String getCode(){
        Random random = new Random();
        String str = random.nextInt(100)+"";
        if(str.length() != 2){
            return getCode();
        }
        return str;
    }

    /**
     * 根据公司id列表获取company名称列表信息
     * @param idList
     * @return
     */
    public ServerResponse<List> getCompanyInfoBycompanyIds(List<Integer> idList){
        System.out.println(idList.size()+"---------------");
        if(idList.size() == 0){
            return ServerResponse.createByErrorMessage("未查询到id");
        }
        StringBuffer sb = new StringBuffer();
        for(Object id:idList){
            sb.append(id+",");
        }
        sb.deleteCharAt(sb.length()-1);
        String idListString = sb.toString();
        //根据id列表获取company列表
        List<Company> companyList = userApiFeign.getCompanyListBycompanyIds(idListString).getData();
        if(companyList == null){
            return ServerResponse.createByErrorMessage("未获取到公司列表");
        }
        List<CooperName> cooperNameList = Lists.newArrayList();
        int x = 0;
        for (Company company:companyList) {
            x = x+1;
            CooperName cooperName = new CooperName();
            cooperName.setId(x);
            cooperName.setName(company.getName());
            cooperNameList.add(cooperName);
        }
        return ServerResponse.createBySuccess(cooperNameList);
    }

    /**
     * fac查询cso列表
     * @param myCompanyName
     * @return
     */
    public ServerResponse<List> selectCsoCooperList(String myCompanyName){
        //获取本公司id
        Integer facCompanyId = userApiFeign.getCompanyId(myCompanyName).getData();
        //查询商务合作表中的和本公司建立合作的公司id列表
        List<Integer> csoIdList = buscooperationMapper.selectcsoIdList(facCompanyId);
        if(csoIdList == null) {
            return ServerResponse.createByErrorMessage("未查到建立合作关系的公司");
        }
        return getCompanyInfoBycompanyIds(csoIdList);

    }

    /**
     * cso查询fac列表
     * @param myCompanyName
     * @return
     */
    public ServerResponse<List> selectFacCooperList(String myCompanyName){
        //获取本公司id
        Integer csoCompanyId = userApiFeign.getCompanyId(myCompanyName).getData();
        //查询商务合作表中的和本公司建立合作的公司id列表
        List<Integer> facIdList = buscooperationMapper.selectfacIdList(csoCompanyId);
        if(facIdList == null){
            return ServerResponse.createByErrorMessage("未查到建立合作关系的公司");
        }
        return getCompanyInfoBycompanyIds(facIdList);
    }

    /**
     * 根据公司名称获取staffNameList信息
     * @param companyName
     * @return
     */
    public ServerResponse<List> selectStaffInfoListByCompanyName(String companyName){
        List<Staff> staffList = userApiFeign.getStaffListByCompanyName(companyName).getData();
        if(staffList == null) {
            return ServerResponse.createByError();
        }
        List<CooperName> staffNameList = Lists.newArrayList();
        int x = 0;
        for(Staff staff: staffList){
            x = x+1;
            CooperName cooperName = new CooperName();
            cooperName.setId(x);
            cooperName.setName(staff.getName());
            staffNameList.add(cooperName);
        }
        return ServerResponse.createBySuccess(staffNameList);
    }

    /**
     * 选择联系人之后，返回联系人电话
     * @param companyName
     * @param staffName
     * @return
     */
    public ServerResponse<String> selectCallNumContNum(String companyName,String staffName){
        return userApiFeign.getUserNameByCompanyNameStaffName(companyName,staffName);
    }

    /**
     * 接受企业承诺书之后返回协议编号
     * @param myCompanyName
     * @return
     */
    public ServerResponse<String> getContNum(String myCompanyName){
        //获取本公司信息
        CompanyInfoVo companyInfoVo = JSON.parseObject(JSON.toJSONString(userApiFeign.getCompanyInfoBycompanyName(myCompanyName).getData()),CompanyInfoVo.class);
        if(companyInfoVo == null){
            return ServerResponse.createByError();
        }
        StringBuffer sb = new StringBuffer();
        sb.append(companyInfoVo.getCode());//公司编号9位
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        sb.append(formatter.format(date));//8位
        sb.append(getCode());//2位
        String contNum = sb.toString();//协议编号19位
        return ServerResponse.createBySuccess(contNum);
    }

    /**
     * 获取公司详细信息生成协议
     * @param companyName
     * @return
     */
    public ServerResponse<CompanyInfoVo> getCompanyInfo(String companyName,String realName,String userName){
        //获取本公司信息
        CompanyInfoVo companyInfoVo = JSON.parseObject(JSON.toJSONString(userApiFeign.getCompanyInfoBycompanyName(companyName).getData()),CompanyInfoVo.class);
        companyInfoVo.setRealName(realName);
        companyInfoVo.setAcctNumOrPhone(userName);
        return ServerResponse.createBySuccess(companyInfoVo);

    }


    //--------------------------



    /**
     * 协议提交保存（fac为甲方）
     * @param signPrtcInfoVo
     * @param servletContext
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<String> submitSignPrtc(SignPrtcInfoVo signPrtcInfoVo,
                                                    ServletContext servletContext){
        try {
            Agreement newAgreement = new Agreement();
            newAgreement.setAgreementCode(signPrtcInfoVo.getContNum());//协议编号
            System.out.println("1111111");
            newAgreement.setName(signPrtcInfoVo.getContName());//协议名称
            newAgreement.setFactoryId(signPrtcInfoVo.getAId());//甲方公司id
            newAgreement.setFacContactName(signPrtcInfoVo.getACallName());//甲方联系人姓名
            newAgreement.setFacPhone(signPrtcInfoVo.getACallNum());//甲方联系人电话
            newAgreement.setCsoId(signPrtcInfoVo.getBId());//乙方公司id
            newAgreement.setCsoContactName(signPrtcInfoVo.getBCallName());//乙方联系人姓名
            newAgreement.setCsoPhone(signPrtcInfoVo.getBCallNum());//乙方联系人电话
            newAgreement.setServiceArea(signPrtcInfoVo.getServArea());//服务范围
            newAgreement.setCsoSignTime(signPrtcInfoVo.getCsoSignDate());//cso签订日期
            newAgreement.setFacSignTime(signPrtcInfoVo.getFacSignDate());//fac签订日期
            newAgreement.setStartTime(signPrtcInfoVo.getServStart());//服务开始时间
            newAgreement.setEndTime(signPrtcInfoVo.getServUntil());//服务结束时间
            newAgreement.setSignStatus(signPrtcInfoVo.getSignStatus());//签约状态
            newAgreement.setServiceItem(signPrtcInfoVo.getServiceItem());//服务项目方式
            newAgreement.setCsoRightDuty(signPrtcInfoVo.getCsoRightDuty());//cso权利义务
            newAgreement.setFacRightDuty(signPrtcInfoVo.getFacRightDuty());//fac权利义务
            newAgreement.setBusinessSecret(signPrtcInfoVo.getBusinessSecret());//商业秘密
            newAgreement.setBreachResponsibility(signPrtcInfoVo.getBreachResponsibility());//违约责任
            newAgreement.setForceMajeure(signPrtcInfoVo.getForceMajeure());//不可抗力
            newAgreement.setOther(signPrtcInfoVo.getOther());//其他
            System.out.println("000000000");
            newAgreement.setCsoSealImg(signPrtcInfoVo.getCsoSealImg());//cso盖章
            newAgreement.setFacSealImg(signPrtcInfoVo.getFacSealImg());//fac盖章
            newAgreement.setCsoSignName(signPrtcInfoVo.getCsoSignName());//cso签名
            newAgreement.setFacSignName(signPrtcInfoVo.getFacSignName());//fac签名
            agreementMapper.insertSelectiveReturnId(newAgreement);//插入记录返回id
            System.out.println("222222");
            Integer returnAgreementId = newAgreement.getId();//获取返回的id
            System.out.println("3333333");
            ProtocolservicePrice newProtocolservicePrice = new ProtocolservicePrice();
            newProtocolservicePrice.setOnlineVideoConference(signPrtcInfoVo.getLineVideo());
            newProtocolservicePrice.setVideoPromotion(signPrtcInfoVo.getVideExt());//视频推广
            newProtocolservicePrice.setDailyVisit(signPrtcInfoVo.getEreVisit());//日常拜访
            newProtocolservicePrice.setQuestionSurvey(signPrtcInfoVo.getQuestion());//问卷调查
            newProtocolservicePrice.setArchives(signPrtcInfoVo.getArchives());//档案
            newProtocolservicePrice.setAdvisoryReport(signPrtcInfoVo.getAdvReport());//咨询报告
            newProtocolservicePrice.setOfflineDepartments(signPrtcInfoVo.getUnderDepart());//线下科室会
            newProtocolservicePrice.setAgreementId(returnAgreementId);//协议id
            protocolservicePriceMapper.insertSelective(newProtocolservicePrice);
            return ServerResponse.createBySuccessMessage("协议保存成功");
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorMessage("协议保存失败");
        }
    }


    //------------------------

    //cso查询签约列表
    public ServerResponse<List<CompanyInfoVo>> csoSelectAllAgreFactory(String myCompanyName){
        //获取本公司id
        Integer csoCompanyId = userApiFeign.getCompanyId(myCompanyName).getData();
        List<CompanyInfoVo> companyInfoVoList=Lists.newArrayList();
        //1、在agreement表中：查询出工厂id、csoId和签约状态,要显示签约状态
        List<Agreement> agreementList = agreementMapper.selectByCsoId(csoCompanyId);
        if(agreementList == null){
            return ServerResponse.createByErrorMessage("未查找到签约对象");
        }
        int i = 0;
        for(Agreement agreement:agreementList){
            i++;
            CompanyInfoVo newCompanyInfoVo = new CompanyInfoVo();
            newCompanyInfoVo.setNumber(i);//序号
            newCompanyInfoVo.setContNum(agreement.getAgreementCode());//协议编号
            newCompanyInfoVo.setName(agreement.getFacContactName());//协议联系人姓名
            newCompanyInfoVo.setTellPhone(agreement.getFacPhone());//协议联系人电话
            newCompanyInfoVo.setOptions("");//操作
            Integer signStatus = agreement.getSignStatus();//签约状态
            if(signStatus == 1){
                newCompanyInfoVo.setSignStatus("申请中");
            }else if(signStatus == 2){
                newCompanyInfoVo.setSignStatus("待审核");
            }else if(signStatus == 3){
                newCompanyInfoVo.setSignStatus("签约中");
            }else if(signStatus == 4){
                newCompanyInfoVo.setSignStatus("已拒绝");
            }
            //获取公司信息
            String name = userApiFeign.getCompanyNameByCompanyId(agreement.getFactoryId()).getData();
            if(name == null){
                return ServerResponse.createByError();
            }
            newCompanyInfoVo.setCompanyName(name);//公司名称
            companyInfoVoList.add(newCompanyInfoVo);
        }
        return ServerResponse.createBySuccess(companyInfoVoList);
    }

    //fac查询签约列表
    public ServerResponse<List<CompanyInfoVo>> facSelectAllAgreFactory(String myCompanyName){
        //获取本公司id
        Integer facCompanyId = userApiFeign.getCompanyId(myCompanyName).getData();
        List<CompanyInfoVo> companyInfoVoList=Lists.newArrayList();
        //1、在agreement表中：查询出工厂id、csoId和签约状态,要显示签约状态
        List<Agreement> agreementList = agreementMapper.selectByFacId(facCompanyId);
        if(agreementList == null){
            return ServerResponse.createByErrorMessage("未查找到签约对象");
        }
        int i = 0;
        for(Agreement agreement:agreementList){
            i++;
            CompanyInfoVo newCompanyInfoVo = new CompanyInfoVo();
            newCompanyInfoVo.setNumber(i);//序号
            newCompanyInfoVo.setContNum(agreement.getAgreementCode());//协议编号
            newCompanyInfoVo.setName(agreement.getCsoContactName());//协议联系人姓名
            newCompanyInfoVo.setTellPhone(agreement.getCsoPhone());//协议联系人电话
            newCompanyInfoVo.setOptions("");//操作
            Integer signStatus = agreement.getSignStatus();//签约状态
            if(signStatus == 1){
                newCompanyInfoVo.setSignStatus("待审核");
            }else if(signStatus == 2){
                newCompanyInfoVo.setSignStatus("申请中");
            }else if(signStatus == 3){
                newCompanyInfoVo.setSignStatus("签约中");
            }else if(signStatus == 4){
                newCompanyInfoVo.setSignStatus("已拒绝");
            }
            //获取公司信息
            String name= userApiFeign.getCompanyNameByCompanyId(agreement.getCsoId()).getData();
            if(name == null){
                return ServerResponse.createByError();
            }
            newCompanyInfoVo.setCompanyName(name);//公司名称
            companyInfoVoList.add(newCompanyInfoVo);
        }
        return ServerResponse.createBySuccess(companyInfoVoList);
    }

   //---------------------------------

    /**
     * 根据协议编号获取协议内容
     * @param agreementCode
     * @return
     */
    public ServerResponse<SignPrtcInfoVo> validSign(String agreementCode){
        Agreement agreement = agreementMapper.selectByAgreementCode(agreementCode);
        if(agreement == null){
            return ServerResponse.createByError();
        }
        SignPrtcInfoVo signPrtcInfoVo = new SignPrtcInfoVo();
        signPrtcInfoVo.setAId(agreement.getFactoryId());//甲方公司id
        signPrtcInfoVo.setACallName(agreement.getFacContactName());//甲方联系人姓名
        signPrtcInfoVo.setACallNum(agreement.getFacPhone());//甲方联系人电话
        signPrtcInfoVo.setBId(agreement.getCsoId());//乙方公司id
        signPrtcInfoVo.setBCallName(agreement.getCsoContactName());//乙方联系人姓名
        signPrtcInfoVo.setBCallNum(agreement.getCsoPhone());//乙方联系人电话
        signPrtcInfoVo.setCsoSignDate(agreement.getCsoSignTime());//cso签约时间
        signPrtcInfoVo.setFacSignDate(agreement.getFacSignTime());//fac签约时间
        signPrtcInfoVo.setSignStatus(agreement.getSignStatus());//签约状态
        signPrtcInfoVo.setCsoSealImg(agreement.getCsoSealImg());//cso公章
        signPrtcInfoVo.setFacSealImg(agreement.getFacSealImg());//fac公章
        signPrtcInfoVo.setCsoSignName(agreement.getCsoSignName());//cso签字
        signPrtcInfoVo.setFacSignName(agreement.getFacSignName());//fac签字
        signPrtcInfoVo.setContNum(agreement.getAgreementCode());//协议编号
        signPrtcInfoVo.setContName(agreement.getName());//协议名称
        signPrtcInfoVo.setServArea(agreement.getServiceArea());//服务区域
        signPrtcInfoVo.setServStart(agreement.getStartTime());//协议开始时间
        signPrtcInfoVo.setServUntil(agreement.getEndTime());//协议结束时间
        signPrtcInfoVo.setServiceItem(agreement.getServiceItem());//服务项目方式
        signPrtcInfoVo.setCsoRightDuty(agreement.getCsoRightDuty());//cso权利义务
        signPrtcInfoVo.setFacRightDuty(agreement.getFacRightDuty());//fac权利义务
        signPrtcInfoVo.setBusinessSecret(agreement.getBusinessSecret());//商业秘密
        signPrtcInfoVo.setBreachResponsibility(agreement.getBreachResponsibility());//违约责任
        signPrtcInfoVo.setForceMajeure(agreement.getForceMajeure());//不可抗力
        signPrtcInfoVo.setOther(agreement.getOther());//其他
        Integer agreementId = agreement.getId();
        ProtocolservicePrice protocolservicePrice = protocolservicePriceMapper.selectByAgreementId(agreementId);
        if(protocolservicePrice == null){
            return ServerResponse.createByError();
        }
        signPrtcInfoVo.setLineVideo(protocolservicePrice.getOnlineVideoConference());//线上视频会
        signPrtcInfoVo.setVideExt(protocolservicePrice.getVideoPromotion());//视频推广
        signPrtcInfoVo.setEreVisit(protocolservicePrice.getDailyVisit());//日常拜访
        signPrtcInfoVo.setQuestion(protocolservicePrice.getQuestionSurvey());//问卷调查
        signPrtcInfoVo.setArchives(protocolservicePrice.getArchives());//档案
        signPrtcInfoVo.setAdvReport(protocolservicePrice.getAdvisoryReport());//咨询报告
        signPrtcInfoVo.setUnderDepart(protocolservicePrice.getOfflineDepartments());//线下会议
        Integer csoId = agreement.getCsoId();
        Integer facId = agreement.getFactoryId();
        String csoCompanyName = userApiFeign.getCompanyNameByCompanyId(csoId).getData();//获取cso公司名称
        String facCompanyName = userApiFeign.getCompanyNameByCompanyId(facId).getData();//获取fac公司名称
        CompanyInfoVo companyInfoVo = JSON.parseObject(JSON.toJSONString(userApiFeign.getCompanyInfoBycompanyName(csoCompanyName).getData()),CompanyInfoVo.class);
        if(companyInfoVo == null){
            return ServerResponse.createByError();
        }
        signPrtcInfoVo.setAComp(facCompanyName);//甲方公司名称
        signPrtcInfoVo.setBComp(csoCompanyName);//乙方公司名称
        signPrtcInfoVo.setCsoBank(companyInfoVo.getOpenBank());//开户银行
        signPrtcInfoVo.setCsoBankNum(companyInfoVo.getBankNum());//银行账号
        return ServerResponse.createBySuccess(signPrtcInfoVo);
    }

    //cso审核签约通过
    public ServerResponse<Agreement> csoPassProtocol(String myCompanyName ,String agreementCode,String csoSignName,String csoSignTime){
        CompanyInfoVo companyInfoVo = JSON.parseObject(JSON.toJSONString(userApiFeign.getCompanyInfoBycompanyName(myCompanyName).getData()),CompanyInfoVo.class);
        if(companyInfoVo == null){
            return ServerResponse.createByError();
        }
        Agreement agreement = new Agreement();
        agreement.setCsoSealImg(companyInfoVo.getSeal());//cso公章
        agreement.setAgreementCode(agreementCode);//协议编号
        agreement.setCsoSignName(csoSignName);//cso签字
        agreement.setCsoSignTime(csoSignTime);//cso签约时间
        agreement.setSignStatus(3);//签约状态：已签约
        int resultCount = agreementMapper.updateByAgreementCodeSelective(agreement);
        if(resultCount >0){
            return ServerResponse.createBySuccessMessage("签约审核成功");
        }
        return ServerResponse.createByErrorMessage("签约审核失败");
    }
    //fac审核签约通过
    public ServerResponse<Agreement> facPassProtocol(String myCompanyName ,String agreementCode,String facSignName,String facSignTime){
        CompanyInfoVo companyInfoVo = JSON.parseObject(JSON.toJSONString(userApiFeign.getCompanyInfoBycompanyName(myCompanyName).getData()),CompanyInfoVo.class);
        if(companyInfoVo == null){
            return ServerResponse.createByError();
        }
        Agreement agreement = new Agreement();
        agreement.setFacSealImg(companyInfoVo.getSeal());//fac公章
        agreement.setAgreementCode(agreementCode);//协议编号
        agreement.setFacSignName(facSignName);//fac签字
        agreement.setFacSignTime(facSignTime);//fac签约时间
        agreement.setSignStatus(3);//签约状态：已签约
        int resultCount = agreementMapper.updateByAgreementCodeSelective(agreement);
        if(resultCount >0){
            return ServerResponse.createBySuccessMessage("签约审核成功");
        }
        return ServerResponse.createByErrorMessage("签约审核失败");
    }

    //拒绝签约
    public ServerResponse<String> refuseSign(String agreementCode){
        Agreement agreement = new Agreement();
        agreement.setAgreementCode(agreementCode);//协议编号
        agreement.setSignStatus(4);//签约状态：拒绝
        int resultCount = agreementMapper.updateByAgreementCodeSelective(agreement);
        if(resultCount >0){
            return ServerResponse.createBySuccessMessage("签约审核成功");
        }
        return ServerResponse.createByErrorMessage("签约审核失败");
    }

    //admin查询cso签约列表
    public ServerResponse<List<CompanyInfoVo>> adminSelectCsoSign(String myCompanyName){
        //获取本公司id
        Integer csoCompanyId = userApiFeign.getCompanyId(myCompanyName).getData();
        List<CompanyInfoVo> companyInfoVoList=Lists.newArrayList();
        //1、在agreement表中：查询出工厂id、csoId和签约状态,要显示签约状态
        List<Agreement> agreementList = agreementMapper.selectByCsoId(csoCompanyId);
        if(agreementList == null){
            return ServerResponse.createByErrorMessage("未查找到签约对象");
        }
        int i = 0;
        for(Agreement agreement:agreementList){
            i++;
            CompanyInfoVo newCompanyInfoVo = new CompanyInfoVo();
            newCompanyInfoVo.setNumber(i);//序号
            newCompanyInfoVo.setContNum(agreement.getAgreementCode());//协议编号
            newCompanyInfoVo.setName(agreement.getFacContactName());//协议联系人姓名
            newCompanyInfoVo.setTellPhone(agreement.getFacPhone());//协议联系人电话
            //获取公司信息
            String name = userApiFeign.getCompanyNameByCompanyId(agreement.getFactoryId()).getData();
            if(name == null){
                return ServerResponse.createByError();
            }
            newCompanyInfoVo.setCompanyName(name);//公司名称
            Integer signStatus = agreement.getSignStatus();//签约状态
            if(signStatus == 3){
                String csoSignTime = agreement.getCsoSignTime();//cso签约时间
                String facSignTime = agreement.getFacSignTime();//fac签约时间
                Integer csoTime = Integer.parseInt(csoSignTime.replaceAll("/",""));
                Integer facTime = Integer.parseInt(facSignTime.replaceAll("/",""));
                if (csoTime > facTime){
                    newCompanyInfoVo.setSignTime(csoSignTime);
                }else{
                    newCompanyInfoVo.setSignTime(facSignTime);
                }
                companyInfoVoList.add(newCompanyInfoVo);
            }

        }
        return ServerResponse.createBySuccess(companyInfoVoList);
    }

    //admin查询fac签约列表
    public ServerResponse<List<CompanyInfoVo>> adminSelectFacSign(String myCompanyName){
        //获取本公司id
        Integer facCompanyId = userApiFeign.getCompanyId(myCompanyName).getData();
        List<CompanyInfoVo> companyInfoVoList=Lists.newArrayList();
        //1、在agreement表中：查询出工厂id、csoId和签约状态,要显示签约状态
        List<Agreement> agreementList = agreementMapper.selectByFacId(facCompanyId);
        if(agreementList == null){
            return ServerResponse.createByErrorMessage("未查找到签约对象");
        }
        int i = 0;
        for(Agreement agreement:agreementList){
            i++;
            CompanyInfoVo newCompanyInfoVo = new CompanyInfoVo();
            newCompanyInfoVo.setNumber(i);//序号
            newCompanyInfoVo.setContNum(agreement.getAgreementCode());//协议编号
            newCompanyInfoVo.setName(agreement.getCsoContactName());//协议联系人姓名
            newCompanyInfoVo.setTellPhone(agreement.getCsoPhone());//协议联系人电话
            //获取公司信息
            String name= userApiFeign.getCompanyNameByCompanyId(agreement.getCsoId()).getData();
            if(name == null){
                return ServerResponse.createByError();
            }
            newCompanyInfoVo.setCompanyName(name);//公司名称

            Integer signStatus = agreement.getSignStatus();//签约状态
            if(signStatus == 3){
                String csoSignTime = agreement.getCsoSignTime();//cso签约时间
                String facSignTime = agreement.getFacSignTime();//fac签约时间
                Integer csoTime = Integer.parseInt(csoSignTime.replaceAll("/",""));
                Integer facTime = Integer.parseInt(facSignTime.replaceAll("/",""));
                if (csoTime > facTime){
                    newCompanyInfoVo.setSignTime(csoSignTime);
                }else{
                    newCompanyInfoVo.setSignTime(facSignTime);
                }
                companyInfoVoList.add(newCompanyInfoVo);
            }
        }
        return ServerResponse.createBySuccess(companyInfoVoList);
    }


}
