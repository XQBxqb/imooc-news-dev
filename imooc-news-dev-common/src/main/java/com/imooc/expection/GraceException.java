package com.imooc.expection;

import com.imooc.grace.result.ResponseStatusEnum;


/**
 * @author 昴星
 * @date 2023-03-11 15:18
 * @explain
 */
public class GraceException {
    public static void display(ResponseStatusEnum responseStatusEnum){
        throw new MyException(responseStatusEnum);
    }
}

