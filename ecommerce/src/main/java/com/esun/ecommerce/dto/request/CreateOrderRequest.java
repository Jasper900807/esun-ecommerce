package com.esun.ecommerce.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 建立訂單請求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {

    /* 會員編號 */
    @NotBlank(message = "會員編號不能為空")
    private String memberId;

    /* 訂單項目清單 */
    @NotEmpty(message = "訂單項目不能為空")
    @Valid  // 驗證 List 內的每個 OrderItemRequest
    private List<OrderItemRequest> items;
}
