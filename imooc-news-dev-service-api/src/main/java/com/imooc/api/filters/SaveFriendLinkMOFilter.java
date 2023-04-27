package com.imooc.api.filters;


import com.imooc.api.wapper.FriendLinkMngSaveMOWrapper;
import com.imooc.utils.extend.DateUtil;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.RequestWrapper;
import java.io.IOException;

/**
 * @author 昴星
 * @date 2023-04-11 21:08
 * @explain 配置过滤器，可以让拦截器处理后能够取到 RequestBody参数值
 */
@Component
@WebFilter(urlPatterns = "/friendLinkMng/saveOrUpdateFriendLink")
public class SaveFriendLinkMOFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        System.out.println(DateUtil.getCurrentDateString());
        ServletRequest requestwapper=null;
        if(servletRequest instanceof HttpServletRequest){
            requestwapper=new FriendLinkMngSaveMOWrapper((HttpServletRequest)servletRequest);
        }
        if(requestwapper==null){
            //防止流读取一次就没有了,将流传递下去
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            filterChain.doFilter(requestwapper,servletResponse);
        }
    }

    @Override
    public void destroy() {
    }
}
