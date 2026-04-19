# AI 智能匹配与能力图谱系统部署文档（Windows 版本）

## 1. 系统环境要求

### 1.1 硬件要求
- **CPU**: 4核及以上
- **内存**: 8GB 及以上
- **磁盘**: 256GB 及以上
- **网络**: 稳定的网络连接

### 1.2 软件要求
- **操作系统**: Windows 10/11 64位
- **JDK**: Oracle JDK 17 或 OpenJDK 17
- **数据库**: 达梦DM8 (Windows 版本) 或 MySQL 8.0
- **图数据库**: Neo4j Community 5.x (Windows 版本)
- **向量数据库**: Milvus 2.4.x (Windows 版本) 或使用Docker容器
- **AI 服务**: Ollama (Windows 版本) + Qwen2.5-3B
- **前端服务**: Nginx (Windows 版本) 或使用内置Tomcat
- **Git**: 用于代码获取
- **Maven**: 用于后端构建
- **Node.js**: 用于前端构建

## 2. 部署步骤

### 2.1 环境准备

1. **安装 JDK 17**
   - 下载 JDK 17 Windows 安装包
   - 运行安装程序并按照提示完成安装
   - 配置环境变量：
     - 右键点击"此电脑" → "属性" → "高级系统设置" → "环境变量"
     - 在"系统变量"中点击"新建"，添加 `JAVA_HOME` 变量，值为 JDK 安装路径（例如：`C:\Program Files\Java\jdk-17`）
     - 编辑 `Path` 变量，添加 `%JAVA_HOME%\bin`

2. **安装 Git**
   - 下载 Git for Windows 安装包
   - 运行安装程序并按照提示完成安装

3. **安装 Maven**
   - 下载 Maven 3.8+ Windows 版本
   - 解压到本地目录（例如：`C:\Program Files\Apache Maven`）
   - 配置环境变量：
     - 添加 `MAVEN_HOME` 变量，值为 Maven 安装路径
     - 编辑 `Path` 变量，添加 `%MAVEN_HOME%\bin`

4. **安装 Node.js**
   - 下载 Node.js 16+ Windows 安装包
   - 运行安装程序并按照提示完成安装

### 2.2 数据库部署

1. **安装达梦DM8**
   - 下载达梦DM8 Windows 版本安装包
   - 运行安装程序并按照提示完成安装
   - 创建数据库实例：
     - 打开"达梦数据库配置助手"
     - 选择"创建数据库实例"
     - 按照向导完成配置，数据库名称设置为 `AI_CAPABILITY_MAPPING`
   - 启动数据库服务：
     - 打开"服务"管理工具
     - 找到"达梦数据库服务"并启动
   - 登录数据库并创建用户：
     - 打开"达梦数据库管理工具"
     - 连接到本地数据库实例
     - 执行以下SQL语句：
       ```sql
       CREATE USER SYSDBA IDENTIFIED BY SYSDBA;
       GRANT DBA TO SYSDBA;
       ```

2. **安装 Neo4j**
   - 下载 Neo4j Community 5.x Windows 版本
   - 解压到本地目录（例如：`C:\Neo4j`）
   - 配置 Neo4j：
     - 编辑 `C:\Neo4j\conf\neo4j.conf` 文件
     - 设置以下参数：
       ```
       dbms.connector.bolt.listen_address=0.0.0.0:7687
       dbms.connector.http.listen_address=0.0.0.0:7474
       dbms.default_database=neo4j
       ```
   - 启动 Neo4j：
     - 打开命令提示符，进入 Neo4j 安装目录
     - 执行命令：`bin\neo4j start`

3. **安装 Milvus**
   - 下载 Milvus 2.4.x Windows 版本或使用 Docker 容器
   - 如果使用 Docker：
     - 安装 Docker Desktop for Windows
     - 运行命令：
       ```bash
       docker run -d --name milvus -p 19530:19530 -p 9091:9091 milvusdb/milvus:v2.4.0
       ```

### 2.3 AI 服务部署

1. **安装 Ollama**
   - 下载 Ollama Windows 版本安装包
   - 运行安装程序并按照提示完成安装
   - 启动 Ollama 服务：
     - 安装完成后，Ollama 服务会自动启动

2. **下载 Qwen2.5-3B 模型**
   - 打开命令提示符
   - 执行命令：
     ```bash
     ollama pull qwen2.5:3b-instruct
     ```

3. **部署 RapidOCR**
   - 打开命令提示符
   - 安装 Python 3.8+（如果未安装）
   - 执行命令：
     ```bash
     pip install rapidocr-onnxruntime
     
     # 启动 RapidOCR 服务
     rapidocr_web --host 0.0.0.0 --port 8000
     ```

### 2.4 代码获取与构建

1. **获取代码**
   - 打开命令提示符
   - 执行命令：
     ```bash
     git clone https://github.com/trae-ai/ai-capability-mapping-system.git
     cd ai-capability-mapping-system
     ```

2. **构建后端服务**
   - 执行命令：
     ```bash
     cd backend/mono-service
     mvn clean package
     ```
   - 构建完成后，会在 `target` 目录生成 `mono-service-1.0.0.jar` 文件

3. **构建前端服务**
   - 执行命令：
     ```bash
     cd ../../frontend
     npm install
     npm run build
     ```
   - 构建完成后，会在 `dist` 目录生成前端静态资源

### 2.5 应用部署

1. **部署后端服务**
   - 打开命令提示符
   - 执行命令：
     ```bash
     java -jar -Xmx2g -Xms1g backend/mono-service/target/mono-service-1.0.0.jar
     ```

2. **部署前端服务**
   - 安装 Nginx for Windows：
     - 下载 Nginx Windows 版本
     - 解压到本地目录（例如：`C:\nginx`）
   - 复制前端静态资源：
     - 将 `frontend/dist` 目录下的所有文件复制到 `C:\nginx\html` 目录
   - 配置 Nginx：
     - 编辑 `C:\nginx\conf\nginx.conf` 文件
     - 修改 server 部分：
       ```nginx
       server {
           listen       80;
           server_name  localhost;
           
           location / {
               root   html;
               index  index.html index.htm;
               try_files $uri $uri/ /index.html;
           }
           
           location /api {
               proxy_pass http://localhost:8080/api;
               proxy_set_header Host $host;
               proxy_set_header X-Real-IP $remote_addr;
               proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
           }
       }
       ```
   - 启动 Nginx：
     - 打开命令提示符，进入 Nginx 安装目录
     - 执行命令：`start nginx`

### 2.6 数据库初始化

1. **执行数据库初始化脚本**
   - 打开达梦数据库管理工具
   - 连接到数据库实例
   - 执行 `backend/mono-service/src/main/resources/sql/init_dm8.sql` 脚本

2. **初始化 Neo4j 数据**
   - 打开浏览器，访问 `http://localhost:7474`
   - 登录 Neo4j（默认用户名/密码：neo4j/neo4j）
   - 执行以下 Cypher 语句：
     ```cypher
     // 创建技能节点
     CREATE (s1:Skill {name: 'Java', category: '编程语言', level: 2});
     CREATE (s2:Skill {name: 'Python', category: '编程语言', level: 2});
     CREATE (s3:Skill {name: 'Spring Boot', category: '框架', level: 2});
     CREATE (s4:Skill {name: 'Vue', category: '前端', level: 2});
     CREATE (s5:Skill {name: 'MySQL', category: '数据库', level: 2});
     
     // 创建个人节点
     CREATE (p:Person {id: 1, name: '张三', city: '合肥', salary: '15k-20k'});
     
     // 创建职位节点
     CREATE (j:Job {id: 1, title: 'Java开发工程师', company: '某科技公司', city: '合肥'});
     
     // 创建关系
     MATCH (p:Person {id: 1}), (s1:Skill {name: 'Java'}) CREATE (p)-[r:HAS_SKILL {level: 4, years: 5}]->(s1);
     MATCH (p:Person {id: 1}), (s3:Skill {name: 'Spring Boot'}) CREATE (p)-[r:HAS_SKILL {level: 3, years: 3}]->(s3);
     MATCH (j:Job {id: 1}), (s1:Skill {name: 'Java'}) CREATE (j)-[r:REQUIRES_SKILL {level: 3, required: true}]->(s1);
     MATCH (j:Job {id: 1}), (s3:Skill {name: 'Spring Boot'}) CREATE (j)-[r:REQUIRES_SKILL {level: 2, required: true}]->(s3);
     ```

### 2.7 系统配置

1. **修改系统配置**
   - 编辑 `backend/mono-service/src/main/resources/application.yml` 文件，根据实际情况修改配置：
     ```yaml
     # 数据库配置
     spring:
       datasource:
         url: jdbc:dm://localhost:5236/AI_CAPABILITY_MAPPING
         username: SYSDBA
         password: SYSDBA
         driver-class-name: dm.jdbc.driver.DmDriver
     
     # Neo4j 配置
     spring:
       data:
         neo4j:
           uri: bolt://localhost:7687
           username: neo4j
           password: neo4j123
     
     # Milvus 配置
     milvus:
       host: localhost
       port: 19530
     
     # Ollama 配置
     ollama:
       base-url: http://localhost:11434
       model: qwen2.5:3b-instruct
     ```

2. **启动所有服务**
   - 启动达梦DM8服务（如果未启动）
   - 启动 Neo4j 服务：
     ```bash
     C:\Neo4j\bin\neo4j start
     ```
   - 启动 Milvus 服务（如果使用 Docker）：
     ```bash
     docker start milvus
     ```
   - 启动 Ollama 服务（自动启动）
   - 启动后端服务：
     ```bash
     java -jar -Xmx2g -Xms1g backend/mono-service/target/mono-service-1.0.0.jar
     ```
   - 启动前端服务（Nginx）：
     ```bash
     C:\nginx\nginx.exe
     ```

## 3. 系统验证

### 3.1 访问系统
- 打开浏览器，访问系统地址：http://localhost:80
- 登录系统
  - 管理员：admin / admin123
  - 普通用户：user1 / user123

### 3.2 功能验证

1. **文档解析功能**
   - 上传简历或职位描述文档
   - 检查解析状态和结果

2. **能力图谱功能**
   - 查看个人能力图谱
   - 查看职位能力图谱
   - 测试图谱可视化和交互功能

3. **智能匹配功能**
   - 执行职位与人才的匹配
   - 查看匹配结果和详细信息

4. **后台管理功能**
   - 查看用户列表
   - 管理技能库
   - 查看系统监控数据

## 4. 故障排查

### 4.1 常见问题

1. **数据库连接失败**
   - 检查数据库服务是否运行
   - 检查数据库连接配置是否正确
   - 检查网络连接是否正常

2. **AI 服务异常**
   - 检查 Ollama 服务是否运行
   - 检查模型是否正确下载
   - 检查网络连接是否正常

3. **系统响应缓慢**
   - 检查系统资源使用情况
   - 优化数据库查询
   - 调整 JVM 内存配置

4. **前端页面无法访问**
   - 检查 Nginx 服务是否运行
   - 检查前端静态资源是否正确部署
   - 检查网络连接是否正常

### 4.2 日志查看

1. **后端日志**
   - 后端服务启动时会在控制台输出日志
   - 可以将日志重定向到文件：
     ```bash
     java -jar -Xmx2g -Xms1g backend/mono-service/target/mono-service-1.0.0.jar > backend.log 2>&1
     ```

2. **数据库日志**
   - 达梦DM8日志：`C:\dmdbms\data\AI_CAPABILITY_MAPPING\log`

3. **Neo4j 日志**
   - Neo4j日志：`C:\Neo4j\logs`

4. **Milvus 日志**
   - 如果使用 Docker：
     ```bash
     docker logs milvus
     ```

## 5. 系统维护

### 5.1 定期备份

1. **数据库备份**
   - 使用达梦数据库管理工具进行备份
   - 或执行命令：
     ```bash
     "C:\dmdbms\bin\dexp.exe" SYSDBA/SYSDBA@localhost:5236 file=C:\backup\ai_capability_mapping.dmp log=C:\backup\ai_capability_mapping.log full=y
     ```

2. **Neo4j 备份**
   - 执行命令：
     ```bash
     C:\Neo4j\bin\neo4j-admin.bat backup --to=C:\backup\neo4j-backup
     ```

3. **文件备份**
   - 备份上传的文档文件：
     ```bash
     robocopy C:\temp\documents C:\backup\documents /E
     ```

### 5.2 系统更新

1. **后端服务更新**
   - 停止当前服务
   - 重新构建并启动新服务

2. **前端服务更新**
   - 重新构建前端
   - 复制新的静态资源到 Nginx 目录
   - 重启 Nginx 服务：
     ```bash
     C:\nginx\nginx.exe -s reload
     ```

3. **数据库更新**
   - 执行数据库更新脚本
   - 重启数据库服务

## 6. 安全注意事项

1. **账号安全**
   - 定期更换密码
   - 使用强密码策略
   - 启用多因素认证

2. **数据安全**
   - 敏感数据加密存储
   - 定期备份数据
   - 限制数据访问权限

3. **系统安全**
   - 配置 Windows 防火墙规则
   - 及时更新系统补丁
   - 定期进行安全审计

4. **网络安全**
   - 使用 HTTPS 协议
   - 配置网络访问控制
   - 监控网络异常流量

## 7. 联系方式

- **技术支持邮箱**: support@trae.com
- **技术支持电话**: 400-123-4567
- **在线支持**: 系统内在线客服

---

**© 2026 Trae AI. 保留所有权利。**