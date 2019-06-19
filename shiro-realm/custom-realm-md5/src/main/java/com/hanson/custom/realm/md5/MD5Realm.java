package com.hanson.custom.realm.md5;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * 自定义Realm
 */
public class MD5Realm extends AuthorizingRealm {


    @Override
    public void setName(String name) {
        super.setName("MD5Realm");
    }

    /**
     * 授权
     *
     * @param principals 身份
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 认证
     *
     * @param token 令牌
     * @return 主体
     * @throws AuthenticationException 认证异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        SimpleAuthenticationInfo info = null;
        if (token instanceof UsernamePasswordToken) {
            UsernamePasswordToken upt = (UsernamePasswordToken) token;
            String uptUsername = upt.getUsername();
            // 调用数据查询是否能够查询到用户
            if (!"hanson".equals(uptUsername)) {
                throw new UnknownAccountException();
            }
            //数据返回的密码以及盐,对应密码明文是root
            String retPwd = "6abf37f91dc9a38ce225c4601dbec901";
            String salt = "hs";
            info = new SimpleAuthenticationInfo(uptUsername, retPwd, ByteSource.Util.bytes(salt), this.getName());
        }

        return info;
    }
}
