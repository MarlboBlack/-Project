package com.niaoren.eurekaclientsign.entity;

public class Buscooperation {
    private Integer id;

    private Integer csoId;

    private Integer factoryId;

    private Integer status;

    private Integer factoryChildId;

    public Buscooperation(Integer id, Integer csoId, Integer factoryId, Integer status, Integer factoryChildId) {
        this.id = id;
        this.csoId = csoId;
        this.factoryId = factoryId;
        this.status = status;
        this.factoryChildId = factoryChildId;
    }

    public Buscooperation() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFactoryChildId() {
        return factoryChildId;
    }

    public void setFactoryChildId(Integer factoryChildId) {
        this.factoryChildId = factoryChildId;
    }
}