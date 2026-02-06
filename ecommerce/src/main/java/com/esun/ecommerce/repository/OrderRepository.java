package com.esun.ecommerce.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.esun.ecommerce.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    /**
     * 建立訂單
     * JSON格式範例: [{"productId":"P001","quantity":2,"price":98000}]
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "CALL sp_create_order(:p_order_id, :p_member_id, :p_order_items)", nativeQuery = true)
    void createOrder(
            @Param("p_order_id") String orderId,
            @Param("p_member_id") String memberId,
            @Param("p_order_items") String orderItemsJson
    );

    /**
     * 查詢訂單詳情
     * 回傳 訂單主檔 & 明細
     */
    @Query(value = "CALL sp_get_order_detail(:p_order_id)", nativeQuery = true)
    List<Map<String, Object>> getOrderDetail(@Param("p_order_id") String orderId);

    /**
     * 調用 SP: 查詢所有訂單
     */
    @Query(value = "CALL sp_get_all_orders()", nativeQuery = true)
    List<Order> getAllOrders();

    /* 根據會員編號查詢訂單 */
    List<Order> findByMemberId(String memberId);

    /* 根據付款狀態查詢訂單 */
    List<Order> findByPayStatus(Integer payStatus);

    /* 根據訂單編號查詢（含明細 */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderDetails WHERE o.orderId = :orderId")
    Order findByIdWithDetails(@Param("orderId") String orderId);
}
