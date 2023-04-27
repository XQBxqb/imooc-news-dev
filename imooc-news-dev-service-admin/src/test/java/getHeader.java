import com.imooc.api.interceptors.AdminMngInterceptor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author 昴星
 * @date 2023-04-01 19:27
 * @explain
 */
public class getHeader {
        final static Logger log= LoggerFactory.getLogger(getHeader.class); //改类的class

        @Test
        private  void getHeders(HttpServletRequest request){
        //2.获得所有头的名称
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {//判断是否还有下一个元素
        String nextElement = headerNames.nextElement();//获取headerNames集合中的请求头
        String header2 = request.getHeader(nextElement);//通过请求头得到请求内容
        log.info("请求头=========={}" + nextElement + "VALUE:" + header2);
            }
        }
}
