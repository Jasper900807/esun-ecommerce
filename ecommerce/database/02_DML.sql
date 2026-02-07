-- ============================================
-- E.SUN Bank E-Commerce Test Data
-- ============================================

USE esun_ecommerce;

-- 清空資料
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE order_detail;
TRUNCATE TABLE `order`;
TRUNCATE TABLE product;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 插入商品測試資料
-- ============================================
INSERT INTO product (product_id, product_name, price, quantity) VALUES
('P001', 'osii 舒壓按摩椅', 98000.00, 5),
('P002', '網友最愛起司蛋糕', 1200.00, 50),
('P003', '真愛密碼項鍊', 8500.00, 20);

-- ============================================
-- 插入訂單測試資料
-- ============================================
INSERT INTO `order` (order_id, member_id, total_price, pay_status) VALUES
('Ms20250801186230', '458', 98000.00, 1),
('Ms20250805157824', '55688', 9700.00, 0),
('Ms20250805258200', '1713', 2400.00, 1);

-- ============================================
-- 插入訂單明細測試資料
-- ============================================
INSERT INTO order_detail (order_item_sn, order_id, product_id, quantity, stand_price, item_price) VALUES
(1, 'Ms20250801186230', 'P001', 1, 98000.00, 98000.00),
(2, 'Ms20250805157824', 'P002', 1, 1200.00, 1200.00),
(3, 'Ms20250805157824', 'P003', 1, 8500.00, 8500.00),
(4, 'Ms20250805258200', 'P002', 2, 1200.00, 2400.00);

-- ============================================
-- 驗證資料
-- ============================================
SELECT '商品資料' AS '資料表';
SELECT * FROM product;

SELECT '訂單資料' AS '資料表';
SELECT * FROM `order`;

SELECT '訂單明細資料' AS '資料表';
SELECT * FROM order_detail;