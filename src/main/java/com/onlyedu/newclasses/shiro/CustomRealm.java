package com.onlyedu.newclasses.shiro;

import com.onlyedu.newclasses.model.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Andy
 * @date 2018/11/23 17:11
 */

public class CustomRealm extends AuthorizingRealm {

    public static int  HASHITERATIONS = 1024;
    public CustomRealm(){

        //设置加密方式
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher(Sha1Hash.ALGORITHM_NAME);
        hashedCredentialsMatcher.setHashIterations(HASHITERATIONS);
        setCredentialsMatcher(hashedCredentialsMatcher);
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        Set<String> permissions = new HashSet<String>();
        permissions.add("user:add");
        permissions.add("user:delete");

        //更据原型查询
        info.setStringPermissions(permissions);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken unToken = (UsernamePasswordToken) token;
        String username = unToken.getUsername();

        if(StringUtils.isEmpty(username)){
            throw new AccountException("Null username are not allowed by this realm.");
        }

        User user = new User();
        user.setUserName("Andy");
        user.setPassWord("a94d5cd0079cfc8db030e1107de1addd1903a01b");

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username,user.getPassWord(),getName());
        return info;
    }
}
