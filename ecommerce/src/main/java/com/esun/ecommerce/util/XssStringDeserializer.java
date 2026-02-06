package com.esun.ecommerce.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import java.io.IOException;

/**
 * XSS 字串反序列化器
 * 
 * 在 JSON 反序列化時自動過濾 XSS
 */
public class XssStringDeserializer extends JsonDeserializer<String> {

    private static final PolicyFactory POLICY = new HtmlPolicyBuilder().toFactory();

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        
        if (value == null || value.isEmpty()) {
            return value;
        }
        
        // 使用 OWASP Sanitizer 清理 XSS
        return POLICY.sanitize(value);
    }
}