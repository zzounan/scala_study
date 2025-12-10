package lessons

/**
 * 第一课：Scala 基础语法
 *
 * 学习目标：
 * - 变量定义 (val vs var)
 * - 基本数据类型
 * - 字符串操作
 * - 控制结构
 */
object L01_BasicSyntax extends App {

  println("=" * 50)
  println("第一课：Scala 基础语法")
  println("=" * 50)

  // ============================================
  // 1. 变量定义
  // ============================================
  println("\n--- 1. 变量定义 ---")

  // val: 不可变变量（推荐使用，类似 Java 的 final）
  val name: String = "Scala"
  val year: Int = 2003

  // var: 可变变量（尽量少用）
  var counter: Int = 0
  counter = counter + 1  // 可以重新赋值

  // 类型推断：Scala 可以自动推断类型
  val language = "Scala"      // 自动推断为 String
  val version = 2.13          // 自动推断为 Double
  val isAwesome = true        // 自动推断为 Boolean

  println(s"语言: $language, 版本: $version, 好用: $isAwesome")

  // ============================================
  // 2. 基本数据类型
  // ============================================
  println("\n--- 2. 基本数据类型 ---")

  val byteVal: Byte = 127           // 8位有符号整数
  val shortVal: Short = 32767       // 16位有符号整数
  val intVal: Int = 2147483647      // 32位有符号整数
  val longVal: Long = 9223372036854775807L  // 64位有符号整数
  val floatVal: Float = 3.14f       // 32位浮点数
  val doubleVal: Double = 3.14159   // 64位浮点数
  val charVal: Char = 'A'           // 16位Unicode字符
  val boolVal: Boolean = true       // 布尔值

  println(s"Int最大值: $intVal")
  println(s"Double值: $doubleVal")

  // ============================================
  // 3. 字符串操作
  // ============================================
  println("\n--- 3. 字符串操作 ---")

  val str1 = "Hello"
  val str2 = "World"

  // 字符串拼接
  val concat = str1 + " " + str2
  println(s"拼接: $concat")

  // 字符串插值（s 插值器）
  val greeting = s"$str1 $str2! 今年是 ${2020 + 5} 年"
  println(s"插值: $greeting")

  // 原始字符串插值（raw 插值器，不处理转义）
  val path = raw"C:\Users\name\documents"
  println(s"原始字符串: $path")

  // 格式化插值（f 插值器）
  val pi = 3.14159
  val formatted = f"圆周率约等于 $pi%.2f"
  println(formatted)

  // 多行字符串
  val multiline =
    """
      |这是多行字符串
      |可以跨越多行
      |使用 stripMargin 去除前导 |
    """.stripMargin
  println(s"多行字符串:$multiline")

  // ============================================
  // 4. 控制结构
  // ============================================
  println("\n--- 4. 控制结构 ---")

  // if-else 表达式（有返回值！）
  val score = 85
  val grade = if (score >= 90) "A"
              else if (score >= 80) "B"
              else if (score >= 70) "C"
              else "D"
  println(s"分数 $score 对应等级: $grade")

  // for 循环
  println("for 循环 1到5:")
  for (i <- 1 to 5) {       // to 包含结束值
    print(s"$i ")
  }
  println()

  println("for 循环 1到4 (until):")
  for (i <- 1 until 5) {    // until 不包含结束值
    print(s"$i ")
  }
  println()

  // for 循环带守卫条件
  println("偶数 (使用守卫):")
  for (i <- 1 to 10 if i % 2 == 0) {
    print(s"$i ")
  }
  println()

  // for 推导式（yield 返回新集合）
  val doubled = for (i <- 1 to 5) yield i * 2
  println(s"数字翻倍: $doubled")

  // 嵌套 for 循环
  println("九九乘法表片段:")
  for {
    i <- 1 to 3
    j <- 1 to 3
  } {
    println(s"$i x $j = ${i * j}")
  }

  // while 循环
  println("while 循环:")
  var count = 0
  while (count < 3) {
    print(s"$count ")
    count += 1
  }
  println()

  // ============================================
  // 5. 表达式 vs 语句
  // ============================================
  println("\n--- 5. 表达式 vs 语句 ---")

  // Scala 中几乎一切都是表达式（有返回值）
  val result = {
    val x = 10
    val y = 20
    x + y  // 最后一行是返回值，不需要 return
  }
  println(s"代码块的结果: $result")

  // Unit 类型（类似 Java 的 void）
  val unitValue: Unit = println("这个打印语句返回 Unit")
  println(s"Unit 值: $unitValue")

  println("\n" + "=" * 50)
  println("第一课完成！运行 L02_Functions 继续学习")
  println("=" * 50)
}
