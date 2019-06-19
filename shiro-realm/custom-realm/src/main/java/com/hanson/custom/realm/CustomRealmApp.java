package com.hanson.custom.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

/**
 * 自定义Realm程序
 */
public class CustomRealmApp {
    /**
     * main方法
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        Factory<SecurityManager> securityManagerFactory
                = new IniSecurityManagerFactory("classpath:custom-realm.ini");
        SecurityManager instance = securityManagerFactory.getInstance();
        SecurityUtils.setSecurityManager(instance);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("hanson1", "123456");
        try {
            subject.login(token);
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            System.out.println("用户名或者密码不对");
            e.printStackTrace();
        }
    }
}
