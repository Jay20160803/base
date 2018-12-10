package com.onlyedu.newclasses;

import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;

import static com.onlyedu.newclasses.shiro.CustomRealm.HASHITERATIONS;

/**
 * @author Andy
 * @date 2018/12/5 15:06
 */
public class TestA {

    @Test
    public void test(){
        String ps =  new SimpleHash(Sha1Hash.ALGORITHM_NAME, "123456", null, HASHITERATIONS).toString();
        System.out.println(ps);
    }
}
