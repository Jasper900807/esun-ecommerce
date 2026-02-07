# E.SUN Bank E-Commerce System - Frontend

玉山銀行後端工程師實作題 - 電商購物中心系統前端

## 📋 專案說明

這是電商購物中心系統的前端應用，使用 Vue.js 3 + Bootstrap 5 開發。

## 🛠️ 技術棧

- **Framework**: Vue.js 3
- **Build Tool**: Vite
- **UI Framework**: Bootstrap 5
- **HTTP Client**: Axios
- **State Management**: Pinia
- **Router**: Vue Router

## ✨ 功能特色

### 1. 商品管理
- 管理員新增商品
- 即時顯示新增結果
- 表單驗證

### 2. 商品列表
- 瀏覽可用商品（庫存 > 0）
- 選擇購買數量
- 加入購物車
- Toast 通知

### 3. 購物車
- 查看已選商品
- 調整購買數量
- 移除商品
- 顯示訂單總金額
- 建立訂單
- 成功/失敗 Modal

## 🚀 快速開始

### 1. 環境要求

- Node.js 18+
- npm 9+

### 2. 安裝依賴

```bash
npm install
```

### 3. 啟動開發伺服器

```bash
npm run dev
```

應用將啟動在 `http://localhost:5173`

### 4. 打包生產版本

```bash
npm run build
```

打包檔案會在 `dist/` 目錄

## 📂 專案結構

```
src/
├── components/              # 元件
│   └── Navbar.vue          # 導航列
├── views/                   # 頁面
│   ├── ProductManagement.vue  # 商品管理
│   ├── ProductList.vue        # 商品列表
│   └── OrderConfirmation.vue  # 購物車
├── stores/                  # 狀態管理
│   └── cart.js             # 購物車 Store
├── services/                # API 服務
│   └── api.js              # API 封裝
├── router/                  # 路由
│   └── index.js            # 路由配置
├── App.vue                  # 主元件
└── main.js                  # 入口檔案
```

## 🌐 頁面路由

| 路徑 | 頁面 | 說明 |
|------|------|------|
| / | 重導向 | 導向商品列表 |
| /products | ProductList | 商品列表 |
| /management | ProductManagement | 商品管理 |
| /order | OrderConfirmation | 購物車 |

## 🔧 API 配置

### 後端 API 位址

預設為 `http://localhost:8080/api`

如需修改，請編輯 `src/services/api.js`：

```javascript
const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api', // 修改這裡
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});
```

## 🎨 UI 組件

使用 Bootstrap 5 的組件：
- Card
- Button
- Form
- Table
- Modal
- Toast
- Badge
- Navbar
- Spinner

## 🛒 購物車功能

### Pinia Store

使用 Pinia 管理購物車狀態：

```javascript
// 加入購物車
cartStore.addToCart(product, quantity);

// 更新數量
cartStore.updateQuantity(productId, quantity);

// 移除商品
cartStore.removeFromCart(productId);

// 清空購物車
cartStore.clearCart();

// 取得訂單資料
const orderData = cartStore.getOrderData();
```

### 狀態管理

```javascript
// 購物車商品
cartItems: []

// 會員編號
memberId: '12345'

// 計算屬性
totalPrice: 購物車總金額
totalItems: 購物車商品總數
```

## 🔌 API 服務

### 商品 API

```javascript
// 新增商品
createProduct(productData)

// 查詢可用商品
getAvailableProducts()

// 查詢所有商品
getAllProducts()

// 查詢單一商品
getProductById(productId)
```

### 訂單 API

```javascript
// 建立訂單
createOrder(orderData)

// 查詢所有訂單
getAllOrders()

// 查詢單一訂單
getOrderById(orderId)

// 查詢會員訂單
getOrdersByMemberId(memberId)
```

## 🎯 使用流程

### 1. 新增商品（管理員）

1. 點擊「商品管理」
2. 填寫商品資訊
3. 點擊「新增商品」
4. 查看成功訊息

### 2. 瀏覽商品（顧客）

1. 進入「商品列表」
2. 瀏覽可用商品
3. 選擇購買數量
4. 點擊「加入購物車」

### 3. 建立訂單

1. 點擊「購物車」
2. 確認商品和數量
3. 輸入會員編號
4. 點擊「建立訂單」
5. 查看訂單成功訊息

## 🐛 故障排除

### 常見問題

**Q: 無法連接後端 API？**
- 確認後端已啟動（http://localhost:8080）
- 檢查 CORS 設定
- 查看瀏覽器 Console

**Q: 購物車狀態丟失？**
- Pinia Store 存在記憶體中
- 重新整理頁面會清空購物車
- 可考慮使用 localStorage 持久化

**Q: Bootstrap 樣式沒有生效？**
- 確認 main.js 有引入 Bootstrap CSS
- 檢查 index.html 有引入 Bootstrap Icons

## 📦 依賴項目

主要依賴：
- vue: ^3.4.0
- vue-router: ^4.2.5
- pinia: ^2.1.7
- axios: ^1.6.2
- bootstrap: ^5.3.2
- @popperjs/core: ^2.11.8

開發依賴：
- vite: ^5.0.0
- @vitejs/plugin-vue: ^5.0.0

## 🚀 部署

### 靜態網站部署

```bash
# 打包
npm run build

# dist/ 資料夾可以部署到任何靜態網站服務
# 例如：Netlify, Vercel, GitHub Pages
```

### Nginx 配置範例

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    root /var/www/ecommerce-frontend/dist;
    index index.html;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
}
```

## 🎨 自訂樣式

### 修改主題色

編輯 `src/App.vue` 或使用 Bootstrap 的 SCSS 變數：

```scss
$primary: #667eea;
$secondary: #764ba2;
```

### 商品管理頁面
- 表單輸入
- 即時驗證
- 成功訊息

### 商品列表頁面
- 商品卡片
- 加入購物車
- Toast 通知

### 購物車頁面
- 商品清單
- 數量調整
- 訂單摘要

## 👨‍💻 作者

**Jasper**
- 玉山銀行後端工程師實作題

## 📄 授權

本專案僅供玉山銀行面試使用，未經授權嚴禁外流。
