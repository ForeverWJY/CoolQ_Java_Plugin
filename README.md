# CoolQ_Java_Plugin
## 感谢Hstb1230大神的大力支持
## 说明
1. 项目使用JDK8+SpringBoot 2.0.1.RELEASE+Mybatis，依赖PHP对接插件（cc.1sls.CtPe.cpk）负责转发消息到Java端
2. 需要SpringBoot基础，[SpringBoot官方教程](https://spring.io/projects/spring-boot)
3. 自定义实现消息处理：参考Demo`com.wjyup.coolq.service.plugins.WeatherService`，使用的是发事件(Guava EventBus)的方式

## 上手使用：
### 1.启动MySQL服务，执行`${Java项目根目录}/sql/init.mysql.sql`，目的：创建`coolq`数据库和用户并分配权限
### 2.把`org.inlinc.inhttp.cpk`插件放入`${CoolQ文件根目录}/app`文件夹下，启动CoolQ并登录QQ，然后启用插件
### 3.HTTP API插件端配置：
- 配置文件所在路径：`${CoolQ文件根目录}/app/org.inlinc.inhttp/${QQ号}/config.ini`
- 以下是配置示例：
```ini
[use]
mod=1 #提交方法，0-socket，1-http
log=1 #使用插件自带日志，0-不使用，1-使用
format=0 #数据格式，0-json，1-key=value
window=1 #配置面板，0-初级配置面板，1-高级配置面板
autoRestart=enable #在插件奔溃时使用自动重启功能，0-不使用，1-使用

[socket] #提交数据-socket
ip= #服务端的ip
port= #服务端的监听端口
sendTime=0 #发送最长等待时间，0为无限等待
receiveTime=0 #接收最长等待时间，0为无限等待
sendCount=3 #发送最多尝试次数
receiveCount=3 #接收最多尝试次数

[http] #提交数据-http
url=http://127.0.0.1:8888/coolq #服务端地址
proxy= #代理地址，地址格式：127.0.0.1:8888
timeOut=30 #请求超时时间，单位：秒
headers= #请求协议头，如需换行请用 \r\n
cookies= #请求Cookies，如需换行请用 \r\n

[rule] #提交规则
headerIs= #提交规则-开头为，每个规则以base64编码保存，用"|"分隔，如果不会操作，请勿乱动
haveThis= #提交规则-内容包含，每个规则以base64编码保存，用"|"分隔，如果不会操作，请勿乱动
regularIs= #提交规则-符合正则表达式，每个规则以base64编码保存，用"|"分隔，如果不会操作，请勿乱动

[httpSocket] #动态交互
ipList=0.0.0.0, #允许连接的ip列表
port=1970 #监听端口
headers= #自定义头部，如需换行请用 \r\n
dataForm=0 #0
workMode=0 #0
enableEventManage=true #真
eventManage=enable

[timeTask] #定时任务
frequency= #请求频率，支持s/min/h格式时间，设置此项表示使用定时任务功能
timeOut=30 #请求超时时间，单位：秒
proxy= #代理地址，地址格式：127.0.0.1:8888
url= #请求地址，如果使用，不能为空
headers= #请求协议头，如需换行请用 \r\n
cookies= #请求Cookies，如需换行请用 \r\n

[data] #数据处理
timeOut=30 #数据有效期
key=123 #校验所需key，设置此项表示使用校验数据
[log]
enable=true #真
writeFile=true #真
useSelf=true
[update]
dev=false #假
```

### 4.Java Web端配置(`data.properties`)：
```properties
# 选择推送消息的方式：websocket http
msg.send.type=http
# 使用Websocket方式推送消息，访问插件的Websocket的Host和Port
ws.host=127.0.0.1
ws.port=1970
# 使用HTTP方式推送消息时，访问插件的HTTP地址
http.host=127.0.0.1
http.port=1970
# 插件端设置的值：扩展功能->数据设置->key，如果没值留空
key=123
# 处理消息中包含CQ码的消息吗？
msg.cq=false
# coolq图片存放路径
coolq.image.path=C:\\Downloads\\\u9177Q\\data\\image
# 管理员QQ，英文逗号分隔
manager.qq=1066231345
```
### 5.启动Java项目，给机器人发消息看IDE的日志（日志默认是DEBUG级别）

## 开发时IDE需要安装的插件
- [lombok](https://projectlombok.org/)

## 有问题？欢迎加入QQ讨论群：`553601318`

## 链接
- [插件论坛下载地址](https://cqp.cc/forum.php?mod=viewthread&tid=28532)
- [插件Github地址](https://github.com/Hstb1230/http-to-cq)
- [插件帮助文档](https://github.com/Hstb1230/http-to-cq/wiki)
