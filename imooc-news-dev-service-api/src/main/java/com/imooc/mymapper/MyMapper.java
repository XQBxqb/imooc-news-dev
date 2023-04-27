package com.imooc.mymapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author 昴星
 * @date 2023-03-13 20:42
 * @explain API层可以不只是api接口，通常api接口所利用的一些资源，或者controller的实现层所利用的资源，常见的就是BaseController，里面有一些常用的方法，还有就是一些
 * 其他技术的接口调用，可以创建一个接口去继承一个技术的接口，那么这也算是一个接口，api层习惯上可分为 controllerApi,config(配置类),interceptor---反正就是这些他们的共同点就是
 * 本身不是业务直接的需求，而是实现业务过程需要的一些条件。
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
