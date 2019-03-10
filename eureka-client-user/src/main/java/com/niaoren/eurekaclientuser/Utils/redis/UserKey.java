package com.niaoren.eurekaclientuser.Utils.redis;

public class UserKey extends BaseKeyPrefix {

    public static final int TOKEN_EXPIRE = 7200;

    private UserKey(int expireSeconds, String keyPrefix) {
        super(expireSeconds, keyPrefix);
    }

    public static UserKey token = new UserKey(TOKEN_EXPIRE,"token");
}
