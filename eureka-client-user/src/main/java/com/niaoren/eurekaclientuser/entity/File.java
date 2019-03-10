package com.niaoren.eurekaclientuser.entity;

import lombok.Data;

import java.util.Date;

@Data
public class File {
    private Integer id;

    private Integer type;

    private String ftpAddress;

    private String companyName;

    private Date time;

    public File(Integer id, Integer type, String ftpAddress, String companyName, Date time) {
        this.id = id;
        this.type = type;
        this.ftpAddress = ftpAddress;
        this.companyName = companyName;
        this.time = time;
    }

    public File() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFtpAddress() {
        return ftpAddress;
    }

    public void setFtpAddress(String ftpAddress) {
        this.ftpAddress = ftpAddress == null ? null : ftpAddress.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}