package lessons

/**
 * 第四课：模式匹配
 *
 * 学习目标：
 * - match 表达式
 * - 各种模式类型
 * - 守卫条件
 * - 提取器
 * - 部分函数
 */
object L04_PatternMatching extends App {

  println("=" * 50)
  println("第四课：模式匹配")
  println("=" * 50)

  // ============================================
  // 1. 基本模式匹配
  // ============================================
  println("\n--- 1. 基本模式匹配 ---")

  def describeNumber(x: Int): String = x match {
    case 0 => "零"
    case 1 => "一"
    case 2 => "二"
    case _ => "其他数字"  // _ 是通配符，匹配任何值
  }

  println(s"0 -> ${describeNumber(0)}")
  println(s"2 -> ${describeNumber(2)}")
  println(s"99 -> ${describeNumber(99)}")

  // ============================================
  // 2. 类型匹配
  // ============================================
  println("\n--- 2. 类型匹配 ---")

  def describeType(x: Any): String = x match {
    case i: Int => s"整数: $i"
    case s: String => s"字符串: $s"
    case d: Double => s"浮点数: $d"
    case list: List[_] => s"列表，长度: ${list.length}"
    case _ => s"未知类型: ${x.getClass.getSimpleName}"
  }

  println(describeType(42))
  println(describeType("hello"))
  println(describeType(3.14))
  println(describeType(List(1, 2, 3)))
  println(describeType(true))

  // ============================================
  // 3. 守卫条件
  // ============================================
  println("\n--- 3. 守卫条件 ---")

  def classify(x: Int): String = x match {
    case n if n < 0 => "负数"
    case 0 => "零"
    case n if n % 2 == 0 => "正偶数"
    case n if n % 2 == 1 => "正奇数"
    case _ => "未知"  // 实际不会到达
  }

  println(s"-5 -> ${classify(-5)}")
  println(s"0 -> ${classify(0)}")
  println(s"4 -> ${classify(4)}")
  println(s"7 -> ${classify(7)}")

  // ============================================
  // 4. 元组模式
  // ============================================
  println("\n--- 4. 元组模式 ---")

  def describePair(pair: (Int, Int)): String = pair match {
    case (0, 0) => "原点"
    case (x, 0) => s"X轴上，x=$x"
    case (0, y) => s"Y轴上，y=$y"
    case (x, y) if x == y => s"对角线上，x=y=$x"
    case (x, y) => s"普通点 ($x, $y)"
  }

  println(describePair((0, 0)))
  println(describePair((5, 0)))
  println(describePair((0, 3)))
  println(describePair((4, 4)))
  println(describePair((2, 7)))

  // ============================================
  // 5. Case Class 模式
  // ============================================
  println("\n--- 5. Case Class 模式 ---")

  case class Person(name: String, age: Int)
  case class Address(city: String, street: String)
  case class Employee(person: Person, address: Address, salary: Double)

  def describeEmployee(emp: Employee): String = emp match {
    case Employee(Person(name, age), Address("北京", _), salary) if salary > 10000 =>
      s"$name 是北京的高薪员工"
    case Employee(Person(name, _), Address(city, _), _) =>
      s"$name 在 $city 工作"
  }

  val emp1 = Employee(Person("张三", 30), Address("北京", "朝阳路"), 15000)
  val emp2 = Employee(Person("李四", 25), Address("上海", "南京路"), 8000)

  println(describeEmployee(emp1))
  println(describeEmployee(emp2))

  // ============================================
  // 6. 列表模式
  // ============================================
  println("\n--- 6. 列表模式 ---")

  def describeList(list: List[Int]): String = list match {
    case Nil => "空列表"
    case List(x) => s"单元素列表: $x"
    case List(x, y) => s"两元素列表: $x 和 $y"
    case head :: tail => s"头: $head, 尾: $tail"  // :: 是 List 的构造器
  }

  println(describeList(Nil))
  println(describeList(List(1)))
  println(describeList(List(1, 2)))
  println(describeList(List(1, 2, 3, 4, 5)))

  // 递归处理列表
  def sumList(list: List[Int]): Int = list match {
    case Nil => 0
    case head :: tail => head + sumList(tail)
  }

  println(s"sumList(List(1,2,3,4,5)) = ${sumList(List(1, 2, 3, 4, 5))}")

  // ============================================
  // 7. Option 模式
  // ============================================
  println("\n--- 7. Option 模式 ---")

  def findUser(id: Int): Option[String] = {
    val users = Map(1 -> "Alice", 2 -> "Bob", 3 -> "Charlie")
    users.get(id)
  }

  def greetUser(id: Int): String = findUser(id) match {
    case Some(name) => s"你好, $name!"
    case None => "用户不存在"
  }

  println(greetUser(1))
  println(greetUser(2))
  println(greetUser(99))

  // ============================================
  // 8. 变量绑定 (@)
  // ============================================
  println("\n--- 8. 变量绑定 ---")

  case class Order(id: Int, items: List[String], total: Double)

  def processOrder(order: Order): String = order match {
    // @ 将匹配的部分绑定到变量
    case Order(_, items @ List(_, _, _), total) if total > 100 =>
      s"大订单，3件商品: ${items.mkString(", ")}"
    case Order(id, first :: _, _) =>
      s"订单 $id 的第一件商品: $first"
    case _ => "其他订单"
  }

  val order1 = Order(1, List("书", "笔", "本"), 150)
  val order2 = Order(2, List("手机", "充电器"), 2000)

  println(processOrder(order1))
  println(processOrder(order2))

  // ============================================
  // 9. 正则表达式模式
  // ============================================
  println("\n--- 9. 正则表达式模式 ---")

  val EmailPattern = """(\w+)@(\w+)\.(\w+)""".r
  val PhonePattern = """(\d{3})-(\d{4})-(\d{4})""".r

  def parseContact(contact: String): String = contact match {
    case EmailPattern(user, domain, ext) =>
      s"邮箱 - 用户: $user, 域名: $domain.$ext"
    case PhonePattern(area, first, second) =>
      s"电话 - 区号: $area, 号码: $first-$second"
    case _ => "未知格式"
  }

  println(parseContact("alice@gmail.com"))
  println(parseContact("138-1234-5678"))
  println(parseContact("invalid"))

  // ============================================
  // 10. 部分函数 (PartialFunction)
  // ============================================
  println("\n--- 10. 部分函数 ---")

  // 部分函数只对部分输入有定义
  val divide: PartialFunction[(Int, Int), Int] = {
    case (x, y) if y != 0 => x / y
  }

  println(s"divide.isDefinedAt((10, 2)) = ${divide.isDefinedAt((10, 2))}")
  println(s"divide.isDefinedAt((10, 0)) = ${divide.isDefinedAt((10, 0))}")
  println(s"divide((10, 2)) = ${divide((10, 2))}")

  // 与 collect 配合使用
  val numbers = List(-2, -1, 0, 1, 2)
  val positiveDoubled = numbers.collect {
    case n if n > 0 => n * 2
  }
  println(s"正数翻倍: $positiveDoubled")

  // ============================================
  // 11. 模式匹配在变量定义中的使用
  // ============================================
  println("\n--- 11. 模式匹配在变量定义中 ---")

  // 元组解构
  val (first, second, third) = (1, "two", 3.0)
  println(s"first=$first, second=$second, third=$third")

  // Case Class 解构
  val Person(userName, userAge) = Person("王五", 28)
  println(s"name=$userName, age=$userAge")

  // 列表解构
  val head :: next :: rest = List(1, 2, 3, 4, 5): @unchecked
  println(s"head=$head, next=$next, rest=$rest")

  // for 推导式中的模式匹配
  val pairs = List((1, "one"), (2, "two"), (3, "three"))
  for ((num, word) <- pairs) {
    println(s"  $num -> $word")
  }

  println("\n" + "=" * 50)
  println("第四课完成！运行 L05_Collections 继续学习")
  println("=" * 50)
}
