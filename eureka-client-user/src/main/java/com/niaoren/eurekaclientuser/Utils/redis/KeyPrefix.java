package com.niaoren.eurekaclientuser.Utils.redis;

public interface KeyPrefix {

    //key的有效期
    public int expireSeconds();
    //获取key的前缀
    public String getKeyPrefix();
}
