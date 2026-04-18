<template>
  <div class="home-container">
    <el-card shadow="hover" class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎使用 AI 智能匹配与能力图谱系统</span>
        </div>
      </template>
      <div class="welcome-content">
        <p>本系统基于银河麒麟操作系统，利用人工智能技术构建智能化的能力图谱与供需匹配系统，实现技术人员能力与需求的高效精准适配。</p>
        <div class="feature-grid">
          <el-card shadow="hover" class="feature-card">
            <div class="feature-icon document-icon"></div>
            <h3>文档智能解析</h3>
            <p>支持 PDF、Word、图片等多格式文件上传，自动识别文档类型并提取关键信息。</p>
            <el-button type="primary" @click="navigateTo('document')">开始解析</el-button>
          </el-card>
          <el-card shadow="hover" class="feature-card">
            <div class="feature-icon graph-icon"></div>
            <h3>双向能力图谱</h3>
            <p>基于结构化数据自动构建人才能力图谱和需求图谱，支持多种布局可视化。</p>
            <el-button type="primary" @click="navigateTo('graph')">查看图谱</el-button>
          </el-card>
          <el-card shadow="hover" class="feature-card">
            <div class="feature-icon match-icon"></div>
            <h3>供需智能匹配</h3>
            <p>基于向量检索实现人才与需求的语义级匹配，通过知识图谱关系推理发现潜在匹配路径。</p>
            <el-button type="primary" @click="navigateTo('match')">开始匹配</el-button>
          </el-card>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const navigateTo = (path) => {
  router.push(`/${path}`)
}
</script>

<style scoped>
.home-container {
  padding: 20px;
}

.welcome-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-content {
  padding: 20px 0;
}

.welcome-content p {
  font-size: 16px;
  line-height: 1.6;
  margin-bottom: 30px;
  color: #606266;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.feature-card {
  text-align: center;
  padding: 30px 20px;
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  color: white;
}

.document-icon {
  background-color: #409eff;
}

.graph-icon {
  background-color: #67c23a;
}

.match-icon {
  background-color: #e6a23c;
}

.feature-card h3 {
  margin: 0 0 15px;
  font-size: 18px;
  font-weight: bold;
}

.feature-card p {
  margin: 0 0 20px;
  font-size: 14px;
  color: #606266;
  line-height: 1.5;
}

.feature-card .el-button {
  margin-top: 10px;
}
</style>