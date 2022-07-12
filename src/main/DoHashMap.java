
public class DoHashMap<K, V> extends AbstractMap<K, V>
        implements Map<K, V>, Cloneable, Serializable {


    /**

     [static] 关键字
     1，加载顺序
     加载类之后，立刻执行 static{} 代码块，代码块之间按顺序执行
     静态资源的加载顺序是严格按照静态资源的定义顺序来加载的。
     包括 静态变量 静态方法 静态代码块 

     2，修饰成员变量
     代表该变量属于类，也就是所有类实例共享
     3，修饰成员方法
     代表该方法可以通过类直接调用，不需要实例化对象
     4，类的实例可以访问 static 变量
     5, static 同样可以修饰class 
     一般用于静态内部类

     https://www.cnblogs.com/xrq730/p/4820992.html
     https://www.cnblogs.com/dolphin0520/p/3799052.html

     */

    /**
     final
     1，final 类不能被继承
     2，final 方法不能被重写
     3，final 变量不能被改变
     （实例变量的引用不能被改变）
     */
    
    /**
    The default initial capacity - MUST be a power of two.

    An instance of HashMap has two parameters that affect its performance: initial capacity and load factor.     
    The capacity is the number of buckets in the hash table, and the initial capacity is simply the capacity at the time the hash table is created. 

    The load factor is a measure of how full the hash table is allowed to get before its capacity is automatically increased. 

    When the number of entries in the hash table exceeds the product of the load factor and the current capacity, 
    the hash table is rehashed (that is, internal data structures are rebuilt) so that the hash table has approximately twice the number of buckets.

    HashMap 的底层数据结构是数组 + 链表的集合体，数组在 HashMap 中又被称为桶(bucket)。遍历 HashMap 需要的时间损耗为 HashMap 实例桶的数量 + (key - value 映射) 的数量。
    因此，如果遍历元素很重要的话，不要把初始容量设置的太高或者负载因子设置的太低。

    在散列表中，每个“桶（bucket）”或者“槽（slot）”会对应一条链表，所有散列值相同的元素我们都放到相同槽位对应的链表中。


        0000 0001 
        0001 0000

        2^4 = 16
    */
            
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.

     如果遍历元素很重要的话，不要把初始容量设置的太高或者负载因子设置的太低。
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;


    /**
     The load factor used when none specified in constructor.
     默认装载因子
     装载因子表示的数学意思就是， used bucket size / all bucket capacity
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;


    /**
    The bin count threshold for using a tree rather than list for a bin. 
    Bins are converted to trees when adding an element to a bin with at least this many nodes. 
    The value must be greater than 2 and should be at least 8 to mesh with assumptions in tree removal about conversion back to plain bins upon shrinkage.

    桶内元素的数量阈值，使用tree 代替list
    简单理解，当桶对应链表长度达到8的时候， 可以进行 链表转化成树
    */
    static final int TREEIFY_THRESHOLD = 8;


    /**
     * The bin count threshold for untreeifying a (split) bin during a
     * resize operation. Should be less than TREEIFY_THRESHOLD, and at
     * most 6 to mesh with shrinkage detection under removal.

     树退化成链表的阈值
     */
    static final int UNTREEIFY_THRESHOLD = 6;

    /**
     * The smallest table capacity for which bins may be treeified.
     * (Otherwise the table is resized if too many nodes in a bin.)
     * Should be at least 4 * TREEIFY_THRESHOLD to avoid conflicts
     * between resizing and treeification thresholds.

     在resize 与 treeify 之间如何选择？
     hash table 至少有64个capacity，之后才值得做treeify
     */
    static final int MIN_TREEIFY_CAPACITY = 64;


/**
内部类 静态内部类
https://docs.oracle.com/javase/tutorial/java/javaOO/nested.html

Nested Classes
The Java programming language allows you to define a class within another class. Such a class is called a nested class and is illustrated here:

class OuterClass {
...
class NestedClass {
...
}
}


Terminology: Nested classes are divided into two categories: non-static and static.
Non-static nested classes are called inner classes.
Nested classes that are declared static are called static nested classes.

class OuterClass {
...
class InnerClass {
...
}
static class StaticNestedClass {
...
}
}

A nested class is a member of its enclosing class.
Non-static nested classes (inner classes) have access to other members of the enclosing class, even if they are declared private.
Static nested classes do not have access to other members of the enclosing class.
As a member of the OuterClass, a nested class can be declared private, public, protected, or package private.
(Recall that outer classes can only be declared public or package private.)

Why Use Nested Classes?
Compelling reasons for using nested classes include the following:

It is a way of logically grouping classes that are only used in one place:
If a class is useful to only one other class, then it is logical to embed it in that class and keep the two together.
Nesting such "helper classes" makes their package more streamlined.
这是一种对只在一个地方使用的类进行逻辑分组的方式。如果一个类只对另一个类有用，那么把它嵌入到那个类中并把两者放在一起是合乎逻辑的。嵌套这样的 "辅助类 "可以使它们的包更加精简。

It increases encapsulation:
Consider two top-level classes, A and B, where B needs access to members of A that would otherwise be declared private. By hiding class B within class A, A's members can be declared private and B can access them. In addition, B itself can be hidden from the outside world.
它增加了封装性。考虑两个顶层类，A和B，其中B需要访问A的（private）成员，否则会被声明为私有。通过将类B隐藏在类A中，A的成员可以被声明为私有，而B可以访问它们。此外，B本身也可以从外部世界隐藏起来。

It can lead to more readable and maintainable code:
Nesting small classes within top-level classes places the code closer to where it is used.
它可以使代码更加可读和可维护。将小类嵌套在顶层类中，使代码更接近于被使用的地方。


Inner Classes
As with instance methods and variables, an inner class is associated with an instance of its enclosing class and has direct access to that object's methods and fields.
Also, because an inner class is associated with an instance, it cannot define any static members itself.
内部类可以访问封装类的成员和方法，但是由于内部类是关联到实例的，所以内部类自己不能定义任何static成员

Objects that are instances of an inner class exist within an instance of the outer class. Consider the following classes:

class OuterClass {
...
class InnerClass {
...
}
}
An instance of InnerClass can exist only within an instance of OuterClass and has direct access to the methods and fields of its enclosing instance.

To instantiate an inner class, you must first instantiate the outer class. Then, create the inner object within the outer object with this syntax:

OuterClass outerObject = new OuterClass();
OuterClass.InnerClass innerObject = outerObject.new InnerClass();
There are two special kinds of inner classes: local classes and anonymous classes.

Static Nested Classes
As with class methods and variables, a static nested class is associated with its outer class. And like static class methods, a static nested class cannot refer directly to instance variables or methods defined in its enclosing class: it can use them only through an object reference. Inner Class and Nested Static Class Example demonstrates this.

Note: A static nested class interacts with the instance members of its outer class (and other classes) just like any other top-level class. In effect, a static nested class is behaviorally a top-level class that has been nested in another top-level class for packaging convenience. Inner Class and Nested Static Class Example also demonstrates this.
You instantiate a static nested class the same way as a top-level class:

StaticNestedClass staticNestedObject = new StaticNestedClass();
Inner Class and Nested Static Class Example
The following example, OuterClass, along with TopLevelClass, demonstrates which class members of OuterClass an inner class (InnerClass), a nested static class (StaticNestedClass), and a top-level class (TopLevelClass) can access:

OuterClass.java

public class OuterClass {

    String outerField = "Outer field";
    static String staticOuterField = "Static outer field";

    class InnerClass {
        void accessMembers() {
            System.out.println(outerField);
            System.out.println(staticOuterField);
        }
    }

    static class StaticNestedClass {
        void accessMembers(OuterClass outer) {
            // Compiler error: Cannot make a static reference to the non-static
            //     field outerField
            // System.out.println(outerField);
            System.out.println(outer.outerField);
            System.out.println(staticOuterField);
        }
    }

    public static void main(String[] args) {
        System.out.println("Inner class:");
        System.out.println("------------");
        OuterClass outerObject = new OuterClass();
        OuterClass.InnerClass innerObject = outerObject.new InnerClass();
        innerObject.accessMembers();

        System.out.println("\nStatic nested class:");
        System.out.println("--------------------");
        StaticNestedClass staticNestedObject = new StaticNestedClass();        
        staticNestedObject.accessMembers(outerObject);
        
        System.out.println("\nTop-level class:");
        System.out.println("--------------------");
        TopLevelClass topLevelObject = new TopLevelClass();        
        topLevelObject.accessMembers(outerObject);                
    }
}
TopLevelClass.java

public class TopLevelClass {

    void accessMembers(OuterClass outer) {     
        // Compiler error: Cannot make a static reference to the non-static
        //     field OuterClass.outerField
        // System.out.println(OuterClass.outerField);
        System.out.println(outer.outerField);
        System.out.println(OuterClass.staticOuterField);
    }  
}
This example prints the following output:

Inner class:
------------
Outer field
Static outer field

Static nested class:
--------------------
Outer field
Static outer field

Top-level class:
--------------------
Outer field
Static outer field
Note that a static nested class interacts with the instance members of its outer class just like any other top-level class. The static nested class StaticNestedClass can't directly access outerField because it's an instance variable of the enclosing class, OuterClass. The Java compiler generates an error at the highlighted statement:

static class StaticNestedClass {
void accessMembers(OuterClass outer) {
// Compiler error: Cannot make a static reference to the non-static
//     field outerField
System.out.println(outerField);
}
}
To fix this error, access outerField through an object reference:

System.out.println(outer.outerField);
Similarly, the top-level class TopLevelClass can't directly access outerField either.

Shadowing
If a declaration of a type (such as a member variable or a parameter name) in a particular scope (such as an inner class or a method definition) has the same name as another declaration in the enclosing scope, then the declaration shadows the declaration of the enclosing scope. You cannot refer to a shadowed declaration by its name alone. The following example, ShadowTest, demonstrates this:


public class ShadowTest {

    public int x = 0;

    class FirstLevel {

        public int x = 1;

        void methodInFirstLevel(int x) {
            System.out.println("x = " + x);
            System.out.println("this.x = " + this.x);
            System.out.println("ShadowTest.this.x = " + ShadowTest.this.x);
        }
    }

    public static void main(String... args) {
        ShadowTest st = new ShadowTest();
        ShadowTest.FirstLevel fl = st.new FirstLevel();
        fl.methodInFirstLevel(23);
    }
}
The following is the output of this example:

x = 23
this.x = 1
ShadowTest.this.x = 0
This example defines three variables named x: the member variable of the class ShadowTest, the member variable of the inner class FirstLevel, and the parameter in the method methodInFirstLevel. The variable x defined as a parameter of the method methodInFirstLevel shadows the variable of the inner class FirstLevel. Consequently, when you use the variable x in the method methodInFirstLevel, it refers to the method parameter. To refer to the member variable of the inner class FirstLevel, use the keyword this to represent the enclosing scope:

System.out.println("this.x = " + this.x);
Refer to member variables that enclose larger scopes by the class name to which they belong. For example, the following statement accesses the member variable of the class ShadowTest from the method methodInFirstLevel:

System.out.println("ShadowTest.this.x = " + ShadowTest.this.x);

对于同名变量或方法，确定好实例对象
内部类this.field
外部类OutClass.this.field
*/

/**
位运算符
Java定义了位运算符，应用于整数类型(int)，长整型(long)，短整型(short)，字符型(char)，和字节型(byte)等类型。

位运算符作用在所有的位上，并且按位运算。假设a = 60，b = 13;它们的二进制格式表示将如下：

A = 0011 1100
B = 0000 1101
-----------------
A&B = 0000 1100
A | B = 0011 1101
A ^ B = 0011 0001
~A= 1100 0011
下表列出了位运算符的基本运算，假设整数变量 A 的值为 60 和变量 B 的值为 13：

操作符	描述	例子
＆	如果相对应位都是1，则结果为1，否则为0					（A＆B），得到12，即0000 1100
|	如果相对应位都是 0，则结果为 0，否则为 1					（A | B）得到61，即 0011 1101
^	如果相对应位值相同，则结果为0，否则为1					（A ^ B）得到49，即 0011 0001
〜	按位取反运算符翻转操作数的每一位，即0变成1，1变成0。		（〜A）得到-61，即1100 0011

<< 		按位左移运算符。左操作数按位左移右操作数指定的位数。		A << 2得到240，即 1111 0000
>> 		按位右移运算符。左操作数按位右移右操作数指定的位数。		A >> 2得到15即 1111
>>> 	按位右移补零操作符。左操作数的值按右操作数指定的位数右移，移动得到的空位以零填充。	A>>>2得到15即0000 1111
*/

/**
hashCode() 计算对象的hashCode value
用于比较两个object 是否相同，主要用在基于Hash Table 散列的数据结构中，判断两个对象是否相同

equals() 方法，默认情况下为 == 操作，即判断对象是否地址相同
一般情况下，重写hashCode() 同时也重写equals()

null == null true
*/
/**
泛型方法
你可以写一个泛型方法，该方法在调用时可以接收不同类型的参数。根据传递给泛型方法的参数类型，编译器适当地处理每一个方法调用。

下面是定义泛型方法的规则：

所有泛型方法声明都有一个类型参数声明部分（由尖括号分隔），该类型参数声明部分在方法返回类型之前（在下面例子中的 <E>）。
每一个类型参数声明部分包含一个或多个类型参数，参数间用逗号隔开。一个泛型参数，也被称为一个类型变量，是用于指定一个泛型类型名称的标识符。
类型参数能被用来声明返回值类型，并且能作为泛型方法得到的实际参数类型的占位符。
泛型方法体的声明和其他方法一样。注意类型参数只能代表引用型类型，不能是原始类型（像 int、double、char 等）。
java 中泛型标记符：

E - Element (在集合中使用，因为集合中存放的是元素)
T - Type（Java 类）
K - Key（键）
V - Value（值）
N - Number（数值类型）
？ - 表示不确定的 java 类型

参考
https://www.cnblogs.com/iyangyuan/archive/2013/04/09/3011274.html



<?> 无限制通配符

( ,E] E以及E的子类
<? extends E> extends 关键字声明了类型的上界，表示参数化的类型可能是所指定的类型，或者是此类型的子类

[E, e) E以及E的父类
<? super E> super 关键字声明了类型的下界，表示参数化的类型可能是指定的类型，或者是此类型的父类

*/


    // /**
    //  * A map entry (key-value pair).  The <tt>Map.entrySet</tt> method returns
    //  * a collection-view of the map, whose elements are of this class.  The
    //  * <i>only</i> way to obtain a reference to a map entry is from the
    //  * iterator of this collection-view.  These <tt>Map.Entry</tt> objects are
    //  * valid <i>only</i> for the duration of the iteration; more formally,
    //  * the behavior of a map entry is undefined if the backing map has been
    //  * modified after the entry was returned by the iterator, except through
    //  * the <tt>setValue</tt> operation on the map entry.
    //  *
    //  * @see Map#entrySet()
    //  * @since 1.2
    //  */
    // interface Entry<K,V> {

    //     K getKey();

    //     V getValue();

    //     V setValue(V value);

    //     boolean equals(Object o);

    //     int hashCode();

    //     public static <K extends Comparable<? super K>, V> Comparator<Map.Entry<K,V>> comparingByKey() {
    //         return (Comparator<Map.Entry<K, V>> & Serializable)
    //             (c1, c2) -> c1.getKey().compareTo(c2.getKey());
    //     }

    //     public static <K, V extends Comparable<? super V>> Comparator<Map.Entry<K,V>> comparingByValue() {
    //         return (Comparator<Map.Entry<K, V>> & Serializable)
    //             (c1, c2) -> c1.getValue().compareTo(c2.getValue());
    //     }

    //     public static <K, V> Comparator<Map.Entry<K, V>> comparingByKey(Comparator<? super K> cmp) {
    //         Objects.requireNonNull(cmp);
    //         return (Comparator<Map.Entry<K, V>> & Serializable)
    //             (c1, c2) -> cmp.compare(c1.getKey(), c2.getKey());
    //     }

    //     public static <K, V> Comparator<Map.Entry<K, V>> comparingByValue(Comparator<? super V> cmp) {
    //         Objects.requireNonNull(cmp);
    //         return (Comparator<Map.Entry<K, V>> & Serializable)
    //             (c1, c2) -> cmp.compare(c1.getValue(), c2.getValue());
    //     }
    // }


	/**
	详解Java中Comparable和Comparator的区别
	https://tobebetterjavaer.com/basic-extra-meal/comparable-omparator.html

	通过上面的两个例子可以比较出 Comparable 和 Comparator 两者之间的区别：

	一个类实现了 Comparable 接口，意味着该类的对象可以直接进行比较（排序），但比较（排序）的方式只有一种，很单一。
	一个类如果想要保持原样，又需要进行不同方式的比较（排序），就可以定制比较器（实现 Comparator 接口）。
	Comparable 接口在 java.lang 包下，而 Comparator 接口在 java.util 包下，算不上是亲兄弟，但可以称得上是表（堂）兄弟。
	*/

    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;

        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final int hashCode() {
            return Objects.hashCode() ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }


            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
	    			/*
	    			Map 类型的equals ，同时比较 key 和 value 
	    			*/
                if (Objects.equals(key, e.getKey())
                        && Objects.equals(value, e.getValue())
                ) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Computes key.hashCode() and spreads (XORs) higher bits of hash
     * to lower.  Because the table uses power-of-two masking, sets of
     * hashes that vary only in bits above the current mask will
     * always collide. (Among known examples are sets of Float keys
     * holding consecutive whole numbers in small tables.)  So we
     * apply a transform that spreads the impact of higher bits
     * downward. There is a tradeoff between speed, utility, and
     * quality of bit-spreading. Because many common sets of hashes
     * are already reasonably distributed (so don't benefit from
     * spreading), and because we use trees to handle large sets of
     * collisions in bins, we just XOR some shifted bits in the
     * cheapest possible way to reduce systematic lossage, as well as
     * to incorporate impact of the highest bits that would otherwise
     * never be used in index calculations because of table bounds.

     key==null hash 结果为0
     key!=null （key的hashCode） 异或 （key的hashCode 右移16位）

     奇怪的知识——位掩码
     https://segmentfault.com/a/1190000039239875

     https://www.cxyxiaowu.com/14188.html
     你管这玩意叫异或运算？

     https://blog.csdn.net/u011123972/article/details/90265415
     由String.hashCode方法引发的int类型乘法溢出的思考

     2的幂次方减1后每一位都是1，让数组每一个位置都能添加到元素。
     例如十进制8，对应二进制1000，减1是0111，这样在&hash值使数组每个位置都是可以添加到元素的，如果有一个位置为0，那么无论hash值是多少那一位总是0，例如0101，&hash后第二位总是0，也就是说数组中下标为2的位置总是空的。
     如果初始化大小设置的不是2的幂次方，hashmap也会调整到比初始化值大且最近的一个2的幂作为capacity。

     1）通过将 Key 的 hash 值与 length-1 进行 & 运算，实现了当前 Key 的定位，
            2 的幂次方可以减少冲突（碰撞）的次数，提高 HashMap 查询效率；

     2）如果 length 为 2 的次幂，则 length-1  转化为二进制必定是 11111…… 的形式，在于 h 的二进制与操作效率会非常的快，而且空间不浪费；
     如果 length 不是 2 的次幂，比如 length 为 15，则 length-1 为 14，对应的二进制为 1110，
     在于 h 与操作，最后一位都为 0，而 0001，0011，0101，1001，1011，0111，1101 这几个位置永远都不能存放元素了，空间浪费相当大，
     更糟的是这种情况中，数组可以使用的位置比数组长度小了很多，这意味着进一步增加了碰撞的几率，减慢了查询的效率！这样就会造成空间的浪费。

     怎么理解这个右移16位？为什么右移16位就增加随机性
     这个hash值，要参与node[] 下标的定位 也就是 (n-1) & hash

     通常来说，我们的n 都不会很大，2^16 = 65536  6w多个key-value
     这时候，我们讲定位在 0001 0000 0000 0000 0000 之内，也就是n-1 （1111 1111 1111 1111）之内
     不要在与操作的位上存在0，这样就会掩盖

     */
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }


    /**
     * Returns x's Class if it is of the form "class C implements
     * Comparable<C>", else null.
     */

    //https://mp.weixin.qq.com/s/YFZtG1KbhVeP05nOtqsQxQ
    //Java中的Type详解

    //秒懂Java之类型系统（Type）
    //http://shusheng007.top/2021/09/09/038/

    //秒懂Java泛型
    //https://blog.csdn.net/ShuSheng0007/article/details/80720406

    //https://www.liaoxuefeng.com/wiki/1252599548343744/1265105920586976
    //廖雪峰 泛型
    
    	/**
		我们再回顾一下extends通配符。作为方法参数，<? extends T>类型和<? super T>类型的区别在于：

		<? extends T>允许调用读方法T get()获取T的引用，但不允许调用写方法set(T)传入T的引用（传入null除外）；

		<? super T>允许调用写方法set(T)传入T的引用，但不允许调用读方法T get()获取T的引用（获取Object除外）。

		一个是允许读不允许写，另一个是允许写不允许读。

		PECS原则
		何时使用extends，何时使用super？为了便于记忆，我们可以用PECS原则：Producer Extends Consumer Super。

		即：如果需要返回T，它是生产者（Producer），要使用extends通配符；如果需要写入T，它是消费者（Consumer），要使用super通配符。

		还是以Collections的copy()方法为例：

		public class Collections {
		    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
		        for (int i=0; i<src.size(); i++) {
		            T t = src.get(i); // src是producer
		            dest.add(t); // dest是consumer
		        }
		    }
		}
		需要返回T的src是生产者，因此声明为List<? extends T>，需要写入T的dest是消费者，因此声明为List<? super T>。

    	*/
    static Class<?> comparableClassFor(Object x) {
        if (x instanceof Comparable) {
            Class<?> c;
            Type[] ts, as;
            Type t;
            ParameterizedType p;

            if ((c = x.getClass()) == String.class) // bypass checks
                return c;


            if ((ts = c.getGenericInterfaces()) != null) {


                for (int i = 0; i < ts.length; ++i) {
                    if (((t = ts[i]) instanceof ParameterizedType) &&
                            ((p = (ParameterizedType) t).getRawType() == Comparable.class) &&
                            (as = p.getActualTypeArguments()) != null &&
                            as.length == 1 && as[0] == c) // type arg is c
                        return c;
                }
            }
        }
        return null;
    }

    /**
     * Returns k.compareTo(x) if x matches kc (k's screened comparable
     * class), else 0.
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // for cast to Comparable
    static int compareComparables(Class<?> kc, Object k, Object x) {
        return (x == null || x.getClass() != kc ? 0 :
                ((Comparable) k).compareTo(x));
    }

    /**
     * Returns a power of two size for the given target capacity.

     https://segmentfault.com/a/1190000039392972
     之所以在开始移位前先将容量-1，是为了避免给定容量已经是8,16这样2的幂时，不减一直接移位会导致得到的结果比预期大。
     比如预期16得到应该是16，直接移位的话会得到32。在上图中就是所有x本身已经是0的情况下，不减1得到的结果变大了。

     1000
     0100
     ----

     1100
     0011
     ----

     1111
    +0001
     ----

    10000 double 了



     找到 > cap 最近的一个2的n次方
     返回大于输入参数且最近的2的整数次幂的数。比如10，则返回16。


     */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;

        //最大就是 2^31 所以16位double之后肯定满足
        //1，n<0 返回 1
        //2，n>=MAXIMUM_CAPACITY(2^31) 返回2^31
        //3, 返回n+1 ，此时的n 一定是全11111111 这种形式的二进制位

        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }


    /**
     * The table, initialized on first use, and resized as
     * necessary. When allocated, length is always a power of two.
     * (We also tolerate length zero in some operations to allow
     * bootstrapping mechanics that are currently not needed.)

     transient
     1，修饰的filed 不参与对象的序列化 Serializable，也就是说，在反序列化的时候拿不到数据 null
     2，transient关键字只能修饰变量，而不能修饰方法和类。

     3、被static关键字修饰的变量不参与序列化，一个静态static变量不管是否被transient修饰，均不能被序列化。（不参与序列化过程）
     4、final变量值参与序列化，final transient同时修饰变量，final不会影响transient，一样不会参与序列化

     此变量就是可以认为是，HashMap的底层是数组，数组存储的链表节点
     为什么这里要transient 修饰？
     此数组可以认为存储的是 引用地址，当序列化的时候只记录这个地址是没效的，因为反序列化的时候已经找不到了
     HashMap 通过自定义实现的反序列化，而不是简单的序列化


     Save the state of the HashMap instance to a stream (i.e.,serialize it).
        private void writeObject(java.io.ObjectOutputStream s)

     Reconstitutes this map from a stream (that is, deserializes it).
        private void readObject(ObjectInputStream s)

     */
    transient Node<K, V>[] table;

    /**
     * Holds cached entrySet(). Note that AbstractMap fields are used
     * for keySet() and values().
     */
    transient Set<Map.Entry<K,V>> entrySet;


    /**
     * The number of key-value mappings contained in this map.
     * 记录了key-value entry 的数量
     */
    transient int size;

    /**
     * The number of times this HashMap has been structurally modified
     * Structural modifications are those that change the number of mappings in
     * the HashMap or otherwise modify its internal structure (e.g.,
     * rehash).  This field is used to make iterators on Collection-views of
     * the HashMap fail-fast.  (See ConcurrentModificationException).
     * 
     * 
     * 结构性修改计数器
     * 迭代器快速失效 判断依据
     
     1.该异常不单单会在多线程情况下发生；
     2.在单线程情况下也可能发生，就是在有使用有fail-fast机制的迭代器遍历集合时，有修改集合的操作也会抛出此异常；
     3.HashMap中的modCount是为了结论2而设计的。
     */
    transient int modCount;


    /**
     * The next size value at which to resize (capacity * load factor).
     final int capacity() {
         return  (table != null) ? 
                    table.length : (threshold > 0) ? 
                        threshold :DEFAULT_INITIAL_CAPACITY;
     }
     */
    int threshold;

    /**
     * The load factor for the hash table.
     */
    final float loadFactor;
    
    
    /**
     * Constructs an empty <tt>HashMap</tt> with the specified initial
     * capacity and load factor.
     * 
     * 构造函数只是指定了 初始容量 加载因子
     */
    public DoHashMap(int initialCapacity, float loadFactor) {
        
        //初始化 capacity 容量边界校验
        //为什么 负数提示，大于就没有提示？
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        
        //加载因子做校验
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                    loadFactor);
        this.loadFactor = loadFactor;
        
        //根据给定capacity 计算上沿2^n 
        this.threshold = tableSizeFor(initialCapacity);
    }
    
    public DoHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }
    
    public DoHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }
    
    public DoHashMap(Map<? extends K, ? extends V> m) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        putMapEntries(m, false);
    }
    /**
     * Implements Map.putAll and Map constructor.
     *
     * @param m the map
     * @param evict false when initially constructing this map, else
     * true (relayed to method afterNodeInsertion).
     */
    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
        int s = m.size();
        if (s > 0) {
            if (table == null) { // pre-size

                //参考
                // The next size value at which to resize
                // threshold = (capacity * load factor).
                // threshold / loadFactor = capacity
                // 计算新的阈值 修正上下边界
                float ft = ((float)s / loadFactor) + 1.0F;

                int t = ((ft < (float)MAXIMUM_CAPACITY) ?
                            (int)ft : MAXIMUM_CAPACITY);

                //当新threshold 超过阈值时，才进行新的阈值申请
                if (t > threshold)
                    threshold = tableSizeFor(t);
            }

            //非初始化状态， 计算是否需要resize
            else if (s > threshold)
                resize();


            //此时，数组已经构造成功 ，遍历赋值
            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                putVal(hash(key), key, value, false, evict);
            }
        }
    }

    /**
     * @return the number of key-value mappings in this map
     */
    public int size() {
        return size;
    }

    /**
     * @return true if this map contains no key-value mappings
     */
    public boolean isEmpty() {
        return size == 0;
    }


    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
     * key.equals(k))}, then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     *
     * <p>A return value of {@code null} does not <i>necessarily</i>
     * indicate that the map contains no mapping for the key; it's also
     * possible that the map explicitly maps the key to {@code null}.
     * The {@link #containsKey containsKey} operation may be used to
     * distinguish these two cases.
     *
     * @see #put(Object, Object)
     */
    public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

    /**
     * Implements Map.get and related methods.
     *
     * @param hash hash for key
     * @param key the key
     * @return the node, or null if none
     */
    final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab;
        Node<K,V> first, e;
        int n;
        K k;


//        如果table !=null 而且table.length >0 也就是说 桶数组有值
//        同时 (n-1) & hash
//        2的幂次方减1后每一位都是1，让数组每一个位置都能添加到元素。
//        例如十进制8，对应二进制1000，减1是0111，这样在&hash值使数组每个位置都是可以添加到元素的，如果有一个位置为0，那么无论hash值是多少那一位总是0，例如0101，&hash后第二位总是0，也就是说数组中下标为2的位置总是空的。
//        如果初始化大小设置的不是2的幂次方，hashmap也会调整到比初始化值大且最近的一个2的幂作为capacity。
//
//        1）通过将 Key 的 hash 值与 length-1 进行 & 运算，实现了当前 Key 的定位，2 的幂次方可以减少冲突（碰撞）的次数，提高 HashMap 查询效率；
//        2）如果 length 为 2 的次幂，则 length-1  转化为二进制必定是 11111…… 的形式，在于 h 的二进制与操作效率会非常的快，而且空间不浪费；
//        如果 length 不是 2 的次幂，比如 length 为 15，则 length-1 为 14，对应的二进制为 1110，
//        在于 h 与操作，最后一位都为 0，而 0001，0011，0101，1001，1011，0111，1101 这几个位置永远都不能存放元素了，空间浪费相当大，
//        更糟的是这种情况中，数组可以使用的位置比数组长度小了很多，这意味着进一步增加了碰撞的几率，减慢了查询的效率！这样就会造成空间的浪费。

        //hash 表存在， 长度 > 0 , 下标对应的第一个位置存在node 节点
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (first = tab[(n - 1) & hash]) != null) {


            //首位node hash值相同，key相同，
                            //如果 key不相同，但是key 不为空 而且equals true 也认为是相同的
                            //返回此node ，也就是说 first 节点就是我们要找的Node

            if (first.hash == hash && // always check first node
                    ((k = first.key) == key || (key != null && key.equals(k))))
                return first;

            //如果first 后继node 不为空，
            // 1，判断一下 first node 是否位 TreeNode （还是 链表）
                    //是 TreeNode ，继续查找对应的真是Node，如果找到了直接返回

            //2，没找到，遍历链表，寻找正确的Node

            //都没找到 返回 null

            if ((e = first.next) != null) {

                if (first instanceof TreeNode)
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);

                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    /**
     * 使用 getNode 方法判断 是否存在数据
     */
    public boolean containsKey(Object key) {
        return getNode(hash(key), key) != null;
    }


    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key</tt>.)
     */
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }

    /**
     * Implements Map.put and related methods.
     *
     * @param hash hash for key
     * @param key the key
     * @param value the value to put
     * @param onlyIfAbsent if true, don't change existing value
     * @param evict if false, the table is in creation mode.
     * @return previous value, or null if none
     */
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {

        Node<K,V>[] tab;
        Node<K,V> p;
        int n, i;

        //先判读是否存在table，空的table 先做resize
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;

        //判断hash定位下的 table 是否位null
        //null 值需要先初始化一个Node
        //非null 则找到对应位置插入
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);

        else {
            Node<K,V> e; K k;
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;//如果hash定位到的Node 与插入的数据相同

                //判断该Node 是Tree 还是 链表
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                //对于链表形式，遍历链表
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        //生产新Node
                        p.next = newNode(hash, key, value, null);
                        //判断此时Node 是否需要转化Tree
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }





}

