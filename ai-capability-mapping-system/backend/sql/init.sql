-- Create database if not exists
CREATE DATABASE IF NOT EXISTS ai_capability_mapping CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ai_capability_mapping;

-- System user table
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role_id BIGINT,
    dept_id BIGINT,
    status VARCHAR(20) DEFAULT 'active',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Role table
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL,
    role_code VARCHAR(50) NOT NULL UNIQUE,
    permissions TEXT,
    status VARCHAR(20) DEFAULT 'active',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Department table
CREATE TABLE IF NOT EXISTS sys_dept (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dept_name VARCHAR(100) NOT NULL,
    parent_id BIGINT DEFAULT 0,
    sort INT DEFAULT 0,
    leader VARCHAR(50),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Document table
CREATE TABLE IF NOT EXISTS doc_file (
    id VARCHAR(36) PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'uploaded',
    user_id BIGINT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Document parse result table
CREATE TABLE IF NOT EXISTS doc_parse_result (
    id VARCHAR(36) PRIMARY KEY,
    doc_id VARCHAR(36) NOT NULL,
    parse_type VARCHAR(50) NOT NULL,
    result_json TEXT,
    status VARCHAR(20) DEFAULT 'pending',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (doc_id) REFERENCES doc_file(id)
);

-- Skill category table
CREATE TABLE IF NOT EXISTS skill_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    parent_id BIGINT DEFAULT 0,
    level INT DEFAULT 1,
    description TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Skill synonym table
CREATE TABLE IF NOT EXISTS skill_synonym (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    skill_id BIGINT NOT NULL,
    synonym VARCHAR(100) NOT NULL,
    source VARCHAR(50),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Match record table
CREATE TABLE IF NOT EXISTS match_record (
    id VARCHAR(36) PRIMARY KEY,
    req_id VARCHAR(36) NOT NULL,
    person_id VARCHAR(36) NOT NULL,
    score DECIMAL(5,2),
    detail_json TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Match feedback table
CREATE TABLE IF NOT EXISTS match_feedback (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    record_id VARCHAR(36) NOT NULL,
    rating INT,
    comment TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (record_id) REFERENCES match_record(id)
);

-- System config table
CREATE TABLE IF NOT EXISTS system_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_key VARCHAR(100) NOT NULL UNIQUE,
    config_value TEXT,
    description VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert default roles
INSERT INTO sys_role (role_name, role_code, permissions) VALUES
('管理员', 'admin', '*'),
('普通用户', 'user', 'document:upload,graph:view,match:execute');

-- Insert default admin user
INSERT INTO sys_user (username, password, role_id, status) VALUES
('admin', 'admin123', 1, 'active'),
('user1', 'user123', 2, 'active');

-- Insert default skill categories
INSERT INTO skill_category (name, parent_id, level) VALUES
('开发技术', 0, 1),
('数据库', 0, 1),
('前端技术', 0, 1),
('Java', 1, 2),
('Spring Boot', 1, 2),
('MySQL', 2, 2),
('Redis', 2, 2),
('Vue.js', 3, 2),
('React', 3, 2);