package lessons;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.function.*;

/**
 * 第二课：函数与方法（Java 版）
 *
 * 学习目标：
 * - 方法定义
 * - Lambda 表达式
 * - 函数式接口
 * - 高阶函数
 * - 方法引用
 */
public class L02_Functions {

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("第二课：函数与方法 (Java 版)");
        System.out.println("==================================================");

        // ============================================
        // 1. 方法定义
        // ============================================
        System.out.println("\n--- 1. 方法定义 ---");

        System.out.println("add(3, 5) = " + add(3, 5));
        System.out.println("multiply(4, 6) = " + multiply(4, 6));
        System.out.println("sayHello() = " + sayHello());

        // ============================================
        // 2. 默认参数（Java 不支持，用重载模拟）
        // ============================================
        System.out.println("\n--- 2. 方法重载（模拟默认参数）---");

        System.out.println(greet("Alice"));                    // 使用默认值
        System.out.println(greet("Bob", "Hi"));               // 覆盖部分默认值
        System.out.println(greet("Charlie", "Hey", "~"));     // 覆盖所有默认值

        // ============================================
        // 3. 可变参数
        // ============================================
        System.out.println("\n--- 3. 可变参数 ---");

        System.out.println("sum(1, 2, 3) = " + sum(1, 2, 3));
        System.out.println("sum(1, 2, 3, 4, 5) = " + sum(1, 2, 3, 4, 5));

        // ============================================
        // 4. Lambda 表达式（类似 Scala 的函数字面量）
        // ============================================
        System.out.println("\n--- 4. Lambda 表达式 ---");

        // 使用函数式接口
        BiFunction<Integer, Integer, Integer> addFunc = (x, y) -> x + y;
        BiFunction<Integer, Integer, Integer> multiplyFunc = (x, y) -> x * y;

        // 简化写法
        Function<Integer, Integer> doubleIt = x -> x * 2;
        Function<Integer, Integer> addOne = x -> x + 1;

        System.out.println("addFunc.apply(3, 4) = " + addFunc.apply(3, 4));
        System.out.println("doubleIt.apply(5) = " + doubleIt.apply(5));

        // ============================================
        // 5. 高阶函数
        // ============================================
        System.out.println("\n--- 5. 高阶函数 ---");

        // 函数作为参数
        System.out.println("applyOperation(10, 3, add) = " +
                applyOperation(10, 3, (a, b) -> a + b));
        System.out.println("applyOperation(10, 3, subtract) = " +
                applyOperation(10, 3, (a, b) -> a - b));
        System.out.println("applyOperation(10, 3, divide) = " +
                applyOperation(10, 3, (a, b) -> a / b));

        // 函数作为返回值
        Function<Integer, Integer> triple = createMultiplier(3);
        Function<Integer, Integer> quadruple = createMultiplier(4);
        System.out.println("triple.apply(5) = " + triple.apply(5));
        System.out.println("quadruple.apply(5) = " + quadruple.apply(5));

        // ============================================
        // 6. 柯里化（Java 中模拟）
        // ============================================
        System.out.println("\n--- 6. 柯里化（模拟）---");

        // Java 中可以用嵌套函数模拟柯里化
        Function<Integer, Function<Integer, Integer>> addCurried =
                x -> y -> x + y;

        System.out.println("addCurried(3)(5) = " + addCurried.apply(3).apply(5));

        // 部分应用
        Function<Integer, Integer> addFive = addCurried.apply(5);
        System.out.println("addFive(10) = " + addFive.apply(10));

        // ============================================
        // 7. 方法引用
        // ============================================
        System.out.println("\n--- 7. 方法引用 ---");

        List<String> words = Arrays.asList("apple", "banana", "cherry");

        // 静态方法引用
        words.stream().map(String::toUpperCase).forEach(System.out::println);

        // 实例方法引用
        System.out.println("字符串长度:");
        words.stream().map(String::length).forEach(len -> System.out.println("  " + len));

        // ============================================
        // 8. 递归函数
        // ============================================
        System.out.println("\n--- 8. 递归函数 ---");

        System.out.println("factorial(10) = " + factorial(10));
        System.out.println("factorialBigInt(100) = " + factorialBigInt(BigInteger.valueOf(100)));

        // ============================================
        // 9. 闭包
        // ============================================
        System.out.println("\n--- 9. 闭包 ---");

        // Java 中 lambda 捕获的变量必须是 effectively final
        final int multiplier = 3;
        Function<Integer, Integer> closureFunc = x -> x * multiplier;

        System.out.println("multiplier = 3 时, closureFunc(10) = " + closureFunc.apply(10));

        // 使用数组或包装类来模拟可变闭包
        final int[] mutableMultiplier = {3};
        Function<Integer, Integer> mutableClosure = x -> x * mutableMultiplier[0];

        System.out.println("mutableMultiplier = 3 时, closure(10) = " + mutableClosure.apply(10));
        mutableMultiplier[0] = 5;
        System.out.println("mutableMultiplier = 5 时, closure(10) = " + mutableClosure.apply(10));

        System.out.println("\n==================================================");
        System.out.println("第二课完成！运行 L03_OOP 继续学习");
        System.out.println("==================================================");
    }

    // ============================================
    // 辅助方法定义
    // ============================================

    // 标准方法定义
    public static int add(int x, int y) {
        return x + y;
    }

    public static int multiply(int x, int y) {
        return x * y;
    }

    public static int subtract(int x, int y) {
        return x - y;
    }

    // 无参数方法
    public static String sayHello() {
        return "Hello!";
    }

    // 无返回值方法
    public static void printMessage(String msg) {
        System.out.println("消息: " + msg);
    }

    // 方法重载模拟默认参数
    public static String greet(String name) {
        return greet(name, "Hello", "!");
    }

    public static String greet(String name, String greeting) {
        return greet(name, greeting, "!");
    }

    public static String greet(String name, String greeting, String punctuation) {
        return greeting + ", " + name + punctuation;
    }

    // 可变参数
    public static int sum(int... numbers) {
        int total = 0;
        for (int n : numbers) {
            total += n;
        }
        return total;
    }

    // 高阶函数：函数作为参数
    public static int applyOperation(int x, int y, BiFunction<Integer, Integer, Integer> operation) {
        return operation.apply(x, y);
    }

    // 高阶函数：函数作为返回值
    public static Function<Integer, Integer> createMultiplier(int factor) {
        return x -> x * factor;
    }

    // 递归
    public static long factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    // 使用 BigInteger 处理大数
    public static BigInteger factorialBigInt(BigInteger n) {
        if (n.compareTo(BigInteger.ONE) <= 0) return BigInteger.ONE;
        return n.multiply(factorialBigInt(n.subtract(BigInteger.ONE)));
    }
}
