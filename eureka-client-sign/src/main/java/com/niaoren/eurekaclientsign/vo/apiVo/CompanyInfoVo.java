package com.niaoren.eurekaclientsign.vo.apiVo;


import lombok.Data;

import java.util.Date;

@Data
public class CompanyInfoVo {
    //staff表
    private String userhead;//headImg

    private String lastLogin;//lastLogin

    private String acctNumOrPhone;//账号手机号

    private String nickName;//昵称

    private String agent;//代理商名称

    private String department;//部门

    private String post;//岗位

    private String pos;//职位

    private String posName;//职称

    private String area;//区域

    private String qqnumber;//qq号

    private String wechat;//微信

    private String workAptitude;

    private String realName;//真实姓名

    private String contactAddress;//联系地址（代理商）

    //company表

    private Integer id;//公司id

    private String code;//code

    private String companyName;//name

    private String synopsis;//synopsis

    private String email;//email

    private String address;//workAddress

    private String webadress;//website

    private String redNumber;//recordCode

    private String creditCode;//unifyCreditCode

    private String name;//contactName

    private String idNumber;//contactIdNumber

    private String tellPhone;//contactPhoneNumber

    private String openBank;//bank

    private String bankNum;//bankNumber

    private String gmpNumber;//gmpNumber

    private String creditHtml;//creditRating

    private Date joinTime;//time

    private Integer validState;//审核状态


    //file表
    private String aptitude;//file1
    private String businLicence;//file2
    private String openPermit;//file3
    private String seal;//file4
    private String controlSystem;//file5
    private String drugAppr;//file6
    private String gmpPermit;//file7
    private String idNumberImg;//file8

    //busCooperation表
    private String cooperStatus;//合作状态

    //agreement表
    private String contNum;//协议编号
    private String signStatus;//签约状态

    //其他
    private Integer number;//公司序号
    private String options;//操作
    private String signTime;//签约时间（以后签约的为主）


}
