package lessons;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 第六课：高级特性 (Java 版)
 *
 * 学习目标：
 * - Optional（类似 Scala 的 Option）
 * - Either 模式（使用自定义类）
 * - 异常处理
 * - 泛型
 * - 懒加载
 */
public class JL06_Advanced {

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("第六课：高级特性 (Java 版)");
        System.out.println("==================================================");

        // ============================================
        // 1. Optional - 处理可能为空的值
        // ============================================
        System.out.println("\n--- 1. Optional ---");

        System.out.println("divide(10, 2) = " + divide(10, 2));
        System.out.println("divide(10, 0) = " + divide(10, 0));

        // 使用 Optional 的方法
        Optional<Double> result = divide(10, 2);
        System.out.println("orElse: " + result.orElse(0.0));
        System.out.println("map: " + result.map(v -> v * 2));
        System.out.println("filter: " + result.filter(v -> v > 3));

        // Optional 链式操作
        Optional<String> email = findUser(1).flatMap(JL06_Advanced::findEmail);
        System.out.println("链式查询: " + email);

        // 类似 for 推导式
        Optional<String> fullInfo = findUser(1)
                .flatMap(user -> findEmail(user)
                        .map(e -> user + " <" + e + ">"));
        System.out.println("flatMap组合: " + fullInfo);

        // ============================================
        // 2. Either 模式 - 表示两种可能的结果
        // ============================================
        System.out.println("\n--- 2. Either 模式 ---");

        System.out.println("divideEither(10, 2) = " + divideEither(10, 2));
        System.out.println("divideEither(10, 0) = " + divideEither(10, 0));

        // 处理 JEither
        JEither<String, Double> either = divideEither(10, 2);
        if (either.isRight()) {
            System.out.println("成功: " + either.getRight());
        } else {
            System.out.println("错误: " + either.getLeft());
        }

        // map 操作
        JEither<String, Double> mapped = divideEither(10, 2).map(v -> v * 100);
        System.out.println("Either map: " + mapped);

        // ============================================
        // 3. 异常处理（类似 Try）
        // ============================================
        System.out.println("\n--- 3. 异常处理 ---");

        System.out.println("parseNumber(\"123\") = " + parseNumber("123"));
        System.out.println("parseNumber(\"abc\") = " + parseNumber("abc"));

        // JTry 的方法
        JTry<Integer> parsed = parseNumber("456");
        System.out.println("getOrElse: " + parsed.getOrElse(0));
        System.out.println("map: " + parsed.map(n -> n * 2));
        System.out.println("toOptional: " + parsed.toOptional());

        // 链式处理
        JTry<Integer> computation = parseNumber("10")
                .flatMap(a -> parseNumber("20").map(b -> a + b));
        System.out.println("链式JTry计算: " + computation);

        // recover
        JTry<Integer> recovered = parseNumber("abc").recover(e -> -1);
        System.out.println("recover: " + recovered);

        // ============================================
        // 4. 泛型类
        // ============================================
        System.out.println("\n--- 4. 泛型类 ---");

        Box<Integer> intBox = new Box<>(42);
        Box<String> stringBox = new Box<>("Hello");
        Box<Integer> mappedBox = intBox.map(n -> n * 2);

        System.out.println("intBox: " + intBox);
        System.out.println("stringBox: " + stringBox);
        System.out.println("mappedBox: " + mappedBox);

        // 泛型方法
        System.out.println("swap((1, \"one\")) = " + swap(new Pair<>(1, "one")));

        // ============================================
        // 5. Comparator（类似上下文边界）
        // ============================================
        System.out.println("\n--- 5. Comparator ---");

        System.out.println("maximum(3, 5) = " + maximum(3, 5, Comparator.naturalOrder()));
        System.out.println("maximum(\"apple\", \"banana\") = " +
                maximum("apple", "banana", Comparator.naturalOrder()));

        // 自定义比较器
        Comparator<PersonRecord> byAge = Comparator.comparingInt(p -> p.age);
        PersonRecord p1 = new PersonRecord("Alice", 30);
        PersonRecord p2 = new PersonRecord("Bob", 25);
        System.out.println("maximum(p1, p2) by age = " + maximum(p1, p2, byAge));

        // ============================================
        // 6. 懒加载 (Lazy)
        // ============================================
        System.out.println("\n--- 6. 懒加载 ---");

        Lazy<Integer> expensiveValue = new Lazy<>(() -> {
            System.out.println("  计算中...");
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            return 42;
        });

        System.out.println("声明了 Lazy");
        System.out.println("第一次访问:");
        System.out.println("  值 = " + expensiveValue.get());
        System.out.println("第二次访问:");
        System.out.println("  值 = " + expensiveValue.get());  // 不会重新计算

        // ============================================
        // 7. 类型别名（Java 不支持，用注释说明）
        // ============================================
        System.out.println("\n--- 7. 类型别名（通过继承模拟）---");

        // Java 不支持类型别名，但可以通过创建包装类或使用泛型来实现类似效果
        UserDatabase db = new UserDatabase();
        db.put(1, "Alice");
        db.put(2, "Bob");
        db.put(3, "Charlie");

        System.out.println("findUserName(1) = " + db.findUserName(1));
        System.out.println("findUserName(99) = " + db.findUserName(99));

        // ============================================
        // 8. 依赖注入模式（类似自类型）
        // ============================================
        System.out.println("\n--- 8. 依赖注入模式 ---");

        Service service = new Service(new ConsoleLogger());
        System.out.println(service.query("SELECT * FROM users"));

        System.out.println("\n==================================================");
        System.out.println("第六课完成！运行 JL07_Practice 进行综合练习");
        System.out.println("==================================================");
    }

    // ============================================
    // 辅助方法
    // ============================================

    public static Optional<Double> divide(int a, int b) {
        if (b == 0) return Optional.empty();
        return Optional.of((double) a / b);
    }

    public static Optional<String> findUser(int id) {
        if (id > 0) return Optional.of("User" + id);
        return Optional.empty();
    }

    public static Optional<String> findEmail(String user) {
        return Optional.of(user + "@example.com");
    }

    public static JEither<String, Double> divideEither(int a, int b) {
        if (b == 0) return JEither.left("除数不能为零");
        return JEither.right((double) a / b);
    }

    public static JTry<Integer> parseNumber(String s) {
        return JTry.of(() -> Integer.parseInt(s));
    }

    public static <A, B> Pair<B, A> swap(Pair<A, B> pair) {
        return new Pair<>(pair.second, pair.first);
    }

    public static <T> T maximum(T a, T b, Comparator<T> comparator) {
        return comparator.compare(a, b) > 0 ? a : b;
    }
}

// ============================================
// JEither 类（模拟 Scala 的 Either，避免命名冲突）
// ============================================

class JEither<L, R> {
    private final L left;
    private final R right;
    private final boolean isRight;

    private JEither(L left, R right, boolean isRight) {
        this.left = left;
        this.right = right;
        this.isRight = isRight;
    }

    public static <L, R> JEither<L, R> left(L value) {
        return new JEither<>(value, null, false);
    }

    public static <L, R> JEither<L, R> right(R value) {
        return new JEither<>(null, value, true);
    }

    public boolean isRight() { return isRight; }
    public boolean isLeft() { return !isRight; }
    public L getLeft() { return left; }
    public R getRight() { return right; }

    public <T> JEither<L, T> map(Function<R, T> f) {
        if (isRight) return JEither.right(f.apply(right));
        return JEither.left(left);
    }

    @Override
    public String toString() {
        return isRight ? "Right(" + right + ")" : "Left(" + left + ")";
    }
}

// ============================================
// JTry 类（模拟 Scala 的 Try，避免命名冲突）
// ============================================

class JTry<T> {
    private final T value;
    private final Exception exception;
    private final boolean isSuccess;

    private JTry(T value, Exception exception, boolean isSuccess) {
        this.value = value;
        this.exception = exception;
        this.isSuccess = isSuccess;
    }

    public static <T> JTry<T> of(Supplier<T> supplier) {
        try {
            return new JTry<>(supplier.get(), null, true);
        } catch (Exception e) {
            return new JTry<>(null, e, false);
        }
    }

    public static <T> JTry<T> success(T value) {
        return new JTry<>(value, null, true);
    }

    public static <T> JTry<T> failure(Exception e) {
        return new JTry<>(null, e, false);
    }

    public boolean isSuccess() { return isSuccess; }
    public boolean isFailure() { return !isSuccess; }
    public T get() { return value; }
    public Exception getException() { return exception; }

    public T getOrElse(T defaultValue) {
        return isSuccess ? value : defaultValue;
    }

    public <U> JTry<U> map(Function<T, U> f) {
        if (isSuccess) {
            try {
                return JTry.success(f.apply(value));
            } catch (Exception e) {
                return JTry.failure(e);
            }
        }
        return JTry.failure(exception);
    }

    public <U> JTry<U> flatMap(Function<T, JTry<U>> f) {
        if (isSuccess) {
            try {
                return f.apply(value);
            } catch (Exception e) {
                return JTry.failure(e);
            }
        }
        return JTry.failure(exception);
    }

    public JTry<T> recover(Function<Exception, T> f) {
        if (isFailure()) {
            try {
                return JTry.success(f.apply(exception));
            } catch (Exception e) {
                return JTry.failure(e);
            }
        }
        return this;
    }

    public Optional<T> toOptional() {
        return isSuccess ? Optional.of(value) : Optional.empty();
    }

    @Override
    public String toString() {
        return isSuccess ? "Success(" + value + ")" : "Failure(" + exception.getMessage() + ")";
    }
}

// ============================================
// Box 泛型类
// ============================================

class Box<T> {
    private final T content;

    public Box(T content) {
        this.content = content;
    }

    public T getContent() { return content; }

    public <U> Box<U> map(Function<T, U> f) {
        return new Box<>(f.apply(content));
    }

    @Override
    public String toString() {
        return "Box(" + content + ")";
    }
}

// ============================================
// Pair 类
// ============================================

class Pair<A, B> {
    final A first;
    final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}

// ============================================
// Lazy 类（懒加载）
// ============================================

class Lazy<T> {
    private Supplier<T> supplier;
    private T value;
    private boolean computed = false;

    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public synchronized T get() {
        if (!computed) {
            value = supplier.get();
            computed = true;
            supplier = null;  // 释放引用
        }
        return value;
    }
}

// ============================================
// 辅助类
// ============================================

class PersonRecord {
    final String name;
    final int age;

    PersonRecord(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person(" + name + ", " + age + ")";
    }
}

// 类型别名模拟
class UserDatabase extends HashMap<Integer, String> {
    public Optional<String> findUserName(int id) {
        return Optional.ofNullable(get(id));
    }
}

// 依赖注入
interface Logger {
    void log(String msg);
}

class ConsoleLogger implements Logger {
    @Override
    public void log(String msg) {
        System.out.println("[LOG] " + msg);
    }
}

class Service {
    private final Logger logger;

    public Service(Logger logger) {
        this.logger = logger;
    }

    public String query(String sql) {
        logger.log("执行查询: " + sql);
        return "结果: " + sql;
    }
}
