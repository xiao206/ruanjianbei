<template>
  <div class="match-container">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>智能匹配</span>
        </div>
      </template>
      <div class="match-section">
        <el-form :model="matchForm" label-width="80px">
          <el-form-item label="需求ID">
            <el-input v-model="matchForm.requirementId" placeholder="输入需求ID" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="executeMatch" :loading="executingMatch">执行匹配</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <div class="results-section">
        <el-divider>匹配结果</el-divider>
        <el-card shadow="hover">
          <el-table :data="matchResults" style="width: 100%" v-loading="executingMatch">
            <template #empty>
              <el-empty description="请输入需求ID并执行匹配" />
            </template>
            <el-table-column prop="personName" label="人才姓名" width="120" />
            <el-table-column label="匹配度" width="100">
              <template #default="scope">
                <el-progress :percentage="Math.round(scope.row.score * 100)" :color="getScoreColor(scope.row.score)" />
              </template>
            </el-table-column>
            <el-table-column label="匹配分数" width="100">
              <template #default="scope">
                {{ Math.round(scope.row.score * 100) }}
              </template>
            </el-table-column>
            <el-table-column label="匹配技能">
              <template #default="scope">
                <el-tag v-for="skill in scope.row.matchSkills" :key="skill" size="small" style="margin-right: 5px">
                  {{ skill }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="技能差距">
              <template #default="scope">
                <el-tag v-for="skill in scope.row.gapSkills" :key="skill" size="small" type="danger" style="margin-right: 5px">
                  {{ skill }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button type="primary" link size="small" @click="viewMatchDetail(scope.row)">
                查看详情
              </el-button>
            </template>
          </el-table-column>
          </el-table>
        </el-card>
      </div>
      
      <div v-if="matchDetail" class="detail-section">
        <el-divider>匹配详情</el-divider>
        <el-card shadow="hover" v-loading="loadingDetail">
          <h3 class="detail-title">{{ matchDetail.personName }} 的匹配报告</h3>
          <el-tabs type="border-card">
            <el-tab-pane label="技能匹配">
              <el-table :data="skillMatchData" style="width: 100%">
                <el-table-column prop="skill" label="技能" width="120" />
                <el-table-column prop="level" label="匹配等级" />
              </el-table>
            </el-tab-pane>
            <el-tab-pane label="学习路径">
              <el-timeline>
                <el-timeline-item v-for="(item, index) in matchDetail.learningPath" :key="index">
                  {{ item }}
                </el-timeline-item>
              </el-timeline>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'

const matchForm = ref({
  requirementId: '',
  topK: 5
})
const executingMatch = ref(false)
const loadingDetail = ref(false)
const matchResults = ref([])
const matchDetail = ref(null)

const executeMatch = async () => {
  if (!matchForm.value.requirementId) {
    ElMessage.warning('请输入需求ID')
    return
  }
  
  executingMatch.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 800))
    matchResults.value = [
      {
        personId: 'user_001',
        personName: '张三',
        score: 0.95,
        matchSkills: ['Java', 'Spring Boot', 'MySQL', 'Redis'],
        gapSkills: ['Python', 'Go'],
        learningPath: [
          '学习 Python 基础语法与工程化',
          '掌握 Django/FastAPI 其中一个框架',
          '补齐 Go 基础与并发模型',
          '完成 1 个中小型实战项目巩固'
        ]
      },
      {
        personId: 'user_002',
        personName: '李四',
        score: 0.82,
        matchSkills: ['Java', 'MySQL'],
        gapSkills: ['Spring Boot', 'Redis', 'Python'],
        learningPath: [
          '学习 Spring Boot 与常用组件',
          '掌握 Redis 缓存与分布式锁',
          '了解微服务基础（注册中心、配置中心、网关）',
          '通过项目落地巩固'
        ]
      },
      {
        personId: 'user_003',
        personName: '王五',
        score: 0.65,
        matchSkills: ['Python', 'MySQL'],
        gapSkills: ['Java', 'Spring Boot', 'Redis', 'Go'],
        learningPath: [
          '系统学习 Java 语法与集合/并发',
          '入门 Spring Boot（Web/DI/配置）',
          '学习 Redis/MySQL 性能优化',
          '完成 Java 后端项目实战'
        ]
      }
    ]
    ElMessage.success('匹配完成')
  } catch (error) {
    ElMessage.error('匹配执行失败')
  } finally {
    executingMatch.value = false
  }
}

const viewMatchDetail = async (row) => {
  loadingDetail.value = true
  matchDetail.value = null // 清空旧数据以显示骨架屏或loading
  try {
    await new Promise(resolve => setTimeout(resolve, 500))
    matchDetail.value = {
      score: row.score,
      personName: row.personName,
      skills: row.matchSkills,
      gap: row.gapSkills,
      learningPath: row.learningPath
    }
  } catch (error) {
    ElMessage.error('加载详情失败')
  } finally {
    loadingDetail.value = false
  }
}

const getScoreColor = (score) => {
  if (score >= 0.9) return '#67C23A'
  if (score >= 0.7) return '#E6A23C'
  return '#F56C6C'
}

const skillMatchData = ref([])

watch(() => matchDetail.value, (newDetail) => {
  if (!newDetail) {
    skillMatchData.value = []
    return
  }

  skillMatchData.value = (newDetail.skills || []).map(skill => ({
    skill,
    level: '匹配'
  }))
})
</script>

<style scoped>
.match-container {
  padding: 20px;
}

.match-section {
  margin-bottom: 30px;
}

.results-section {
  margin-top: 30px;
}

.detail-section {
  margin-top: 30px;
}

.el-progress {
  margin-top: 5px;
}
</style>
