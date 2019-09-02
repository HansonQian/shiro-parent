package com.hanson.custom.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义Realm，进行授权
 */
public class CustomizeRealmAuthorize extends AuthorizingRealm {

    @Override
    public void setName(String name) {
        super.setName("CustomizeRealmAuthorize");
    }

    /**
     * 授权
     *
     * @param principals 身份信息
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 从principals获取主身份信息
        // 将getPrimaryPrincipal方法返回值转为真实身份类型
        String userCode = String.valueOf(principals.getPrimaryPrincipal());
        // 根据身份信息获取权限信息
        // 连接数据库...
        // 模拟从数据库获取到数据
        List<String> permissions = new ArrayList<>();
        permissions.add("user:create");
        permissions.add("items:add");
        // 查询到权限数据,返回授权信息
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 将上边查询到的授权信息填充到SimpleAuthorizationInfo实例中
        simpleAuthorizationInfo.addStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        SimpleAuthenticationInfo info = null;
        if (token instanceof UsernamePasswordToken) {
            UsernamePasswordToken upt = (UsernamePasswordToken) token;
            String userName = upt.getUsername();
            String inputPassword = String.valueOf(upt.getPassword());
            // 根据用户名到数据库中查询密码
            if (!"hanson".equals(userName)) {
                throw new UnknownAccountException("根据[" + userName + "]查询不到用户信息");
            }
            // 静态密码
            String retPassword = "root";
            // 密码比对
            if (!retPassword.equals(inputPassword)) {
                throw new IncorrectCredentialsException("根据[" + userName + "]查询到用户信息,但是密码不对");
            }
            // 返回认证信息由父类AuthenticatingRealm进行认证
            info = new SimpleAuthenticationInfo(userName, retPassword, this.getName());
        }
        return info;
    }
}
