package com.niaoren.eurekaclientuser.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Validstate {
    private Integer id;

    private String companyName;

    private Integer subState;

    private Integer auditState;

    private Integer passState;

    private Date time;

    private Integer adminUserId;

    public Validstate(Integer id, String companyName, Integer subState, Integer auditState, Integer passState, Date time, Integer adminUserId) {
        this.id = id;
        this.companyName = companyName;
        this.subState = subState;
        this.auditState = auditState;
        this.passState = passState;
        this.time = time;
        this.adminUserId = adminUserId;
    }

    public Validstate() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public Integer getSubState() {
        return subState;
    }

    public void setSubState(Integer subState) {
        this.subState = subState;
    }

    public Integer getAuditState() {
        return auditState;
    }

    public void setAuditState(Integer auditState) {
        this.auditState = auditState;
    }

    public Integer getPassState() {
        return passState;
    }

    public void setPassState(Integer passState) {
        this.passState = passState;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Integer adminUserId) {
        this.adminUserId = adminUserId;
    }
}