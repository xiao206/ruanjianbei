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
          action="/api/v1/document/upload"
          :headers="{ Authorization: 'Bearer ' + token }"
          :data="{ type: 'resume' }"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError"
          :file-list="fileList"
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            拖拽文件到此处，或 <em>点击上传</em>
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
import { ref, computed } from 'vue'
import { UploadFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const fileList = ref([])
const uploadedFile = ref(null)
const parseStatus = ref('pending')
const parseResult = ref(null)
const checkingStatus = ref(false)
const gettingResult = ref(false)

const token = localStorage.getItem('token') || ''

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

const handleUploadSuccess = (response) => {
  if (response.code === 200) {
    uploadedFile.value = response.data
    parseStatus.value = response.data.status
    ElMessage.success('文件上传成功')
  } else {
    ElMessage.error('文件上传失败: ' + response.message)
  }
}

const handleUploadError = (error) => {
  ElMessage.error('文件上传失败: ' + error.message)
}

const checkStatus = async () => {
  if (!uploadedFile.value) return
  
  checkingStatus.value = true
  try {
    const response = await fetch(`/api/v1/document/${uploadedFile.value.id}/status`)
    const data = await response.json()
    if (data.code === 200) {
      parseStatus.value = data.data.status
      ElMessage.success('状态更新成功')
    } else {
      ElMessage.error('获取状态失败: ' + data.message)
    }
  } catch (error) {
    ElMessage.error('网络错误: ' + error.message)
  } finally {
    checkingStatus.value = false
  }
}

const getResult = async () => {
  if (!uploadedFile.value) return
  
  gettingResult.value = true
  try {
    const response = await fetch(`/api/v1/document/${uploadedFile.value.id}/result`)
    const data = await response.json()
    if (data.code === 200) {
      parseResult.value = data.data
      ElMessage.success('获取结果成功')
    } else {
      ElMessage.error('获取结果失败: ' + data.message)
    }
  } catch (error) {
    ElMessage.error('网络错误: ' + error.message)
  } finally {
    gettingResult.value = false
  }
}

const skillTableData = computed(() => {
  if (!parseResult.value || !parseResult.value.skills) return []
  return Object.entries(parseResult.value.skills).map(([name, level]) => ({
    name,
    level
  }))
})

const projectTableData = computed(() => {
  if (!parseResult.value || !parseResult.value.projects) return []
  return Object.entries(parseResult.value.projects).map(([name, description]) => ({
    name,
    description
  }))
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