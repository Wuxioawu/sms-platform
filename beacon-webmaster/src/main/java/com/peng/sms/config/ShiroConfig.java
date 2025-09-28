//package com.peng.sms.config;
//
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.AuthenticationInfo;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.authc.SimpleAuthenticationInfo;
//import org.apache.shiro.realm.Realm;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//
//public class ShiroConfig {
//
//    @Bean
//    public Realm myRealm() {
//        return new Realm() {
//            @Override
//            public String getName() {
//                return "myRealm";
//            }
//
//            @Override
//            public boolean supports(AuthenticationToken token) {
//                return true; // 支持所有类型的 token，按需修改
//            }
//
//            @Override
//            public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
//                String username = (String) token.getPrincipal();
//                String password = new String((char[]) token.getCredentials());
//                if ("admin".equals(username) && "123456".equals(password)) {
//                    return new SimpleAuthenticationInfo(username, password, getName());
//                }
//                throw new AuthenticationException("认证失败");
//            }
//        };
//    }
//
//}
