import axios from 'axios';

// 建立 Axios 實例
const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api', // 後端 API 位址
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 10 秒超時
});

// 請求攔截器（未實作）
apiClient.interceptors.request.use(
  (config) => {
    // 可以在這裡加入 JWT Token
    // const token = localStorage.getItem('token');
    // if (token) {
    //   config.headers.Authorization = `Bearer ${token}`;
    // }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 回應攔截器（統一處理錯誤）
apiClient.interceptors.response.use(
  (response) => {
    return response.data; // 直接回傳 data（已經是 ApiResponse 格式）
  },
  (error) => {
    // 統一錯誤處理
    const message = error.response?.data?.message || '系統錯誤，請稍後再試';
    console.error('API Error:', message);
    return Promise.reject(error);
  }
);

// ========== 商品 API ==========

/**
 * 新增商品
 */
export const createProduct = (productData) => {
  return apiClient.post('/products', productData);
};

/**
 * 查詢所有可用商品（庫存 > 0）
 */
export const getAvailableProducts = () => {
  return apiClient.get('/products/available');
};

/**
 * 查詢所有商品
 */
export const getAllProducts = () => {
  return apiClient.get('/products');
};

/**
 * 根據商品編號查詢商品
 */
export const getProductById = (productId) => {
  return apiClient.get(`/products/${productId}`);
};

// ========== 訂單 API ==========

/**
 * 建立訂單
 */
export const createOrder = (orderData) => {
  return apiClient.post('/orders', orderData);
};

/**
 * 查詢所有訂單
 */
export const getAllOrders = () => {
  return apiClient.get('/orders');
};

/**
 * 根據訂單編號查詢訂單
 */
export const getOrderById = (orderId) => {
  return apiClient.get(`/orders/${orderId}`);
};

/**
 * 根據會員編號查詢訂單
 */
export const getOrdersByMemberId = (memberId) => {
  return apiClient.get(`/orders/member/${memberId}`);
};

export default apiClient;