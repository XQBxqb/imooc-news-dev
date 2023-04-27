import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * @author 昴星
 * @date 2023-03-28 18:55
 * @explain depency spring-cloud-starter-openfeign
*           该工具的作用就是加密，与普通工具加密不同，能够更灵活的加密，让信息更安全。
 */
public class BCrytpUtils {
        @Test
        public void bcTest(){
                //hashpw:单向加密，每次生成的密码不一样。密码循环并且加盐（一个128bits随机字符串，22字符）后hash，循环默认十次，这样生成管理员密码。
                String hashpw = BCrypt.hashpw("123", BCrypt.gensalt());
                System.out.println(hashpw);
        }
}
