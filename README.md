## file-manage
文件管理平台(File Management Platform)

#项目简介：这是一个简单的文件管理系统的后台接口项目

主要包括两大模块：文件管理接口、用户管理接口

文件管理接口：文件分片上传、断点下载、文件删除、以及文件基本信息的增删改查等功能

用户管理接口：登录、注册、分页查询用户信息、查询指定用户信息、忘记密码、重置密码、删除用户、修改用户等基本功能

##使用说明：

#1.克隆本项目

本项目地址为：https://github.com/HaoQiangJiang/file-manage.git ,如果对你有用，欢迎给个star

#2.初始化数据
（本系统使用mysql数据库）

在mysql中创建数据库 file

手动导入sec/main/resource/目录下的file.sql文件

#3.配置项目
你已经下载项目，并且初始化好了数据库，那么接下来只需要更改相应的配置就可以运行该项目了

更改src/resources/application.yml配置：

 开发环境配置，根据实际情况调整数据库连接信息
 
spring:

  datasource:
  
    driver-class-name: com.mysql.jdbc.Driver
    
    url: jdbc:mysql://127.0.0.1:3306/file?serverTimezone=UTC
    
    username: root
    
    password: root
 
 #4.启动项目
#启动

右键直接运行com.hs.HsEduApplication 类即可已启动项目

启动成功后访问：http://localhost:9001/swagger-ui.html#

#5.测试账号

userame:admin

passwd:admin

#6.文件默认上传地址：

本地D:\data目录下，默认下载目录也是这个目录。
