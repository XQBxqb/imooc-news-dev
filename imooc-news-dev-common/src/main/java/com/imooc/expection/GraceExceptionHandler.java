package com.imooc.expection;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 昴星
 * @date 2023-03-11 15:31
 * @explain 统一异常拦截处理，
 *          可以返回给前端JSON
 */

@ControllerAdvice
public class GraceExceptionHandler {

    @ExceptionHandler(MyException.class)
    @ResponseBody
    public GraceJSONResult returnMyException(MyException myException){
         return GraceJSONResult.exception(myException.getResponseStatusEnum());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public GraceJSONResult returnMaxUploadSizeExceededException(MaxUploadSizeExceededException maxUploadSizeExceededException){
        return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_MAX_SIZE_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public GraceJSONResult handleMethodArgument(MethodArgumentNotValidException methodArgumentNotValidException){
        BindingResult result = methodArgumentNotValidException.getBindingResult();
        Map<String, String> errorsMap = errorsMap(result);
        return GraceJSONResult.errorMap(errorsMap);
    }


    public Map<String,String> errorsMap(BindingResult bindingResult) {
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
