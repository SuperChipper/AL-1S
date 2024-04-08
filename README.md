# AL-1S
![1700065944630](./al1s/表情7.png)
## 概要

AL-1S 是基于mirai开发的项目，依赖于 [https://github.com/itbaima-study/itbaima-robot-starter](https://github.com/itbaima-study/itbaima-robot-starter) 开发的基于Spring Boot服务的QQ机器人

同时使用清华大学开源的[ChatGLM3](https://github.com/SuperChipper/ChatGLM3)实现了实时对话功能

[Mirai 项目地址](https://github.com/mamoe/mirai)

## 食用指南

### 设备需求

如果想完整加载体验ChatGLM的对话功能，需要16G以上的显存来保证正常运行。（当然也可以自行修改源文件中ChatGPT.java的请求格式和地址来直接使用ChatGPT）

### 安装
在`src\main\resources`目录下application.yaml中添加
```yaml
server:
  port: 8080
itbaima:
  robot:
     username: 
     login-type: password
     password: 
     protocol: android_pad
     signer:
       version: 8.9.58               #使用的客户端版本
       url: http://127.0.0.1:8888    #刚刚部署的签名服务器地址
       type: kiliokuara                #使用的签名服务器类型
       authorization-key: kfc        #验证秘钥，默认kfc
       server-identity-key: vivo50   #服务器身份秘钥，默认vivo50
     data:
       work-dir: robot-data
       cache-dir: cache
       contact-cache: false
       save-device-id: true
```
其中，需要填入你部署的账号的QQ用户账号和密码
同时QQ的安卓平板登录需要使用认证服务器

具体部署教程:
https://github.com/itbaima-study/itbaima-robot-starter/wiki



## 更新日志

08/04/2024:增加了对蔚蓝档案国服b站的资讯动态爬取

## License
本项目同样基于GPL3.0开源


