package com.esun.ecommerce.exception;

import com.esun.ecommerce.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全域異常處理器
 * 
 * 統一處理所有異常，回傳標準的 ApiResponse 格式
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 處理業務邏輯異常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        log.error("業務邏輯異常：{}", e.getMessage());
        
        ApiResponse<Void> response = ApiResponse.error(
                e.getMessage(),
                e.getErrorCode() != null ? e.getErrorCode() : "BUSINESS_ERROR"
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 處理資源不存在異常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error("資源不存在：{}", e.getMessage());
        
        ApiResponse<Void> response = ApiResponse.error(e.getMessage(), "RESOURCE_NOT_FOUND");
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * 處理參數驗證異常（@Valid）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException e) {
        log.error("參數驗證失敗：{}", e.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .success(false)
                .message("參數驗證失敗")
                .errorCode("VALIDATION_ERROR")
                .data(errors)
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 處理參數綁定異常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleBindException(BindException e) {
        log.error("參數綁定失敗：{}", e.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .success(false)
                .message("參數格式錯誤")
                .errorCode("BIND_ERROR")
                .data(errors)
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 處理參數類型不匹配異常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        log.error("參數類型錯誤：{}", e.getMessage());
        
        String message = String.format("參數 '%s' 的值 '%s' 類型錯誤，應為 %s",
                e.getName(),
                e.getValue(),
                e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知");
        
        ApiResponse<Void> response = ApiResponse.error(message, "TYPE_MISMATCH");
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 處理 SQL 異常（包含 Stored Procedure 的錯誤）
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiResponse<Void>> handleSQLException(SQLException e) {
        log.error("資料庫異常：SQLState={}, ErrorCode={}, Message={}",
                e.getSQLState(), e.getErrorCode(), e.getMessage());
        
        // 解析 SP 拋出的錯誤訊息
        String message = e.getMessage();
        if (message != null && message.contains("SQLSTATE[45000]")) {
            // 提取 SP 的自訂錯誤訊息
            int startIndex = message.indexOf("SQLSTATE[45000]");
            if (startIndex != -1) {
                message = message.substring(startIndex + 16).trim();
            }
        }
        
        ApiResponse<Void> response = ApiResponse.error(
                "資料庫操作失敗：" + message,
                "DATABASE_ERROR"
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 處理非法參數異常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("非法參數：{}", e.getMessage());
        
        ApiResponse<Void> response = ApiResponse.error(e.getMessage(), "ILLEGAL_ARGUMENT");
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 處理空指標異常
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNullPointerException(NullPointerException e) {
        log.error("空指標異常", e);
        
        ApiResponse<Void> response = ApiResponse.error(
                "系統內部錯誤：資料為空",
                "NULL_POINTER_ERROR"
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 處理所有未捕獲的異常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception e) {
        log.error("系統異常", e);
        
        ApiResponse<Void> response = ApiResponse.error(
                "系統內部錯誤：" + e.getMessage(),
                "INTERNAL_SERVER_ERROR"
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}