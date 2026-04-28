<template>
  <div class="document-container">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>文档智能解析</span>
        </div>
      </template>
      <div class="upload-section">
        <el-upload
          class="upload-demo"
          drag
          action="#"
          :auto-upload="false"
          :on-change="handleFileChange"
          :show-file-list="false"
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            将文件拖到此处，或 <em>点击选择文件</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              支持 PDF、Word、图片等格式，单个文件不超过 50MB
            </div>
          </template>
        </el-upload>
      </div>
      
      <div v-if="uploadedFile" class="parse-section">
        <el-divider>解析状态</el-divider>
        <el-card shadow="hover">
          <div class="file-info">
            <el-tag>{{ uploadedFile.fileName }}</el-tag>
            <el-tag :type="statusType">{{ statusText }}</el-tag>
          </div>
          <el-button type="primary" @click="checkStatus" :loading="checkingStatus">
            检查状态
          </el-button>
          <el-button type="success" @click="getResult" :loading="gettingResult" :disabled="!canGetResult">
            查看结果
          </el-button>
        </el-card>
      </div>
      
      <div v-if="parseResult" class="result-section">
        <el-divider>解析结果</el-divider>
        <el-card shadow="hover">
          <el-tabs type="border-card">
            <el-tab-pane label="技能信息">
              <el-table :data="skillTableData" style="width: 100%">
                <el-table-column prop="name" label="技能名称" width="180" />
                <el-table-column prop="level" label="技能等级" />
              </el-table>
            </el-tab-pane>
            <el-tab-pane label="项目经验">
              <el-table :data="projectTableData" style="width: 100%">
                <el-table-column prop="name" label="项目名称" width="180" />
                <el-table-column prop="description" label="项目描述" />
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onUnmounted } from 'vue'
import { UploadFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const fileList = ref([])
const uploadedFile = ref(null)
const parseStatus = ref('pending')
const parseResult = ref(null)
const checkingStatus = ref(false)
const gettingResult = ref(false)

const token = localStorage.getItem('token') || ''

let pollingTimer = null

const statusType = computed(() => {
  switch (parseStatus.value) {
    case 'completed': return 'success'
    case 'failed': return 'danger'
    case 'processing': return 'warning'
    default: return 'info'
  }
})

const statusText = computed(() => {
  switch (parseStatus.value) {
    case 'completed': return '解析完成'
    case 'failed': return '解析失败'
    case 'processing': return '解析中'
    case 'pending': return '待解析'
    default: return parseStatus.value
  }
})

const canGetResult = computed(() => parseStatus.value === 'completed')

const handleFileChange = (file) => {
  const isLt50M = file.size / 1024 / 1024 < 50
  if (!isLt50M) {
    ElMessage.error('上传文件大小不能超过 50MB!')
    return false
  }

  // 模拟上传成功
  ElMessage.success('文件上传成功，正在开始解析...')
  uploadedFile.value = {
    id: 'mock-doc-' + Date.now(),
    fileName: file.name
  }
  parseStatus.value = 'processing'
  parseResult.value = null

  // 自动开始轮询状态
  startPolling()
}

const startPolling = () => {
  if (pollingTimer) clearInterval(pollingTimer)
  checkingStatus.value = true
  
  let mockProgress = 0
  pollingTimer = setInterval(() => {
    mockProgress += 20
    if (mockProgress >= 100) {
      clearInterval(pollingTimer)
      checkingStatus.value = false
      parseStatus.value = 'completed'
      ElMessage.success('解析已完成')
      getResult()
    }
  }, 1000)
}

const checkStatus = async () => {
  if (!uploadedFile.value) return
  if (parseStatus.value === 'completed') {
    ElMessage.success('解析已完成')
    return
  }
  ElMessage.info('解析正在进行中，请稍后...')
}

const getResult = async () => {
  if (!uploadedFile.value || parseStatus.value !== 'completed') return
  
  gettingResult.value = true
  try {
    // 模拟网络延迟
    await new Promise(resolve => setTimeout(resolve, 800))
    parseResult.value = {
      skills: [
        { name: 'Java', level: '专家' },
        { name: 'Spring Boot', level: '熟练' },
        { name: 'Vue.js', level: '掌握' },
        { name: 'MySQL', level: '熟练' }
      ],
      projects: [
        '基于微服务架构的电商后台管理系统',
        'AI能力图谱与智能匹配平台研发'
      ],
      education: '计算机科学与技术 本科',
      experience: '5年 Java 后端开发经验'
    }
  } catch (error) {
    ElMessage.error('获取解析结果失败')
  } finally {
    gettingResult.value = false
  }
}

const skillTableData = computed(() => {
  if (!parseResult.value || !parseResult.value.skills) return []
  return parseResult.value.skills
})

const projectTableData = computed(() => {
  if (!parseResult.value || !parseResult.value.projects) return []
  return parseResult.value.projects.map(p => ({
    name: p,
    description: '项目描述暂无'
  }))
})

onUnmounted(() => {
  if (pollingTimer) clearInterval(pollingTimer)
})
</script>

<style scoped>
.document-container {
  padding: 20px;
}

.upload-section {
  margin-bottom: 30px;
}

.upload-demo {
  border: 2px dashed #d9d9d9;
  border-radius: 6px;
  padding: 40px 20px;
  text-align: center;
}

.parse-section {
  margin-bottom: 30px;
}

.file-info {
  margin-bottom: 20px;
}

.file-info .el-tag {
  margin-right: 10px;
}

.result-section {
  margin-top: 30px;
}
</style>