package com.imooc.pojo.bo;

import javax.validation.constraints.NotBlank;

/**
 * @author 昴星
 * @date 2023-04-10 19:11
 * @explain
 */
public class SaveFriendLink {
    private String id;
    private String linkName;
    private String linkUrl;
    private Integer isDelete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
