package com.hanson.authentication;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

/**
 * 认证
 */
public class AuthenticationApp {

    /**
     * main方法
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        //1、构建SecurityManager工厂
        Factory<SecurityManager> securityManagerFactory
                = new IniSecurityManagerFactory("classpath:shiro.ini");
        //2、创建SecurityManager
        SecurityManager instance = securityManagerFactory.getInstance();
        //3、将SecurityManager设置到当前的运行环境中
        SecurityUtils.setSecurityManager(instance);
        //4、创建Subject
        Subject subject = SecurityUtils.getSubject();
        //5、准备Token
        UsernamePasswordToken token = new UsernamePasswordToken("hanson", "123456");
        //6、执行认证、异常信息都是AuthenticationException子类
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            System.out.println("未知帐号");
            e.printStackTrace();
        } catch (IncorrectCredentialsException e) {
            System.out.println("密码错误");
            e.printStackTrace();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        //7、是否认证通过
        boolean authenticated = subject.isAuthenticated();
        System.out.println("是否认证通过:" + authenticated);
        //8、退出操作
        subject.logout();
        //9、退出之后是否认证通过
        boolean subjectAuthenticated = subject.isAuthenticated();
        System.out.println("退出之后是否认证通过:" + subjectAuthenticated);
    }
}
