package com.imooc.pojo.vo;

import java.util.Date;

/**
 * VO显示层对象，通常是 Web 向模板渲染引擎层传输的对象
 * @author: 昴 星
 * @time: 2023/3/16 21:42
 */
public class AppUserVO {

    private String id;
    private String nickname;
    private String face;
    private Integer activeStatus;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

}
