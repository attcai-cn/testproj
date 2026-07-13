# 练习-前后端分离后台管理系统 (AI整理)

本项目是一个基于 **Spring Boot + Vue 3** 的前后端分离后台管理系统，支持**管理员**与**普通用户**双角色登录，具备用户 CRUD、分页查询、Excel 导入导出、文件上传下载等基础功能。

---

## 技术栈

### 后端 (`springbt/`)

| 技术 | 版本 |
|------|------|
| Spring Boot | 3.4.3 |
| Java | 23 |
| MyBatis | 3.0.3 |
| MySQL Connector | - |
| PageHelper | 1.4.7 |
| Hutool | 5.8.24 |
| Apache POI | 5.3.0 |
| Java JWT | 4.4.0 |

### 前端 (`newvue/newvueproj/`)

| 技术 | 版本 |
|------|------|
| Vue | 3.5.13 |
| Vue Router | 4.5.0 |
| Element Plus | 2.9.5 |
| Vite | 6.1.0 |
| Axios | 1.8.1 |
| Sass | 1.85.1 |

---

## 项目结构

```
├── springbt/                       # Spring Boot 后端
│   ├── src/main/java/com/example/springbt/
│   │   ├── SpringbtApplication.java
│   │   ├── common/                 # 公共组件
│   │   │   ├── Result.java         # 统一响应封装
│   │   │   ├── JWTInterceptor.java # JWT 拦截器
│   │   │   ├── WebConfig.java      # Web 配置（拦截器注册）
│   │   │   └── CorsConfig.java     # 跨域配置
│   │   ├── controller/             # 控制器层
│   │   │   ├── WebController.java  # 登录/注册/公共接口
│   │   │   ├── AdminController.java# 管理员 CRUD、导入导出
│   │   │   ├── UserController.java # 用户 CRUD、导入导出
│   │   │   └── FileController.java # 文件上传/下载
│   │   ├── services/               # 业务层
│   │   ├── mapper/                 # MyBatis Mapper 接口
│   │   ├── entity/                 # 实体类
│   │   │   ├── Account.java        # 基础账户实体（含 token 等非 DB 字段）
│   │   │   ├── Admin.java          # 管理员
│   │   │   └── User.java           # 普通用户
│   │   ├── utils/
│   │   │   └── tokenUtils.java     # JWT Token 生成与解析
│   │   └── exception/              # 全局异常处理
│   └── src/main/resources/
│       ├── application.yaml        # 应用配置（MySQL、MyBatis）
│       └── mapper/                 # XML 映射文件
│           ├── AdminMapper.xml
│           └── UserMapper.xml
├── newvue/                         # 前端目录
│   └── newvueproj/                 # Vue 3 项目
│       ├── src/
│       │   ├── main.js             # 入口
│       │   ├── App.vue
│       │   ├── router/router.js    # 路由配置
│       │   ├── utils/request.js    # Axios 封装（含 token 拦截）
│       │   ├── views/              # 页面
│       │   │   ├── Login.vue       # 登录
│       │   │   ├── Register.vue    # 注册
│       │   │   ├── manager.vue     # 管理员后台布局
│       │   │   ├── Admin.vue       # 管理员信息管理
│       │   │   ├── User.vue        # 用户信息管理
│       │   │   ├── Welcome.vue     # 欢迎页
│       │   │   ├── About.vue       # 关于页
│       │   │   ├── 404.vue         # 404 页面
│       │   │   └── User/           # 普通用户端页面
│       │   │       ├── Guest.vue   # 用户布局
│       │   │       ├── Welcome.vue
│       │   │       ├── About.vue
│       │   │       └── MainPage.vue
│       │   └── assets/             # 静态资源
│       ├── vite.config.js
│       └── index.html
├── files/                          # 文件上传存储目录
└── README.md                       # 本文件
```

---

## 数据库

- **数据库类型**：MySQL
- **数据库名**：`code2025`
- **表结构**：
  - `admin` — 管理员表
  - `user` — 用户表
  - 公共字段：`id`, `username`, `password`, `name`, `phone`, `email`

> 当前项目中未包含 SQL 初始化脚本，需手动建表。

---

## 已实现功能

### 认证与授权
- [x] 登录（支持管理员 / 用户角色选择）
- [x] 注册（仅普通用户注册）
- [x] JWT Token 认证与鉴权
- [x] 登录状态拦截（未登录跳转到登录页）

### 管理员后台
- [x] 管理员信息管理（增删改查、分页、模糊搜索）
- [x] 用户信息管理（增删改查、分页、模糊搜索）
- [x] 批量删除
- [x] Excel 批量导入 / 导出
- [x] 左侧菜单导航 + 面包屑

### 普通用户端
- [x] 欢迎页
- [x] 关于页

### 通用功能
- [x] 文件上传 / 下载
- [x] 全局异常捕获
- [x] 跨域处理（CORS）

---

## 启动方式

### 后端
```bash
cd springbt
# 确保本地 MySQL 已启动，且数据库 code2025 存在
# 修改 application.yaml 中的数据库密码等配置
mvn spring-boot:run
```
后端默认运行在 `http://localhost:8080`

### 前端
```bash
cd newvue/newvueproj
npm install
npm run dev
```
前端默认运行在 Vite 本地开发服务器（如 `http://localhost:5173`）

---

## 接口说明

| 接口 | 方法 | 说明 |
|------|------|------|
| `/login` | POST | 登录（角色：ADMIN / USER） |
| `/register` | POST | 用户注册 |
| `/user/selectPage` | GET | 用户分页查询 |
| `/user/add` | POST | 新增用户 |
| `/user/update` | PUT | 修改用户 |
| `/user/delete/{id}` | DELETE | 删除用户 |
| `/user/deleteBatch` | DELETE | 批量删除用户 |
| `/user/export` | GET | 导出用户 Excel |
| `/user/import` | POST | 导入用户 Excel |
| `/admin/*` | - | 管理员相关接口（同 user） |
| `/files/upload` | POST | 文件上传 |
| `/files/download/{fileName}` | GET | 文件下载 |

---

## 当前开发状态

这是一个开发到一半的项目，核心 CRUD 和权限框架已搭建完成，但仍有以下待完善空间：

- 前端部分页面（如个人信息、修改密码）尚未实现具体功能
- 数据库缺少初始化 SQL 脚本
- 部分代码存在重复导入、注释代码未清理等细节问题
- 用户端功能相对简单，仅有欢迎页和关于页
