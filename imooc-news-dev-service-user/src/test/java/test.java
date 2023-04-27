import com.imooc.utils.extend.DateUtil;
import org.junit.Test;

import javax.persistence.Table;
import java.util.Date;

/**
 * @author 昴星
 * @date 2023-04-13 22:00
 * @explain
 */
public class test {
    @Test
    public void test(){
        Date date = DateUtil.stringToDate("2000-04-02", DateUtil.ISO_EXPANDED_DATE_FORMAT);
        System.out.println(date);
    }
}
