<template>
  <div class="home-container">
    <div class="hero-section">
      <h1 class="hero-title">AI 智能匹配与能力图谱系统</h1>
      <p class="hero-subtitle">
        基于大模型与知识图谱的智能人才与岗位需求匹配平台，助力企业精准识别人才，提供个性化学习路径建议。
      </p>
      <div class="hero-actions">
        <el-button type="primary" size="large" @click="router.push('/document')">
          开始体验
        </el-button>
        <el-button size="large" @click="router.push('/graph')">
          查看能力图谱
        </el-button>
      </div>
    </div>

    <div class="features-section">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="feature-card" shadow="hover" @click="router.push('/document')">
            <div class="feature-icon">
              <el-icon><Document /></el-icon>
            </div>
            <h3>文档智能解析</h3>
            <p>基于大语言模型，自动从简历或岗位需求文档中提取核心技能、项目经验与学历信息。</p>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="feature-card" shadow="hover" @click="router.push('/graph')">
            <div class="feature-icon">
              <el-icon><Share /></el-icon>
            </div>
            <h3>双向能力图谱</h3>
            <p>利用图数据库构建人才能力和岗位需求关系网，提供直观的技能结构可视化与图谱推理。</p>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="feature-card" shadow="hover" @click="router.push('/match')">
            <div class="feature-icon">
              <el-icon><DataLine /></el-icon>
            </div>
            <h3>供需智能匹配</h3>
            <p>结合向量检索与图谱技术，精准匹配人才与岗位，并智能生成消除技能差距的学习路径。</p>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { Document, Share, DataLine } from '@element-plus/icons-vue'

const router = useRouter()
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.hero-section {
  text-align: center;
  padding: 4rem 0;
  margin-bottom: 2rem;
}

.hero-title {
  font-size: 2.5rem;
  color: var(--el-text-color-primary);
  margin-bottom: 1rem;
}

.hero-subtitle {
  font-size: 1.2rem;
  color: var(--el-text-color-regular);
  margin-bottom: 2rem;
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
  line-height: 1.6;
}

.hero-actions {
  display: flex;
  justify-content: center;
  gap: 1rem;
}

.features-section {
  padding: 2rem 0;
}

.feature-card {
  height: 100%;
  text-align: center;
  transition: transform 0.3s;
  cursor: pointer;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  font-size: 3rem;
  color: var(--el-color-primary);
  margin-bottom: 1rem;
}

.feature-card h3 {
  font-size: 1.2rem;
  margin-bottom: 1rem;
  color: var(--el-text-color-primary);
}

.feature-card p {
  color: var(--el-text-color-secondary);
  line-height: 1.5;
}
</style>