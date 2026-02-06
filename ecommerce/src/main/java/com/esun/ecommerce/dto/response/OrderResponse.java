package com.esun.ecommerce.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.esun.ecommerce.dto.response.OrderResponse.OrderDetailResponse;
import com.esun.ecommerce.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 訂單回應
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private String orderId;
    private String memberId;
    private BigDecimal totalPrice;
    private Integer payStatus;
    private String payStatusText;  // "未付款" 或 "已付款"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderDetailResponse> items;  // 訂單明細

    /**
     * 訂單明細內部類別
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderDetailResponse {
        private Long orderItemSn;
        private String productId;
        private String productName;
        private Integer quantity;
        private BigDecimal standPrice;
        private BigDecimal itemPrice;
    }



    /**
     * 從 Entity 轉換為 Response
     */
    public static OrderResponse fromEntity(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderDetailResponse> detailResponses = order.getOrderDetails().stream()
                .map(detail -> OrderDetailResponse.builder()
                        .orderItemSn(detail.getOrderItemSn())
                        .productId(detail.getProductId())
                        .productName(detail.getProduct() != null ? detail.getProduct().getProductName() : null)
                        .quantity(detail.getQuantity())
                        .standPrice(detail.getStandPrice())
                        .itemPrice(detail.getItemPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .memberId(order.getMemberId())
                .totalPrice(order.getTotalPrice())
                .payStatus(order.getPayStatus())
                .payStatusText(order.getPayStatus() == 1 ? "已付款" : "未付款")
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(detailResponses)
                .build();
    }
}
