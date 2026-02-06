package com.esun.ecommerce.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 訂單項目請求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequest {
    /* 商品編號 */
    @NotBlank(message = "商品編號不能為空")
    private String productId;

    /* 購買數量 */
    @NotNull(message = "購買數量不能為空")
    @Min(value = 1, message = "購買數量至少為 1")
    private Integer quantity;

    /* 商品單價 (後端會驗證) */
    @NotNull(message = "商品單價不能為空")
    @Min(value = 0, message = "商品單價不能為負數")
    private BigDecimal price;
}
