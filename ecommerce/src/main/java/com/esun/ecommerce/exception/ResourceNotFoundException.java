package com.esun.ecommerce.exception;

/**
 * 資源不存在異常
 * 
 * 用途：處理查詢不到資源的情況
 * 例如：商品不存在、訂單不存在等
 */
public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;
    private String resourceId;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String resourceId) {
        super(String.format("%s 不存在，ID：%s", resourceName, resourceId));
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getResourceId() {
        return resourceId;
    }
}