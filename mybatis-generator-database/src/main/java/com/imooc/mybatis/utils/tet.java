package com.imooc.mybatis.utils;

import java.io.File;

/**
 * @author 昴星
 * @date 2023-03-28 20:11
 * @explain
 */
public class tet {
    public static void main(String[] args) {
        File configFile = new File("D:\\idea project\\imooc-news\\imooc-news-dev\\mybatis-generator-database\\generatorConfig-admin.xml");
        System.out.println(configFile.length());
    }
}
