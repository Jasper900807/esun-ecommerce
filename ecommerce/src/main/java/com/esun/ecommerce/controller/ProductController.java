package com.esun.ecommerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esun.ecommerce.dto.request.CreateProductRequest;
import com.esun.ecommerce.dto.response.ApiResponse;
import com.esun.ecommerce.dto.response.ProductResponse;
import com.esun.ecommerce.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    /**
     * 新增商品
     * 
     * POST /api/products
     * 
     * Request Body:
     * {
     *   "productId": "P001",
     *   "productName": "商品名稱",
     *   "price": 1000.00,
     *   "quantity": 50
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        
        log.info("收到新增商品請求：{}", request);
        
        ProductResponse product = productService.createProduct(request);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("商品新增成功", product));
    }

    /**
     * 查詢所有可用商品（庫存 > 0）
     * 
     * GET /api/products/available
     */
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAvailableProducts() {
        
        log.info("收到查詢可用商品請求");
        
        List<ProductResponse> products = productService.getAvailableProducts();
        
        return ResponseEntity.ok(
                ApiResponse.success("查詢成功", products)
        );
    }

    /**
     * 查詢所有商品
     * 
     * GET /api/products
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        
        log.info("收到查詢所有商品請求");
        
        List<ProductResponse> products = productService.getAllProducts();
        
        return ResponseEntity.ok(
                ApiResponse.success("查詢成功", products)
        );
    }

    /**
     * 根據商品編號查詢商品
     * 
     * GET /api/products/{productId}
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable String productId) {
        
        log.info("收到查詢商品請求，商品編號：{}", productId);
        
        ProductResponse product = productService.getProductById(productId);
        
        return ResponseEntity.ok(
                ApiResponse.success("查詢成功", product)
        );
    }
}
