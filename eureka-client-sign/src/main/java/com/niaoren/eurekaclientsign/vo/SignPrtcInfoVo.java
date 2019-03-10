package com.niaoren.eurekaclientsign.vo;

import lombok.Data;


@Data
public class SignPrtcInfoVo {

    //Agreement表
    private Integer aId;//甲方公司id
    private String aComp;//甲方公司名称
    private String aCallName;//甲方联系人名称
    private String aCallNum;//甲方联系电话

    private Integer bId;//乙方公司id
    private String bComp;//乙方公司名称
    private String bCallName;//乙方联系人姓名
    private String bCallNum;//乙方联系电话

    private String csoSignDate;//cso签约日期
    private String facSignDate;//fac签约日期
    private Integer signStatus;//签约状态

    private String csoSealImg;//cso公章
    private String facSealImg;//fac公章
    private String csoSignName;//cso签名
    private String facSignName;//fac签名


    private String contNum;//协议编号agreementCode
    private String contName;//协议名称name
    private String servArea;//服务区域serviceArea

    private String servStart;//服务开始时间
    private String servUntil;//服务结束时间

    private String serviceItem;//服务项目方式
    private String csoRightDuty;//cso权利义务
    private String facRightDuty;//fac权利义务
    private String businessSecret;//商业秘密
    private String breachResponsibility;//违约责任
    private String forceMajeure;//不可抗力
    private String other;//其他


    //ProtocolservicePrice表
    private int lineVideo;//线上视频会  onlineVideoConference
    private int videExt;//视频推广  videoPromotion
    private int ereVisit;//日常拜访  dailyVisit
    private int question; //问卷调研    questionSurvey
    private int archives;//档案          archives
    private int advReport;//咨询报告    advisoryReport
    private int underDepart;//线下会议   offlineDepartments

    //Company表
    private String csoBank;
    private String csoBankNum;



}
