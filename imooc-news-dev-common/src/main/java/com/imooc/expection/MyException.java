package com.imooc.expection;

import com.imooc.grace.result.ResponseStatusEnum;

/**
 * @author 昴星
 * @date 2023-03-11 15:20
 * @explain 自定义异常统一处理
 *          便于解耦，service和controller错误解耦
 */
public class MyException extends RuntimeException{
    private ResponseStatusEnum responseStatusEnum;
    public MyException(ResponseStatusEnum responseStatusEnum){
        super("异常状态码: "+ responseStatusEnum.status()+" ;异常信息为: "+ responseStatusEnum.msg());
        this.responseStatusEnum=responseStatusEnum;
    }

    public void setResponseStatusEnum(ResponseStatusEnum responseStatusEnum) {
        this.responseStatusEnum = responseStatusEnum;
    }

    public ResponseStatusEnum getResponseStatusEnum() {
        return responseStatusEnum;
    }
}
