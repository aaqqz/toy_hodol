import { createRouter, createWebHistory } from 'vue-router'
import WriteView from '../views/WriteView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/write',
      name: 'write',
      component: WriteView,
    }
  ],
})

export default router
