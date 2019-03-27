package polymorphism;

/**
 * @Author chenk
 * @Date 2019/3/27  17:24
 **/

public class A {
    public String show(D obj) {
        return ("polymorphism.A and polymorphism.D");
    }

    public String show(A obj) {
        return ("polymorphism.A and polymorphism.A");
    }
}
