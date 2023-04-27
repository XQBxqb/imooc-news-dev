package com.imooc.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 昴星
 * @date 2023-04-30 13:06
 * @explain
 */
public class BingResultMapUtils {
    public static Map<String,String> errorsMap(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Map<String, String> map = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            //错误对应的属性
            String field = fieldError.getField();
            //错误对应的消息
            String message = fieldError.getDefaultMessage();
            map.put(field, message);
        }
        return map;
    }
}
