package com.esun.ecommerce.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

/**
 * XSS 請求包裝器
 * 
 * 使用 OWASP Java HTML Sanitizer 過濾 XSS 攻擊
 */
public class XssRequestWrapper extends HttpServletRequestWrapper {

    // OWASP HTML Sanitizer 策略（移除所有 HTML 標籤）
    private static final PolicyFactory POLICY = new HtmlPolicyBuilder().toFactory();

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 過濾請求參數
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return cleanXss(value);
    }

    /**
     * 過濾請求參數陣列
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        
        String[] cleanValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            cleanValues[i] = cleanXss(values[i]);
        }
        return cleanValues;
    }

    /**
     * 過濾請求頭
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return cleanXss(value);
    }

    /**
     * XSS 清理方法
     * 
     * 使用 OWASP HTML Sanitizer 移除危險的 HTML/JavaScript
     */
    private String cleanXss(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        
        // 使用 OWASP Sanitizer 清理
        return POLICY.sanitize(value);
    }
}