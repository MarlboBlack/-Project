package com.niaoren.eurekaclientuser.Utils.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class JedisUtil {

    /**
     * 往redis中存入一个javabean
     * @param keyPrefix   为了保证不同的key之间不重复，我们加了一个key的前缀
     * @param key         key值
     * @param value       要存入的javabean
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix keyPrefix,String key,T value){
        Jedis jedis = null;
        try{
            jedis = MyJedisPool.getJedis();
            //生成存入redis中的真正的key
            String realKey = keyPrefix.getKeyPrefix()+key;
            String str = beanToString(value);
            if(str == null||str.length() <= 0){
                return false;
            }
            jedis.set(realKey,str);
            return true;
        }finally{
            MyJedisPool.returnToPool(jedis);
        }
    }

    /**
     * 往redis中存入一个有超时时间的键
     * @param keyPrefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean expireSet(KeyPrefix keyPrefix,String key,T value){
        Jedis jedis = null;
        try{
            jedis = MyJedisPool.getJedis();
            //生成存入redis中的真正的key
            String realKey = keyPrefix.getKeyPrefix()+key;
            String str = beanToString(value);
            int expireTime = keyPrefix.expireSeconds();
            if(str == null||str.length() <= 0){
                return false;
            }
            //NX表示键不存在时，才对键进行设置操作，
            //XX表示键存在是，才对键进行设置操作
            //EX设置键的过期时间是second
            //PX设置键的过期时间是millSecond
            jedis.set(realKey,str,"NX","EX",expireTime);
            return true;
        }finally{
            MyJedisPool.returnToPool(jedis);
        }
    }

    /**
     * 从redis中取出一个javabean
     * @param keyPrefix 为了保证不同的key之间不重复，我们加了一个key的前缀
     * @param key       key值
     * @param clazz     要取出的javabean的类型
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix keyPrefix,String key,Class<T> clazz){
        Jedis jedis = null;
        try{
            jedis = MyJedisPool.getJedis();
            //生成存入redis中的真正的key
            String realKey = keyPrefix.getKeyPrefix()+key;
            String str = jedis.get(realKey);
            T t = stringToBean(str,clazz);
            return t;
        }finally{
            MyJedisPool.returnToPool(jedis);
        }

    }

    /**
     * 根据key获取值
     * @param key
     * @return
     */
    public String get(String key){
        Jedis jedis = null;
        try{
            jedis = MyJedisPool.getJedis();
            Boolean exists = jedis.exists(key);
            if(exists)
                return jedis.get(key);
        }catch(Exception e){
        }finally{
            MyJedisPool.returnToPool(jedis);
        }
        return "0";
    }


    /**
     * 给某个键增加过期时间
     * @param keyName
     * @param expireTime
     */
    public void expire(String keyName,int expireTime){
        Jedis jedis = null;
        try{
            jedis = MyJedisPool.getJedis();
            jedis.expire(keyName,expireTime);
        }finally{
            MyJedisPool.returnToPool(jedis);
        }
    }


    /**
     * 将一个javabean转成String
     * @param value  要转换的javabean
     * @param <T>
     * @return
     */
    private <T> String beanToString(T value){
        if(value == null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class||clazz ==Integer.class){
            return value+"";
        }else if(clazz == String.class){
            return (String)value;
        }else if(clazz == long.class||clazz == Long.class){
            return value+"";
        }else{
            return JSON.toJSONString(value);
        }
    }

    /**
     * 将String转成javabean
     * @param str    要转换的字符串
     * @param clazz     要转换成的java类
     * @param <T>
     * @return
     */
    private <T> T stringToBean(String str,Class<T> clazz){
        if(str == null||str.length() <= 0||clazz == null){
            return null;
        }
        if(clazz == int.class||clazz == Integer.class){
            return (T)Integer.valueOf(str);
        }else if(clazz == String.class){
            return (T) str;
        }else if(clazz == long.class||clazz == Long.class){
            return (T)Long.valueOf(str);
        }else{
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }
    }
}
