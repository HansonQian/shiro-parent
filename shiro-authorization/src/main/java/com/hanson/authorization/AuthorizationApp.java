package com.hanson.authorization;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import java.util.Arrays;

/**
 * 授权
 */
public class AuthorizationApp {
    /**
     * main方法
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        // 1、通过ini文件构建SecurityManagerFactory
        Factory<SecurityManager> securityManagerFactory
                = new IniSecurityManagerFactory("classpath:shiro-permission.ini");
        // 2、通过SecurityManagerFactory获得一个SecurityManager实例
        SecurityManager instance = securityManagerFactory.getInstance();
        // 3、装载当前的实例信息
        SecurityUtils.setSecurityManager(instance);
        // 4、获取Subject对象
        Subject subject = SecurityUtils.getSubject();
        // 5、通过指定的TOKEN进行认证
        try {
            subject.login(new UsernamePasswordToken("hanson","root"));
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        System.out.println("是否认证通过:"+subject.isAuthenticated());

        // 6、授权
        // 6.1、基于角色的授权
        boolean hasRole1 = subject.hasRole("role1");
        System.out.println("是否有具备角色[单个]:"+hasRole1);
        boolean hasAllRoles = subject.hasAllRoles(Arrays.asList("role1", "role2"));
        System.out.println("是否有具备角色[多个]:"+hasAllRoles);
        // 6.2、基于资源的授权
        boolean permitted = subject.isPermitted("user:create");
        System.out.println("是否具备资源权限[单个]:"+permitted);
        boolean permittedAll = subject.isPermittedAll("user:create:1",
                "user:delete");
        System.out.println("是否具备资源权限[多个]:"+permittedAll);
        // 使用check方法进行授权，如果授权不通过会抛出异常
        subject.checkPermission("person:create:1");
    }
}
