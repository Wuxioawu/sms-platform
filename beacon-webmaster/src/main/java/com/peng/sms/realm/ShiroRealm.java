package com.peng.sms.realm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    private final String ADMIN = "admin";

    {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(1024);
        this.setCredentialsMatcher(credentialsMatcher);
    }

//    @Autowired
//    private SmsUserService userService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String username = (String) authenticationToken.getPrincipal();

//        SmsUser smsUser = userService.findByUsername(username);

        if(username == null) {
            log.info("[webmaster] -> ShiroRealm -> doGetAuthenticationInfo: the username is null ");
            return null;
        }

        if (!username.equals(ADMIN)) {
            log.info("[webmaster] -> ShiroRealm -> doGetAuthenticationInfo: the username is wrong");
            return null;
        }

//        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(smsUser,smsUser.getPassword(),"shiroRealm");
//        info.setCredentialsSalt(ByteSource.Util.bytes(smsUser.getSalt()));

        return null;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }
}
