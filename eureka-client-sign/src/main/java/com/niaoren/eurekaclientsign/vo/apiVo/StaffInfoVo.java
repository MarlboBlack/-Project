package com.niaoren.eurekaclientsign.vo.apiVo;

import lombok.Data;

@Data
public class StaffInfoVo {
    //staff表
    private String companyName;//公司名称
    private String acctNumOrPhone;//账号（手机号）
    private String nickName;//昵称
    private String agent;//代理商名称
    private String department;//部门
    private String post;//岗位
    private String pos;//职位
    private String posName;//职称
    private String area;//负责区域
    private String editHead;//编辑头像
    private String qqnumber;//qq号码
    private String wechat;//微信
    private String workAptitude;//从业资格证书
    private String name;//真实姓名
    private String registerTime;//注册时间



    //company表
    private String gmpNumber;//GMP编号
    private String openBank;//开户银行
    private String bankNum;//银行账号
    private String creditCode;//社会统一信用代码
    private String idNumber;//身份证
    private String email;//邮箱
    private String tellPhone;//联系电话
    private String address;//工作地址
    private String webadress;//网址
    private String redNumber;//备案号
    private String synopsis;//简介

    //file表
    private String idNumberImg;//身份证图片
    private String businLicence;//营业执照
    private String openPermit;//开户许可证
    private String seal;//公章水印
    private String gmpPermit;//gmp证书
    private String drugAppr;//药品批文
    private String aptitude;//资质证书
    private String controlSystem;//企业内控制度

    //其他
    private Integer number;//子账号序号
    private String options;//操作



}
