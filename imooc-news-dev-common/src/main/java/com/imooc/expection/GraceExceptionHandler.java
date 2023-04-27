package com.imooc.expection;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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
}
