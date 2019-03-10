package com.niaoren.eurekaclientsign.entity.apiEntity;

import lombok.Data;

import java.util.Date;

@Data
public class Company {
    private Integer id;

    private String code;

    private Integer type;

    private String name;

    private String synopsis;

    private String email;

    private String workAddress;

    private String website;

    private String recordCode;

    private String unifyCreditCode;

    private String contactName;

    private String contactIdNumber;

    private String contactPhoneNumber;

    private String bank;

    private String bankNumber;

    private String gmpNumber;

    private String creditRating;

    private Integer runState;

    private Integer validState;

    private Date time;

    public Company(Integer id, String code, Integer type, String name, String synopsis, String email, String workAddress, String website, String recordCode, String unifyCreditCode, String contactName, String contactIdNumber, String contactPhoneNumber, String bank, String bankNumber, String gmpNumber, String creditRating, Integer runState, Integer validState, Date time) {
        this.id = id;
        this.code = code;
        this.type = type;
        this.name = name;
        this.synopsis = synopsis;
        this.email = email;
        this.workAddress = workAddress;
        this.website = website;
        this.recordCode = recordCode;
        this.unifyCreditCode = unifyCreditCode;
        this.contactName = contactName;
        this.contactIdNumber = contactIdNumber;
        this.contactPhoneNumber = contactPhoneNumber;
        this.bank = bank;
        this.bankNumber = bankNumber;
        this.gmpNumber = gmpNumber;
        this.creditRating = creditRating;
        this.runState = runState;
        this.validState = validState;
        this.time = time;
    }

    public Company() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis == null ? null : synopsis.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress == null ? null : workAddress.trim();
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website == null ? null : website.trim();
    }

    public String getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(String recordCode) {
        this.recordCode = recordCode == null ? null : recordCode.trim();
    }

    public String getUnifyCreditCode() {
        return unifyCreditCode;
    }

    public void setUnifyCreditCode(String unifyCreditCode) {
        this.unifyCreditCode = unifyCreditCode == null ? null : unifyCreditCode.trim();
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName == null ? null : contactName.trim();
    }

    public String getContactIdNumber() {
        return contactIdNumber;
    }

    public void setContactIdNumber(String contactIdNumber) {
        this.contactIdNumber = contactIdNumber == null ? null : contactIdNumber.trim();
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber == null ? null : contactPhoneNumber.trim();
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank == null ? null : bank.trim();
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber == null ? null : bankNumber.trim();
    }

    public String getGmpNumber() {
        return gmpNumber;
    }

    public void setGmpNumber(String gmpNumber) {
        this.gmpNumber = gmpNumber == null ? null : gmpNumber.trim();
    }

    public String getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating == null ? null : creditRating.trim();
    }

    public Integer getRunState() {
        return runState;
    }

    public void setRunState(Integer runState) {
        this.runState = runState;
    }

    public Integer getValidState() {
        return validState;
    }

    public void setValidState(Integer validState) {
        this.validState = validState;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}