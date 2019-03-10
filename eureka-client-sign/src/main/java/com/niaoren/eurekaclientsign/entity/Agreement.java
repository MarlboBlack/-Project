package com.niaoren.eurekaclientsign.entity;

public class Agreement {
    private Integer id;

    private String agreementCode;

    private String name;

    private Integer csoId;

    private Integer factoryId;

    private String csoContactName;

    private String facContactName;

    private String csoPhone;

    private String facPhone;

    private String serviceArea;

    private String startTime;

    private String endTime;

    private String csoSignTime;

    private String facSignTime;

    private Integer signStatus;

    private String csoSealImg;

    private String facSealImg;

    private String csoSignName;

    private String facSignName;

    private String serviceItem;

    private String csoRightDuty;

    private String facRightDuty;

    private String businessSecret;

    private String breachResponsibility;

    private String forceMajeure;

    private String other;

    public Agreement(Integer id, String agreementCode, String name, Integer csoId, Integer factoryId, String csoContactName, String facContactName, String csoPhone, String facPhone, String serviceArea, String startTime, String endTime, String csoSignTime, String facSignTime, Integer signStatus, String csoSealImg, String facSealImg, String csoSignName, String facSignName, String serviceItem, String csoRightDuty, String facRightDuty, String businessSecret, String breachResponsibility, String forceMajeure, String other) {
        this.id = id;
        this.agreementCode = agreementCode;
        this.name = name;
        this.csoId = csoId;
        this.factoryId = factoryId;
        this.csoContactName = csoContactName;
        this.facContactName = facContactName;
        this.csoPhone = csoPhone;
        this.facPhone = facPhone;
        this.serviceArea = serviceArea;
        this.startTime = startTime;
        this.endTime = endTime;
        this.csoSignTime = csoSignTime;
        this.facSignTime = facSignTime;
        this.signStatus = signStatus;
        this.csoSealImg = csoSealImg;
        this.facSealImg = facSealImg;
        this.csoSignName = csoSignName;
        this.facSignName = facSignName;
        this.serviceItem = serviceItem;
        this.csoRightDuty = csoRightDuty;
        this.facRightDuty = facRightDuty;
        this.businessSecret = businessSecret;
        this.breachResponsibility = breachResponsibility;
        this.forceMajeure = forceMajeure;
        this.other = other;
    }

    public Agreement() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAgreementCode() {
        return agreementCode;
    }

    public void setAgreementCode(String agreementCode) {
        this.agreementCode = agreementCode == null ? null : agreementCode.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCsoId() {
        return csoId;
    }

    public void setCsoId(Integer csoId) {
        this.csoId = csoId;
    }

    public Integer getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    public String getCsoContactName() {
        return csoContactName;
    }

    public void setCsoContactName(String csoContactName) {
        this.csoContactName = csoContactName == null ? null : csoContactName.trim();
    }

    public String getFacContactName() {
        return facContactName;
    }

    public void setFacContactName(String facContactName) {
        this.facContactName = facContactName == null ? null : facContactName.trim();
    }

    public String getCsoPhone() {
        return csoPhone;
    }

    public void setCsoPhone(String csoPhone) {
        this.csoPhone = csoPhone == null ? null : csoPhone.trim();
    }

    public String getFacPhone() {
        return facPhone;
    }

    public void setFacPhone(String facPhone) {
        this.facPhone = facPhone == null ? null : facPhone.trim();
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea == null ? null : serviceArea.trim();
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    public String getCsoSignTime() {
        return csoSignTime;
    }

    public void setCsoSignTime(String csoSignTime) {
        this.csoSignTime = csoSignTime == null ? null : csoSignTime.trim();
    }

    public String getFacSignTime() {
        return facSignTime;
    }

    public void setFacSignTime(String facSignTime) {
        this.facSignTime = facSignTime == null ? null : facSignTime.trim();
    }

    public Integer getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(Integer signStatus) {
        this.signStatus = signStatus;
    }

    public String getCsoSealImg() {
        return csoSealImg;
    }

    public void setCsoSealImg(String csoSealImg) {
        this.csoSealImg = csoSealImg == null ? null : csoSealImg.trim();
    }

    public String getFacSealImg() {
        return facSealImg;
    }

    public void setFacSealImg(String facSealImg) {
        this.facSealImg = facSealImg == null ? null : facSealImg.trim();
    }

    public String getCsoSignName() {
        return csoSignName;
    }

    public void setCsoSignName(String csoSignName) {
        this.csoSignName = csoSignName == null ? null : csoSignName.trim();
    }

    public String getFacSignName() {
        return facSignName;
    }

    public void setFacSignName(String facSignName) {
        this.facSignName = facSignName == null ? null : facSignName.trim();
    }

    public String getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(String serviceItem) {
        this.serviceItem = serviceItem == null ? null : serviceItem.trim();
    }

    public String getCsoRightDuty() {
        return csoRightDuty;
    }

    public void setCsoRightDuty(String csoRightDuty) {
        this.csoRightDuty = csoRightDuty == null ? null : csoRightDuty.trim();
    }

    public String getFacRightDuty() {
        return facRightDuty;
    }

    public void setFacRightDuty(String facRightDuty) {
        this.facRightDuty = facRightDuty == null ? null : facRightDuty.trim();
    }

    public String getBusinessSecret() {
        return businessSecret;
    }

    public void setBusinessSecret(String businessSecret) {
        this.businessSecret = businessSecret == null ? null : businessSecret.trim();
    }

    public String getBreachResponsibility() {
        return breachResponsibility;
    }

    public void setBreachResponsibility(String breachResponsibility) {
        this.breachResponsibility = breachResponsibility == null ? null : breachResponsibility.trim();
    }

    public String getForceMajeure() {
        return forceMajeure;
    }

    public void setForceMajeure(String forceMajeure) {
        this.forceMajeure = forceMajeure == null ? null : forceMajeure.trim();
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other == null ? null : other.trim();
    }
}