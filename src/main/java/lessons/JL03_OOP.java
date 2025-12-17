package lessons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 第三课：面向对象编程 (Java 版)
 *
 * 学习目标：
 * - 类和对象
 * - 构造器
 * - 继承
 * - 接口（类似 Scala 的 Trait）
 * - 单例模式
 * - 不可变类（类似 Scala 的 Case Class）
 */
public class JL03_OOP {

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("第三课：面向对象编程 (Java 版)");
        System.out.println("==================================================");

        // ============================================
        // 1. 类定义
        // ============================================
        System.out.println("\n--- 1. 类定义 ---");

        Person person1 = new Person("Alice", 25);
        Person person2 = new Person("Bob");  // 使用重载构造器

        System.out.println(person1.introduce());
        System.out.println("person2.age = " + person2.getAge());
        person2.setAge(30);
        System.out.println("修改后 person2.age = " + person2.getAge());

        // ============================================
        // 2. 访问修饰符
        // ============================================
        System.out.println("\n--- 2. 访问修饰符 ---");

        BankAccount account = new BankAccount(1000);
        System.out.println("初始余额: " + account.getBalance());
        account.deposit(500);
        System.out.println("存入500后: " + account.getBalance());
        account.withdraw(200);
        System.out.println("取出200后: " + account.getBalance());

        // ============================================
        // 3. 继承
        // ============================================
        System.out.println("\n--- 3. 继承 ---");

        Dog dog = new Dog("旺财", "金毛");
        Cat cat = new Cat("咪咪");

        System.out.println(dog.speak());
        System.out.println(cat.speak());

        // 多态
        List<Animal> animals = new ArrayList<>();
        animals.add(dog);
        animals.add(cat);
        animals.add(new Animal("未知动物"));

        System.out.println("所有动物:");
        for (Animal a : animals) {
            System.out.println("  " + a.speak());
        }

        // ============================================
        // 4. 抽象类
        // ============================================
        System.out.println("\n--- 4. 抽象类 ---");

        Rectangle rect = new Rectangle(5, 3);
        Circle circle = new Circle(4);

        System.out.println("矩形: " + rect.describe());
        System.out.println("圆形: " + circle.describe());

        // ============================================
        // 5. 接口（类似 Scala 的 Trait）
        // ============================================
        System.out.println("\n--- 5. 接口 ---");

        Duck duck = new Duck("唐老鸭");
        System.out.println(duck.speak());
        System.out.println(duck.fly());
        System.out.println(duck.swim());
        System.out.println("飞行高度: " + duck.getAltitude() + "米");

        // 接口作为类型
        makeItFly(duck);

        // ============================================
        // 6. 单例模式（类似 Scala 的 object）
        // ============================================
        System.out.println("\n--- 6. 单例模式 ---");

        System.out.println("PI = " + MathUtils.PI);
        System.out.println("square(5) = " + MathUtils.square(5));
        System.out.println("cube(3) = " + MathUtils.cube(3));

        // ============================================
        // 7. 工厂模式（类似 Scala 的伴生对象）
        // ============================================
        System.out.println("\n--- 7. 工厂模式 ---");

        Counter c1 = Counter.create();
        Counter c2 = Counter.create();
        Counter c3 = Counter.create();

        System.out.println("c1.count = " + c1.getCount());
        System.out.println("c2.count = " + c2.getCount());
        System.out.println("c3.count = " + c3.getCount());
        System.out.println("总实例数: " + Counter.getTotalInstances());

        // ============================================
        // 8. 不可变类（类似 Scala 的 Case Class）
        // ============================================
        System.out.println("\n--- 8. 不可变类 (类似 Case Class) ---");

        Point p1 = new Point(3, 4);
        Point p2 = new Point(3, 4);
        Point p3 = p1.withX(10);  // 类似 copy

        System.out.println("p1 = " + p1);
        System.out.println("p2 = " + p2);
        System.out.println("p1.equals(p2): " + p1.equals(p2));  // 结构相等
        System.out.println("p3 = " + p3);

        // 不可变类用于数据传输
        User user = new User(1L, "张三", "zhangsan@example.com");
        System.out.println("用户: " + user);

        // ============================================
        // 9. 结果类型（模拟 Scala 的 Sealed Trait）
        // ============================================
        System.out.println("\n--- 9. 结果类型 ---");

        Result success = Result.success(42);
        Result failure = Result.failure("出错了");

        System.out.println(handleResult(success));
        System.out.println(handleResult(failure));

        System.out.println("\n==================================================");
        System.out.println("第三课完成！运行 JL04_PatternMatching 继续学习");
        System.out.println("==================================================");
    }

    // 接口作为参数类型
    public static void makeItFly(Flyable flyer) {
        System.out.println("  " + flyer.fly() + " 高度: " + flyer.getAltitude() + "米");
    }

    // 处理结果
    public static String handleResult(Result result) {
        if (result.isSuccess()) {
            return "成功: " + result.getValue();
        } else {
            return "失败: " + result.getMessage();
        }
    }
}

// ============================================
// 类定义
// ============================================

class Person {
    private final String name;  // 不可变
    private int age;            // 可变

    // 主构造器
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // 辅助构造器（重载）
    public Person(String name) {
        this(name, 0);
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String introduce() {
        return "我叫 " + name + "，今年 " + age + " 岁";
    }

    @Override
    public String toString() {
        return "Person(" + name + ", " + age + ")";
    }
}

class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public double getBalance() { return balance; }

    public void deposit(double amount) {
        if (amount > 0) balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

// ============================================
// 继承
// ============================================

class Animal {
    protected final String name;

    public Animal(String name) {
        this.name = name;
    }

    public String speak() {
        return name + " 发出声音";
    }
}

class Dog extends Animal {
    private final String breed;

    public Dog(String name, String breed) {
        super(name);
        this.breed = breed;
    }

    @Override
    public String speak() {
        return name + " (" + breed + ") 说: 汪汪!";
    }
}

class Cat extends Animal {
    public Cat(String name) {
        super(name);
    }

    @Override
    public String speak() {
        return name + " 说: 喵~";
    }
}

// ============================================
// 抽象类
// ============================================

abstract class Shape {
    public abstract double area();
    public abstract double perimeter();

    public String describe() {
        return "面积: " + area() + ", 周长: " + perimeter();
    }
}

class Rectangle extends Shape {
    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() { return width * height; }

    @Override
    public double perimeter() { return 2 * (width + height); }
}

class Circle extends Shape {
    private final double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double area() { return Math.PI * radius * radius; }

    @Override
    public double perimeter() { return 2 * Math.PI * radius; }
}

// ============================================
// 接口（类似 Scala 的 Trait，Java 8 支持默认方法）
// ============================================

interface Flyable {
    default String fly() { return "飞行中..."; }
    int getAltitude();
}

interface Swimmable {
    default String swim() { return "游泳中..."; }
}

class Duck extends Animal implements Flyable, Swimmable {
    public Duck(String name) {
        super(name);
    }

    @Override
    public int getAltitude() { return 100; }

    @Override
    public String speak() {
        return name + " 说: 嘎嘎!";
    }
}

// ============================================
// 单例模式
// ============================================

final class MathUtils {
    public static final double PI = 3.14159;

    private MathUtils() {} // 私有构造器

    public static double square(double x) { return x * x; }
    public static double cube(double x) { return x * x * x; }
}

// ============================================
// 工厂模式
// ============================================

class Counter {
    private static int instances = 0;
    private final int count;

    private Counter(int count) {
        this.count = count;
    }

    public static Counter create() {
        instances++;
        return new Counter(instances);
    }

    public int getCount() { return count; }
    public static int getTotalInstances() { return instances; }
}

// ============================================
// 不可变类（类似 Case Class，Java 8 兼容）
// ============================================

final class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    // 类似 Scala 的 copy 方法
    public Point withX(int newX) { return new Point(newX, this.y); }
    public Point withY(int newY) { return new Point(this.x, newY); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point(" + x + ", " + y + ")";
    }
}

final class User {
    private final long id;
    private final String name;
    private final String email;

    public User(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "User(" + id + ", " + name + ", " + email + ")";
    }
}

// ============================================
// 结果类型（模拟 Sealed Trait，Java 8 兼容）
// ============================================

class Result {
    private final boolean success;
    private final Integer value;
    private final String message;

    private Result(boolean success, Integer value, String message) {
        this.success = success;
        this.value = value;
        this.message = message;
    }

    public static Result success(int value) {
        return new Result(true, value, null);
    }

    public static Result failure(String message) {
        return new Result(false, null, message);
    }

    public boolean isSuccess() { return success; }
    public Integer getValue() { return value; }
    public String getMessage() { return message; }
}
