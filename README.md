# Translateit-API
[TranslateIt](https://github.com/zunpiau/TranslateIt) 项目的后端部分，基于 Spring MVC

## 系统设计
- UID 生成
  - 简化的 snowflake 算法：去除区域编号和机器编号（单机部署）、6位序列号
  - 每秒最多产生 2^6*1000 即 64000 个 UID
  - 装配为 Spring Bean 保证单例
- Token 设计   
  - token = UserID + timestamp + hash(UserID + salt + timestamp)   
  - 由服务器产生并返回，客户端每次请求时带上 token。由于 hash 签名，token 无法伪造，不需要检查数据库。时间戳用于检查过期失效   
  - token 过期后可以获取新的 token 以维持登录状态
  - 使用已被用于刷新或不存在的 token 去获取新 token，所有 token 失效，客户端必须重新登录
- 密码存储
  - 用户密码使用 BCrypt 加密
  - token 使用加盐 SHA-256
   
参考链接：   
[如何安全的存储用户的密码](http://www.freebuf.com/articles/web/28527.html)   
[全局唯一ID设计](https://www.jianshu.com/p/61817cf48cc3)   
[细节：纠正你的网站注册时[验证邮箱]的流程](http://www.webppd.com/thread-55-1-2.html)   
[一种新的移动APP保持登陆的实现机制介绍](https://zhuanlan.zhihu.com/p/27440512)