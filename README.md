### shiro-parent
#### 1、shiro认证
> 工程shiro-authentication
##### 1.1、认证流程
- 通过INI文件创建`SecurityManagerFactory`
- 从`SecurityManagerFactory`获取一个`SecurityManager`对象
- 将`SecurityManager`绑定到`SecurityUtils`中(单例存在)
- 从`SecurityUtils`获取`Subject`
- 调用`Subject.login(token)`进行认证
    - 实际是交给`SecurityManager`调用认证器`Authenticator(接口)`的实例`ModularRealmAuthenticator`进行认证
        - `ModularRealmAuthenticator`通过传入的token调用`doAuthenticate`方法
        - 在`doAuthenticate`方法中会判断`Realm`是否存在多个
            - 存在一个,调用`doSingleRealmAuthentication`
                - 在该方法中会根据传入的token到ini文件中查找信息`realm.getAuthenticationInfo(token);`
                - 查询得到返回`AuthenticationInfo`
                    - 返回的`AuthenticationInfo`包含密码,会和传入的密码进行比对
                    - 比对正确则认证通过
                    - 比对失败则抛出`IncorrectCredentialsException`
                - 查询不到抛出异常`UnknownAccountException`
            - 存在多个,调用`doMultiRealmAuthentication`
                - 和调用一个类似，多了一个认证策略`AuthenticationStrategy`,这个需要额外的配置
##### 2、自定义Realm
> 工程shiro-realm,实际工作中用户的信息应当存在数据库中,上面认证示例采用的是在ini文件中配置用户信息

- Shiro提供的Realm
    - 最基础的接口是Realm
        - CachingRealm: 负责缓存处理
        - AuthenticationRealm: 负责认证
        - AuthorizingRealm: 负责授权,通常自定义的realm继承AuthorizingRealm