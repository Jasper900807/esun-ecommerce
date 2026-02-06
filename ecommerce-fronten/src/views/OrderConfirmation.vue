<template>
  <div class="container mt-4">
    <h2 class="mb-4">🛒 購物車</h2>

    <!-- 空購物車 -->
    <div v-if="cartItems.length === 0" class="text-center py-5">
      <i class="bi bi-cart-x" style="font-size: 5rem; color: #ccc;"></i>
      <h4 class="mt-3">購物車是空的</h4>
      <p class="text-muted">快去挑選喜歡的商品吧！</p>
      <router-link to="/products" class="btn btn-primary">
        <i class="bi bi-shop"></i> 前往商品列表
      </router-link>
    </div>

    <!-- 購物車內容 -->
    <div v-else class="row">
      <!-- 左側：商品列表 -->
      <div class="col-lg-8">
        <div class="card shadow-sm mb-4">
          <div class="card-header bg-light">
            <h5 class="mb-0">商品清單</h5>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-hover mb-0">
                <thead class="table-light">
                  <tr>
                    <th>商品名稱</th>
                    <th>單價</th>
                    <th style="width: 150px;">數量</th>
                    <th>小計</th>
                    <th style="width: 80px;">操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in cartItems" :key="item.productId">
                    <td>
                      <strong>{{ item.productName }}</strong>
                      <br />
                      <small class="text-muted">{{ item.productId }}</small>
                    </td>
                    <td class="align-middle">
                      NT$ {{ item.price.toLocaleString() }}
                    </td>
                    <td class="align-middle">
                      <div class="input-group input-group-sm">
                        <button 
                          class="btn btn-outline-secondary" 
                          type="button"
                          @click="updateQuantity(item.productId, item.quantity - 1)"
                        >
                          <i class="bi bi-dash"></i>
                        </button>
                        <input
                          type="number"
                          class="form-control text-center"
                          v-model.number="item.quantity"
                          @change="updateQuantity(item.productId, item.quantity)"
                          :max="item.maxQuantity"
                          min="1"
                        />
                        <button 
                          class="btn btn-outline-secondary" 
                          type="button"
                          @click="updateQuantity(item.productId, item.quantity + 1)"
                          :disabled="item.quantity >= item.maxQuantity"
                        >
                          <i class="bi bi-plus"></i>
                        </button>
                      </div>
                      <small class="text-muted">最多 {{ item.maxQuantity }}</small>
                    </td>
                    <td class="align-middle fw-bold text-primary">
                      NT$ {{ (item.price * item.quantity).toLocaleString() }}
                    </td>
                    <td class="align-middle">
                      <button 
                        class="btn btn-sm btn-danger"
                        @click="removeItem(item.productId)"
                      >
                        <i class="bi bi-trash"></i>
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <!-- 右側：訂單摘要 -->
      <div class="col-lg-4">
        <div class="card shadow-sm sticky-top" style="top: 100px;">
          <div class="card-header bg-primary text-white">
            <h5 class="mb-0">訂單摘要</h5>
          </div>
          <div class="card-body">
            <div class="mb-3">
              <label for="memberId" class="form-label">會員編號</label>
              <input
                type="text"
                class="form-control"
                id="memberId"
                v-model="cartStore.memberId"
                placeholder="請輸入會員編號"
              />
            </div>

            <hr />

            <div class="d-flex justify-content-between mb-2">
              <span>商品總數：</span>
              <span class="fw-bold">{{ totalItems }} 件</span>
            </div>

            <div class="d-flex justify-content-between mb-3">
              <span class="fs-5">訂單總金額：</span>
              <span class="fs-4 fw-bold text-primary">
                NT$ {{ totalPrice.toLocaleString() }}
              </span>
            </div>

            <hr />

            <!-- 建立訂單按鈕 -->
            <div class="d-grid gap-2">
              <button
                class="btn btn-success btn-lg"
                @click="createOrder"
                :disabled="loading || !cartStore.memberId"
              >
                <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
                {{ loading ? '處理中...' : '建立訂單' }}
              </button>
              <button
                class="btn btn-outline-secondary"
                @click="clearCart"
                :disabled="loading"
              >
                清空購物車
              </button>
            </div>

            <div v-if="!cartStore.memberId" class="alert alert-warning mt-3 mb-0" role="alert">
              <small><i class="bi bi-exclamation-triangle"></i> 請輸入會員編號</small>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 成功 Modal -->
    <div 
      class="modal fade" 
      id="successModal" 
      tabindex="-1" 
      ref="successModalEl"
    >
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header bg-success text-white">
            <h5 class="modal-title">
              <i class="bi bi-check-circle-fill"></i> 訂單建立成功
            </h5>
            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <div class="text-center py-3">
              <i class="bi bi-check-circle text-success" style="font-size: 4rem;"></i>
              <h4 class="mt-3">訂單已成功建立！</h4>
              <p class="text-muted">訂單編號：<strong>{{ orderResult?.orderId }}</strong></p>
              <p class="text-muted">訂單金額：<strong class="text-primary">NT$ {{ orderResult?.totalPrice?.toLocaleString() }}</strong></p>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">關閉</button>
            <router-link to="/products" class="btn btn-primary" data-bs-dismiss="modal">
              繼續購物
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- 錯誤 Modal -->
    <div 
      class="modal fade" 
      id="errorModal" 
      tabindex="-1" 
      ref="errorModalEl"
    >
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header bg-danger text-white">
            <h5 class="modal-title">
              <i class="bi bi-exclamation-triangle-fill"></i> 訂單建立失敗
            </h5>
            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <p>{{ errorMessage }}</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">關閉</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useCartStore } from '../stores/cart';
import { createOrder as createOrderAPI } from '../services/api';
import { Modal } from 'bootstrap';

const cartStore = useCartStore();

const cartItems = computed(() => cartStore.cartItems);
const totalPrice = computed(() => cartStore.totalPrice);
const totalItems = computed(() => cartStore.totalItems);

const loading = ref(false);
const errorMessage = ref('');
const orderResult = ref(null);
const successModalEl = ref(null);
const errorModalEl = ref(null);

// 更新數量
const updateQuantity = (productId, quantity) => {
  cartStore.updateQuantity(productId, quantity);
};

// 移除商品
const removeItem = (productId) => {
  if (confirm('確定要移除此商品嗎？')) {
    cartStore.removeFromCart(productId);
  }
};

// 清空購物車
const clearCart = () => {
  if (confirm('確定要清空購物車嗎？')) {
    cartStore.clearCart();
  }
};

// 建立訂單
const createOrder = async () => {
  if (!cartStore.memberId) {
    alert('請輸入會員編號');
    return;
  }

  loading.value = true;
  errorMessage.value = '';

  try {
    const orderData = cartStore.getOrderData();
    const response = await createOrderAPI(orderData);

    if (response.success) {
      orderResult.value = response.data;
      
      // 顯示成功 Modal
      const modal = new Modal(successModalEl.value);
      modal.show();

      // 清空購物車
      cartStore.clearCart();
    }
  } catch (error) {
    errorMessage.value = error.response?.data?.message || '訂單建立失敗，請稍後再試';
    
    // 顯示錯誤 Modal
    const modal = new Modal(errorModalEl.value);
    modal.show();
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.table th {
  font-weight: 600;
}

.input-group-sm .form-control {
  max-width: 70px;
}
</style>