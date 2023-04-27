package com.imooc.admin.controller;
import com.aliyun.core.utils.StringUtils;
import com.imooc.admin.mapper.CategoryMapper;
import com.imooc.admin.service.CategoryService;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.CategoryMngControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.CategoryBO;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.extend.RedisCommon;
import com.imooc.utils.extend.RedisOperator;
import org.checkerframework.checker.units.qual.C;
import org.n3r.idworker.Sid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author 昴星
 * @date 2023-04-01 10:49
 * @explain
 */

@RestController
public class CategoryMngController extends BaseController implements CategoryMngControllerApi {

    final static Logger log=LoggerFactory.getLogger(CategoryMngController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisOperator redis;


    @Override
    public GraceJSONResult getCatList() {
        List<Category> categoryServiceList = categoryService.getList();
        return GraceJSONResult.ok(categoryServiceList);
    }

    @Override
    public GraceJSONResult saveOrUpdateCategory(@RequestBody @Valid CategoryBO categoryBO,
                                                BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = errorsMap(bindingResult);
            return GraceJSONResult.errorMap(errorMap);
        }
        Category category = new Category();
        BeanUtils.copyProperties(categoryBO,category);
        boolean isExit=false ;;
        if(category.getId()==null){
            isExit=categoryService.isCateExit(category.getName(),null);
            if(isExit) return GraceJSONResult.errorMsg("姓名重复");
            boolean isAddSuccess = categoryService.insertOne(category);
            if(!isAddSuccess) return GraceJSONResult.errorMsg("更新失败!");
            return GraceJSONResult.ok();
        }
        isExit=categoryService.isCateExit(category.getName(), categoryBO.getOldName());
        if(isExit) return GraceJSONResult.errorMsg("姓名重复");
        boolean isUpdateSuccess = categoryService.updateOne(category);
        if(!isUpdateSuccess) return GraceJSONResult.errorMsg("更新失败!");
        return GraceJSONResult.ok();
    }

    //@Override
    public GraceJSONResult getCats() {
        String categroyListStr = redis.get(RedisCommon.REDIS_ALL_CATEGROY);
        List<Category> categoryList=null;
        if(!StringUtils.isBlank(categroyListStr)) {
            categoryList = JsonUtils.jsonToList(categroyListStr, Category.class);
        }else{
            categoryList = categoryService.getList();
            if(categoryList.size()==0) return GraceJSONResult.errorMsg("系统错误，请联系管理员");
            redis.set(RedisCommon.REDIS_ALL_CATEGROY,JsonUtils.objectToJson(categoryList));
        }
        return GraceJSONResult.ok(categoryList);
    }
}
