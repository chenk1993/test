package polymorphism;

/**
 * @Author chenk
 * @Date 2019/3/27  17:33
 **/

public class B extends A{
    public String show(B obj){
        return ("polymorphism.B and polymorphism.B");
    }

    public String show(A obj){
        return ("polymorphism.B and polymorphism.A");
    }
}
