import { createRouter, createWebHistory } from 'vue-router';
import ProductManagement from '../views/ProductManagement.vue';
import ProductList from '../views/ProductList.vue';
import OrderConfirmation from '../views/OrderConfirmation.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/products',
    },
    {
      path: '/management',
      name: 'ProductManagement',
      component: ProductManagement,
      meta: { title: '商品管理' },
    },
    {
      path: '/products',
      name: 'ProductList',
      component: ProductList,
      meta: { title: '商品列表' },
    },
    {
      path: '/order',
      name: 'OrderConfirmation',
      component: OrderConfirmation,
      meta: { title: '訂單確認' },
    },
  ],
});

// 路由守衛：設定頁面標題
router.beforeEach((to, from, next) => {
  document.title = to.meta.title || 'E.SUN E-Commerce';
  next();
});

export default router;