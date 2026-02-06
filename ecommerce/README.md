# E.SUN Bank E-Commerce System - Backend

玉山銀行後端工程師實作題 - 電商購物中心系統後端

## 📋 專案說明

這是一個電商購物中心系統的後端 API，使用 Spring Boot 開發，提供商品管理和訂單管理功能。

## 🛠️ 技術棧

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: MySQL 8.0+
- **ORM**: Spring Data JPA + Hibernate
- **API Style**: RESTful API
- **Build Tool**: Maven

## ✨ 核心功能

### 1. 商品管理
- 新增商品
- 查詢可用商品（庫存 > 0）
- 查詢所有商品
- 根據商品編號查詢

### 2. 訂單管理
- 建立訂單
- 自動扣減庫存
- 查詢訂單詳情
- 查詢會員訂單

### 3. 安全防護
- **SQL Injection 防護**: JPA Prepared Statement + Stored Procedure 參數化查詢
- **XSS 防護**: OWASP HTML Sanitizer
- **Transaction 管理**: @Transactional + SP 內建 Transaction
- **參數驗證**: Bean Validation

## 📂 專案結構

```
src/main/java/com/esun/ecommerce/
├── config/                    # 配置類
│   ├── CorsConfig.java       # CORS 跨域設定
│   ├── JacksonConfig.java    # JSON 序列化設定
│   └── WebSecurityConfig.java # 安全配置
├── controller/                # 控制器層
│   ├── ProductController.java
│   └── OrderController.java
├── service/                   # 業務邏輯層
│   ├── ProductService.java
│   └── OrderService.java
├── repository/                # 數據訪問層
│   ├── ProductRepository.java
│   ├── OrderRepository.java
│   └── OrderDetailRepository.java
├── entity/                    # 實體類
│   ├── Product.java
│   ├── Order.java
│   └── OrderDetail.java
├── dto/                       # 數據傳輸對象
│   ├── request/
│   │   ├── CreateProductRequest.java
│   │   ├── CreateOrderRequest.java
│   │   └── OrderItemRequest.java
│   └── response/
│       ├── ApiResponse.java
│       ├── ProductResponse.java
│       └── OrderResponse.java
├── exception/                 # 異常處理
│   ├── BusinessException.java
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java
└── util/                      # 工具類
    ├── XssFilter.java
    └── XssRequestWrapper.java

DB/                            # 資料庫腳本
├── 01_DDL.sql                # 資料表結構
├── 02_DML.sql                # 測試資料
└── 03_StoredProcedures.sql   # 存儲過程
```

## 🚀 快速開始

### 1. 環境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 2. 資料庫設置

```bash
# 登入 MySQL
mysql -u root -p

# 執行資料庫腳本
source DB/01_DDL.sql
source DB/02_DML.sql
source DB/03_StoredProcedures.sql
```

### 3. 配置應用

修改 `src/main/resources/application.properties`：

```properties
# 資料庫配置
spring.datasource.url=jdbc:mysql://localhost:3306/esun_ecommerce
spring.datasource.username=root
spring.datasource.password=your_password
```

### 4. 啟動應用

```bash
# 使用 Maven
mvn spring-boot:run

# 或打包後執行
mvn clean package
java -jar target/ecommerce-1.0.0.jar
```

應用將啟動在 `http://localhost:8080`

## 📡 API 文檔

### API 端點

#### 商品 API

| 方法 | 路徑 | 說明 |
|------|------|------|
| POST | /api/products | 新增商品 |
| GET | /api/products | 查詢所有商品 |
| GET | /api/products/available | 查詢可用商品 |
| GET | /api/products/{productId} | 查詢單一商品 |

#### 訂單 API

| 方法 | 路徑 | 說明 |
|------|------|------|
| POST | /api/orders | 建立訂單 |
| GET | /api/orders | 查詢所有訂單 |
| GET | /api/orders/{orderId} | 查詢單一訂單 |
| GET | /api/orders/member/{memberId} | 查詢會員訂單 |

## 🧪 API 測試範例

### 新增商品

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "productId": "P004",
    "productName": "iPhone 15 Pro",
    "price": 36900.00,
    "quantity": 10
  }'
```

### 建立訂單

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "memberId": "12345",
    "items": [
      {
        "productId": "P001",
        "quantity": 1,
        "price": 98000.00
      }
    ]
  }'
```

## 🔒 安全特性

### SQL Injection 防護
- JPA 使用 Prepared Statement
- Stored Procedure 參數化查詢
- Bean Validation 參數驗證

### XSS 防護
- 使用 OWASP Java HTML Sanitizer
- 全域 XSS 過濾器
- JSON 反序列化時自動清理

### Transaction 管理
- Service 層使用 @Transactional
- Stored Procedure 內建 Transaction
- 異常自動 Rollback

## 🗄️ 資料庫設計

### 資料表

#### product (商品表)
- product_id - 商品編號 (PK)
- product_name - 商品名稱
- price - 售價
- quantity - 庫存
- created_at - 建立時間
- updated_at - 更新時間

#### order (訂單表)
- order_id - 訂單編號 (PK)
- member_id - 會員編號
- total_price - 訂單總金額
- pay_status - 付款狀態
- created_at - 建立時間
- updated_at - 更新時間

#### order_detail (訂單明細表)
- order_item_sn - 訂單明細流水號 (PK, Auto Increment)
- order_id - 訂單編號 (FK)
- product_id - 商品編號 (FK)
- quantity - 數量
- stand_price - 單價
- item_price - 小計
- created_at - 建立時間

### Stored Procedures

- sp_add_product - 新增商品
- sp_get_available_products - 查詢可用商品
- sp_get_product_by_id - 查詢單一商品
- sp_update_inventory - 更新庫存
- sp_create_order - 建立訂單（核心 Transaction）
- sp_get_order_detail - 查詢訂單詳情
- sp_get_all_orders - 查詢所有訂單

## 📝 開發注意事項

### 三層式架構
- **展示層 (Controller)**: 處理 HTTP 請求
- **業務層 (Service)**: 業務邏輯處理
- **資料層 (Repository)**: 數據訪問

### Transaction 最佳實踐
- 在 Service 層加上 @Transactional
- 設定 rollbackFor = Exception.class
- SP 內部也實作 Transaction

### 異常處理
- 使用 GlobalExceptionHandler 統一處理
- 回傳標準 ApiResponse 格式
- 記錄詳細錯誤日誌

## 🐛 故障排除

### 常見問題

**Q: 資料庫連接失敗？**
- 檢查 MySQL 是否啟動
- 檢查 application.properties 的帳密設定
- 確認資料庫 esun_ecommerce 已建立

**Q: Stored Procedure 執行失敗？**
- 確認已執行 03_StoredProcedures.sql
- 檢查 MySQL 權限
- 查看 Spring Boot 日誌

**Q: CORS 錯誤？**
- 檢查 CorsConfig.java 的 allowedOrigins
- 確認前端 URL 已加入白名單

## 📦 依賴項目

主要依賴：
- Spring Boot Web
- Spring Data JPA
- MySQL Connector
- Lombok
- Bean Validation
- OWASP HTML Sanitizer
- Springdoc OpenAPI

## 👨‍💻 作者

**Jasper**
- 玉山銀行後端工程師實作題

## 📄 授權

本專案僅供玉山銀行面試使用，未經授權嚴禁外流。
