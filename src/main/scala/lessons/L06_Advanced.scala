package lessons

/**
 * 第六课：高级特性
 *
 * 学习目标：
 * - Option / Either / Try
 * - 隐式转换和隐式参数
 * - 类型参数（泛型）
 * - 上下文边界
 * - 懒加载
 */
object L06_Advanced extends App {

  println("=" * 50)
  println("第六课：高级特性")
  println("=" * 50)

  // ============================================
  // 1. Option - 处理可能为空的值
  // ============================================
  println("\n--- 1. Option ---")

  // Option 有两个子类：Some(value) 和 None
  def divide(a: Int, b: Int): Option[Double] = {
    if (b == 0) None
    else Some(a.toDouble / b)
  }

  println(s"divide(10, 2) = ${divide(10, 2)}")
  println(s"divide(10, 0) = ${divide(10, 0)}")

  // 使用 Option 的方法
  val result = divide(10, 2)
  println(s"getOrElse: ${result.getOrElse(0.0)}")
  println(s"map: ${result.map(_ * 2)}")
  println(s"filter: ${result.filter(_ > 3)}")

  // Option 链式操作
  def findUser(id: Int): Option[String] = if (id > 0) Some(s"User$id") else None
  def findEmail(user: String): Option[String] = Some(s"$user@example.com")

  val email = findUser(1).flatMap(findEmail)
  println(s"链式查询: $email")

  // for 推导式处理 Option
  val fullInfo = for {
    user <- findUser(1)
    email <- findEmail(user)
  } yield s"$user <$email>"
  println(s"for推导式: $fullInfo")

  // ============================================
  // 2. Either - 表示两种可能的结果
  // ============================================
  println("\n--- 2. Either ---")

  // Either[Left, Right]：Left 通常表示错误，Right 表示成功
  def divideEither(a: Int, b: Int): Either[String, Double] = {
    if (b == 0) Left("除数不能为零")
    else Right(a.toDouble / b)
  }

  println(s"divideEither(10, 2) = ${divideEither(10, 2)}")
  println(s"divideEither(10, 0) = ${divideEither(10, 0)}")

  // 处理 Either
  divideEither(10, 2) match {
    case Right(value) => println(s"成功: $value")
    case Left(error) => println(s"错误: $error")
  }

  // Either 是右偏的，可以用 map/flatMap
  val calculated = divideEither(10, 2).map(_ * 100)
  println(s"Either map: $calculated")

  // ============================================
  // 3. Try - 处理异常
  // ============================================
  println("\n--- 3. Try ---")

  import scala.util.{Try, Success, Failure}

  def parseNumber(s: String): Try[Int] = Try(s.toInt)

  println(s"parseNumber(\"123\") = ${parseNumber("123")}")
  println(s"parseNumber(\"abc\") = ${parseNumber("abc")}")

  // 处理 Try
  parseNumber("123") match {
    case Success(n) => println(s"解析成功: $n")
    case Failure(e) => println(s"解析失败: ${e.getMessage}")
  }

  // Try 的方法
  val parsed = parseNumber("456")
  println(s"getOrElse: ${parsed.getOrElse(0)}")
  println(s"map: ${parsed.map(_ * 2)}")
  println(s"toOption: ${parsed.toOption}")
  println(s"toEither: ${parsed.toEither}")

  // 链式处理
  val computation = for {
    a <- parseNumber("10")
    b <- parseNumber("20")
  } yield a + b
  println(s"链式Try计算: $computation")

  // recover 处理异常
  val recovered = parseNumber("abc").recover {
    case _: NumberFormatException => -1
  }
  println(s"recover: $recovered")

  // ============================================
  // 4. 隐式参数
  // ============================================
  println("\n--- 4. 隐式参数 ---")

  // 定义隐式值
  case class Config(appName: String, version: String)
  implicit val defaultConfig: Config = Config("MyApp", "1.0.0")

  // 使用隐式参数
  def showAppInfo()(implicit config: Config): String = {
    s"${config.appName} v${config.version}"
  }

  println(s"隐式配置: ${showAppInfo()}")  // 自动使用 defaultConfig

  // 显式传递
  val customConfig = Config("CustomApp", "2.0.0")
  println(s"显式配置: ${showAppInfo()(customConfig)}")

  // ============================================
  // 5. 隐式转换
  // ============================================
  println("\n--- 5. 隐式转换 ---")

  // 隐式类：为现有类型添加方法
  implicit class RichInt(val n: Int) extends AnyVal {
    def times(f: => Unit): Unit = (1 to n).foreach(_ => f)
    def squared: Int = n * n
    def isEven: Boolean = n % 2 == 0
  }

  println("执行3次:")
  3.times(print("* "))
  println()

  println(s"5.squared = ${5.squared}")
  println(s"4.isEven = ${4.isEven}")

  // 为 String 添加方法
  implicit class RichString(val s: String) extends AnyVal {
    def shout: String = s.toUpperCase + "!"
    def words: List[String] = s.split("\\s+").toList
  }

  println(s"\"hello world\".shout = ${"hello world".shout}")
  println(s"\"hello world\".words = ${"hello world".words}")

  // ============================================
  // 6. 类型参数（泛型）
  // ============================================
  println("\n--- 6. 类型参数 ---")

  // 泛型类
  class Box[T](val content: T) {
    def map[U](f: T => U): Box[U] = new Box(f(content))
    override def toString: String = s"Box($content)"
  }

  val intBox = new Box(42)
  val stringBox = new Box("Hello")
  val mappedBox = intBox.map(_ * 2)

  println(s"intBox: $intBox")
  println(s"stringBox: $stringBox")
  println(s"mappedBox: $mappedBox")

  // 泛型方法
  def swap[A, B](pair: (A, B)): (B, A) = (pair._2, pair._1)
  println(s"swap((1, \"one\")) = ${swap((1, "one"))}")

  // 类型边界
  class Container[T <: AnyRef](val item: T)  // T 必须是引用类型
  // class Container[T >: Null](val item: T)  // T 必须是 Null 的超类型

  // ============================================
  // 7. 上下文边界
  // ============================================
  println("\n--- 7. 上下文边界 ---")

  // 使用 Ordering 进行比较
  def maximum[T: Ordering](a: T, b: T): T = {
    val ord = implicitly[Ordering[T]]
    if (ord.gt(a, b)) a else b
  }

  println(s"maximum(3, 5) = ${maximum(3, 5)}")
  println(s"maximum(\"apple\", \"banana\") = ${maximum("apple", "banana")}")

  // 自定义排序
  case class Person(name: String, age: Int)
  implicit val personOrdering: Ordering[Person] = Ordering.by(_.age)

  val p1 = Person("Alice", 30)
  val p2 = Person("Bob", 25)
  println(s"maximum($p1, $p2) = ${maximum(p1, p2)}")

  // ============================================
  // 8. 懒加载 (lazy)
  // ============================================
  println("\n--- 8. 懒加载 ---")

  lazy val expensiveValue = {
    println("  计算中...")
    Thread.sleep(100)
    42
  }

  println("声明了 lazy val")
  println("第一次访问:")
  println(s"  值 = $expensiveValue")
  println("第二次访问:")
  println(s"  值 = $expensiveValue")  // 不会重新计算

  // ============================================
  // 9. 类型别名
  // ============================================
  println("\n--- 9. 类型别名 ---")

  type UserId = Int
  type UserName = String
  type UserDatabase = Map[UserId, UserName]

  val users: UserDatabase = Map(
    1 -> "Alice",
    2 -> "Bob",
    3 -> "Charlie"
  )

  def findUserName(id: UserId)(implicit db: UserDatabase): Option[UserName] = {
    db.get(id)
  }

  implicit val defaultDb: UserDatabase = users
  println(s"findUserName(1) = ${findUserName(1)}")
  println(s"findUserName(99) = ${findUserName(99)}")

  // ============================================
  // 10. 自类型 (Self Type)
  // ============================================
  println("\n--- 10. 自类型 ---")

  trait Logger {
    def log(msg: String): Unit = println(s"[LOG] $msg")
  }

  trait Database { this: Logger =>  // 要求混入 Logger
    def query(sql: String): String = {
      log(s"执行查询: $sql")
      s"结果: $sql"
    }
  }

  class Service extends Database with Logger

  val service = new Service
  println(service.query("SELECT * FROM users"))

  println("\n" + "=" * 50)
  println("第六课完成！运行 L07_Practice 进行综合练习")
  println("=" * 50)
}
