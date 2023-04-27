import org.junit.Test;

/**
 * @author 昴星
 * @date 2023-03-25 14:20
 * @explain
 */
public class test {
    @Test
    public void test(){
        String name="1123|sdf";
        //字符串内，如果手写去拼的话，也就\和“需要转义，前面加\一个就行
        System.out.println(name);
        String[] split1 = name.split("\\|");
        // * ^ : | . \   这几个是正则表达式特殊的符号，如果要用其中之一，应在使用前加上\\转义（正则表达式中\\才是实际意义的一个\）
        for(String str : split1){
            System.out.println(str);
        }
    }
}
