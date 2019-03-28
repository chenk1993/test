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

## 元素的 remove 操作
不要在 foreach 循环里进行元素的 remove/add 操作。remove 元素请使用 Iterator
方式，如果并发操作，需要对 Iterator 对象加锁

![image](https://github.com/chenk1993/test/blob/master/src/image/4.png)
![image](https://github.com/chenk1993/test/blob/master/src/image/5.png)
从异常信息可以发现，异常出现在checkForComodification()方法中。

　　我们不忙看checkForComodification()方法的具体实现，我们先根据程序的代码一步一步看ArrayList源码的实现：

　　首先看ArrayList的iterator()方法的具体实现，查看源码发现在ArrayList的源码中并没有iterator()这个方法，那么很显然这个方法应该是其父类或者实现的接口中的方法，我们在其父类AbstractList中找到了iterator()方法的具体实现，下面是其实现代码：

public Iterator<E> iterator() {

    return new Itr();
    
}
 　　从这段代码可以看出返回的是一个指向Itr类型对象的引用，我们接着看Itr的具体实现，在AbstractList类中找到了Itr类的具体实现，它是AbstractList的一个成员内部类，下面这段代码是Itr类的所有实现：

private class Itr implements Iterator<E> {

    int cursor = 0;
    
    int lastRet = -1;
    
    int expectedModCount = modCount;
    
    public boolean hasNext() {
    
           return cursor != size();
           
    }
    
    public E next() {
    
           checkForComodification();
        try {
        
        E next = get(cursor);
        
        lastRet = cursor++;
        
        return next;
        
        } catch (IndexOutOfBoundsException e) {
        
        checkForComodification();
        
        throw new NoSuchElementException();
        
        }
        
    }
    
    public void remove() {
    
        if (lastRet == -1)
        
        throw new IllegalStateException();
        
           checkForComodification();
 
        try {
        
        AbstractList.this.remove(lastRet);
        
        if (lastRet < cursor)
        
            cursor--;
            
        lastRet = -1;
        
        expectedModCount = modCount;
        
        } catch (IndexOutOfBoundsException e) {
        
        throw new ConcurrentModificationException();
        
        }
        
    }
 
    final void checkForComodification() {
    
        if (modCount != expectedModCount)
        
        throw new ConcurrentModificationException();
        
    }
    
}
 　　首先我们看一下它的几个成员变量：
 
　　cursor：表示下一个要访问的元素的索引，从next()方法的具体实现就可看出

　　lastRet：表示上一个访问的元素的索引

　　expectedModCount：表示对ArrayList修改次数的期望值，它的初始值为modCount。

　　modCount是AbstractList类中的一个成员变量

protected transient int modCount = 0;

 　 该值表示对List的修改次数，查看ArrayList的add()和remove()方法就可以发现，每次调用add()方法或者remove()方法就会对modCount进行加1操作。
　　
    好了，到这里我们再看看上面的程序：

　　当调用list.iterator()返回一个Iterator之后，通过Iterator的hashNext()方法判断是否还有元素未被访问，我们看一下hasNext()方法，hashNext()方法的实现很简单：

public boolean hasNext() {

    return cursor != size();
    
}
 　 如果下一个访问的元素下标不等于ArrayList的大小，就表示有元素需要访问，这个很容易理解，如果下一个访问元素的下标等于ArrayList的大小，则肯定到达末尾了。
　　
    然后通过Iterator的next()方法获取到下标为0的元素，我们看一下next()方法的具体实现：

public E next() {

    checkForComodification();
    
 try {
 
    E next = get(cursor);
    
    lastRet = cursor++;
    
    return next;
    
 } catch (IndexOutOfBoundsException e) {
 
    checkForComodification();
    
    throw new NoSuchElementException();
    
 }
 
}
 　 
    这里是非常关键的地方：首先在next()方法中会调用checkForComodification()方法，然后根据cursor的值获取到元素，接着将cursor的值赋给lastRet，并对cursor的值进行加1操作。初始时，cursor为0，lastRet为-1，那么调用一次之后，cursor的值为1，lastRet的值为0。注意此时，modCount为0，expectedModCount也为0。
　　
    接着往下看，程序中判断当前元素的值是否为2，若为2，则调用list.remove()方法来删除该元素。
　　
    我们看一下在ArrayList中的remove()方法做了什么：

public boolean remove(Object o) {

    if (o == null) {
    
        for (int index = 0; index < size; index++)
        
            if (elementData[index] == null) {
            
                fastRemove(index);
                
                return true;
                
            }
            
    } else {
    
        for (int index = 0; index < size; index++)
        
            if (o.equals(elementData[index])) {
            
                fastRemove(index);
                
                return true;
                
            }
            
    }
    
    return false;
    
}
 
 
private void fastRemove(int index) {

    modCount++;
    
    int numMoved = size - index - 1;
    
    if (numMoved > 0)
    
        System.arraycopy(elementData, index+1, elementData, index,numMoved);
        
    elementData[--size] = null; // Let gc do its work
    
}

 　 通过remove方法删除元素最终是调用的fastRemove()方法，在fastRemove()方法中，首先对modCount进行加1操作（因为对集合修改了一次），然后接下来就是删除元素的操作，最后将size进行减1操作，并将引用置为null以方便垃圾收集器进行回收工作。
 
　　那么注意此时各个变量的值：对于iterator，其expectedModCount为0，cursor的值为1，lastRet的值为0。

　　对于list，其modCount为1，size为0。

　　接着看程序代码，执行完删除操作后，继续while循环，调用hasNext方法()判断，由于此时cursor为1，而size为0，那么返回true，所以继续执行while循环，然后继续调用iterator的next()方法：

　　注意，此时要注意next()方法中的第一句：checkForComodification()。

　　在checkForComodification方法中进行的操作是：

final void checkForComodification() {

    if (modCount != expectedModCount)
    
    throw new ConcurrentModificationException();
    
}
 　 如果modCount不等于expectedModCount，则抛出ConcurrentModificationException异常。
　　很显然，此时modCount为1，而expectedModCount为0，因此程序就抛出了ConcurrentModificationException异常。
　　到这里，想必大家应该明白为何上述代码会抛出ConcurrentModificationException异常了。
　　关键点就在于：调用list.remove()方法导致modCount和expectedModCount的值不一致。
![image](https://github.com/chenk1993/test/blob/master/src/image/6.png)
在Itr类中也给出了一个remove()方法：

public void remove() {

    if (lastRet == -1)
    
    throw new IllegalStateException();
    
       checkForComodification();
 
    try {
    
    AbstractList.this.remove(lastRet);
    
    if (lastRet < cursor)
    
        cursor--;
        
    lastRet = -1;
    
    expectedModCount = modCount;
    
    } catch (IndexOutOfBoundsException e) {
    
    throw new ConcurrentModificationException();
    
    }
    
}
    在这个方法中，删除元素实际上调用的就是list.remove()方法，但是它多了一个操作：

    expectedModCount = modCount;
    
 　 因此，在迭代器中如果要删除元素的话，需要调用Itr类的remove方法。

