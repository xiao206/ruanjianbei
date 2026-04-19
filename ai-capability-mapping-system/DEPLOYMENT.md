# AI 智能匹配与能力图谱系统部署文档

## 1. 系统环境要求

### 1.1 硬件要求
- **CPU**: 4核 LoongArch 处理器
- **内存**: 8GB 及以上
- **磁盘**: 256GB 及以上
- **网络**: 稳定的网络连接

### 1.2 软件要求
- **操作系统**: 银河麒麟高级服务器操作系统 V11/V10 (LoongArch)
- **JDK**: 华为毕升 JDK 17 (LoongArch 版本)
- **数据库**: 达梦DM8 (LoongArch 版本)
- **图数据库**: Neo4j Community 5.x (LoongArch 版本)
- **向量数据库**: Milvus 2.4.x (LoongArch 版本)
- **AI 服务**: Ollama + Qwen2.5-3B (LoongArch 版本)
- **前端服务**: Nginx (LoongArch 版本)

## 2. 部署步骤

### 2.1 环境准备

1. **安装银河麒麟操作系统**
   - 下载银河麒麟高级服务器操作系统 V11/V10 (LoongArch 版本)
   - 按照官方文档安装操作系统

2. **安装华为毕升 JDK 17**
   ```bash
   # 下载毕升 JDK 17 (LoongArch 版本)
   wget https://repo.huaweicloud.com/java/jdk/17/bisheng/jdk-17.0.7_linux-loongarch64_bin.tar.gz
   
   # 解压并安装
   tar -zxvf jdk-17.0.7_linux-loongarch64_bin.tar.gz -C /usr/local/
   
   # 配置环境变量
   echo "export JAVA_HOME=/usr/local/jdk-17.0.7"
   echo "export PATH=$JAVA_HOME/bin:$PATH"
   echo "export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar"
   >> /etc/profile
   
   # 生效环境变量
   source /etc/profile
   ```

3. **安装 Nginx**
   ```bash
   # 安装 Nginx
   yum install -y nginx
   
   # 启动 Nginx
   systemctl start nginx
   systemctl enable nginx
   ```

### 2.2 数据库部署

1. **安装达梦DM8**
   - 下载达梦DM8 (LoongArch 版本)
   - 按照官方文档安装达梦DM8
   - 创建数据库实例和用户
   ```bash
   # 初始化数据库实例
   /opt/dmdbms/bin/dminit path=/opt/dmdbms/data DB_NAME=AI_CAPABILITY_MAPPING
   
   # 启动数据库服务
   systemctl start DmServiceAI_CAPABILITY_MAPPING
   
   # 登录数据库并创建用户
   /opt/dmdbms/bin/disql SYSDBA/SYSDBA@localhost:5236
   SQL> CREATE USER SYSDBA IDENTIFIED BY SYSDBA;
   SQL> GRANT DBA TO SYSDBA;
   ```

2. **安装 Neo4j**
   - 下载 Neo4j Community 5.x (LoongArch 版本)
   - 按照官方文档安装 Neo4j
   - 配置 Neo4j
   ```bash
   # 编辑配置文件
   vi /etc/neo4j/neo4j.conf
   # 设置以下参数
   dbms.connector.bolt.listen_address=0.0.0.0:7687
   dbms.connector.http.listen_address=0.0.0.0:7474
   dbms.default_database=neo4j
   
   # 启动 Neo4j
   systemctl start neo4j
   systemctl enable neo4j
   ```

3. **安装 Milvus**
   - 下载 Milvus 2.4.x (LoongArch 版本)
   - 按照官方文档安装 Milvus
   - 启动 Milvus 服务
   ```bash
   # 启动 Milvus 服务
   systemctl start milvus
   systemctl enable milvus
   ```

### 2.3 AI 服务部署

1. **安装 Ollama**
   - 下载 Ollama (LoongArch 版本)
   - 安装 Ollama
   ```bash
   # 安装 Ollama
   dpkg -i ollama_0.3.0_loongarch64.deb
   
   # 启动 Ollama 服务
   systemctl start ollama
   systemctl enable ollama
   ```

2. **下载 Qwen2.5-3B 模型**
   ```bash
   # 拉取 Qwen2.5-3B 模型
   ollama pull qwen2.5:3b-instruct
   ```

3. **部署 RapidOCR**
   - 安装 Python 3.8+
   - 安装 RapidOCR
   ```bash
   # 安装 RapidOCR
   pip install rapidocr-onnxruntime
   
   # 启动 RapidOCR 服务
   rapidocr_web --host 0.0.0.0 --port 8000
   ```

### 2.4 应用部署

1. **部署后端服务**
   - 上传后端应用包 `mono-service-1.0.0.jar` 到服务器
   - 启动后端服务
   ```bash
   # 启动后端服务
   java -jar -Xmx2g -Xms1g mono-service-1.0.0.jar --spring.profiles.active=kylin
   ```

2. **部署前端服务**
   - 上传前端静态资源到 Nginx 目录
   ```bash
   # 复制前端静态资源到 Nginx 目录
   cp -r dist/* /usr/share/nginx/html/
   
   # 配置 Nginx
   vi /etc/nginx/conf.d/default.conf
   # 添加以下配置
   server {
       listen 80;
       server_name localhost;
       
       location / {
           root /usr/share/nginx/html;
           index index.html index.htm;
           try_files $uri $uri/ /index.html;
       }
       
       location /api {
           proxy_pass http://localhost:8080/api;
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
           proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
       }
   }
   
   # 重启 Nginx
   systemctl restart nginx
   ```

### 2.5 数据库初始化

1. **执行数据库初始化脚本**
   ```bash
   # 登录达梦DM8
   /opt/dmdbms/bin/disql SYSDBA/SYSDBA@localhost:5236
   
   # 执行初始化脚本
   SQL> START /workspace/ai-capability-mapping-system/backend/mono-service/src/main/resources/sql/init_dm8.sql;
   ```

2. **初始化 Neo4j 数据**
   - 访问 Neo4j 管理界面 (http://localhost:7474)
   - 执行以下 Cypher 语句
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

### 2.6 系统配置

1. **修改系统配置**
   - 编辑 `application.yml` 文件，根据实际情况修改配置
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
   ```bash
   # 启动达梦DM8
   systemctl start DmServiceAI_CAPABILITY_MAPPING
   
   # 启动 Neo4j
   systemctl start neo4j
   
   # 启动 Milvus
   systemctl start milvus
   
   # 启动 Ollama
   systemctl start ollama
   
   # 启动后端服务
   java -jar -Xmx2g -Xms1g mono-service-1.0.0.jar --spring.profiles.active=kylin
   
   # 启动前端服务
   systemctl restart nginx
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
   ```bash
   # 查看后端服务日志
   tail -f /path/to/mono-service.log
   ```

2. **数据库日志**
   ```bash
   # 查看达梦DM8日志
   tail -f /opt/dmdbms/data/AI_CAPABILITY_MAPPING/log/dm_20260418.log
   ```

3. **Neo4j 日志**
   ```bash
   # 查看 Neo4j 日志
   tail -f /var/log/neo4j/neo4j.log
   ```

4. **Milvus 日志**
   ```bash
   # 查看 Milvus 日志
   tail -f /var/log/milvus/milvus.log
   ```

## 5. 系统维护

### 5.1 定期备份

1. **数据库备份**
   ```bash
   # 备份达梦DM8数据库
   /opt/dmdbms/bin/dexp SYSDBA/SYSDBA@localhost:5236 file=/backup/ai_capability_mapping.dmp log=/backup/ai_capability_mapping.log full=y
   ```

2. **Neo4j 备份**
   ```bash
   # 备份 Neo4j 数据库
   neo4j-admin backup --to=/backup/neo4j-backup
   ```

3. **文件备份**
   ```bash
   # 备份上传的文档文件
   tar -zcvf /backup/documents.tar.gz /tmp/documents/
   ```

### 5.2 系统更新

1. **后端服务更新**
   - 停止当前服务
   - 上传新的应用包
   - 启动新服务

2. **前端服务更新**
   - 上传新的静态资源
   - 重启 Nginx 服务

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
   - 配置防火墙规则
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