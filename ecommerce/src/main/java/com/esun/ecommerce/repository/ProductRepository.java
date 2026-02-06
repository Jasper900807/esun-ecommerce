package com.esun.ecommerce.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.esun.ecommerce.entity.Product;

@Repository
public interface  ProductRepository extends JpaRepository<Product, String>{

    /* 新增商品 */
    @Modifying
    @Transactional
    @Query(value = "CALL sp_add_product(:p_product_id, :p_product_name, :p_price, :p_quantity)", nativeQuery = true)
    void addProduct(
            @Param("p_product_id") String productId,
            @Param("p_product_name") String productName,
            @Param("p_price") BigDecimal price,
            @Param("p_quantity") Integer quantity
    );

    /* 查詢可用商品 */
    @Modifying
    @Transactional
    @Query(value = "CALL sp_get_available_products()", nativeQuery = true)
    List<Product> getAvailableProducts();

    /* 根據商品編號查詢商品 */
    @Query(value = "CALL sp_get_product_by_id(:p_product_id)", nativeQuery = true)
    Product getProductById(@Param("p_product_id") String productId);

    /* 更新庫存 */
    @Query(value = "CALL sp_update_inventory(:p_product_id, :p_quantity)", nativeQuery = true)
    void updateInventory(
            @Param("p_product_id") String productId,
            @Param("p_quantity") Integer quantity
    );

    /* 查詢所有商品 */
    List<Product> findAll();

    /* 根據庫存查詢 */
    List<Product> findByQuantityGreaterThan(Integer quantity);
}
