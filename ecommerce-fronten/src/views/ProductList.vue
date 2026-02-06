<template>
  <div class="container mt-4">
    <h2 class="mb-4">🛍️ 商品列表</h2>

    <!-- Loading -->
    <div v-if="loading" class="text-center py-5">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">載入中...</span>
      </div>
      <p class="mt-3">載入商品中...</p>
    </div>

    <!-- 錯誤訊息 -->
    <div v-else-if="errorMessage" class="alert alert-danger" role="alert">
      <i class="bi bi-exclamation-triangle-fill"></i> {{ errorMessage }}
    </div>

    <!-- 商品列表 -->
    <div v-else-if="products.length > 0" class="row g-4">
      <div 
        v-for="product in products" 
        :key="product.productId"
        class="col-md-6 col-lg-4"
      >
        <div class="card h-100 shadow-sm">
          <div class="card-body">
            <h5 class="card-title">{{ product.productName }}</h5>
            <p class="card-text text-muted">商品編號：{{ product.productId }}</p>
            <div class="d-flex justify-content-between align-items-center mb-3">
              <span class="fs-4 text-primary fw-bold">
                NT$ {{ product.price.toLocaleString() }}
              </span>
              <span class="badge bg-info">
                庫存：{{ product.quantity }}
              </span>
            </div>

            <!-- 數量選擇 -->
            <div class="input-group mb-3">
              <span class="input-group-text">數量</span>
              <input
                type="number"
                class="form-control"
                v-model.number="quantities[product.productId]"
                :max="product.quantity"
                min="1"
              />
            </div>

            <!-- 加入購物車按鈕 -->
            <button
              class="btn btn-primary w-100"
              @click="addToCart(product)"
              :disabled="!quantities[product.productId] || quantities[product.productId] < 1"
            >
              <i class="bi bi-cart-plus"></i> 加入購物車
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 無商品 -->
    <div v-else class="alert alert-info text-center" role="alert">
      <i class="bi bi-info-circle-fill"></i> 目前沒有可用的商品
    </div>

    <!-- Toast 通知 -->
    <div class="toast-container position-fixed bottom-0 end-0 p-3">
      <div 
        ref="toastEl"
        class="toast align-items-center text-white bg-success border-0" 
        role="alert"
      >
        <div class="d-flex">
          <div class="toast-body">
            <i class="bi bi-check-circle-fill"></i> {{ toastMessage }}
          </div>
          <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useCartStore } from '../stores/cart';
import { getAvailableProducts } from '../services/api';
import { Toast } from 'bootstrap';

const router = useRouter();
const cartStore = useCartStore();

const products = ref([]);
const quantities = ref({});
const loading = ref(false);
const errorMessage = ref('');
const toastMessage = ref('');
const toastEl = ref(null);

// 載入商品
const loadProducts = async () => {
  loading.value = true;
  errorMessage.value = '';

  try {
    const response = await getAvailableProducts();
    
    if (response.success) {
      products.value = response.data;
      
      // 初始化每個商品的數量為 1
      products.value.forEach((product) => {
        quantities.value[product.productId] = 1;
      });
    }
  } catch (error) {
    errorMessage.value = error.response?.data?.message || '載入商品失敗，請稍後再試';
  } finally {
    loading.value = false;
  }
};

// 加入購物車
const addToCart = (product) => {
  const quantity = quantities.value[product.productId];
  
  if (quantity > product.quantity) {
    alert(`商品庫存不足！最多只能購買 ${product.quantity} 件`);
    return;
  }

  cartStore.addToCart(product, quantity);
  
  // 顯示 Toast 通知
  toastMessage.value = `已加入 ${product.productName} x ${quantity} 到購物車`;
  const toast = new Toast(toastEl.value);
  toast.show();
  
  // 重置數量為 1
  quantities.value[product.productId] = 1;
};

onMounted(() => {
  loadProducts();
});
</script>