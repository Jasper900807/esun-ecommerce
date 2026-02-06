package com.esun.ecommerce.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 統一 API 回應格式
 * 
 * 成功範例：
 * {
 *   "success": true,
 *   "message": "操作成功",
 *   "data": {...},
 *   "timestamp": "2025-02-06T10:30:00"
 * }
 * 
 * 失敗範例：
 * {
 *   "success": false,
 *   "message": "商品不存在",
 *   "errorCode": "PRODUCT_NOT_FOUND",
 *   "timestamp": "2025-02-06T10:30:00"
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)  // null 的欄位不回傳
public class ApiResponse<T> {

    private Boolean success;
    private String message;
    private T data;
    private String errorCode;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    // ========== 靜態方法 ==========

    /* 成功回應（有資料） */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("操作成功")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /* 成功回應（有資料 + 自訂訊息）*/
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }


    /* 成功回應（無資料) */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /* 失敗回應 */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /* 失敗回應（含錯誤代碼）*/
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
