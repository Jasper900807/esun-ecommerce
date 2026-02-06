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

import com.esun.ecommerce.dto.request.CreateOrderRequest;
import com.esun.ecommerce.dto.response.ApiResponse;
import com.esun.ecommerce.dto.response.OrderResponse;
import com.esun.ecommerce.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

     private final OrderService orderService;
     /**
     * 建立訂單
     * 
     * POST /api/orders
     * 
     * Request Body:
     * {
     *   "memberId": "12345",
     *   "items": [
     *     {
     *       "productId": "P001",
     *       "quantity": 2,
     *       "price": 98000.00
     *     },
     *     {
     *       "productId": "P002",
     *       "quantity": 1,
     *       "price": 1200.00
     *     }
     *   ]
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        
        log.info("收到建立訂單請求，會員編號：{}，商品數量：{}", 
                request.getMemberId(), 
                request.getItems().size());
        
        OrderResponse order = orderService.createOrder(request);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("訂單建立成功", order));
    }

    /**
     * 查詢所有訂單
     * 
     * GET /api/orders
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        
        log.info("收到查詢所有訂單請求");
        
        List<OrderResponse> orders = orderService.getAllOrders();
        
        return ResponseEntity.ok(
                ApiResponse.success("查詢成功", orders)
        );
    }

    /**
     * 根據訂單編號查詢訂單
     * 
     * GET /api/orders/{orderId}
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @PathVariable String orderId) {
        
        log.info("收到查詢訂單請求，訂單編號：{}", orderId);
        
        OrderResponse order = orderService.getOrderById(orderId);
        
        return ResponseEntity.ok(
                ApiResponse.success("查詢成功", order)
        );
    }

    /**
     * 根據會員編號查詢訂單
     * 
     * GET /api/orders/member/{memberId}
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByMemberId(
            @PathVariable String memberId) {
        
        log.info("收到查詢會員訂單請求，會員編號：{}", memberId);
        
        List<OrderResponse> orders = orderService.getOrdersByMemberId(memberId);
        
        return ResponseEntity.ok(
                ApiResponse.success("查詢成功", orders)
        );
    }
}
