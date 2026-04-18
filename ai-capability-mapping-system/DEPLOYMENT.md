# AI 智能匹配与能力图谱系统部署指南

## 1. 部署架构

系统采用微服务架构，包含以下组件：

- **前端**：Vue 3 + Element Plus + AntV G6
- **后端**：Spring Boot 微服务
  - Gateway 服务
  - 文档解析服务
  - 图谱服务
  - 匹配服务
  - 后台管理服务
- **数据库**：
  - MySQL / 达梦 DM8
  - Neo4j
  - Milvus
  - Redis
- **中间件**：
  - MinIO（文件存储）
  - RabbitMQ（消息队列）
- **AI 服务**：
  - Ollama（LLM 推理）
  - PaddleOCR（OCR 服务）

## 2. 环境要求

### 2.1 硬件要求
- **最低配置**：8核 CPU，32GB 内存，500GB 磁盘
- **推荐配置**：16核 CPU，64GB 内存，1TB 磁盘

### 2.2 软件要求
- **操作系统**：银河麒麟高级服务器操作系统 V10/V11
- **JDK**：华为毕升 JDK 17
- **数据库**：MySQL 8.0 / 达梦 DM8
- **容器**：Docker 20.10+，Docker Compose 1.29+

## 3. 部署方式

### 3.1 Docker Compose 部署（推荐）

#### 步骤 1：准备环境
1. 安装 Docker 和 Docker Compose
2. 克隆代码仓库
3. 进入项目目录

#### 步骤 2：配置环境变量
编辑 `.env` 文件，设置必要的环境变量：

```env
# MySQL 配置
MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE=ai_capability_mapping

# Neo4j 配置
NEO4J_AUTH=neo4j/neo4j123

# MinIO 配置
MINIO_ROOT_USER=minioadmin
MINIO_ROOT_PASSWORD=minioadmin

# 系统配置
SYSTEM_ENV=production
```

#### 步骤 3：启动服务

```bash
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps
```

#### 步骤 4：初始化数据库

```bash
# 执行数据库初始化脚本
docker exec -i mysql mysql -uroot -proot < backend/sql/init.sql
```

#### 步骤 5：访问系统
- 前端：http://localhost:3000
- 后端 API：http://localhost:8080
- 管理后台：http://localhost:3000/admin

### 3.2 手动部署

#### 步骤 1：安装依赖

1. 安装 JDK 17
2. 安装 MySQL / 达梦 DM8
3. 安装 Neo4j
4. 安装 Milvus
5. 安装 Redis
6. 安装 MinIO
7. 安装 RabbitMQ
8. 安装 Ollama

#### 步骤 2：配置服务

1. 配置数据库连接
2. 配置中间件连接
3. 配置 AI 服务连接

#### 步骤 3：编译打包

```bash
# 编译后端服务
cd backend
mvn clean package

# 编译前端服务
cd ../frontend
npm install
npm run build
```

#### 步骤 4：部署服务

1. 部署后端服务
2. 部署前端静态资源
3. 启动所有服务

## 4. 服务管理

### 4.1 启动服务

```bash
# 启动 Docker 容器
docker-compose up -d

# 启动单个服务
docker-compose up -d <service-name>
```

### 4.2 停止服务

```bash
# 停止所有服务
docker-compose down

# 停止单个服务
docker-compose stop <service-name>
```

### 4.3 查看日志

```bash
# 查看所有服务日志
docker-compose logs

# 查看单个服务日志
docker-compose logs <service-name>
```

## 5. 配置管理

### 5.1 环境变量

主要环境变量配置：

- `MYSQL_ROOT_PASSWORD`：MySQL 根密码
- `NEO4J_AUTH`：Neo4j 认证信息
- `MINIO_ROOT_USER`：MinIO 用户名
- `MINIO_ROOT_PASSWORD`：MinIO 密码
- `OLLAMA_BASE_URL`：Ollama 服务地址

### 5.2 配置文件

主要配置文件：

- `backend/*/src/main/resources/application.yml`：服务配置
- `backend/admin-service/src/main/resources/application-kylin.yml`：银河麒麟适配配置

## 6. 监控与维护

### 6.1 系统监控

- **服务状态**：访问 http://localhost:8080/api/v1/admin/monitor
- **数据库监控**：使用各数据库自带监控工具
- **容器监控**：使用 Docker 自带监控工具

### 6.2 常见问题

1. **服务启动失败**：检查容器日志，确认依赖服务是否正常
2. **数据库连接失败**：检查数据库配置和网络连接
3. **AI 服务不可用**：检查 Ollama 和 PaddleOCR 服务状态
4. **前端访问异常**：检查 Nginx 配置和前端构建文件

## 7. 离线部署

### 7.1 准备离线包

1. 导出 Docker 镜像
2. 下载所需模型文件
3. 准备离线依赖包

### 7.2 离线部署步骤

1. 导入 Docker 镜像
2. 部署数据库和中间件
3. 部署 AI 模型
4. 部署应用服务
5. 启动所有服务

## 8. 安全配置

### 8.1 网络安全

- 配置防火墙规则
- 限制外部访问
- 使用 HTTPS

### 8.2 数据安全

- 启用国密算法
- 配置数据加密
- 定期备份数据

### 8.3 权限管理

- 配置 RBAC 权限
- 定期更新密码
- 审计操作日志

## 9. 性能优化

### 9.1 数据库优化

- 配置索引
- 优化查询
- 定期清理数据

### 9.2 服务优化

- 配置 JVM 参数
- 启用缓存
- 负载均衡

### 9.3 AI 服务优化

- 配置模型参数
- 使用 GPU 加速
- 启用模型量化

## 10. 故障恢复

### 10.1 备份策略

- 定期备份数据库
- 备份配置文件
- 备份重要数据

### 10.2 恢复步骤

1. 停止所有服务
2. 恢复备份数据
3. 重启服务
4. 验证系统状态

## 11. 版本升级

### 11.1 升级步骤

1. 备份现有数据
2. 停止旧版本服务
3. 部署新版本服务
4. 执行数据库迁移
5. 启动新版本服务
6. 验证系统功能

### 11.2 回滚策略

1. 停止新版本服务
2. 恢复旧版本服务
3. 恢复备份数据
4. 启动旧版本服务

## 12. 联系方式

- **技术支持**：support@trae.com
- **文档地址**：https://docs.trae.com/ai-capability-mapping
- **GitHub**：https://github.com/trae-ai/ai-capability-mapping-system
