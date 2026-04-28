<template>
  <div class="login-container">
    <el-card shadow="hover" class="login-card">
      <template #header>
        <div class="card-header">
          <span>用户登录</span>
        </div>
      </template>
      <div class="login-form">
        <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" label-width="80px">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="loginForm.username" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleLogin" :loading="loading">登录</el-button>
          </el-form-item>
        </el-form>
        <div class="login-tips">
          <p>测试账号：</p>
          <p>管理员：admin / admin123</p>
          <p>普通用户：user1 / user123</p>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于 6 位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // Mock 登录逻辑
        await new Promise(resolve => setTimeout(resolve, 800))
        
        let mockUser = null
        if (loginForm.username === 'admin' && loginForm.password === 'admin123') {
          mockUser = { id: 1, username: 'admin', userType: 'admin', role: 'admin' }
        } else if (loginForm.username === 'user1' && loginForm.password === 'user123') {
          mockUser = { id: 2, username: 'user1', userType: 'user', role: 'user' }
        } else {
          throw new Error('用户名或密码错误 (请使用测试账号)')
        }

        localStorage.setItem('token', 'mock-token-' + Date.now())
        localStorage.setItem('user', JSON.stringify(mockUser))
        
        ElMessage.success('登录成功')
        // 强制触发事件让App.vue响应
        window.dispatchEvent(new Event('storage'))
        router.push('/')
      } catch (error) {
        ElMessage.error(error.message)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.login-card {
  width: 400px;
  padding: 20px;
}

.login-form {
  margin-top: 20px;
}

.login-tips {
  margin-top: 20px;
  padding: 10px;
  background-color: #f0f9ff;
  border: 1px solid #e1f5fe;
  border-radius: 4px;
  font-size: 14px;
  color: #0288d1;
}

.login-tips p {
  margin: 5px 0;
}
</style>