package lessons;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 第四课：模式匹配 (Java 版)
 *
 * 学习目标：
 * - switch 语句/表达式
 * - instanceof 类型检查
 * - 正则表达式
 * - Optional 处理
 *
 * 注意：Java 8 的模式匹配能力有限，这里展示等效实现
 */
public class JL04_PatternMatching {

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("第四课：模式匹配 (Java 版)");
        System.out.println("==================================================");

        // ============================================
        // 1. 基本模式匹配（switch）
        // ============================================
        System.out.println("\n--- 1. 基本模式匹配 (switch) ---");

        System.out.println("0 -> " + describeNumber(0));
        System.out.println("2 -> " + describeNumber(2));
        System.out.println("99 -> " + describeNumber(99));

        // ============================================
        // 2. 类型匹配（instanceof）
        // ============================================
        System.out.println("\n--- 2. 类型匹配 ---");

        System.out.println(describeType(42));
        System.out.println(describeType("hello"));
        System.out.println(describeType(3.14));
        System.out.println(describeType(Arrays.asList(1, 2, 3)));
        System.out.println(describeType(true));

        // ============================================
        // 3. 守卫条件（if-else）
        // ============================================
        System.out.println("\n--- 3. 守卫条件 ---");

        System.out.println("-5 -> " + classify(-5));
        System.out.println("0 -> " + classify(0));
        System.out.println("4 -> " + classify(4));
        System.out.println("7 -> " + classify(7));

        // ============================================
        // 4. 元组模式（使用类）
        // ============================================
        System.out.println("\n--- 4. 坐标点匹配 ---");

        System.out.println(describePair(0, 0));
        System.out.println(describePair(5, 0));
        System.out.println(describePair(0, 3));
        System.out.println(describePair(4, 4));
        System.out.println(describePair(2, 7));

        // ============================================
        // 5. 对象模式（手动解构）
        // ============================================
        System.out.println("\n--- 5. 对象模式 ---");

        Employee emp1 = new Employee(
                new PersonInfo("张三", 30),
                new Address("北京", "朝阳路"),
                15000
        );
        Employee emp2 = new Employee(
                new PersonInfo("李四", 25),
                new Address("上海", "南京路"),
                8000
        );

        System.out.println(describeEmployee(emp1));
        System.out.println(describeEmployee(emp2));

        // ============================================
        // 6. 列表模式（手动匹配）
        // ============================================
        System.out.println("\n--- 6. 列表模式 ---");

        System.out.println(describeList(Collections.emptyList()));
        System.out.println(describeList(Arrays.asList(1)));
        System.out.println(describeList(Arrays.asList(1, 2)));
        System.out.println(describeList(Arrays.asList(1, 2, 3, 4, 5)));

        // 递归处理列表
        System.out.println("sumList([1,2,3,4,5]) = " + sumList(Arrays.asList(1, 2, 3, 4, 5)));

        // ============================================
        // 7. Optional 模式
        // ============================================
        System.out.println("\n--- 7. Optional 模式 ---");

        System.out.println(greetUser(1));
        System.out.println(greetUser(2));
        System.out.println(greetUser(99));

        // ============================================
        // 8. 正则表达式模式
        // ============================================
        System.out.println("\n--- 8. 正则表达式模式 ---");

        System.out.println(parseContact("alice@gmail.com"));
        System.out.println(parseContact("138-1234-5678"));
        System.out.println(parseContact("invalid"));

        // ============================================
        // 9. 枚举匹配
        // ============================================
        System.out.println("\n--- 9. 枚举匹配 ---");

        System.out.println("LOW: " + describePriority(Priority.LOW));
        System.out.println("MEDIUM: " + describePriority(Priority.MEDIUM));
        System.out.println("HIGH: " + describePriority(Priority.HIGH));

        // ============================================
        // 10. 策略模式（替代部分函数）
        // ============================================
        System.out.println("\n--- 10. 策略模式 ---");

        System.out.println("safeDivide(10, 2) = " + safeDivide(10, 2));
        System.out.println("safeDivide(10, 0) = " + safeDivide(10, 0));

        // 使用 Stream 过滤和转换（类似 collect）
        List<Integer> numbers = Arrays.asList(-2, -1, 0, 1, 2);
        List<Integer> positiveDoubled = new ArrayList<>();
        for (int n : numbers) {
            if (n > 0) {
                positiveDoubled.add(n * 2);
            }
        }
        System.out.println("正数翻倍: " + positiveDoubled);

        System.out.println("\n==================================================");
        System.out.println("第四课完成！运行 JL05_Collections 继续学习");
        System.out.println("==================================================");
    }

    // ============================================
    // 辅助方法
    // ============================================

    public static String describeNumber(int x) {
        switch (x) {
            case 0: return "零";
            case 1: return "一";
            case 2: return "二";
            default: return "其他数字";
        }
    }

    public static String describeType(Object x) {
        if (x instanceof Integer) {
            return "整数: " + x;
        } else if (x instanceof String) {
            return "字符串: " + x;
        } else if (x instanceof Double) {
            return "浮点数: " + x;
        } else if (x instanceof List) {
            return "列表，长度: " + ((List<?>) x).size();
        } else {
            return "未知类型: " + x.getClass().getSimpleName();
        }
    }

    public static String classify(int x) {
        if (x < 0) {
            return "负数";
        } else if (x == 0) {
            return "零";
        } else if (x % 2 == 0) {
            return "正偶数";
        } else {
            return "正奇数";
        }
    }

    public static String describePair(int x, int y) {
        if (x == 0 && y == 0) {
            return "原点";
        } else if (y == 0) {
            return "X轴上，x=" + x;
        } else if (x == 0) {
            return "Y轴上，y=" + y;
        } else if (x == y) {
            return "对角线上，x=y=" + x;
        } else {
            return "普通点 (" + x + ", " + y + ")";
        }
    }

    public static String describeEmployee(Employee emp) {
        if ("北京".equals(emp.address.city) && emp.salary > 10000) {
            return emp.person.name + " 是北京的高薪员工";
        } else {
            return emp.person.name + " 在 " + emp.address.city + " 工作";
        }
    }

    public static String describeList(List<Integer> list) {
        if (list.isEmpty()) {
            return "空列表";
        } else if (list.size() == 1) {
            return "单元素列表: " + list.get(0);
        } else if (list.size() == 2) {
            return "两元素列表: " + list.get(0) + " 和 " + list.get(1);
        } else {
            return "头: " + list.get(0) + ", 尾: " + list.subList(1, list.size());
        }
    }

    public static int sumList(List<Integer> list) {
        if (list.isEmpty()) {
            return 0;
        } else {
            return list.get(0) + sumList(list.subList(1, list.size()));
        }
    }

    public static Optional<String> findUser(int id) {
        Map<Integer, String> users = new HashMap<>();
        users.put(1, "Alice");
        users.put(2, "Bob");
        users.put(3, "Charlie");
        return Optional.ofNullable(users.get(id));
    }

    public static String greetUser(int id) {
        Optional<String> user = findUser(id);
        if (user.isPresent()) {
            return "你好, " + user.get() + "!";
        } else {
            return "用户不存在";
        }
        // 或者使用: return user.map(name -> "你好, " + name + "!").orElse("用户不存在");
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile("(\\w+)@(\\w+)\\.(\\w+)");
    private static final Pattern PHONE_PATTERN = Pattern.compile("(\\d{3})-(\\d{4})-(\\d{4})");

    public static String parseContact(String contact) {
        Matcher emailMatcher = EMAIL_PATTERN.matcher(contact);
        Matcher phoneMatcher = PHONE_PATTERN.matcher(contact);

        if (emailMatcher.matches()) {
            String user = emailMatcher.group(1);
            String domain = emailMatcher.group(2);
            String ext = emailMatcher.group(3);
            return "邮箱 - 用户: " + user + ", 域名: " + domain + "." + ext;
        } else if (phoneMatcher.matches()) {
            String area = phoneMatcher.group(1);
            String first = phoneMatcher.group(2);
            String second = phoneMatcher.group(3);
            return "电话 - 区号: " + area + ", 号码: " + first + "-" + second;
        } else {
            return "未知格式";
        }
    }

    public static String describePriority(Priority priority) {
        switch (priority) {
            case LOW: return "低优先级";
            case MEDIUM: return "中优先级";
            case HIGH: return "高优先级";
            default: return "未知";
        }
    }

    public static Optional<Integer> safeDivide(int a, int b) {
        if (b == 0) {
            return Optional.empty();
        }
        return Optional.of(a / b);
    }
}

// 辅助类
class PersonInfo {
    final String name;
    final int age;

    PersonInfo(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

class Address {
    final String city;
    final String street;

    Address(String city, String street) {
        this.city = city;
        this.street = street;
    }
}

class Employee {
    final PersonInfo person;
    final Address address;
    final double salary;

    Employee(PersonInfo person, Address address, double salary) {
        this.person = person;
        this.address = address;
        this.salary = salary;
    }
}

enum Priority {
    LOW, MEDIUM, HIGH
}
