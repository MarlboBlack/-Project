package com.niaoren.eurekaclientuser;

import com.niaoren.eurekaclientuser.Utils.redis.MyJedisPool;
import com.niaoren.eurekaclientuser.Utils.redis.JedisUtil;
import com.niaoren.eurekaclientuser.Utils.redis.UserKey;
import com.niaoren.eurekaclientuser.entity.User;
import org.junit.Test;

public class MyTest {

    @Test
    public void test3(){
        JedisUtil myJedisUtil = new JedisUtil();
        User user = new User("lisi","000000",45);
        myJedisUtil.set(UserKey.token,"11111",user);
    }
    @Test
    public void test4(){
        JedisUtil myJedisUtil = new JedisUtil();
        User user = myJedisUtil.get(UserKey.token,"11111",User.class);
        System.out.println(user.toString());
    }
}
