package com.imooc.admin.service.impl;

import com.imooc.admin.mapper.CategoryMapper;
import com.imooc.admin.service.CategoryService;
import com.imooc.pojo.Category;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import wiremock.org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author 昴星
 * @date 2023-04-12 19:22
 * @explain
 */

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper mapper;

    @Override
    public boolean insertOne(Category category) {
        int result = mapper.insert(category);
        if(result==1) return true;
        return false;
    }

    @Override
    public boolean updateOne(Category category) {
        int result = mapper.updateByPrimaryKey(category);
        if(result==1) return true;
        return false;
    }

    @Override
    public List<Category> getList() {
        List<Category> categoryList = mapper.selectAll();
        return categoryList;
    }

    @Override
    public boolean isCateExit(String categoryName,String oldCatName) {
        Example example = new Example(Category.class);
        Example.Criteria catCriteria = example.createCriteria();
        catCriteria.andEqualTo("name", categoryName);
        if (StringUtils.isNotBlank(oldCatName)) {
            catCriteria.andNotEqualTo("name", oldCatName);
        }

        List<Category> catList = mapper.selectByExample(example);

        boolean isExist = false;
        if (catList != null && !catList.isEmpty() && catList.size() > 0) {
            isExist = true;
        }
        return isExist;
    }
}
