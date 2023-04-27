package com.imooc.utils;

import com.github.pagehelper.PageInfo;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.List;

/**
 * @author 昴星
 * @date 2023-04-13 21:42
 * @explain
 */
public class PageUtils {
    public static PagedGridResult pagedResult(List<?> list,
                                              Integer page){
        PageInfo<?> pageList=new PageInfo<>(list);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(list);
        gridResult.setPage(page);
        gridResult.setRecords(pageList.getTotal());
        gridResult.setTotal(pageList.getPages());
        return gridResult;
    }
}
