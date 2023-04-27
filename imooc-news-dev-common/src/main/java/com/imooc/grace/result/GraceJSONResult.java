package com.imooc.grace.result;

import java.util.Map;

/**
 * 自定义响应数据类型枚举升级版本
 *
 * @Title: IMOOCJSONResult.java
 * @Package com.imooc.utils
 * @Description: 自定义响应数据结构
 * 				本类可提供给 H5/ios/安卓/公众号/小程序 使用
 * 				前端接受此类数据（json object)后，可自行根据业务去实现相关功能
 *
 * @Copyright: Copyright (c) 2020
 * @Company: www.imooc.com
 * @author 慕课网 - 风间影月
 * @version V2.0
 */
public class GraceJSONResult {

    // 响应业务状态码
    private Integer status;

    // 响应消息
    private String msg;

    // 是否成功
    private Boolean success;

    // 响应数据，可以是Object，也可以是List或Map等
    private Object data;
    /*
     *  @description:
     *  如果一个方法中使用了静态的方法或者对象，不一定需要将该方法定义为静态的。但是，如果该方法需要在没有创建类的实例对象的情况下被调用，那么该方法必须定义为静态的
     *
     *  在Java中，静态方法可以返回对象，这种方式被称为静态工厂方法（Static Factory Method）。静态方法返回对象的好处包括：

        简化对象创建过程
        静态工厂方法可以封装对象创建的过程，让调用者不必关心对象的创建细节。调用者只需要调用静态方法并传入必要的参数，即可获得一个对象。

        隐藏实现细节
        静态工厂方法可以隐藏对象的创建过程和实现细节。调用者无需知道对象是如何创建的，也无需知道对象的内部实现细节，从而提高了代码的安全性和可维护性。

        可以返回不同的对象
        静态工厂方法可以根据参数的不同返回不同类型的对象，这样可以在一定程度上提高代码的灵活性。例如，一个静态工厂方法可以根据传入的参数返回不同类型的数据库连接对象，
        * 这样可以避免在代码中直接使用new关键字创建对象，从而提高了代码的可维护性和可扩展性。

        可以实现单例模式
        静态工厂方法可以用于实现单例模式，即只创建一个对象并重复使用。由于静态方法可以在整个应用程序中被调用，因此可以保证只有一个实例对象被创建。
        * 单例模式--- 1:可以确保一个类只有一个实例，这样可以避免多个实例之间产生的冲突和重复操作，提高了系统的性能和可靠性。
        *           2:全局访问点.单例模式提供了一个全局访问点，可以在整个系统中方便地访问实例。这样可以避免因为多个对象之间的信息传递而产生的复杂性和耦合性。
                    3:节省系统资源.单例模式可以节省系统资源，因为只有一个实例需要占用系统资源。这对于需要频繁创建和销毁对象的场景尤其有用，例如数据库连接等。
                    4:简化代码实现.单例模式可以简化代码的实现，因为只需要创建一个实例并提供一个全局访问点即可。这样可以避免因为多个对象之间的协作而产生的复杂性和代码冗余。
        总之，静态方法返回对象可以提高代码的可读性、可维护性和可扩展性，同时也可以实现一些特殊的对象创建方式，例如单例模式等。
     * @author: 昴星
     * @time: 2023/3/18 20:16
     */

    /**
     * 成功返回，带有数据的，直接往OK方法丢data数据即可
     * @param data
     * @return
     */
    public static GraceJSONResult ok(Object data) {
        return new GraceJSONResult(data);
    }
    /**
     * 成功返回，不带有数据的，直接调用ok方法，data无须传入（其实就是null）
     * @return
     */
    public static GraceJSONResult ok() {
        return new GraceJSONResult(ResponseStatusEnum.SUCCESS);
    }
    public GraceJSONResult(Object data) {
        this.status = ResponseStatusEnum.SUCCESS.status();
        this.msg = ResponseStatusEnum.SUCCESS.msg();
        this.success = ResponseStatusEnum.SUCCESS.success();
        this.data = data;
    }


    /**
     * 错误返回，直接调用error方法即可，当然也可以在ResponseStatusEnum中自定义错误后再返回也都可以
     * @return
     */
    public static GraceJSONResult error() {
        return new GraceJSONResult(ResponseStatusEnum.FAILED);
    }

    /**
     * 错误返回，map中包含了多条错误信息，可以用于表单验证，把错误统一的全部返回出去
     * @param map
     * @return
     */
    public static GraceJSONResult errorMap(Map map) {
        return new GraceJSONResult(ResponseStatusEnum.FAILED, map);
    }

    /**
     * 错误返回，直接返回错误的消息
     * @param msg
     * @return
     */
    public static GraceJSONResult errorMsg(String msg) {
        return new GraceJSONResult(ResponseStatusEnum.FAILED, msg);
    }

    /**
     * 错误返回，token异常，一些通用的可以在这里统一定义
     * @return
     */
    public static GraceJSONResult errorTicket() {
        return new GraceJSONResult(ResponseStatusEnum.TICKET_INVALID);
    }

    /**
     * 自定义错误范围，需要传入一个自定义的枚举，可以到[ResponseStatusEnum.java[中自定义后再传入
     * @param responseStatus
     * @return
     */
    public static GraceJSONResult errorCustom(ResponseStatusEnum responseStatus) {
        return new GraceJSONResult(responseStatus);
    }
    public static GraceJSONResult exception(ResponseStatusEnum responseStatus) {
        return new GraceJSONResult(responseStatus);
    }

    public GraceJSONResult(ResponseStatusEnum responseStatus) {
        this.status = responseStatus.status();
        this.msg = responseStatus.msg();
        this.success = responseStatus.success();
    }
    public GraceJSONResult(ResponseStatusEnum responseStatus, Object data) {
        this.status = responseStatus.status();
        this.msg = responseStatus.msg();
        this.success = responseStatus.success();
        this.data = data;
    }
    public GraceJSONResult(ResponseStatusEnum responseStatus, String msg) {
        this.status = responseStatus.status();
        this.msg = msg;
        this.success = responseStatus.success();
    }

    public GraceJSONResult() {
    }
    /**
     * @description:JSON返回给前端的JSON对象中获得数据就是属性值的get方法，所以get一定要写
     * @param
     * @return:
     * @author: 昴星
     * @time: 2023/3/18 20:07
     */
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getSuccess() {return success;}

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
