# CoolQ_Java_Plugin
## 项目使用Spring+Spring MVC+Mybatis架构，依赖于PHP对接插件（cc.1sls.CtPe.cpk）[论坛下载地址](https://cqp.cc/forum.php?mod=viewthread&tid=28532)
## 感谢HSTB大神的支持，使得Java实现websocket方式推送消息，接收使用POST JSON方式，[插件的开发文档地址](https://d.1sls.cn/CtPePro)
## 需先启用PHP对接插件，然后设置如下：
1. 接口地址：127.0.0.1:8080/coolq/coolq
2. 请求方式：POST，数据格式：JSON
3. 勾选：开启双向交互模式&监听端口：1970
4. 点击“保存并重新加载进程”
5. 启动Java Web项目，可以先启动
