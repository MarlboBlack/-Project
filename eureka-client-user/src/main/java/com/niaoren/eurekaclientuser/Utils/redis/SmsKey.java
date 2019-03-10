package com.niaoren.eurekaclientuser.Utils.redis;

public class SmsKey extends BaseKeyPrefix{

    public static final int SMS_EXPIRE = 20;

    private SmsKey(int expireSeconds, String keyPrefix) {
        super(expireSeconds, keyPrefix);
    }

    public static SmsKey smsKey = new SmsKey(SMS_EXPIRE,"sms");
}
