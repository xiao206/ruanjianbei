<template>
  <div class="app-container">
    <el-container>
      <el-header v-if="!isLoginPage">
        <div class="header-content">
          <h1 class="app-title">AI 智能匹配与能力图谱系统</h1>
          <el-menu :default-active="activeMenu" class="el-menu-demo" mode="horizontal" @select="handleMenuSelect">
            <el-menu-item index="home">首页</el-menu-item>
            <el-menu-item index="document">文档解析</el-menu-item>
            <el-menu-item index="graph">能力图谱</el-menu-item>
            <el-menu-item index="match">智能匹配</el-menu-item>
            <el-menu-item index="admin" v-if="isAdmin">后台管理</el-menu-item>
            <el-menu-item index="login" v-if="!isLoggedIn">登录</el-menu-item>
            <el-menu-item index="logout" v-if="isLoggedIn" @click="handleLogout">退出</el-menu-item>
          </el-menu>
        </div>
      </el-header>
      <el-main>
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const activeMenu = ref('home')
const isLoggedIn = ref(false)
const isAdmin = ref(false)

const isLoginPage = computed(() => route.path === '/login')

watch(() => route.path, (newPath) => {
  const path = newPath.substring(1) || 'home'
  activeMenu.value = path
})

const handleMenuSelect = (key) => {
  router.push(`/${key}`)
}

const handleLogout = () => {
  // 清除登录状态
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  isLoggedIn.value = false
  isAdmin.value = false
  router.push('/login')
}

// 检查登录状态
const checkLoginStatus = () => {
  const token = localStorage.getItem('token')
  if (token) {
    isLoggedIn.value = true
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    isAdmin.value = user.role === 'admin'
  }
}

checkLoginStatus()
</script>

<style>
.app-container {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  background-color: #409eff;
  color: white;
}

.app-title {
  margin: 0;
  font-size: 20px;
  font-weight: bold;
}

.el-menu-demo {
  border-bottom: none;
}

.el-menu-demo .el-menu-item {
  color: white;
}

.el-menu-demo .el-menu-item.is-active {
  background-color: rgba(255, 255, 255, 0.2);
  color: white;
}

.el-main {
  padding: 20px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>