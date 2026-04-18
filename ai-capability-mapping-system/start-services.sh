#!/bin/bash

# 启动服务脚本
echo "=== 启动 AI 智能匹配与能力图谱系统服务 ==="

# 进入项目目录
cd "$(dirname "$0")"

# 启动 Docker 容器
echo "启动 Docker 容器..."
docker-compose up -d

# 检查容器状态
echo "检查容器状态..."
docker-compose ps

echo "\n=== 服务启动完成 ==="
echo "系统服务已启动，请等待几分钟让服务完全初始化"
echo "然后运行 ./test-system.sh 进行测试"
