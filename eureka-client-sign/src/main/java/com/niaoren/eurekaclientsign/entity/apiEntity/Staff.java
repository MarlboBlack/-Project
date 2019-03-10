package com.niaoren.eurekaclientsign.entity.apiEntity;

import lombok.Data;

import java.util.Date;

@Data
public class Staff {
    private Integer id;

    private String userCode;

    private Integer userType;

    private String username;

    private String password;

    private String nickname;

    private String headimg;

    private String name;

    private String agentName;

    private String contactAddress;

    private String qq;

    private String wechat;

    private String companyCode;

    private String companyName;

    private String department;

    private String job;

    private String area;

    private String hospitalDept;

    private String hospitalPosition;

    private String hospitalTitle;

    private String phoneNumber;

    private String idCardNumber;

    private String workCompany;

    private Date time;

    private String lastLogin;

    private String workAptitude;

    public Staff(Integer id, String userCode, Integer userType, String username, String password, String nickname, String headimg, String name, String agentName, String contactAddress, String qq, String wechat, String companyCode, String companyName, String department, String job, String area, String hospitalDept, String hospitalPosition, String hospitalTitle, String phoneNumber, String idCardNumber, String workCompany, Date time, String lastLogin, String workAptitude) {
        this.id = id;
        this.userCode = userCode;
        this.userType = userType;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.headimg = headimg;
        this.name = name;
        this.agentName = agentName;
        this.contactAddress = contactAddress;
        this.qq = qq;
        this.wechat = wechat;
        this.companyCode = companyCode;
        this.companyName = companyName;
        this.department = department;
        this.job = job;
        this.area = area;
        this.hospitalDept = hospitalDept;
        this.hospitalPosition = hospitalPosition;
        this.hospitalTitle = hospitalTitle;
        this.phoneNumber = phoneNumber;
        this.idCardNumber = idCardNumber;
        this.workCompany = workCompany;
        this.time = time;
        this.lastLogin = lastLogin;
        this.workAptitude = workAptitude;
    }

    public Staff() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode == null ? null : userCode.trim();
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg == null ? null : headimg.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName == null ? null : agentName.trim();
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress == null ? null : contactAddress.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat == null ? null : wechat.trim();
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode == null ? null : companyCode.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job == null ? null : job.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getHospitalDept() {
        return hospitalDept;
    }

    public void setHospitalDept(String hospitalDept) {
        this.hospitalDept = hospitalDept == null ? null : hospitalDept.trim();
    }

    public String getHospitalPosition() {
        return hospitalPosition;
    }

    public void setHospitalPosition(String hospitalPosition) {
        this.hospitalPosition = hospitalPosition == null ? null : hospitalPosition.trim();
    }

    public String getHospitalTitle() {
        return hospitalTitle;
    }

    public void setHospitalTitle(String hospitalTitle) {
        this.hospitalTitle = hospitalTitle == null ? null : hospitalTitle.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber == null ? null : idCardNumber.trim();
    }

    public String getWorkCompany() {
        return workCompany;
    }

    public void setWorkCompany(String workCompany) {
        this.workCompany = workCompany == null ? null : workCompany.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin == null ? null : lastLogin.trim();
    }

    public String getWorkAptitude() {
        return workAptitude;
    }

    public void setWorkAptitude(String workAptitude) {
        this.workAptitude = workAptitude == null ? null : workAptitude.trim();
    }
}