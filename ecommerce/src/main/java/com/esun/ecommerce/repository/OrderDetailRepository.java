package com.esun.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.esun.ecommerce.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    /* 根據訂單編號查詢訂單明細 */
    List<OrderDetail> findByOrderId(String orderId);

    /* 根據商品編號查詢訂單明細 */
    List<OrderDetail> findByProductId(String productId);
}
