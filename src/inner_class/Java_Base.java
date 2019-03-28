package inner_class;

/**
 * @Author chenk
 * @Date 2019/3/28  9:27
 **/

public class Java_Base {
    class B{}
    static class C{}
    public static void main(String[] args) {
        B b=new Java_Base().new B();
        C c=new C();
    }
}
