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
          <el-form-item label="职位ID">
            <el-input v-model="matchForm.jobId" placeholder="输入职位ID" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="executeMatch" :loading="executingMatch">执行匹配</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <div v-if="matchResults" class="results-section">
        <el-divider>匹配结果</el-divider>
        <el-card shadow="hover">
          <el-table :data="matchResults" style="width: 100%" v-loading="executingMatch">
            <template #empty>
              <el-empty description="请输入需求ID并执行匹配" />
            </template>
            <el-table-column prop="personName" label="人才姓名" width="120" />
            <el-table-column label="匹配度" width="100">
              <template #default="scope">
                <el-progress :percentage="scope.row.score * 100" :color="getScoreColor(scope.row.score)" />
              </template>
            </el-table-column>
            <el-table-column prop="score" label="匹配分数" width="100" />
            <el-table-column prop="skills" label="匹配技能">
              <template #default="scope">
                <el-tag v-for="skill in scope.row.skills" :key="skill" size="small" style="margin-right: 5px">
                  {{ skill }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="gap" label="技能差距">
              <template #default="scope">
                <el-tag v-for="skill in scope.row.gap" :key="skill" size="small" type="danger" style="margin-right: 5px">
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
              <el-list>
                <el-list-item v-for="(item, index) in matchDetail.learningPath" :key="index">
                  {{ item }}
                </el-list-item>
              </el-list>
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
        score: 95,
        matchSkills: ['Java', 'Spring Boot', 'MySQL', 'Redis'],
        gapSkills: ['Python', 'Go'],
        learningPath: '建议学习Python基础语法及Django框架，补充多语言开发能力。'
      },
      {
        personId: 'user_002',
        personName: '李四',
        score: 82,
        matchSkills: ['Java', 'MySQL'],
        gapSkills: ['Spring Boot', 'Redis', 'Python'],
        learningPath: '建议深入学习Spring Boot微服务架构及Redis缓存中间件，提升后端系统设计能力。'
      },
      {
        personId: 'user_003',
        personName: '王五',
        score: 65,
        matchSkills: ['Python', 'MySQL'],
        gapSkills: ['Java', 'Spring Boot', 'Redis', 'Go'],
        learningPath: '该候选人主要技能栈为Python，与当前Java后端需求差距较大，建议系统学习Java生态。'
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

// 监听匹配详情变化，更新技能匹配数据
watch(() => matchDetail.value, (newDetail) => {
  if (newDetail && newDetail.skillMatch) {
    skillMatchData.value = Object.entries(newDetail.skillMatch).map(([skill, level]) => ({
      skill,
      level
    }))
  } else {
    skillMatchData.value = []
  }
}, { deep: true })
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