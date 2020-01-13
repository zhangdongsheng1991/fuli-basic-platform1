赋力基础能力平台2.0
===================

### 一、新特性：

 *   `网关gateway加入动态路由，更便捷地管理服务路由；`
 *   `网关gateway优化与oauth2.0的集成；`
 *   `不同端的登陆授权分离，提供完整的oauth2.0授权模式；`
 *   `集成springboot admin 服务监控，可实时查看服务器的运行状态；`
 *  ` 集成alibaba-sentinel；`
 *  ` 集成ELK日志追踪。提供日志追踪组件，方便服务集成；  `     
 *   `新加服务api采集功能，实现api一键采集 ；`   
 *   `优化jenkins+docker自动化部署；`


### 二、架构：

#### 1、总体架构
<img src="/doc/images/framework2.png" width="75%"  align="middle" />

#### 2、授权流程
<img src="/doc/images/oauth2.png" width="40%"  align="middle" />

#### 3、elk日志追踪
参考： http://172.16.0.251/open/elk-cloud.git


快速开始
========

### 一、目录结构

            fuli-basic-platform （root）
            │
            ├─api-gateway (api网关)
            │
            ├─auth-center (认证中心)
            │  │
            │  ├─base-auth  （基础认证服务）
            │  │
            │  └─open-auth (开放平台认证服务)
            │      
            ├─doc （doc文档）
            │  │
            │  └─images （存放一些图片）
            │
            ├─log-center  （日志中心）
            │  │
            │  ├─logs-trace-core （日志追踪核心组件）
            │     
            ├─monitor-center   （监控中心）
            │  │
            │  └─sba-server  （springboot admin 监控服务）
            │      
         
### 二、环境准备

#### 1、sentinel服务端安装
```
1、docker run --name sentinel --restart always  -d -p 8858:8858  bladex/sentinel-dashboard
```
```
2、访问http://172.16.4.6:8858/#/login  默认用户名：sentinel 密码：sentinel，访问效果如下：
```
<img src="/doc/images/sentinel.png" width="50%"  align="middle" />

#### 2、ELK安装
```
参考： http://172.16.0.251/open/elk-cloud.git
```

### 三、服务集成

#### 1、服务集成alibaba-sentinel
sentinel控制台dashboard开发环境地址：http://172.16.4.6:8857 用户：sentinel 密码：sentinel

1)pom.xml引入spring-cloud-starter-alibaba-sentinel依赖
```maven
   <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        <version>0.9.0.RELEASE</version>
    </dependency>
```
2)nacos的配置文件加上以下配置
```yml
spring:
  cloud:
    sentinel:
      transport:
        port: 9004  # sentinel客户端端口
        dashboard: 172.16.4.6:8857 # sentinel控制台地址

# 开启sentinel feign 支持
feign:
  sentinel:
    enabled: true
# 开启sentinel resttemplate 支持
resttemplate:
  sentinel:
    enabled: true

management:
  endpoints:
    web:
      exposure:
        include: '*'
  endpoint:
    health:
      show-details: ALWAYS
```
3)sentinel的功能使用请参考官方文档：https://github.com/alibaba/Sentinel
#### 2、网关spring gateway集成alibaba-sentinel
1)pom.xml引入以下依赖
```maven
    <!-- 阿里sentinel 代替Hystrix-->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        <version>2.1.0.RELEASE</version>
    </dependency>
    <!-- 阿里巴巴  sentinel 集成gateway-->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-alibaba-sentinel-gateway</artifactId>
        <version>2.1.0.RELEASE</version>
    </dependency>

```

2)nacos的配置文件加上以下配置
```yml
spring:
  cloud:
    sentinel:
      transport:
        port: 9004  # sentinel客户端端口
        dashboard: 172.16.4.6:8857 # sentinel控制台地址

# 开启sentinel feign 支持
feign:
  sentinel:
    enabled: true
# 开启sentinel resttemplate 支持
resttemplate:
  sentinel:
    enabled: true
management:
  endpoints:
    web:
      exposure:
        include: '*'
  endpoint:
    health:
      show-details: ALWAYS
```
3)sentinel的功能使用请参考官方文档：https://github.com/alibaba/Sentinel

#### 3、集成springboot admin

1)集成请参考sba-server

2)使用

a、启动sba-server服务

b、访问http://localhost:8527 用户：admin 密码：admin。 访问效果如下

<img src="/doc/images/sba.png" width="50%"  align="middle" />

#### 4、集成elk日志追踪

参考： http://172.16.0.251/open/elk-cloud.git

### 四、使用网关动态路由
#### 1、路由的增删改，参考swagger文档 http://localhost:9527/doc.html 如下：
<img src="/doc/images/editroute.png" width="70%"  align="middle" />

#### `2、对路由进行操作后需刷新网关，刷新端点为： http://172.16.4.4/api/route/refresh`

### 五、使用服务api采集

#### 1、服务中加入 ResourceController.java 和 GatherApi.java  参考springcloud-nacos-example示例项目

#### 2、base-auth里提供服务采集的接口：http://172.16.4.4/api/gather/api?serviceId=cloud-demo，被采集到的接口初始为可以访问


