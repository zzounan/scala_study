package lessons;

/**
 * 第一课：Java 基础语法（对比 Scala）
 *
 * 学习目标：
 * - 变量定义 (final vs 普通变量)
 * - 基本数据类型
 * - 字符串操作
 * - 控制结构
 */
public class JL01_BasicSyntax {

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("第一课：Java 基础语法");
        System.out.println("==================================================");

        // ============================================
        // 1. 变量定义
        // ============================================
        System.out.println("\n--- 1. 变量定义 ---");

        // final: 不可变变量（类似 Scala 的 val）
        final String name = "Java";
        final int year = 1995;

        // 普通变量（类似 Scala 的 var）
        int counter = 0;
        counter = counter + 1;  // 可以重新赋值

        // Java 8 需要显式声明类型
        String language = "Java";
        double version = 8.0;
        boolean isAwesome = true;

        System.out.println("语言: " + language + ", 版本: " + version + ", 好用: " + isAwesome);

        // ============================================
        // 2. 基本数据类型
        // ============================================
        System.out.println("\n--- 2. 基本数据类型 ---");

        byte byteVal = 127;                        // 8位有符号整数
        short shortVal = 32767;                    // 16位有符号整数
        int intVal = 2147483647;                   // 32位有符号整数
        long longVal = 9223372036854775807L;       // 64位有符号整数
        float floatVal = 3.14f;                    // 32位浮点数
        double doubleVal = 3.14159;                // 64位浮点数
        char charVal = 'A';                        // 16位Unicode字符
        boolean boolVal = true;                    // 布尔值

        System.out.println("Int最大值: " + intVal);
        System.out.println("Double值: " + doubleVal);

        // ============================================
        // 3. 字符串操作
        // ============================================
        System.out.println("\n--- 3. 字符串操作 ---");

        String str1 = "Hello";
        String str2 = "World";

        // 字符串拼接
        String concat = str1 + " " + str2;
        System.out.println("拼接: " + concat);

        // 字符串格式化（类似 Scala 的 s 插值器）
        String greeting = String.format("%s %s! 今年是 %d 年", str1, str2, 2020 + 5);
        System.out.println("格式化: " + greeting);

        // 多行字符串（Java 8 方式）
        String multiline = "这是多行字符串\n" +
                "可以跨越多行\n" +
                "使用 + 连接";
        System.out.println("多行字符串:\n" + multiline);

        // 格式化数字
        double pi = 3.14159;
        String formatted = String.format("圆周率约等于 %.2f", pi);
        System.out.println(formatted);

        // ============================================
        // 4. 控制结构
        // ============================================
        System.out.println("\n--- 4. 控制结构 ---");

        // if-else 语句
        int score = 85;
        String grade;
        if (score >= 90) {
            grade = "A";
        } else if (score >= 80) {
            grade = "B";
        } else if (score >= 70) {
            grade = "C";
        } else {
            grade = "D";
        }
        System.out.println("分数 " + score + " 对应等级: " + grade);

        // for 循环
        System.out.print("for 循环 1到5: ");
        for (int i = 1; i <= 5; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        System.out.print("for 循环 1到4: ");
        for (int i = 1; i < 5; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        // for 循环带条件（类似 Scala 的守卫）
        System.out.print("偶数: ");
        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                System.out.print(i + " ");
            }
        }
        System.out.println();

        // 增强 for 循环
        int[] numbers = {1, 2, 3, 4, 5};
        System.out.print("增强 for: ");
        for (int n : numbers) {
            System.out.print(n + " ");
        }
        System.out.println();

        // while 循环
        System.out.print("while 循环: ");
        int count = 0;
        while (count < 3) {
            System.out.print(count + " ");
            count++;
        }
        System.out.println();

        // ============================================
        // 5. 表达式 vs 语句
        // ============================================
        System.out.println("\n--- 5. 表达式 vs 语句 ---");

        // Java 中 if 是语句，不是表达式（与 Scala 不同）
        // 但可以使用三元运算符
        int x = 10;
        int y = 20;
        int result = x + y;
        System.out.println("计算结果: " + result);

        // 三元运算符（类似 Scala 的 if 表达式）
        String message = result > 25 ? "大于25" : "小于等于25";
        System.out.println("判断: " + message);

        // switch 语句 (Java 8)
        int day = 3;
        String dayName;
        switch (day) {
            case 1: dayName = "周一"; break;
            case 2: dayName = "周二"; break;
            case 3: dayName = "周三"; break;
            case 4: dayName = "周四"; break;
            case 5: dayName = "周五"; break;
            case 6: dayName = "周六"; break;
            case 7: dayName = "周日"; break;
            default: dayName = "无效"; break;
        }
        System.out.println("今天是: " + dayName);

        System.out.println("\n==================================================");
        System.out.println("第一课完成！运行 JL02_Functions 继续学习");
        System.out.println("==================================================");
    }
}
