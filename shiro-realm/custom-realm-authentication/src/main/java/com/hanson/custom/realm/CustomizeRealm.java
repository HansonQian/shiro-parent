package com.hanson.custom.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


/**
 * 自定义Realm
 */
public class CustomizeRealm extends AuthorizingRealm {

    /**
     * 设置Realm的名称
     *
     * @param name
     */
    @Override
    public void setName(String name) {

    }

    /**
     * 认证
     *
     * @param token 令牌
     * @return 认证信息
     * @throws AuthenticationException 认证异常信息
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println(token.getClass().getName());
        SimpleAuthenticationInfo info = null;
        if (token instanceof UsernamePasswordToken) {
            UsernamePasswordToken upt = (UsernamePasswordToken) token;
            String userName = upt.getUsername();
            String inputPassword = String.valueOf(upt.getPassword());
            System.out.println("inputPassword:"+inputPassword);
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


    /**
     * 授权
     *
     * @param principals 身份信息
     * @return 授权信息
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
}
