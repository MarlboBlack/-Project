package com.niaoren.eurekaclientuser.common;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class LocalCache {

    private static Logger logger = LoggerFactory.getLogger(LocalCache.class);

    public static final String VALID_PREFIX= "Valid_";

    //构建一个并发级别为4，初始容量为100，最大容量为1000，到期时间为5分钟的缓存
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().concurrencyLevel(4).initialCapacity(100).maximumSize(1000).expireAfterAccess(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    return "null";//返回"null"便于取值的时候判断
                }
            });
    //放入数据
    public static void setKey(String key,String value){
        localCache.put(key,value);
    }
    //取出数据
    public static String getKey(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)){
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            logger.error("缓存localCache出现异常",e);
        }
        return null;
    }
}
