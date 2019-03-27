# test
## 多态polymorphism
![image](https://github.com/chenk1993/test/blob/master/src/image/1.png)

运行结果如下

1--polymorphism.A and polymorphism.A

2--polymorphism.A and polymorphism.A

3--polymorphism.A and polymorphism.D

4--polymorphism.B and polymorphism.A

5--polymorphism.B and polymorphism.A

6--polymorphism.A and polymorphism.D

7--polymorphism.B and polymorphism.B

8--polymorphism.B and polymorphism.B

9--polymorphism.A and polymorphism.D


在继承链中对象方法的调用存在一个优先级：this.show(O)、super.show(O)、this.show((super)O)、super.show((super)O)。
