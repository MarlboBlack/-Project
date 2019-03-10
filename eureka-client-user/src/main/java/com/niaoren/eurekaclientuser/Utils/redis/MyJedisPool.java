package com.niaoren.eurekaclientuser.Utils.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

public class MyJedisPool {
    private static redis.clients.jedis.JedisPool jedisPool = null;

    //创建jedis连接池
    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("application");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(Integer.parseInt(resourceBundle.getString("redis.poolMaxTotal")));
        jedisPoolConfig.setMaxIdle(Integer.parseInt(resourceBundle.getString("redis.poolMaxIdle")));
        jedisPoolConfig.setMaxWaitMillis(Integer.parseInt(resourceBundle.getString("redis.poolMaxWait"))*1000);

        jedisPool = new redis.clients.jedis.JedisPool(jedisPoolConfig,resourceBundle.getString("redis.host"),Integer.parseInt(resourceBundle.getString("redis.port")));

    }

    //获取jedis实例
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }

    //放回jedisPool内
    public static void returnToPool(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }
}
