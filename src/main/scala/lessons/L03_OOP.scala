package lessons

/**
 * 第三课：面向对象编程
 *
 * 学习目标：
 * - 类和对象
 * - 构造器
 * - 继承
 * - Trait（特质）
 * - 单例对象和伴生对象
 * - Case Class
 */
object L03_OOP extends App {

  println("=" * 50)
  println("第三课：面向对象编程")
  println("=" * 50)

  // ============================================
  // 1. 类定义
  // ============================================
  println("\n--- 1. 类定义 ---")

  // 基本类定义
  class Person(val name: String, var age: Int) {
    // val 参数：生成只读字段
    // var 参数：生成可读写字段

    // 辅助构造器
    def this(name: String) = this(name, 0)

    // 方法
    def introduce(): String = s"我叫 $name，今年 $age 岁"

    // 重写 toString
    override def toString: String = s"Person($name, $age)"
  }

  val person1 = new Person("Alice", 25)
  val person2 = new Person("Bob")  // 使用辅助构造器

  println(person1.introduce())
  println(s"person2.age = ${person2.age}")
  person2.age = 30  // var 可以修改
  println(s"修改后 person2.age = ${person2.age}")

  // ============================================
  // 2. 访问修饰符
  // ============================================
  println("\n--- 2. 访问修饰符 ---")

  class BankAccount(initialBalance: Double) {
    private var _balance: Double = initialBalance  // 私有字段

    // Getter
    def balance: Double = _balance

    // 公开方法
    def deposit(amount: Double): Unit = {
      if (amount > 0) _balance += amount
    }

    def withdraw(amount: Double): Boolean = {
      if (amount > 0 && amount <= _balance) {
        _balance -= amount
        true
      } else false
    }
  }

  val account = new BankAccount(1000)
  println(s"初始余额: ${account.balance}")
  account.deposit(500)
  println(s"存入500后: ${account.balance}")
  account.withdraw(200)
  println(s"取出200后: ${account.balance}")

  // ============================================
  // 3. 继承
  // ============================================
  println("\n--- 3. 继承 ---")

  // 基类
  class Animal(val name: String) {
    def speak(): String = s"$name 发出声音"
  }

  // 子类
  class Dog(name: String, val breed: String) extends Animal(name) {
    override def speak(): String = s"$name (${breed}) 说: 汪汪!"
  }

  class Cat(name: String) extends Animal(name) {
    override def speak(): String = s"$name 说: 喵~"
  }

  val dog = new Dog("旺财", "金毛")
  val cat = new Cat("咪咪")

  println(dog.speak())
  println(cat.speak())

  // 多态
  val animals: List[Animal] = List(dog, cat, new Animal("未知动物"))
  println("所有动物:")
  animals.foreach(a => println(s"  ${a.speak()}"))

  // ============================================
  // 4. 抽象类
  // ============================================
  println("\n--- 4. 抽象类 ---")

  abstract class Shape {
    def area: Double          // 抽象方法
    def perimeter: Double     // 抽象方法
    def describe(): String = s"面积: $area, 周长: $perimeter"  // 具体方法
  }

  class Rectangle(val width: Double, val height: Double) extends Shape {
    def area: Double = width * height
    def perimeter: Double = 2 * (width + height)
  }

  class Circle(val radius: Double) extends Shape {
    def area: Double = math.Pi * radius * radius
    def perimeter: Double = 2 * math.Pi * radius
  }

  val rect = new Rectangle(5, 3)
  val circle = new Circle(4)

  println(s"矩形: ${rect.describe()}")
  println(s"圆形: ${circle.describe()}")

  // ============================================
  // 5. Trait（特质）
  // ============================================
  println("\n--- 5. Trait（特质）---")

  // Trait 类似于 Java 接口，但可以有实现
  trait Flyable {
    def fly(): String = "飞行中..."
    def altitude: Int  // 抽象成员
  }

  trait Swimmable {
    def swim(): String = "游泳中..."
  }

  // 混入多个 Trait
  class Duck(name: String) extends Animal(name) with Flyable with Swimmable {
    def altitude: Int = 100
    override def speak(): String = s"$name 说: 嘎嘎!"
  }

  val duck = new Duck("唐老鸭")
  println(duck.speak())
  println(duck.fly())
  println(duck.swim())
  println(s"飞行高度: ${duck.altitude}米")

  // Trait 作为类型
  def makeItFly(flyer: Flyable): Unit = {
    println(s"  ${flyer.fly()} 高度: ${flyer.altitude}米")
  }
  makeItFly(duck)

  // ============================================
  // 6. 单例对象（Object）
  // ============================================
  println("\n--- 6. 单例对象 ---")

  // object 定义单例
  object MathUtils {
    val PI = 3.14159
    def square(x: Double): Double = x * x
    def cube(x: Double): Double = x * x * x
  }

  println(s"PI = ${MathUtils.PI}")
  println(s"square(5) = ${MathUtils.square(5)}")
  println(s"cube(3) = ${MathUtils.cube(3)}")

  // ============================================
  // 7. 伴生对象
  // ============================================
  println("\n--- 7. 伴生对象 ---")

  // 伴生对象与类同名，可以访问彼此的私有成员
  class Counter private(val count: Int)  // 私有构造器

  object Counter {
    private var instances = 0

    // 工厂方法
    def apply(): Counter = {
      instances += 1
      new Counter(instances)
    }

    def totalInstances: Int = instances
  }

  val c1 = Counter()  // 使用 apply 方法，等同于 Counter.apply()
  val c2 = Counter()
  val c3 = Counter()

  println(s"c1.count = ${c1.count}")
  println(s"c2.count = ${c2.count}")
  println(s"c3.count = ${c3.count}")
  println(s"总实例数: ${Counter.totalInstances}")

  // ============================================
  // 8. Case Class（重要！）
  // ============================================
  println("\n--- 8. Case Class ---")

  // Case Class 自动提供：
  // - 不可变字段（默认 val）
  // - apply 工厂方法（不需要 new）
  // - unapply 提取器（用于模式匹配）
  // - equals 和 hashCode
  // - toString
  // - copy 方法

  case class Point(x: Int, y: Int)

  val p1 = Point(3, 4)        // 不需要 new
  val p2 = Point(3, 4)
  val p3 = p1.copy(x = 10)    // 复制并修改部分字段

  println(s"p1 = $p1")
  println(s"p2 = $p2")
  println(s"p1 == p2: ${p1 == p2}")  // 结构相等
  println(s"p3 = $p3")

  // Case Class 常用于数据传输
  case class User(id: Long, name: String, email: String)

  val user = User(1, "张三", "zhangsan@example.com")
  println(s"用户: $user")

  // ============================================
  // 9. 密封类（Sealed Class）
  // ============================================
  println("\n--- 9. 密封类 ---")

  // sealed 限制继承只能在同一文件中
  // 编译器可以检查模式匹配是否完整
  sealed trait Result
  case class Success(value: Int) extends Result
  case class Failure(message: String) extends Result

  def handleResult(result: Result): String = result match {
    case Success(v) => s"成功: $v"
    case Failure(m) => s"失败: $m"
  }

  println(handleResult(Success(42)))
  println(handleResult(Failure("出错了")))

  println("\n" + "=" * 50)
  println("第三课完成！运行 L04_PatternMatching 继续学习")
  println("=" * 50)
}
