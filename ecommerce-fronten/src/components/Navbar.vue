<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-primary sticky-top shadow">
    <div class="container">
      <router-link class="navbar-brand fw-bold" to="/products">
        🏦 E.SUN 購物中心
      </router-link>
      
      <button 
        class="navbar-toggler" 
        type="button" 
        data-bs-toggle="collapse" 
        data-bs-target="#navbarNav"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ms-auto">
          <li class="nav-item">
            <router-link class="nav-link" to="/products">
              <i class="bi bi-shop"></i> 商品列表
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/management">
              <i class="bi bi-gear"></i> 商品管理
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link position-relative" to="/order">
              <i class="bi bi-cart"></i> 購物車
              <span 
                v-if="totalItems > 0" 
                class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
              >
                {{ totalItems }}
              </span>
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { computed } from 'vue';
import { useCartStore } from '../stores/cart';

const cartStore = useCartStore();
const totalItems = computed(() => cartStore.totalItems);
</script>

<style scoped>
.navbar-brand {
  font-size: 1.5rem;
}

.nav-link {
  font-weight: 500;
  transition: all 0.3s ease;
}

.nav-link:hover {
  transform: translateY(-2px);
}
</style>