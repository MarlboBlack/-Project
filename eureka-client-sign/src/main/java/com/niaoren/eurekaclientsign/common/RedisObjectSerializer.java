package com.niaoren.eurekaclientsign.common;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.*;


public class RedisObjectSerializer implements RedisSerializer<Object> {

    static final byte[] EMPTY_ARRAY = new byte[0];

    //对象序列化成字节数组
    @Override
    public byte[] serialize(Object o) throws SerializationException {
        if(o == null){
            return EMPTY_ARRAY;
        }
        ObjectOutputStream oos = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(o);
            byte[] b = bos.toByteArray();
            return b;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //字节数组反序列化成对象
    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (isEmpty(bytes)){
            return null;
        }
        ObjectInputStream ois = null;
        ByteArrayInputStream bis = null;
        bis = new ByteArrayInputStream(bytes);
        try {
            ois = new ObjectInputStream(bis);
            Object obj = ois.readObject();
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    private boolean isEmpty(byte[] data){
        return (data == null||data.length == 0);
    }
}
