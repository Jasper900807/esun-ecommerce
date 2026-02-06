import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

export const useCartStore = defineStore('cart', () => {
  // 狀態
  const cartItems = ref([]);
  const memberId = ref('12345'); // 預設會員編號（實際應該從登入取得）

  // 計算屬性：購物車總金額
  const totalPrice = computed(() => {
    return cartItems.value.reduce((total, item) => {
      return total + item.price * item.quantity;
    }, 0);
  });

  // 計算屬性：購物車商品數量
  const totalItems = computed(() => {
    return cartItems.value.reduce((total, item) => {
      return total + item.quantity;
    }, 0);
  });

  // 方法：加入購物車
  const addToCart = (product, quantity) => {
    // 檢查商品是否已在購物車
    const existingItem = cartItems.value.find(
      (item) => item.productId === product.productId
    );

    if (existingItem) {
      // 如果已存在，增加數量（不超過庫存）
      const newQuantity = existingItem.quantity + quantity;
      if (newQuantity <= product.quantity) {
        existingItem.quantity = newQuantity;
      } else {
        alert(`商品庫存不足！最多只能購買 ${product.quantity} 件`);
        existingItem.quantity = product.quantity;
      }
    } else {
      // 如果不存在，新增到購物車
      cartItems.value.push({
        productId: product.productId,
        productName: product.productName,
        price: product.price,
        quantity: quantity,
        maxQuantity: product.quantity, // 記錄最大庫存
      });
    }
  };

  // 方法：更新購物車商品數量
  const updateQuantity = (productId, quantity) => {
    const item = cartItems.value.find((item) => item.productId === productId);
    if (item) {
      if (quantity <= 0) {
        // 如果數量為 0，移除商品
        removeFromCart(productId);
      } else if (quantity <= item.maxQuantity) {
        item.quantity = quantity;
      } else {
        alert(`商品庫存不足！最多只能購買 ${item.maxQuantity} 件`);
        item.quantity = item.maxQuantity;
      }
    }
  };

  // 方法：從購物車移除商品
  const removeFromCart = (productId) => {
    const index = cartItems.value.findIndex(
      (item) => item.productId === productId
    );
    if (index > -1) {
      cartItems.value.splice(index, 1);
    }
  };

  // 方法：清空購物車
  const clearCart = () => {
    cartItems.value = [];
  };

  // 方法：取得訂單資料（用於提交給後端）
  const getOrderData = () => {
    return {
      memberId: memberId.value,
      items: cartItems.value.map((item) => ({
        productId: item.productId,
        quantity: item.quantity,
        price: item.price,
      })),
    };
  };

  return {
    // 狀態
    cartItems,
    memberId,
    // 計算屬性
    totalPrice,
    totalItems,
    // 方法
    addToCart,
    updateQuantity,
    removeFromCart,
    clearCart,
    getOrderData,
  };
});