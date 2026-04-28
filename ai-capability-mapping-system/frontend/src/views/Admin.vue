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
            <el-table :data="users" style="width: 100%" v-loading="loadingUsers">
            <template #empty>
              <el-empty description="暂无用户数据" />
            </template>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="username" label="用户名" width="150" />
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
    await new Promise(resolve => setTimeout(resolve, 600))
    users.value = [
      { id: 1, username: 'admin', name: '系统管理员', role: 'admin', status: 'active', createTime: '2026-04-28' },
      { id: 2, username: 'user1', name: '张三', role: 'user', status: 'active', createTime: '2026-04-28' },
      { id: 3, username: 'user2', name: '李四', role: 'user', status: 'inactive', createTime: '2026-04-28' }
    ]
  } catch (error) {
    ElMessage.error('加载用户失败')
  } finally {
    loadingUsers.value = false
  }
}

const loadSkills = async () => {
  loadingSkills.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 600))
    skills.value = [
      { id: 101, name: 'Java', category: '后端开发', level: '专家', description: '熟练掌握JVM、Spring框架等' },
      { id: 102, name: 'Vue 3', category: '前端开发', level: '高级', description: '熟练掌握Composition API等' },
      { id: 103, name: 'Python', category: '算法/后端', level: '中级', description: '熟悉基础语法与常用库' }
    ]
  } catch (error) {
    ElMessage.error('加载技能失败')
  } finally {
    loadingSkills.value = false
  }
}

const loadMonitorData = async () => {
  try {
    await new Promise(resolve => setTimeout(resolve, 600))
    monitorData.value = {
      services: [
        { name: 'Gateway Service', status: 'up' },
        { name: 'Auth Service', status: 'up' },
        { name: 'Document Service', status: 'up' },
        { name: 'Graph Service', status: 'up' }
      ],
      databases: [
        { name: 'MySQL / DM8', status: 'up' },
        { name: 'Neo4j', status: 'up' },
        { name: 'Milvus', status: 'up' },
        { name: 'Redis', status: 'up' }
      ],
      aiModels: [
        { name: 'Ollama (LLM)', status: 'up' },
        { name: 'BGE-M3 (Embedding)', status: 'up' }
      ]
    }
  } catch (error) {
    ElMessage.error('加载监控数据失败')
  }
}

const addUser = () => {
  ElMessageBox.prompt('请输入新用户名', '添加用户', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
  }).then(({ value }) => {
    if (value) {
      users.value.push({
        id: users.value.length + 1,
        username: value,
        name: '测试用户',
        role: 'user',
        status: 'active'
      })
      ElMessage.success('用户添加成功 (Mock)')
    }
  }).catch(() => {})
}

const editUser = (user) => {
  ElMessageBox.prompt('修改用户名', '编辑用户', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputValue: user.username
  }).then(({ value }) => {
    if (value) {
      const target = users.value.find(u => u.id === user.id)
      if (target) target.username = value
      ElMessage.success('用户信息已更新 (Mock)')
    }
  }).catch(() => {})
}

const deleteUser = (userId) => {
  ElMessageBox.confirm(`确定要删除此用户吗？`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    users.value = users.value.filter(u => u.id !== userId)
    ElMessage.success('用户已删除 (Mock)')
  }).catch(() => {})
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