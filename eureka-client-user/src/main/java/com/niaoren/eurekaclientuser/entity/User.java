package com.niaoren.eurekaclientuser.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private String name;
    private String password;
    private int age;

    public User(String name,String password,int age){
        this.name = name;
        this.password = password;
        this.age = age;
    }

}
