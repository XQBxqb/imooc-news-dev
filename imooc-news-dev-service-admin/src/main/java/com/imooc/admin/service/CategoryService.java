package com.imooc.admin.service;

import com.imooc.pojo.Category;
import io.swagger.models.auth.In;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 昴星
 * @date 2023-04-12 19:20
 * @explain
 */
public interface CategoryService {
    @Transactional
    public boolean insertOne(Category category);

    @Transactional
    public boolean updateOne(Category category);

    public List<Category> getList();

    public boolean isCateExit(String categoryName,String oldCatName);


}
