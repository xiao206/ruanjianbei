#!/bin/bash

# 系统测试脚本
echo "=== AI 智能匹配与能力图谱系统测试 ==="
echo "开始测试系统功能..."

# 测试 API 端点
test_api() {
  local url=$1
  local method=${2:-GET}
  local data=${3:-}
  local headers=${4:-}
  
  echo "\n测试: $url"
  if [ "$method" = "POST" ]; then
    response=$(curl -s -X POST -H "Content-Type: application/json" $headers -d "$data" "$url")
  else
    response=$(curl -s -X GET $headers "$url")
  fi
  
  echo "响应: $response"
  
  # 检查响应是否包含成功代码
  if echo "$response" | grep -q '"code":200'; then
    echo "✓ 测试通过"
    return 0
  else
    echo "✗ 测试失败"
    return 1
  fi
}

# 测试登录
echo "\n=== 测试登录功能 ==="
login_response=$(curl -s -X POST -H "Content-Type: application/json" -d '{"username":"admin","password":"admin123"}' "http://localhost:8080/api/v1/auth/login")
echo "登录响应: $login_response"

# 提取 token
token=$(echo "$login_response" | grep -o '"token":"[^"]*"' | cut -d '"' -f 4)
if [ -z "$token" ]; then
  echo "✗ 登录失败，无法获取 token"
  exit 1
fi
echo "✓ 登录成功，获取到 token"

# 测试文档解析
echo "\n=== 测试文档解析功能 ==="
test_api "http://localhost:8080/api/v1/document/1/status" "GET" "" "-H \"Authorization: Bearer $token\""

# 测试图谱服务
echo "\n=== 测试图谱服务 ==="
test_api "http://localhost:8080/api/v1/graph/person/person1" "GET" "" "-H \"Authorization: Bearer $token\""

# 测试匹配服务
echo "\n=== 测试匹配服务 ==="
test_api "http://localhost:8080/api/v1/match/execute" "POST" '{"requirementId":"req1"}' "-H \"Authorization: Bearer $token\""

# 测试后台管理
echo "\n=== 测试后台管理 ==="
test_api "http://localhost:8080/api/v1/admin/users" "GET" "" "-H \"Authorization: Bearer $token\""

# 测试系统监控
echo "\n=== 测试系统监控 ==="
test_api "http://localhost:8080/api/v1/admin/monitor" "GET" "" "-H \"Authorization: Bearer $token\""

echo "\n=== 测试完成 ==="
echo "系统功能测试已完成"
