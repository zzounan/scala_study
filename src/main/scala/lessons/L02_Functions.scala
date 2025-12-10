package lessons

/**
 * 第二课：函数与方法
 *
 * 学习目标：
 * - 方法定义
 * - 函数字面量（Lambda）
 * - 高阶函数
 * - 柯里化
 * - 部分应用函数
 */
object L02_Functions extends App {

  println("=" * 50)
  println("第二课：函数与方法")
  println("=" * 50)

  // ============================================
  // 1. 方法定义
  // ============================================
  println("\n--- 1. 方法定义 ---")

  // 标准方法定义
  def add(x: Int, y: Int): Int = {
    x + y  // 最后一个表达式就是返回值
  }

  // 简化写法（单行方法可省略花括号）
  def multiply(x: Int, y: Int): Int = x * y

  // 返回类型可推断（但推荐显式声明）
  def subtract(x: Int, y: Int) = x - y

  // 无参数方法
  def sayHello(): String = "Hello!"
  def currentTime: Long = System.currentTimeMillis()  // 无副作用时可省略括号

  // 无返回值方法（返回 Unit）
  def printMessage(msg: String): Unit = {
    println(s"消息: $msg")
  }

  println(s"add(3, 5) = ${add(3, 5)}")
  println(s"multiply(4, 6) = ${multiply(4, 6)}")
  println(s"sayHello() = ${sayHello()}")

  // ============================================
  // 2. 默认参数和命名参数
  // ============================================
  println("\n--- 2. 默认参数和命名参数 ---")

  def greet(name: String, greeting: String = "Hello", punctuation: String = "!"): String = {
    s"$greeting, $name$punctuation"
  }

  println(greet("Alice"))                           // 使用默认值
  println(greet("Bob", "Hi"))                       // 覆盖部分默认值
  println(greet("Charlie", "Hey", "~"))             // 覆盖所有默认值
  println(greet(name = "David", punctuation = "?")) // 命名参数

  // ============================================
  // 3. 可变参数
  // ============================================
  println("\n--- 3. 可变参数 ---")

  def sum(numbers: Int*): Int = {
    numbers.sum
  }

  println(s"sum(1, 2, 3) = ${sum(1, 2, 3)}")
  println(s"sum(1, 2, 3, 4, 5) = ${sum(1, 2, 3, 4, 5)}")

  // 使用 _* 展开序列
  val nums = List(1, 2, 3, 4, 5)
  println(s"sum(nums: _*) = ${sum(nums: _*)}")

  // ============================================
  // 4. 函数字面量（Lambda / 匿名函数）
  // ============================================
  println("\n--- 4. 函数字面量 ---")

  // 完整写法
  val addFunc: (Int, Int) => Int = (x: Int, y: Int) => x + y

  // 简化写法（类型推断）
  val multiplyFunc = (x: Int, y: Int) => x * y

  // 更简化（使用占位符 _）
  val double: Int => Int = _ * 2
  val addOne: Int => Int = _ + 1

  println(s"addFunc(3, 4) = ${addFunc(3, 4)}")
  println(s"double(5) = ${double(5)}")

  // ============================================
  // 5. 高阶函数
  // ============================================
  println("\n--- 5. 高阶函数 ---")

  // 函数作为参数
  def applyOperation(x: Int, y: Int, operation: (Int, Int) => Int): Int = {
    operation(x, y)
  }

  println(s"applyOperation(10, 3, add) = ${applyOperation(10, 3, add)}")
  println(s"applyOperation(10, 3, subtract) = ${applyOperation(10, 3, subtract)}")
  println(s"applyOperation(10, 3, (a, b) => a / b) = ${applyOperation(10, 3, (a, b) => a / b)}")

  // 函数作为返回值
  def createMultiplier(factor: Int): Int => Int = {
    (x: Int) => x * factor
  }

  val triple = createMultiplier(3)
  val quadruple = createMultiplier(4)
  println(s"triple(5) = ${triple(5)}")
  println(s"quadruple(5) = ${quadruple(5)}")

  // ============================================
  // 6. 柯里化（Currying）
  // ============================================
  println("\n--- 6. 柯里化 ---")

  // 柯里化方法：多个参数列表
  def addCurried(x: Int)(y: Int): Int = x + y

  println(s"addCurried(3)(5) = ${addCurried(3)(5)}")

  // 部分应用
  val addFive = addCurried(5) _
  println(s"addFive(10) = ${addFive(10)}")

  // 实际应用：自定义控制结构
  def withResource[T](resource: String)(operation: String => T): T = {
    println(s"  打开资源: $resource")
    val result = operation(resource)
    println(s"  关闭资源: $resource")
    result
  }

  println("使用 withResource:")
  val content = withResource("data.txt") { name =>
    s"读取 $name 的内容"
  }
  println(s"  结果: $content")

  // ============================================
  // 7. 部分应用函数
  // ============================================
  println("\n--- 7. 部分应用函数 ---")

  def formatDate(year: Int, month: Int, day: Int): String = {
    f"$year-$month%02d-$day%02d"
  }

  // 固定年份
  val format2024 = formatDate(2024, _: Int, _: Int)
  println(s"format2024(12, 25) = ${format2024(12, 25)}")

  // 固定年份和月份
  val formatDec2024 = formatDate(2024, 12, _: Int)
  println(s"formatDec2024(31) = ${formatDec2024(31)}")

  // ============================================
  // 8. 递归函数
  // ============================================
  println("\n--- 8. 递归函数 ---")

  // 普通递归
  def factorial(n: Int): BigInt = {
    if (n <= 1) 1
    else n * factorial(n - 1)
  }

  println(s"factorial(10) = ${factorial(10)}")

  // 尾递归（使用 @tailrec 注解确保编译器优化）
  import scala.annotation.tailrec

  def factorialTailRec(n: Int): BigInt = {
    @tailrec
    def loop(current: Int, accumulator: BigInt): BigInt = {
      if (current <= 1) accumulator
      else loop(current - 1, current * accumulator)
    }
    loop(n, 1)
  }

  println(s"factorialTailRec(10) = ${factorialTailRec(10)}")
  println(s"factorialTailRec(100) = ${factorialTailRec(100)}")

  // ============================================
  // 9. 闭包
  // ============================================
  println("\n--- 9. 闭包 ---")

  var multiplier = 3
  val closureFunc = (x: Int) => x * multiplier  // 捕获外部变量

  println(s"multiplier = 3 时, closureFunc(10) = ${closureFunc(10)}")
  multiplier = 5
  println(s"multiplier = 5 时, closureFunc(10) = ${closureFunc(10)}")

  println("\n" + "=" * 50)
  println("第二课完成！运行 L03_OOP 继续学习")
  println("=" * 50)
}
