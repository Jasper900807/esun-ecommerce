package com.esun.ecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esun.ecommerce.dto.request.CreateProductRequest;
import com.esun.ecommerce.dto.response.ProductResponse;
import com.esun.ecommerce.entity.Product;
import com.esun.ecommerce.exception.BusinessException;
import com.esun.ecommerce.exception.ResourceNotFoundException;
import com.esun.ecommerce.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 商品服務層
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    
    private final ProductRepository productRepository;

    /**
     * 新增商品
     * 
     * @param request 新增商品請求
     * @return 商品回應
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductResponse createProduct(CreateProductRequest request) {
        log.info("開始新增商品，商品編號：{}", request.getProductId());

        try {
            // 檢查商品編號是否已存在
            if (productRepository.existsById(request.getProductId())) {
                throw new BusinessException("商品編號已存在：" + request.getProductId());
            }

            // 新增商品
            productRepository.addProduct(
                    request.getProductId(),
                    request.getProductName(),
                    request.getPrice(),
                    request.getQuantity()
            );

            log.info("商品新增成功，商品編號：{}", request.getProductId());

            // 查詢剛新增的商品並回傳
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("商品新增後查詢失敗"));

            return ProductResponse.fromEntity(product);

        } catch (Exception e) {
            log.error("新增商品失敗，商品編號：{}，錯誤：{}", request.getProductId(), e.getMessage());
            throw new BusinessException("新增商品失敗：" + e.getMessage());
        }
    }



    /**
     * 查詢所有可用商品（庫存 > 0）
     * 
     * @return 商品列表
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getAvailableProducts() {
        log.info("查詢所有可用商品");

        try {
            // 調用 SP 查詢可用商品
            List<Product> products = productRepository.getAvailableProducts();

            log.info("查詢到 {} 個可用商品", products.size());

            return products.stream()
                    .map(ProductResponse::fromEntity)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("查詢可用商品失敗，錯誤：{}", e.getMessage());
            throw new BusinessException("查詢可用商品失敗：" + e.getMessage());
        }
    }

    /**
     * 根據商品編號查詢商品
     * 
     * @param productId 商品編號
     * @return 商品回應
     */
    @Transactional(readOnly = true)
    public ProductResponse getProductById(String productId) {
        log.info("查詢商品，商品編號：{}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("商品不存在：" + productId));

        return ProductResponse.fromEntity(product);
    }

    /**
     * 查詢所有商品
     * 
     * @return 商品列表
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        log.info("查詢所有商品");

        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 更新商品庫存
     * 
     * @param productId 商品編號
     * @param quantity 扣除數量
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateInventory(String productId, Integer quantity) {
        log.info("更新商品庫存，商品編號：{}，扣除數量：{}", productId, quantity);

        try {
            // 更新庫存
            productRepository.updateInventory(productId, quantity);

            log.info("商品庫存更新成功");

        } catch (Exception e) {
            log.error("更新商品庫存失敗，錯誤：{}", e.getMessage());
            throw new BusinessException("更新商品庫存失敗：" + e.getMessage());
        }
    }
}
