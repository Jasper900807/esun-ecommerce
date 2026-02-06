package com.esun.ecommerce.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esun.ecommerce.dto.request.CreateOrderRequest;
import com.esun.ecommerce.dto.request.OrderItemRequest;
import com.esun.ecommerce.dto.response.OrderResponse;
import com.esun.ecommerce.entity.Order;
import com.esun.ecommerce.entity.Product;
import com.esun.ecommerce.exception.BusinessException;
import com.esun.ecommerce.exception.ResourceNotFoundException;
import com.esun.ecommerce.repository.OrderRepository;
import com.esun.ecommerce.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;  // JSON 處理


    /**
     * 建立訂單（調用 SP，含 Transaction）
     * 
     * 核心流程：
     * 1. 驗證所有商品存在且庫存足夠
     * 2. 生成訂單編號
     * 3. 將訂單項目轉為 JSON
     * 4. 調用 SP（SP 內會自動扣庫存 + 新增訂單 + 新增明細）
     * 
     * @param request 建立訂單請求
     * @return 訂單回應
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("開始建立訂單，會員編號：{}，商品數量：{}", request.getMemberId(), request.getItems().size());

        // 1. 驗證商品存在且庫存足夠
        validateOrderItems(request.getItems());

        // 2. 生成訂單編號（格式：Ms + yyyyMMddHHmmss）
        String orderId = generateOrderId();
        log.info("生成訂單編號：{}", orderId);

        // 3. 將訂單項目轉為 JSON 格式
        String orderItemsJson = convertOrderItemsToJson(request.getItems());
        log.info("訂單項目 JSON：{}", orderItemsJson);

        try {
            // 4. 調用 SP 建立訂單（SP 內包含 Transaction）
            orderRepository.createOrder(orderId, request.getMemberId(), orderItemsJson);

            log.info("訂單建立成功，訂單編號：{}", orderId);

            // 5. 查詢剛建立的訂單並回傳
            Order order = orderRepository.findByIdWithDetails(orderId);
            if (order == null) {
                throw new ResourceNotFoundException("訂單建立後查詢失敗");
            }

            return OrderResponse.fromEntity(order);

        } catch (Exception e) {
            log.error("建立訂單失敗，訂單編號：{}，錯誤：{}", orderId, e.getMessage(), e);
            throw new BusinessException("建立訂單失敗：" + e.getMessage());
        }
    }

    /**
     * 查詢訂單詳情
     * 
     * @param orderId 訂單編號
     * @return 訂單回應
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(String orderId) {
        log.info("查詢訂單，訂單編號：{}", orderId);

        Order order = orderRepository.findByIdWithDetails(orderId);
        if (order == null) {
            throw new ResourceNotFoundException("訂單不存在：" + orderId);
        }

        return OrderResponse.fromEntity(order);
    }

    /**
     * 查詢所有訂單
     * 
     * @return 訂單列表
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        log.info("查詢所有訂單");

        List<Order> orders = orderRepository.getAllOrders();

        return orders.stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 根據會員編號查詢訂單
     * 
     * @param memberId 會員編號
     * @return 訂單列表
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByMemberId(String memberId) {
        log.info("查詢會員訂單，會員編號：{}", memberId);

        List<Order> orders = orderRepository.findByMemberId(memberId);

        return orders.stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }




    // ========== 私有方法 ==========


    /* 驗證訂單項目（商品存在 + 庫存足夠）*/
    private void validateOrderItems(List<OrderItemRequest> items) {
        for (OrderItemRequest item : items) {
            // 查詢商品
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("商品不存在：" + item.getProductId()));

            // 檢查庫存
            if (product.getQuantity() < item.getQuantity()) {
                throw new BusinessException(
                        String.format("商品庫存不足：%s，需要：%d，剩餘：%d",
                                product.getProductName(),
                                item.getQuantity(),
                                product.getQuantity())
                );
            }

            // 驗證價格（防止前端竄改價格）
            if (product.getPrice().compareTo(item.getPrice()) != 0) {
                throw new BusinessException(
                        String.format("商品價格不符：%s，正確價格：%s，傳入價格：%s",
                                product.getProductName(),
                                product.getPrice(),
                                item.getPrice())
                );
            }
        }
    }

    /* 生成訂單編號（格式：Ms20250206143055）*/
    private String generateOrderId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "Ms" + LocalDateTime.now().format(formatter);
    }

    /**
     * 將訂單項目轉為 JSON 格式
     * 
     * 範例：[{"productId":"P001","quantity":2,"price":98000}]
     */
    private String convertOrderItemsToJson(List<OrderItemRequest> items) {
        try {
            List<Map<String, Object>> jsonItems = items.stream()
                    .map(item -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("productId", item.getProductId());
                        map.put("quantity", item.getQuantity());
                        map.put("price", item.getPrice());
                        return map;
                    })
                    .collect(Collectors.toList());

            return objectMapper.writeValueAsString(jsonItems);

        } catch (JsonProcessingException e) {
            log.error("轉換訂單項目為 JSON 失敗", e);
            throw new BusinessException("訂單資料格式錯誤");
        }
    }
}
