<template>
  <div class="container mt-4">
    <div class="row justify-content-center">
      <div class="col-lg-8">
        <div class="card shadow">
          <div class="card-header bg-primary text-white">
            <h3 class="mb-0">⚙️ 新增商品</h3>
          </div>
          <div class="card-body">
            <!-- 成功訊息 -->
            <div v-if="successMessage" class="alert alert-success alert-dismissible fade show" role="alert">
              <i class="bi bi-check-circle-fill"></i> {{ successMessage }}
              <button type="button" class="btn-close" @click="successMessage = ''"></button>
            </div>

            <!-- 錯誤訊息 -->
            <div v-if="errorMessage" class="alert alert-danger alert-dismissible fade show" role="alert">
              <i class="bi bi-exclamation-triangle-fill"></i> {{ errorMessage }}
              <button type="button" class="btn-close" @click="errorMessage = ''"></button>
            </div>

            <!-- 新增商品表單 -->
            <form @submit.prevent="handleSubmit">
              <div class="mb-3">
                <label for="productId" class="form-label">商品編號 *</label>
                <input
                  type="text"
                  class="form-control"
                  id="productId"
                  v-model="formData.productId"
                  placeholder="例如：P001"
                  required
                />
                <div class="form-text">格式：P 開頭加數字，例如 P001</div>
              </div>

              <div class="mb-3">
                <label for="productName" class="form-label">商品名稱 *</label>
                <input
                  type="text"
                  class="form-control"
                  id="productName"
                  v-model="formData.productName"
                  placeholder="請輸入商品名稱"
                  required
                />
              </div>

              <div class="mb-3">
                <label for="price" class="form-label">售價 (元) *</label>
                <input
                  type="number"
                  class="form-control"
                  id="price"
                  v-model.number="formData.price"
                  placeholder="請輸入售價"
                  step="0.01"
                  min="0.01"
                  required
                />
              </div>

              <div class="mb-3">
                <label for="quantity" class="form-label">庫存數量 *</label>
                <input
                  type="number"
                  class="form-control"
                  id="quantity"
                  v-model.number="formData.quantity"
                  placeholder="請輸入庫存數量"
                  min="0"
                  required
                />
              </div>

              <div class="d-grid gap-2">
                <button type="submit" class="btn btn-primary btn-lg" :disabled="loading">
                  <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
                  {{ loading ? '新增中...' : '新增商品' }}
                </button>
              </div>
            </form>
          </div>
        </div>

        <!-- 已新增的商品列表 -->
        <div v-if="addedProducts.length > 0" class="card shadow mt-4">
          <div class="card-header bg-success text-white">
            <h5 class="mb-0">✅ 已新增的商品</h5>
          </div>
          <div class="card-body">
            <div class="list-group">
              <div 
                v-for="product in addedProducts" 
                :key="product.productId"
                class="list-group-item"
              >
                <div class="d-flex justify-content-between align-items-center">
                  <div>
                    <h6 class="mb-1">{{ product.productName }}</h6>
                    <small class="text-muted">
                      商品編號：{{ product.productId }} | 
                      售價：NT$ {{ product.price.toLocaleString() }} | 
                      庫存：{{ product.quantity }}
                    </small>
                  </div>
                  <span class="badge bg-success">已新增</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { createProduct } from '../services/api';

const formData = ref({
  productId: '',
  productName: '',
  price: null,
  quantity: null,
});

const loading = ref(false);
const successMessage = ref('');
const errorMessage = ref('');
const addedProducts = ref([]);

const handleSubmit = async () => {
  loading.value = true;
  successMessage.value = '';
  errorMessage.value = '';

  try {
    const response = await createProduct(formData.value);
    
    if (response.success) {
      successMessage.value = response.message;
      addedProducts.value.unshift(response.data);
      
      // 清空表單
      formData.value = {
        productId: '',
        productName: '',
        price: null,
        quantity: null,
      };
    }
  } catch (error) {
    errorMessage.value = error.response?.data?.message || '新增商品失敗，請稍後再試';
  } finally {
    loading.value = false;
  }
};
</script>