package string_test;

/**
 * @Author chenk
 * @Date 2019/3/28  10:11
 **/

public class StringTest {
    public static void main(String[] args) {
        String str = "start";
        for (int i = 0; i < 100; i++) {
            str = str + "hello";
        }

        StringBuffer str1=new StringBuffer("start");
        for (int i = 0; i < 100; i++) {
            str1.append("hello");
        }
    }
}
