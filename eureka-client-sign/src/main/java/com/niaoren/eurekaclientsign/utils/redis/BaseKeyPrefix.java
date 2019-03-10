package com.niaoren.eurekaclientsign.utils.redis;

public abstract class BaseKeyPrefix implements KeyPrefix{

    private int expireSeconds;

    private String keyPrefix;

    public BaseKeyPrefix(int expireSeconds,String keyPrefix){
        this.expireSeconds = expireSeconds;
        this.keyPrefix = keyPrefix;
    }

    /**
     * 设置过期时间
     * @return
     */
    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    /**
     * 获取key的前缀，为了保证不同的key前缀不重复，我们用类去继承本抽象类，比如UserKey，OrderKey
     * 并在keyPrefix前面加上类名，这样肯定不会重复（毕竟不同的类去继承的时候，类名是不可能相同的）
     * @return
     */
    @Override
    public String getKeyPrefix() {
        String className = getClass().getSimpleName();
        return className+":"+keyPrefix;
    }
}
