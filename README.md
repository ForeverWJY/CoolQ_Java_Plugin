# CoolQ_Java_Plugin
## 项目使用Spring+Spring MVC+Mybatis架构，依赖于PHP对接插件（cc.1sls.CtPe.cpk）[论坛下载地址](https://cqp.cc/forum.php?mod=viewthread&tid=28532)
## 感谢HSTB大神的支持，使得Java实现websocket方式推送消息，接收使用POST JSON方式，[插件的开发文档地址](https://d.1sls.cn/CtPePro)
## 需先启用PHP对接插件，然后设置如下：

### Websocket设置(1.4.2版本已改为Socket+HTTP方式)：
1. 接口地址：`127.0.0.1:8080/coolq/coolq`
2. 请求方式：`POST`，数据格式：`JSON`
3. 勾选：开启双向交互模式&监听端口：`1970`
4. 点击“保存并重新加载进程”
5. 启动Java Web项目，可以先启动

### Socket + HTTP 推送设置：
1. 提交方式选择：Socket，数据格式：`Json`
2. 然后设置IP：`127.0.0.1`，端口：`8888`，启动一下java web coolq服务，点击`测试连通性`
3. 扩展选项卡里面的动态交互，勾选`开启动态交互`，设置监听端口：`1970`
4. 校验数据完整性可以选择是否设置，如果勾选，请设置`Key`和`Secret`
5. 根据设置在`data.properties`文件中设置对应项

## 欢迎加入QQ讨论群：553601318

## 链接
[CtPe插件地址](https://github.com/Hstb1230/CtPe)
