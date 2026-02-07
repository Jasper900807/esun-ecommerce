-- ============================================
-- E.SUN Bank E-Commerce Stored Procedures
-- ============================================
USE esun_ecommerce;

-- 設定分隔符號
DELIMITER $$

-- ============================================
-- 1. sp_add_product - 新增商品
-- ============================================
DROP PROCEDURE IF EXISTS sp_add_product$$

CREATE PROCEDURE sp_add_product(
    IN p_product_id VARCHAR(50),
    IN p_product_name VARCHAR(200),
    IN p_price DECIMAL(10, 2),
    IN p_quantity INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN 
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = '新增商品失敗';
    END;

    START TRANSACTION;

    -- 查看商品編號是否存在
    IF EXISTS (SELECT 1 FROM product WHERE product_id = p_product_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = '商品編號已存在';
    END IF;

    IF p_price < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = '商品價格不能為負數';
    END IF;

    IF p_quantity < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = '商品庫存不能為負數';
    END IF;

    -- 新增商品
    INSERT INTO product (product_id, product_name, price, quantity)
    VALUES (p_product_id, p_product_name, p_price, p_quantity);

    COMMIT;
END$$

-- ============================================
-- 2. sp_get_available_products - 查詢可用商品
-- ============================================

DROP PROCEDURE IF EXISTS sp_get_available_products$$

CREATE PROCEDURE sp_get_available_products()
BEGIN
    SELECT
        product_id,
        product_name,
        price,
        quantity,
        created_at,
        updated_at
    FROM product
    WHERE quantity > 0
    ORDER BY product_id;
END$$

-- ============================================
-- 3. sp_get_product_by_id - 根據商品編號查詢商品
-- ============================================
DROP PROCEDURE IF EXISTS sp_get_product_by_id$$
CREATE PROCEDURE sp_get_product_by_id(
    IN p_product_id VARCHAR(50)
)
BEGIN 
    SELECT
        product_id,
        product_name,
        price,
        quantity,
        created_at,
        updated_at
    FROM product
    WHERE product_id = p_product_id;
END$$

-- ============================================
-- 4. sp_check_inventory - 檢查庫存是否足夠
-- ============================================
DROP PROCEDURE IF EXISTS sp_check_inventory$$

CREATE PROCEDURE sp_check_inventory(
    IN p_product_id VARCHAR(50),
    IN p_required_quantity INT,
    OUT p_is_available BOOLEAN
)
BEGIN
    DECLARE v_current_quantity  INT;

    SELECT quantity INTO v_current_quantity
    FROM product
    WHERE product_id = p_product_id
    FOR UPDATE; -- 悲觀鎖

    IF v_current_quantity IS NULL THEN
        SET p_is_available = FALSE;
    ELSEIF v_current_quantity >= p_required_quantity THEN
        SET p_is_available = TRUE;
    ELSE
        SET p_is_available = FALSE;
    END IF;
END$$


-- ============================================
-- 5. sp_update_inventory - 更新商品庫存
-- ============================================
DROP PROCEDURE IF EXISTS sp_update_inventory$$

CREATE PROCEDURE sp_update_inventory(
    IN p_product_id VARCHAR(50),
    IN p_quantity INT
)
BEGIN
    DECLARE v_current_quantity INT;
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = '更新庫存失敗';
    END;

    START TRANSACTION;

    -- 查詢庫存
    SELECT quantity INTO v_current_quantity
    FROM product
    WHERE product_id = p_product_id
    FOR UPDATE; -- 悲觀鎖

    -- 檢查商品是否存在
    IF v_current_quantity IS NULL THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = '商品不存在';
    END IF;

    -- 檢查庫存是否足夠
    IF v_current_quantity < p_quantity THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = '庫存不足';
    END IF;

    -- 更新庫存
    UPDATE product
    SET quantity = quantity - p_quantity
    WHERE product_id = p_product_id;

    COMMIT;
END$$


-- ============================================
-- 6. sp_create_order - 建立訂單 (核心 Transaction)
-- ============================================
DROP PROCEDURE IF EXISTS sp_create_order$$

CREATE PROCEDURE sp_create_order(
    IN p_order_id VARCHAR(50),
    IN p_member_id VARCHAR(50),
    IN p_order_items JSON  -- JSON 格式: [{"productId":"P001","quantity":2,"price":1000}]
)
BEGIN
    DECLARE v_item_count INT;
    DECLARE v_index INT DEFAULT 0;
    DECLARE v_product_id VARCHAR(50);
    DECLARE v_quantity INT;
    DECLARE v_price DECIMAL(10, 2);
    DECLARE v_item_price DECIMAL(10, 2);
    DECLARE v_total_price DECIMAL(10, 2) DEFAULT 0;
    DECLARE v_current_quantity INT;
    DECLARE v_error_msg VARCHAR(500);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    -- 檢查訂單編號是否存在
    IF EXISTS (SELECT 1 FROM `order` WHERE order_id = p_order_id) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = '訂單編號已存在';
    END IF;

    -- 取得訂單項目數量
    SET v_item_count = JSON_LENGTH(p_order_items);

    -- 驗證至少有一個商品
    IF v_item_count = 0 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = '訂單至少需要一個商品';
    END IF;

    -- 先建立訂單主檔（total_price 先設為 0）
    INSERT INTO `order` (order_id, member_id, total_price, pay_status)
    VALUES (p_order_id, p_member_id, 0, 0);

    WHILE v_index < v_item_count DO
        -- 轉換JSON資料
        SET v_product_id = JSON_UNQUOTE(JSON_EXTRACT(p_order_items, CONCAT('$[', v_index, '].productId')));
        SET v_quantity = JSON_EXTRACT(p_order_items, CONCAT('$[', v_index, '].quantity'));
        SET v_price = JSON_EXTRACT(p_order_items, CONCAT('$[', v_index, '].price'));

        -- 檢查數量
        IF v_quantity <= 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '商品數量需大於0';
        END IF;

        -- 檢查庫存 (悲觀鎖)
        SELECT quantity INTO v_current_quantity
        FROM product
        WHERE product_id = v_product_id
        FOR UPDATE;

        -- 檢查商品是否存在
        IF v_current_quantity IS NULL THEN
            SET v_error_msg = CONCAT('商品不存在: ', v_product_id);
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = v_error_msg;
        END IF;

        -- 檢查庫存是否足夠
        IF v_current_quantity < v_quantity THEN
            SET v_error_msg = CONCAT('商品庫存不足: ', v_product_id, ', 需要: ', v_quantity, ', 剩餘: ', v_current_quantity);
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = v_error_msg;
        END IF;

        -- 更新庫存
        UPDATE product
        SET quantity = quantity - v_quantity
        WHERE product_id = v_product_id;

        -- 計算單品項總價
        SET v_item_price = v_price * v_quantity;
        SET v_total_price = v_total_price + v_item_price;

        -- 新增訂單明細
        INSERT INTO order_detail (order_id, product_id, quantity, stand_price, item_price)
        VALUES (p_order_id, v_product_id, v_quantity, v_price, v_item_price);

        SET v_index = v_index + 1;
    END WHILE;

    -- 更新訂單主檔
    UPDATE `order`
    SET total_price = v_total_price
    WHERE order_id = p_order_id;

    COMMIT;

    -- 回傳訂單資訊
    SELECT
        o.order_id,
        o.member_id,
        o.total_price,
        o.pay_status,
        o.created_at
    FROM `order` o
    WHERE o.order_id = p_order_id;

END$$


-- ============================================
-- 7. sp_get_order_detail - 查詢訂單詳情
-- ============================================
DROP PROCEDURE IF EXISTS sp_get_order_detail$$

CREATE PROCEDURE sp_get_order_detail(
    IN p_order_id VARCHAR(50)
)
BEGIN

    -- 查詢訂單主檔
    SELECT 
        o.order_id,
        o.member_id,
        o.total_price,
        o.pay_status,
        o.created_at,
        o.updated_at
    FROM `order` o
    WHERE o.order_id = p_order_id;

    -- 查詢訂單明細
    SELECT 
        od.order_item_sn,
        od.order_id,
        od.product_id,
        p.product_name,
        od.quantity,
        od.stand_price,
        od.item_price,
        od.created_at
    FROM order_detail od
    LEFT JOIN product p ON od.product_id = p.product_id
    WHERE od.order_id = p_order_id
    ORDER BY od.order_item_sn;
END$$


-- ============================================
-- 8. sp_get_all_orders - 查詢所有訂單
-- ============================================
DROP PROCEDURE IF EXISTS sp_get_all_orders$$

CREATE PROCEDURE sp_get_all_orders()
BEGIN
    SELECT 
        order_id,
        member_id,
        total_price,
        pay_status,
        created_at,
        updated_at
    FROM `order`
    ORDER BY created_at DESC;
END$$

-- 恢復分隔符號
DELIMITER ;
