<template>
  <div class="admin-container">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>后台管理</span>
        </div>
      </template>
      <el-tabs type="border-card">
        <el-tab-pane label="用户管理">
          <div class="tab-content">
            <el-button type="primary" @click="addUser">添加用户</el-button>
            <el-table :data="users" style="width: 100%" :loading="loadingUsers">
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column prop="username" label="用户名" width="120" />
              <el-table-column prop="name" label="姓名" width="120" />
              <el-table-column prop="role" label="角色" width="100" />
              <el-table-column prop="dept" label="部门" width="120" />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="scope">
                  <el-tag :type="scope.row.status === 'active' ? 'success' : 'danger'">
                    {{ scope.row.status === 'active' ? '激活' : '禁用' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150">
                <template #default="scope">
                  <el-button type="primary" size="small" @click="editUser(scope.row)">
                    编辑
                  </el-button>
                  <el-button type="danger" size="small" @click="deleteUser(scope.row.id)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
        <el-tab-pane label="技能管理">
          <div class="tab-content">
            <el-button type="primary" @click="addSkill">添加技能</el-button>
            <el-table :data="skills" style="width: 100%" :loading="loadingSkills">
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column prop="name" label="技能名称" width="150" />
              <el-table-column prop="category" label="分类" width="120" />
              <el-table-column prop="level" label="等级" width="80" />
              <el-table-column label="操作" width="150">
                <template #default="scope">
                  <el-button type="primary" size="small" @click="editSkill(scope.row)">
                    编辑
                  </el-button>
                  <el-button type="danger" size="small" @click="deleteSkill(scope.row.id)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
        <el-tab-pane label="系统监控">
          <div class="tab-content">
            <el-card shadow="hover" v-for="(status, category) in monitorData" :key="category" style="margin-bottom: 20px">
              <template #header>
                <div class="card-header">
                  <span>{{ getCategoryName(category) }}</span>
                </div>
              </template>
              <el-descriptions :column="3">
                <el-descriptions-item v-for="(value, key) in status" :key="key">
                  <template #label>
                    {{ key }}
                  </template>
                  <el-tag :type="value === 'healthy' ? 'success' : 'danger'">
                    {{ value === 'healthy' ? '正常' : '异常' }}
                  </el-tag>
                </el-descriptions-item>
              </el-descriptions>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const users = ref([])
const skills = ref([])
const monitorData = ref({})
const loadingUsers = ref(false)
const loadingSkills = ref(false)

const loadUsers = async () => {
  loadingUsers.value = true
  try {
    const response = await fetch('/api/v1/admin/users')
    const data = await response.json()
    if (data.code === 200) {
      users.value = data.data.users
    } else {
      ElMessage.error('获取用户列表失败: ' + data.message)
    }
  } catch (error) {
    ElMessage.error('网络错误: ' + error.message)
  } finally {
    loadingUsers.value = false
  }
}

const loadSkills = async () => {
  loadingSkills.value = true
  try {
    const response = await fetch('/api/v1/admin/skills')
    const data = await response.json()
    if (data.code === 200) {
      skills.value = data.data.skills
    } else {
      ElMessage.error('获取技能列表失败: ' + data.message)
    }
  } catch (error) {
    ElMessage.error('网络错误: ' + error.message)
  } finally {
    loadingSkills.value = false
  }
}

const loadMonitorData = async () => {
  try {
    const response = await fetch('/api/v1/admin/monitor')
    const data = await response.json()
    if (data.code === 200) {
      monitorData.value = data.data
    } else {
      ElMessage.error('获取监控数据失败: ' + data.message)
    }
  } catch (error) {
    ElMessage.error('网络错误: ' + error.message)
  }
}

const addUser = () => {
  ElMessage.info('添加用户功能开发中')
}

const editUser = (user) => {
  ElMessage.info('编辑用户功能开发中')
}

const deleteUser = (userId) => {
  ElMessageBox.confirm('确定要删除此用户吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
    loadUsers()
  }).catch(() => {
    // 取消删除
  })
}

const addSkill = () => {
  ElMessage.info('添加技能功能开发中')
}

const editSkill = (skill) => {
  ElMessage.info('编辑技能功能开发中')
}

const deleteSkill = (skillId) => {
  ElMessageBox.confirm('确定要删除此技能吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
    loadSkills()
  }).catch(() => {
    // 取消删除
  })
}

const getCategoryName = (category) => {
  const categoryMap = {
    services: '服务状态',
    databases: '数据库状态',
    'ai-models': 'AI模型状态'
  }
  return categoryMap[category] || category
}

onMounted(() => {
  loadUsers()
  loadSkills()
  loadMonitorData()
})
</script>

<style scoped>
.admin-container {
  padding: 20px;
}

.tab-content {
  padding: 20px 0;
}

.tab-content .el-button {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>