# CoolQ_Java_Plugin
## 设计
1. 项目使用JDK8+SpringBoot+Mybatis，依赖PHP对接插件（cc.1sls.CtPe.cpk）做服务端，[插件论坛下载地址](https://cqp.cc/forum.php?mod=viewthread&tid=28532)
2. 消息处理类需要继承抽象类`ResolveMessageService`，并加入`@Repository`注解，消息处理类存放在`com.wjyup.coolq.service.impl.plugins`包下，可以在`data.properties`中设置路径（配置`plugin.package.path=com.xxx`即可），通过扫描该包下的所有类，并调用doit方法，进行消息处理，以前是通过反射进行加载，现改为Spring容器初始化完毕之后加载插件类
3. 需要MySQL数据库，详情看`application.properties`信息

## 感谢HSTB大神的大力支持
## 需先启用酷Q的插件，然后设置如下：

### 2.1.4版本：
1. 接口地址：`http://127.0.0.1:8080/coolq`，数据结构：`json`
2. 动态交互-监听端口：`1970`
3. 数据处理，删除key输入框的值
4. 关闭设置界面并刷新配置
5. 启动Java Web项目，可以先启动
6. `data.properties`是配置文件，请结合`ConfigCache`和注释一起查看

### 开发时需要安装的插件
1. `lombok`

## 欢迎加入QQ讨论群：553601318

## 链接
[插件地址](https://github.com/Hstb1230/CtPe)

[Doc帮助文档](https://www.kancloud.cn/zerolib/http-to-cq/389312)
