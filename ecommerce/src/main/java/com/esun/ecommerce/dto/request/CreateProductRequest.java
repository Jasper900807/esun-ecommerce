package com.esun.ecommerce.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新增商品請求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductRequest {

    /* 商品編號 */
    @NotBlank(message = "商品編號不能為空")
    @Pattern(regexp = "^P\\d{3,}$", message = "商品編號格式錯誤，應為 P 開頭加數字，例如：P001")
    private String productId;

    /* 商品名稱 */
    @NotBlank(message = "商品名稱不能為空")
    @Size(min = 1, max = 200, message = "商品名稱長度必須在 1-200 字元之間")
    private String productName;

    /* 售價 */
    @NotNull(message = "售價不能為空")
    @DecimalMin(value = "0.01", message = "售價必須大於 0")
    @Digits(integer = 8, fraction = 2, message = "售價格式錯誤，最多 8 位整數，2 位小數")
    private BigDecimal price;

    /* 庫存數量 */
    @NotNull(message = "庫存數量不能為空")
    @Min(value = 0, message = "庫存數量不能為負數")
    private Integer quantity;
}
