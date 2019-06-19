package com.hanson.custom.realm.md5;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class MD5RealmApp {
    /**
     * main方法
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        //md5Hash("root","hs",2);
        shiro("hanson", "123456");
    }

    /**
     * @param userName 身份
     * @param password 凭证
     */
    public static void shiro(String userName, String password) {
        Factory<SecurityManager> securityManagerFactory
                = new IniSecurityManagerFactory("classpath:md5-realm.ini");
        SecurityManager instance = securityManagerFactory.getInstance();
        SecurityUtils.setSecurityManager(instance);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        try {
            subject.login(token);
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            System.out.println("用户名或者密码不对");
            e.printStackTrace();
        }
    }

    /**
     * @param sourcePwd      原始密码
     * @param salt           盐
     * @param hashIterations 散列次数
     */
    public static void md5Hash(String sourcePwd, String salt, int hashIterations) {
        Md5Hash md5Hash = new Md5Hash(sourcePwd, salt, hashIterations);
        String pwdMd5Hash = md5Hash.toString();
        System.out.println(pwdMd5Hash);
        SimpleHash simpleHash = new SimpleHash("md5", sourcePwd, salt, hashIterations);
        System.out.println(simpleHash);
    }


}
