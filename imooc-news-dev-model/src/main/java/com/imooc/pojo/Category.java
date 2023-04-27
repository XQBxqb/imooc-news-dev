package com.imooc.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Table;

/**
 * @author 昴星
 * @date 2023-04-12 19:08
 * @explain
 */
@Table(name="category")
public class Category {
    @Id
    private Integer id;

    @Field("name")
    private String name;

    @Field("tag_color")
    private String tagColor;

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
}
