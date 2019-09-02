# shiro-parent
## 1、shiro认证
> 工程shiro-authentication
### 1.1、认证流程
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
### 1.2、自定义Realm，进行认证

> 工程shiro-realm#custom-realm-authentication

- Shiro提供的Realm
    - 最基础的接口是Realm
        - CachingRealm: 负责缓存处理
        - AuthenticationRealm: 负责认证
        - AuthorizingRealm: 负责授权,通常自定义的realm继承AuthorizingRealm
- 自定义Realm步骤
    - 1、编写类继承`AuthorizingRealm`
    - 2、重写方法
        - `void setName(String name)` 设置Realm名称
        - `AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)` 认证方法
        - `AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)` 授权方法
    - 3、配置Realm
        - 需要在ini文件中注入自定义的Realm(参考custom-realm-authentication工程)

### 1.3、自定义Realm，支持散列算法
> 实际应用是将盐和散列后的值存在数据库中，自定义realm从数据库取出盐和加密后的值由shiro完成密码校验。
>
> 工程 shiro-realm#custom-realm-authentication-md5

- 散列算法：
    散列算法一般用于生成一段文本的摘要信息，散列算法不可逆，将内容可以生成摘要，无法将摘要转成原始内容。散列算法常用于对密码进行散列，常用的散列算法有MD5、SHA。
    一般散列算法需要提供一个salt（盐）与原始内容生成摘要信息，这样做的目的是为了安全性，
    比如：111111的md5值是：96e79218965eb72c92a549dd5a330112，
    拿着“96e79218965eb72c92a549dd5a330112”去md5破解网站很容易进行破解，
    如果要是对111111和salt（盐，一个随机数）进行散列，这样虽然密码都是111111加不同的盐会生成不同的散列值。
- 实现步骤
    - 1、编写类继承`AuthorizingRealm`
    - 2、重写方法
        - `void setName(String name)` 设置Realm名称
        - `AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)` 认证方法
        - `AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)` 授权方法
    - 3、配置Realm
        - 需要在ini文件中注入自定义的Realm(参考 shiro-realm#custom-realm-authentication-md5工程)
## 2、shiro授权

> 工程shiro-authorization

### 2.1、授权流程

- 对subject进行授权，调用方法isPermitted（"permission串"）
- SecurityManager执行授权，通过ModularRealmAuthorizer执行授权
- ModularRealmAuthorizer执行realm（自定义的CustomRealm）从数据库查询权限数据，调用realm的授权方法：doGetAuthorizationInfo
- realm从数据库查询权限数据，返回ModularRealmAuthorizer
- ModularRealmAuthorizer调用PermissionResolver进行权限串比对
- 如果比对后，isPermitted中"permission串"在realm查询到权限数据中，说明用户访问permission串有权限，否则 没有权限，抛出异常

### 2.2、授权方式

Shrio支持三种方式授权

- **编程式**

  通过写if/else 授权代码块完成

  ```java
  if(subject.hasRole("admin")){
      System.out.println("有权限");
  }else{
      System.out.println("无权限");
  }
  ```

- **声明式**

  通过在执行的Java方法上放置相应的注解完成

  ```java
  @RequiresRoles("admin")
  public void hello(){
      System.out.println("有权限");
  }
  ```

- **JSP/GSP标签**

  在JSP/GSP 页面通过相应的标签完成

  ```jsp
  <shiro:hasRole name="admin">
  <!— 有权限—>
  </shiro:hasRole>
  ```

### 2.3、定义权限内容

```ini
# 用户
[users]
# 用户hanson密码root,此用户具有role1,role2两个角色
hanson=root,role1,role2
# 用户lisi密码lisi,此用户具有role2角色
lisi=lisi,role2
# 权限
[roles]
# 角色role1对资源user拥有create、update权限
role1=user:create,user:update
# 角色role2对资源user拥有create、delete权限
role2=user:create,user:delete
# 角色role3对资源user拥有create权限
role3=user:create
```

#### 2.3.1、权限操作标识符号格式

```shell
资源:操作:实例
```

举例：

```shell
user:create:01  # 表示对用户资源的01实例进行create操作
user:create     # 表示对用户资源进行create操作,相当于user:create:*
user:create:*   # 表示对所有用户资源实例进行create操作
user:*:01       # 表示对用户资源实例01进行所有操作
```

### 2.4、程序说明

```java
// 6、授权
// 6.1、基于角色的授权
boolean hasRole1 = subject.hasRole("role1");
 System.out.println("是否有具备角色[单个]:" + hasRole1);
boolean hasAllRoles = subject.hasAllRoles(Arrays.asList("role1", "role2"));
System.out.println("是否有具备角色[多个]:" + hasAllRoles);
// 6.2、基于资源的授权
boolean permitted = subject.isPermitted("user:create");
System.out.println("是否具备资源权限[单个]:" + permitted);
boolean permittedAll = subject.isPermittedAll("user:create:1","user:delete");
System.out.println("是否具备资源权限[多个]:" + permittedAll);
// 使用check方法进行授权，如果授权不通过会抛出异常
subject.checkPermission("person:create:1");
```

### 2.5、自定义Realm，进行授权

​	实际开发，应该从数据库中获取权限数据。就需要自定义Realm，由Realm从数据库中获取数据。

Realm根据用户身份查询权限数据，将权限数据返回给authorizer（授权器）。

​	参考shiro-realm#custom-realm-authorize工程





