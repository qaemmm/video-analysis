# 视频解析网站

这是一个基于 Spring Boot 和 Thymeleaf 开发的视频解析网站，支持多平台视频解析，并具有用户积分系统。

## 功能特点

1. 用户系统
   - 用户注册和登录
   - 用户积分管理
   - 每日登录奖励
   - 安全的会话管理

2. 视频解析
   - 支持多平台视频解析
   - 基于 Coze API 的智能解析
   - 积分消耗机制
   - 游客访问控制

3. 界面设计
   - 响应式布局
   - 用户友好的导航
   - 积分显示
   - 清晰的操作提示

## 技术栈

- 后端：Spring Boot 3.x
- 前端：Thymeleaf + Bootstrap 5
- 数据库：MySQL
- 安全框架：Spring Security
- API 集成：Coze API

## 项目结构

```
video-analysis/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/videoanalysis/
│   │   │       ├── config/        # 配置类
│   │   │       ├── controller/    # 控制器
│   │   │       ├── model/         # 数据模型
│   │   │       ├── repository/    # 数据访问层
│   │   │       ├── service/       # 业务逻辑层
│   │   │       └── util/          # 工具类
│   │   └── resources/
│   │       ├── static/           # 静态资源
│   │       ├── templates/        # 页面模板
│   │       └── application.yml   # 配置文件
│   └── test/                     # 测试代码
├── pom.xml                       # Maven 配置
└── README.md                     # 项目说明
```

## 使用说明

1. 环境要求
   - JDK 17+
   - Maven 3.6+
   - MySQL 8.0+

2. 配置说明
   - 在 `application.yml` 中配置数据库连接信息
   - 配置 Coze API 密钥

3. 运行项目
   ```bash
   mvn spring-boot:run
   ```

4. 访问地址
   - 本地访问：http://localhost:8080

## 积分规则

- 新用户注册：赠送 10 积分
- 每日首次登录：奖励 10 积分
- 视频解析：消耗 1 积分/次
- 积分不足时无法解析视频

## 注意事项

- 请确保有足够的积分进行视频解析
- 每日登录奖励仅限首次登录
- 游客可以访问首页，但需要登录才能使用解析功能 