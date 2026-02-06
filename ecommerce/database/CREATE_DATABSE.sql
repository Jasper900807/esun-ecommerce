-- ============================================
-- E.SUN Bank E-Commerce Database Schema
-- ============================================

-- 建立資料庫
CREATE DATABASE IF NOT EXISTS esun_ecommerce
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE esun_ecommerce;

-- ============================================
-- 刪除資料表（注意順序：先刪除子表，再刪除父表）
-- ============================================
DROP TABLE IF EXISTS order_detail;  -- 先刪除子表
DROP TABLE IF EXISTS `order`;       -- 再刪除父表
DROP TABLE IF EXISTS product;       -- 最後刪除

-- ============================================
-- 1. Product 商品表
-- ============================================
CREATE TABLE product (
    product_id VARCHAR(50) PRIMARY KEY COMMENT '商品編號',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名稱',
    price DECIMAL(10, 2) NOT NULL COMMENT '售價',
    quantity INT NOT NULL DEFAULT 0 COMMENT '庫存',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
    INDEX idx_quantity (quantity),
    INDEX idx_product_name (product_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品主檔';

-- ============================================
-- 2. Order 訂單表
-- ============================================
CREATE TABLE `order` (
    order_id VARCHAR(50) PRIMARY KEY COMMENT '訂單編號',
    member_id VARCHAR(50) NOT NULL COMMENT '會員編號',
    total_price DECIMAL(10, 2) NOT NULL COMMENT '訂單總金額',
    pay_status TINYINT NOT NULL DEFAULT 0 COMMENT '付款狀態(0:未付款, 1:已付款)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
    INDEX idx_member_id (member_id),
    INDEX idx_created_at (created_at),
    INDEX idx_pay_status (pay_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='訂單表';

-- ============================================
-- 3. OrderDetail 訂單明細表
-- ============================================
CREATE TABLE order_detail (
    order_item_sn BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '訂單明細流水號',
    order_id VARCHAR(50) NOT NULL COMMENT '訂單編號',
    product_id VARCHAR(50) NOT NULL COMMENT '商品編號',
    quantity INT NOT NULL COMMENT '數量',
    stand_price DECIMAL(10, 2) NOT NULL COMMENT '單價',
    item_price DECIMAL(10, 2) NOT NULL COMMENT '單品項總價',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
    FOREIGN KEY (order_id) REFERENCES `order`(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE RESTRICT,
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='訂單明細表';