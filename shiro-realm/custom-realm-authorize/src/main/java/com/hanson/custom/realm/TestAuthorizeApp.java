package com.hanson.custom.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class TestAuthorizeApp {
    public static void main(String[] args) {
        Factory<SecurityManager> securityManagerFactory =
                new IniSecurityManagerFactory("classpath:shiro-realm.ini");
        SecurityManager securityManager = securityManagerFactory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("hanson", "root");
        try {
            subject.login(token);
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            e.printStackTrace();
        }
        System.out.println("是否认证通过:" + subject.isAuthenticated());

        // 基于资源的授权
        boolean permitted = subject.isPermitted("user:create");
        System.out.println("是否具备资源权限[单个]:" + permitted);

        boolean permittedAll = subject.isPermittedAll("user:create",
                "items:add");
        System.out.println("是否具备资源权限[多个]:" + permittedAll);

        // 使用check方法进行授权，如果授权不通过会抛出异常
        subject.checkPermission("person:create");
    }
}
