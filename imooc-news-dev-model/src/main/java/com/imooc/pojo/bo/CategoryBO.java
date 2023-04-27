package com.imooc.pojo.bo;

import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import javax.validation.constraints.NotBlank;

/**
 * @author 昴星
 * @date 2023-04-12 19:10
 * @explain
 */
public class CategoryBO {
    private Integer id;
    @NotBlank(message = "姓名应存在")
    private String name;

    private String oldName;

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagColor() {
        return tagColor;
    }

    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }
    @NotBlank(message = "标签的颜色不能为空")
    private String tagColor;
}
