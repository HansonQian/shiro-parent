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
            // 根据用户账号从数据库取出盐和加密后的值
            //..这里使用静态数据
            //如果根据账号没有找到用户信息则返回null，shiro抛出异常“账号不存在”
            if (!"hanson".equals(uptUsername)) {
                throw new UnknownAccountException();
            }
            //按照固定规则加密码结果 ，此密码 要在数据库存储，原始密码是root，盐是hs
            String retPwd = "6abf37f91dc9a38ce225c4601dbec901";
            //盐，随机数，此随机数也在数据库存储
            String salt = "hs";
            info = new SimpleAuthenticationInfo(uptUsername, retPwd, ByteSource.Util.bytes(salt), this.getName());
        }
        // 返回认证信息
        return info;
    }
}
