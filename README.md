# 个人待办提醒工具 — 后端服务

基于 Spring Boot 3.4.4 的 RESTful API 后端，采用 JPA + SQLite，为前端 Vue 应用提供数据接口。

---

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.4.4 | Web 框架 |
| Spring Data JPA | 内置 | ORM 持久化 |
| SQLite | 3.45.1.0 | 关系型数据库 |
| JWT (jjwt) | 0.12.5 | 用户认证 |
| Spring Security | 内置 | 安全框架 |
| Jakarta Validation | 内置 | 参数校验 |
| JUnit 5 | 内置 | 单元测试 |

---

## 项目结构

```
src/
├── main/
│   ├── java/com/example/todobackend/
│   │   ├── TodoBackendApplication.java      # 启动类，启用调度
│   │   ├── entity/                          # 实体层（JPA Entity）
│   │   │   ├── User.java                   # 用户
│   │   │   ├── Todo.java                   # 待办
│   │   │   ├── Category.java               # 分类
│   │   │   └── ReminderLog.java            # 提醒日志
│   │   ├── repository/                      # 数据访问层
│   │   │   ├── UserRepository.java
│   │   │   ├── TodoRepository.java
│   │   │   ├── CategoryRepository.java
│   │   │   └── ReminderLogRepository.java
│   │   ├── service/                          # 业务逻辑层
│   │   │   ├── AuthService.java
│   │   │   ├── TodoService.java
│   │   │   ├── CategoryService.java
│   │   │   └── ReminderService.java
│   │   ├── controller/                       # REST 接口层
│   │   │   ├── AuthController.java
│   │   │   ├── TodoController.java
│   │   │   ├── CategoryController.java
│   │   │   └── ReminderController.java
│   │   ├── dto/                              # 数据传输对象
│   │   │   ├── RegisterRequest.java
│   │   │   ├── LoginRequest.java
│   │   │   ├── LoginResponse.java
│   │   │   ├── TodoRequest.java
│   │   │   └── CategoryRequest.java
│   │   ├── scheduler/                        # 定时任务
│   │   │   └── ReminderScheduler.java
│   │   ├── config/                           # 配置类
│   │   │   ├── SecurityConfig.java          # Spring Security 配置
│   │   │   ├── JwtUtil.java                # JWT 工具（生成/验证 Token）
│   │   │   ├── JwtAuthenticationFilter.java # 认证过滤器
│   │   │   └── CorsConfig.java             # 跨域配置
│   │   └── common/                           # 公共组件
│   │       ├── Result.java                   # 统一响应封装
│   │       ├── BusinessException.java       # 业务异常
│   │       └── GlobalExceptionHandler.java  # 全局异常处理
│   └── resources/
│       └── application.yaml                  # 应用配置
└── test/java/                                # 单元测试
```

---

## 快速开始

### 前置要求

- JDK 17+
- Maven 3.8+

### 1. 进入目录

```bash
cd 第4次上机-编码实现/todoBackend
```

### 2. 编译打包

```bash
mvn clean package -DskipTests
```

### 3. 启动服务

```bash
mvn spring-boot:run
```

服务启动后访问 `http://localhost:8080`。

> 数据库文件自动生成于 `./data/todo.db`，首次启动时 JPA 会根据 Entity 自动建表。

### 4. 运行测试

```bash
mvn test
```

---

## 配置说明

配置文件位于 `src/main/resources/application.yaml`，关键配置项如下：

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `spring.datasource.url` | `jdbc:sqlite:./data/todo.db` | SQLite 数据库路径 |
| `server.port` | `8080` | 服务端口 |
| `jwt.secret` | （见配置文件） | JWT 签名密钥，需满足 HMAC-SHA 最小长度 |
| `jwt.expiration` | `86400000` | Token 有效期，单位毫秒（默认 24 小时） |
| `spring.jpa.hibernate.ddl-auto` | `update` | 启动时自动同步 Entity 到数据库 |

如需修改端口：

```yaml
server:
  port: 9090
```

---

## 数据库

### 建表说明

JPA 启动时根据 Entity 自动建表，共 5 张表：

| 表名 | 说明 |
|------|------|
| `user` | 用户表 |
| `todo` | 待办事项表 |
| `category` | 分类表 |
| `reminder_log` | 提醒日志表 |
| `todo_category` | 待办-分类关联表（多对多） |

### 核心字段取值

**todo.status**：`TODO` | `IN_PROGRESS` | `DONE` | `EXPIRED` | `CANCELLED`

**todo.priority**：`HIGH` | `MEDIUM` | `LOW`

**todo.repeat_rule**：`DAILY` | `WEEKLY` | `MONTHLY` | `NULL`

**reminder_log.result**：`SUCCESS` | `FAILED`

---

## 接口文档

所有接口基础路径：`http://localhost:8080/api`

### 统一响应格式

```json
// 成功
{ "code": 200, "message": "success", "data": {...} }

// 失败
{ "code": 400, "message": "错误描述", "data": null }
```

---

### 1. 用户认证

#### 注册用户

```
POST /api/auth/register
Content-Type: application/json
```

**请求体**：

```json
{
  "username": "alice",
  "password": "password123",
  "email": "alice@example.com"
}
```

| 字段 | 类型 | 约束 |
|------|------|------|
| username | String | 3-50字符，非空，唯一 |
| password | String | 至少6字符，非空 |
| email | String | 有效邮箱格式，非空 |

**成功响应**：`code=200, data=null`

**错误响应**：`code=400` — 用户名已存在 / 邮箱已被注册

---

#### 用户登录

```
POST /api/auth/login
Content-Type: application/json
```

**请求体**：

```json
{
  "username": "alice",
  "password": "password123"
}
```

**成功响应**：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzM4NCJ9...",
    "user": {
      "id": 1,
      "username": "alice",
      "email": "alice@example.com"
    }
  }
}
```

**错误响应**：`code=401` — 用户名或密码错误

> 后续请求在 Header 中携带：`Authorization: Bearer <token>`

---

### 2. 待办管理

所有待办接口需携带 JWT Token（登录后获取）。

#### 查询待办列表（分页）

```
GET /api/todos?status=&categoryId=&keyword=&page=0&size=10
Authorization: Bearer <token>
```

| 参数 | 类型 | 说明 |
|------|------|------|
| status | String | 筛选状态，可选 |
| categoryId | Integer | 按分类筛选，可选 |
| keyword | String | 按标题关键词模糊搜索，可选 |
| page | Integer | 页码（从0开始），默认0 |
| size | Integer | 每页条数，默认10 |

**成功响应**：

```json
{
  "code": 200,
  "data": {
    "content": [
      {
        "id": 1,
        "title": "完成报告",
        "description": "撰写项目总结",
        "status": "TODO",
        "priority": "HIGH",
        "dueDate": "2026-04-25T18:00:00",
        "remindAt": "2026-04-25T17:00:00",
        "isReminded": 0,
        "repeatRule": null,
        "categories": [{ "id": 1, "name": "工作", "color": "#1890FF" }],
        "createdAt": "2026-04-19T08:00:00",
        "updatedAt": "2026-04-19T08:00:00"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "size": 10,
    "number": 0
  }
}
```

---

#### 创建待办

```
POST /api/todos
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体**：

```json
{
  "title": "完成报告",
  "description": "撰写项目总结",
  "priority": "HIGH",
  "dueDate": "2026-04-25T18:00:00",
  "remindAt": "2026-04-25T17:00:00",
  "repeatRule": null,
  "categoryIds": [1]
}
```

| 字段 | 类型 | 约束 |
|------|------|------|
| title | String | 必填，最多200字符 |
| description | String | 可选 |
| priority | String | 可选，HIGH/MEDIUM/LOW，默认 MEDIUM |
| dueDate | LocalDateTime | 可选，截止时间 |
| remindAt | LocalDateTime | 可选，设定提醒时间 |
| repeatRule | String | 可选，DAILY/WEEKLY/MONTHLY |
| categoryIds | Set\<Integer\> | 可选，关联分类 ID 列表 |

---

#### 更新待办

```
PUT /api/todos/{id}
Authorization: Bearer <token>
Content-Type: application/json
```

请求体同创建接口，所有字段均可选（只更新非空字段）。

**错误响应**：`code=404` — 待办不存在 / `code=403` — 无权访问

---

#### 删除待办

```
DELETE /api/todos/{id}
Authorization: Bearer <token>
```

**成功响应**：`code=200, data=null`

---

#### 标记待办完成

```
PUT /api/todos/{id}/done
Authorization: Bearer <token>
```

将指定待办的 `status` 更新为 `DONE`。

**成功响应**：`code=200`，返回更新后的 Todo 对象。

---

### 3. 分类管理

#### 查询分类列表

```
GET /api/categories
Authorization: Bearer <token>
```

**成功响应**：

```json
{
  "code": 200,
  "data": [
    { "id": 1, "name": "工作", "color": "#1890FF", "userId": 1 }
  ]
}
```

---

#### 创建分类

```
POST /api/categories
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体**：

```json
{
  "name": "工作",
  "color": "#1890FF"
}
```

| 字段 | 类型 | 约束 |
|------|------|------|
| name | String | 必填，最多50字符 |
| color | String | 可选，默认 `#1890FF` |

---

#### 更新分类

```
PUT /api/categories/{id}
Authorization: Bearer <token>
Content-Type: application/json
```

请求体同创建接口。

---

#### 删除分类

```
DELETE /api/categories/{id}
Authorization: Bearer <token>
```

---

### 4. 提醒日志

#### 查询提醒日志

```
GET /api/reminders/logs?todoId=1
Authorization: Bearer <token>
```

**成功响应**：

```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "todoId": 1,
      "remindedAt": "2026-04-25T17:00:00",
      "method": "BROWSER",
      "result": "SUCCESS"
    }
  ]
}
```

---

## 认证机制

### 流程说明

```
客户端 POST /api/auth/login
         ↓
   AuthService 验证用户名密码
         ↓
   JwtUtil.generateToken() 生成 Token（包含 userId, username，有效期24h）
         ↓
   返回 { token, user }
         ↓
客户端后续请求携带 Header: Authorization: Bearer <token>
         ↓
   JwtAuthenticationFilter 拦截所有请求（白名单除外）
         ↓
   验证 Token 签名和有效期，有效则将 userId 注入 request.attribute
         ↓
   Controller 通过 uid(request) 获取当前用户 ID
```

### 白名单（无需认证）

- `POST /api/auth/register`
- `POST /api/auth/login`

### 认证失败响应

```json
{ "code": 401, "message": "未提供认证令牌", "data": null }
{ "code": 401, "message": "令牌无效或已过期", "data": null }
```

---

## 定时调度

`ReminderScheduler` 自动执行，无需手动触发：

| 任务 | 触发规则 | 说明 |
|------|----------|------|
| checkReminders | 每分钟 `0 * * * * ?` | 发送到期提醒并记录日志 |
| handleRepeat | 每小时第1分钟 `0 1 * * * ?` | 更新重复待办的下次提醒时间 |

---

## 异常处理

| 异常场景 | HTTP 状态码 | 响应 message |
|----------|-------------|-------------|
| 参数校验失败 | 400 | jakarta.validation 提示信息 |
| 业务逻辑错误 | 自定义（见接口文档） | BusinessException 消息 |
| 待办/分类不存在 | 404 | 提示信息 |
| 无权访问 | 403 | 提示信息 |
| 未认证 | 401 | 令牌相关提示 |
| 服务器内部错误 | 500 | "服务器内部错误：..." |
