# E.SUN Bank E-Commerce System

玉山銀行後端工程師實作題 - 電商購物中心系統

## 📋 專案簡介

這是一個完整的電商購物中心系統，包含前端 Vue.js 應用和後端 Spring Boot API，實作了商品管理、訂單管理、購物車等核心功能。

## 🎯 實作要求完成度

### ✅ 核心功能

- [x] 新增商品功能
- [x] 新增訂單功能（含庫存扣減）
- [x] 顯示庫存大於零的商品清單
- [x] 顧客可勾選多個商品並設定購買數量
- [x] 購買數量不能大於庫存量
- [x] 顯示訂單內容（各商品金額小計及訂單總金額）
- [x] 建立訂單後更新商品庫存

### ✅ 系統架構

- [x] Web Server + Application Server + 關聯式資料庫的三層式架構
- [x] 展示層（Controller）
- [x] 業務層（Service）
- [x] 資料層（Repository）
- [x] 共用層（DTO、Exception、Util）

### ✅ 技術要求

- [x] 使用 Vue.js 做為前端技術
- [x] 使用 Spring Boot 搭建應用程式
- [x] 使用 RESTful API 風格
- [x] 使用 Maven 建立專案
- [x] 透過 Stored Procedure 存取資料庫
- [x] 實作 Transaction 避免資料錯亂
- [x] DDL 和 DML 存放在 DB 資料夾
- [x] 防止 SQL Injection
- [x] 防止 XSS 攻擊

## 🏗️ 系統架構

```
┌─────────────────┐
│   前端 (Vue.js)  │  http://localhost:5173
│   - 商品管理     │
│   - 商品列表     │
│   - 購物車       │
└────────┬────────┘
         │ HTTP/REST API
         │
┌────────▼────────┐
│  後端 (Spring)   │  http://localhost:8080
│   - Controller   │
│   - Service      │
│   - Repository   │
└────────┬────────┘
         │ JDBC + SP
         │
┌────────▼────────┐
│  資料庫 (MySQL)  │  localhost:3306
│   - product      │
│   - order        │
│   - order_detail │
└─────────────────┘
```

## 📁 專案結構

```
esun-ecommerce/
├── backend/                      # 後端 Spring Boot
│   ├── src/main/java/
│   │   └── com/esun/ecommerce/
│   │       ├── config/          # 配置類
│   │       ├── controller/      # 控制器
│   │       ├── service/         # 業務邏輯
│   │       ├── repository/      # 資料訪問
│   │       ├── entity/          # 實體類
│   │       ├── dto/             # 數據傳輸對象
│   │       ├── exception/       # 異常處理
│   │       └── util/            # 工具類
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── DB/                      # 資料庫腳本
│   │   ├── 01_DDL.sql
│   │   ├── 02_DML.sql
│   │   └── 03_StoredProcedures.sql
│   ├── pom.xml
│   └── README.md
│
└── frontend/                     # 前端 Vue.js
    ├── src/
    │   ├── components/          # 元件
    │   ├── views/               # 頁面
    │   ├── stores/              # 狀態管理
    │   ├── services/            # API 服務
    │   ├── router/              # 路由
    │   ├── App.vue
    │   └── main.js
    ├── package.json
    └── README.md
```

## 🚀 快速開始

### 前置要求

- JDK 17+
- Maven 3.6+
- Node.js 18+
- MySQL 8.0+

### 1. 資料庫設置

```bash
# 登入 MySQL
mysql -u root -p

# 執行資料庫腳本
source backend/DB/01_DDL.sql
source backend/DB/02_DML.sql
source backend/DB/03_StoredProcedures.sql
```

### 2. 啟動後端

```bash
cd backend
mvn spring-boot:run
```

後端將啟動在 `http://localhost:8080`

### 3. 啟動前端

```bash
cd frontend
npm install
npm run dev
```

前端將啟動在 `http://localhost:5173`

### 4. 訪問應用

開啟瀏覽器訪問：`http://localhost:5173`

## 📡 API 文檔

### 主要端點

#### 商品 API
- `POST /api/products` - 新增商品
- `GET /api/products/available` - 查詢可用商品
- `GET /api/products/{id}` - 查詢單一商品

#### 訂單 API
- `POST /api/orders` - 建立訂單
- `GET /api/orders/{id}` - 查詢訂單詳情
- `GET /api/orders/member/{memberId}` - 查詢會員訂單

## 🔒 安全特性

### SQL Injection 防護
- ✅ JPA Prepared Statement
- ✅ Stored Procedure 參數化查詢
- ✅ Bean Validation 輸入驗證

### XSS 防護
- ✅ OWASP HTML Sanitizer
- ✅ 全域 XSS 過濾器
- ✅ JSON 反序列化清理

### Transaction 管理
- ✅ Service 層 @Transactional
- ✅ Stored Procedure 內建 Transaction
- ✅ 異常自動 Rollback

## 🗄️ 資料庫設計

### ER Diagram

```
┌─────────────┐
│   product   │
├─────────────┤
│ product_id  │ PK
│ product_name│
│ price       │
│ quantity    │
└──────┬──────┘
       │
       │ 1:N
       │
┌──────▼──────────┐
│  order_detail   │
├─────────────────┤
│ order_item_sn   │ PK
│ order_id        │ FK
│ product_id      │ FK
│ quantity        │
│ stand_price     │
│ item_price      │
└──────┬──────────┘
       │
       │ N:1
       │
┌──────▼──────┐
│    order    │
├─────────────┤
│ order_id    │ PK
│ member_id   │
│ total_price │
│ pay_status  │
└─────────────┘
```

## 🧪 測試

### 手動測試流程

1. 進入「商品管理」新增商品
2. 進入「商品列表」瀏覽商品
3. 選擇商品並加入購物車
4. 進入「購物車」確認訂單
5. 輸入會員編號並建立訂單
6. 確認訂單建立成功
7. 檢查商品庫存已扣減

### API 測試

使用 Postman 測試 API 端點

## 📝 開發紀錄

### 技術決策

1. **為何使用 Stored Procedure？**
   - 符合題目要求
   - 提供額外的安全層
   - 資料庫層面的 Transaction 控制

2. **為何使用 Pinia 而非 Vuex？**
   - Vue 3 官方推薦
   - 更簡潔的 API
   - 更好的 TypeScript 支援

3. **為何使用 Bootstrap 而非手刻 CSS？**
   - 快速開發
   - 響應式設計
   - 一致的 UI 組件

### 遇到的挑戰與解決

1. **XSS 過濾器無效**
   - 問題：只過濾 URL 參數，未過濾 JSON Body
   - 解決：實作 Jackson 自訂 Deserializer

2. **LocalDateTime 序列化錯誤**
   - 問題：自訂 ObjectMapper 覆蓋預設配置
   - 解決：註冊 JavaTimeModule

3. **CORS 跨域問題**
   - 問題：前後端分離導致跨域請求被阻擋
   - 解決：實作 CorsConfig 允許前端 URL

## 🎯 未來改進

### 功能擴展
- [ ] 會員登入系統（JWT Token）
- [ ] 訂單狀態管理（待付款、已付款、已出貨）
- [ ] 商品分類與搜尋
- [ ] 訂單歷史查詢
- [ ] 購物車持久化（localStorage）

### 技術優化
- [ ] Redis 快取
- [ ] 分頁查詢
- [ ] 圖片上傳
- [ ] 單元測試
- [ ] Docker 容器化

## 📚 參考文件

- [Spring Boot 官方文檔](https://spring.io/projects/spring-boot)
- [Vue.js 官方文檔](https://vuejs.org/)
- [Bootstrap 5 文檔](https://getbootstrap.com/)
- [OWASP 安全指南](https://owasp.org/)

## 👨‍💻 作者

**Jasper**
- 玉山銀行後端工程師實作題
- GitHub: https://github.com/Jasper900807
- Email: jasper900807@gmail.com

## 📄 授權

本專案僅供玉山銀行面試使用，未經授權嚴禁外流。

## 🙏 致謝

感謝玉山銀行提供此次實作機會，讓我能夠展示技術能力並學習成長。
