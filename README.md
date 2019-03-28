# test
## 多态polymorphism

public class A {
    public String show(D obj) {
        return ("A and D");
    }

    public String show(A obj) {
        return ("A and A");
    }

}

public class B extends A{
    public String show(B obj){
        return ("B and B");
    }
    
    public String show(A obj){
        return ("B and A");
    }
}

public class C extends B{

}

public class D extends B{

}

public class Test {
    public static void main(String[] args) {
        A a1 = new A();
        A a2 = new B();
        B b = new B();
        C c = new C();
        D d = new D();
        
        System.out.println("1--" + a1.show(b));
        System.out.println("2--" + a1.show(c));
        System.out.println("3--" + a1.show(d));
        System.out.println("4--" + a2.show(b));
        System.out.println("5--" + a2.show(c));
        System.out.println("6--" + a2.show(d));
        System.out.println("7--" + b.show(b));
        System.out.println("8--" + b.show(c));
        System.out.println("9--" + b.show(d));      
    }
}

运行结果如下

![image](https://github.com/chenk1993/test/blob/master/src/image/1.png)



在继承链中对象方法的调用存在一个优先级：this.show(O)、super.show(O)、this.show((super)O)、super.show((super)O)。


## 内部类


![image](https://github.com/chenk1993/test/blob/master/src/image/2.png)

Static Nested Class是被声明为静态（static）的内部类，它可以不依赖于外部类实例被实例化。而通常的内部类需要在外部类实例化后才能实例化。

## 循环体内，字符串的连接方式

![image](https://github.com/chenk1993/test/blob/master/src/image/3.png)

使用str=str+"hello"每次循环都会 new 出一个 StringBuilder 对象，
然后进行 append 操作，最后通过 toString 方法返回 String 对象，造成内存资源浪费。
