import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'home',
    alias: '/home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/document',
    name: 'document',
    component: () => import('../views/Document.vue')
  },
  {
    path: '/graph',
    name: 'graph',
    component: () => import('../views/Graph.vue')
  },
  {
    path: '/match',
    name: 'match',
    component: () => import('../views/Match.vue')
  },
  {
    path: '/admin',
    name: 'admin',
    component: () => import('../views/Admin.vue')
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/Login.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router