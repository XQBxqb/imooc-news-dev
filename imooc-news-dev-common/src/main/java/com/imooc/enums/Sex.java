package com.imooc.enums;

/**
 * @Desc: 性别 枚举
 */
public enum Sex {
    woman(0, "女"),
    man(1, "男"),
    secret(2, "保密");

    public final  Integer type;//public static final
    public final String value;
    /**
     * @description: private static Sex
     * @param type
     * @param value
     * @return: 
     * @author: 昴星
     * @time: 2023/3/18 20:27
     */

    Sex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
