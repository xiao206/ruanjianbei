-- 达梦DM8数据库初始化脚本

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS AI_CAPABILITY_MAPPING;

-- 使用数据库
USE AI_CAPABILITY_MAPPING;

-- 创建系统用户表
CREATE TABLE sys_user (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    user_type VARCHAR(20) NOT NULL, -- personal 或 enterprise
    status VARCHAR(20) NOT NULL DEFAULT 'active',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建文档表
CREATE TABLE doc_file (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(512) NOT NULL,
    doc_type VARCHAR(20) NOT NULL, -- resume 或 job
    status VARCHAR(20) NOT NULL DEFAULT 'pending', -- pending, processing, completed, failed
    user_id BIGINT NOT NULL,
    result_json CLOB,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES sys_user(id)
);

-- 创建技能分类表
CREATE TABLE skill_category (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    parent_id BIGINT,
    level INT NOT NULL DEFAULT 1,
    description VARCHAR(512),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES skill_category(id)
);

-- 创建技能同义词表
CREATE TABLE skill_synonym (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    skill_id BIGINT NOT NULL,
    synonym VARCHAR(100) NOT NULL,
    source VARCHAR(50),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (skill_id) REFERENCES skill_category(id)
);

-- 创建职位信息表
CREATE TABLE job_info (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    company_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    city VARCHAR(100),
    salary_range VARCHAR(100),
    status VARCHAR(20) NOT NULL DEFAULT 'active',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (company_id) REFERENCES sys_user(id)
);

-- 创建匹配记录表
CREATE TABLE match_record (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    job_id BIGINT NOT NULL,
    person_id BIGINT NOT NULL,
    score DECIMAL(5,2) NOT NULL,
    detail_json CLOB,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (job_id) REFERENCES job_info(id),
    FOREIGN KEY (person_id) REFERENCES sys_user(id)
);

-- 创建匹配反馈表
CREATE TABLE match_feedback (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    record_id BIGINT NOT NULL,
    rating INT NOT NULL,
    comment VARCHAR(512),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (record_id) REFERENCES match_record(id)
);

-- 创建系统配置表
CREATE TABLE system_config (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    config_key VARCHAR(100) NOT NULL UNIQUE,
    config_value VARCHAR(512) NOT NULL,
    description VARCHAR(512),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入初始数据
-- 插入系统配置
INSERT INTO system_config (config_key, config_value, description) VALUES
('max_file_size', '50MB', '最大文件大小'),
('thread_pool_size', '4', '线程池大小'),
('ollama_model', 'qwen2.5:3b-instruct', 'Ollama模型'),
('embedding_model', 'bge-m3', 'Embedding模型');

-- 插入初始技能分类
INSERT INTO skill_category (name, parent_id, level, description) VALUES
('编程语言', NULL, 1, '编程语言分类'),
('框架', NULL, 1, '框架分类'),
('数据库', NULL, 1, '数据库分类'),
('前端', NULL, 1, '前端分类'),
('Java', 1, 2, 'Java编程语言'),
('Python', 1, 2, 'Python编程语言'),
('Spring Boot', 2, 2, 'Spring Boot框架'),
('Vue', 4, 2, 'Vue前端框架'),
('MySQL', 3, 2, 'MySQL数据库'),
('Redis', 3, 2, 'Redis缓存数据库');

-- 插入初始用户
INSERT INTO sys_user (username, password, user_type, status) VALUES
('admin', 'admin123', 'enterprise', 'active'),
('user1', 'user123', 'personal', 'active');

COMMIT;