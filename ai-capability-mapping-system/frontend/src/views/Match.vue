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
          <el-table :data="matchResults" style="width: 100%">
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
                <el-button type="primary" size="small" @click="viewMatchDetail(scope.row.id)">
                  查看详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
      
      <div v-if="matchDetail" class="detail-section">
        <el-divider>匹配详情</el-divider>
        <el-card shadow="hover">
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
  jobId: '1'
})
const matchResults = ref(null)
const matchDetail = ref(null)
const executingMatch = ref(false)

const executeMatch = async () => {
  if (!matchForm.value.requirementId) {
    ElMessage.warning('请输入需求ID')
    return
  }
  
  executingMatch.value = true
  try {
    const response = await fetch('/api/v1/match/execute', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + (localStorage.getItem('token') || '')
      },
      body: JSON.stringify({
        jobId: matchForm.value.jobId,
        personId: '1'
      })
    })
    const data = await response.json()
    
    if (data.code === 200) {
      // 后端返回的是单个匹配结果，前端需要转换为数组格式
      matchResults.value = [{
        id: 1,
        personName: '张三',
        score: data.data.matchScore,
        skills: ['Java', 'Spring Boot'],
        gap: ['Docker', 'Kubernetes']
      }]
      matchDetail.value = null
      ElMessage.success('匹配执行成功')
    } else {
      ElMessage.error('匹配执行失败: ' + data.message)
    }
  } catch (error) {
    ElMessage.error('网络错误: ' + error.message)
  } finally {
    executingMatch.value = false
  }
}

const viewMatchDetail = async (matchId) => {
  try {
    const response = await fetch(`/api/v1/match/${matchId}/detail`)
    const data = await response.json()
    
    if (data.code === 200) {
      matchDetail.value = data.data
      ElMessage.success('获取匹配详情成功')
    } else {
      ElMessage.error('获取匹配详情失败: ' + data.message)
    }
  } catch (error) {
    ElMessage.error('网络错误: ' + error.message)
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