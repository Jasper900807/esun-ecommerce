-- ============================================
-- 測試 Stored Procedures
-- ============================================

-- 測試 1: 查詢可用商品
CALL sp_get_available_products();

-- 測試 2: 新增商品
CALL sp_add_product('P004', 'iPhone 15 Pro', 36900.00, 10);

-- 測試 3: 查詢特定商品
CALL sp_get_product_by_id('P001');

-- 測試 4: 建立訂單 (JSON 格式)
CALL sp_create_order(
    'Ms20250206001234',
    '12345',
    '[{"productId":"P002","quantity":2,"price":1200},{"productId":"P003","quantity":1,"price":8500}]'
);

SHOW ERRORS;
SHOW WARNINGS;
-- 測試 5: 查詢訂單詳情
CALL sp_get_order_detail('Ms20250206001234');

-- 測試 6: 查詢所有訂單
CALL sp_get_all_orders();