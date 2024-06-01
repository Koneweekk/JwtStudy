import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const requireAuth = () => (from, to, next) => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    return next()
  } 
  next('/login'); 
}

const LoggedIn = () => (from, to, next) => {
  const token = localStorage.getItem('accessToken');
  if(!token) {
    return next()
  }
  next('/')
}

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      beforeEnter: requireAuth()
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/user/LoginView.vue'),
      beforeEnter: LoggedIn()
    },
    {
      path: '/signup',
      name: 'signup',
      component: () => import('@/views/user/SignupView.vue'),
      beforeEnter: LoggedIn()
    },
  ]
})

export default router
