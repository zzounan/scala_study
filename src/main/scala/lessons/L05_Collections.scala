package lessons

/**
 * 第五课：集合操作
 *
 * 学习目标：
 * - 不可变集合 vs 可变集合
 * - List, Set, Map 基本操作
 * - 常用高阶函数
 * - 集合转换
 */
object L05_Collections extends App {

  println("=" * 50)
  println("第五课：集合操作")
  println("=" * 50)

  // ============================================
  // 1. List（不可变链表）
  // ============================================
  println("\n--- 1. List ---")

  // 创建 List
  val numbers = List(1, 2, 3, 4, 5)
  val empty = List.empty[Int]
  val range = (1 to 5).toList
  val filled = List.fill(5)("x")  // 创建5个"x"

  println(s"numbers: $numbers")
  println(s"filled: $filled")

  // List 操作
  println(s"head: ${numbers.head}")           // 第一个元素
  println(s"tail: ${numbers.tail}")           // 除第一个外的所有元素
  println(s"last: ${numbers.last}")           // 最后一个元素
  println(s"init: ${numbers.init}")           // 除最后一个外的所有元素
  println(s"length: ${numbers.length}")
  println(s"isEmpty: ${numbers.isEmpty}")
  println(s"contains(3): ${numbers.contains(3)}")
  println(s"indexOf(3): ${numbers.indexOf(3)}")

  // 添加元素（返回新 List）
  val prepended = 0 :: numbers      // 前置
  val appended = numbers :+ 6       // 后置
  val concatenated = numbers ++ List(6, 7, 8)  // 连接

  println(s"0 :: numbers = $prepended")
  println(s"numbers :+ 6 = $appended")
  println(s"numbers ++ List(6,7,8) = $concatenated")

  // ============================================
  // 2. Vector（高效随机访问）
  // ============================================
  println("\n--- 2. Vector ---")

  val vector = Vector(1, 2, 3, 4, 5)
  println(s"vector: $vector")
  println(s"vector(2): ${vector(2)}")         // 随机访问 O(log32(n))
  println(s"vector.updated(2, 99): ${vector.updated(2, 99)}")  // 更新元素

  // ============================================
  // 3. Set（无重复元素）
  // ============================================
  println("\n--- 3. Set ---")

  val fruits = Set("apple", "banana", "cherry")
  val moreFruits = Set("banana", "date", "elderberry")

  println(s"fruits: $fruits")
  println(s"contains banana: ${fruits.contains("banana")}")
  println(s"fruits + date: ${fruits + "date"}")
  println(s"fruits - banana: ${fruits - "banana"}")

  // 集合运算
  println(s"并集: ${fruits | moreFruits}")
  println(s"交集: ${fruits & moreFruits}")
  println(s"差集: ${fruits &~ moreFruits}")

  // ============================================
  // 4. Map（键值对）
  // ============================================
  println("\n--- 4. Map ---")

  val scores = Map("Alice" -> 95, "Bob" -> 87, "Charlie" -> 92)

  println(s"scores: $scores")
  println(s"Alice的分数: ${scores("Alice")}")
  println(s"安全获取: ${scores.get("David")}")  // 返回 Option
  println(s"带默认值: ${scores.getOrElse("David", 0)}")

  // 添加/更新
  val updatedScores = scores + ("David" -> 88)
  val removedScores = scores - "Bob"

  println(s"添加 David: $updatedScores")
  println(s"移除 Bob: $removedScores")

  // 遍历
  println("遍历 Map:")
  for ((name, score) <- scores) {
    println(s"  $name: $score")
  }

  // ============================================
  // 5. 元组（Tuple）
  // ============================================
  println("\n--- 5. 元组 ---")

  val pair = ("Scala", 2003)
  val triple = ("Alice", 25, "Engineer")

  println(s"pair: $pair")
  println(s"pair._1: ${pair._1}, pair._2: ${pair._2}")

  // 解构
  val (language, year) = pair
  println(s"language: $language, year: $year")

  // swap
  println(s"pair.swap: ${pair.swap}")

  // ============================================
  // 6. 常用高阶函数
  // ============================================
  println("\n--- 6. 常用高阶函数 ---")

  val nums = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

  // map: 转换每个元素
  val doubled = nums.map(_ * 2)
  println(s"map(_ * 2): $doubled")

  // filter: 过滤元素
  val evens = nums.filter(_ % 2 == 0)
  println(s"filter(_ % 2 == 0): $evens")

  // filterNot: 反向过滤
  val odds = nums.filterNot(_ % 2 == 0)
  println(s"filterNot(_ % 2 == 0): $odds")

  // flatMap: map + flatten
  val nested = List(List(1, 2), List(3, 4), List(5))
  val flattened = nested.flatMap(x => x)
  println(s"flatMap: $flattened")

  val words = List("hello", "world")
  val chars = words.flatMap(_.toList)
  println(s"words.flatMap(_.toList): $chars")

  // fold/reduce: 聚合
  val sum = nums.reduce(_ + _)
  val product = nums.reduce(_ * _)
  println(s"reduce(_ + _): $sum")
  println(s"reduce(_ * _): $product")

  val sumWithInit = nums.foldLeft(100)(_ + _)  // 带初始值
  println(s"foldLeft(100)(_ + _): $sumWithInit")

  // 字符串拼接示例
  val sentence = words.foldLeft("说:")(_ + " " + _)
  println(s"foldLeft拼接: $sentence")

  // ============================================
  // 7. 更多集合操作
  // ============================================
  println("\n--- 7. 更多集合操作 ---")

  // take / drop
  println(s"take(3): ${nums.take(3)}")
  println(s"drop(3): ${nums.drop(3)}")
  println(s"takeWhile(_ < 5): ${nums.takeWhile(_ < 5)}")
  println(s"dropWhile(_ < 5): ${nums.dropWhile(_ < 5)}")

  // slice
  println(s"slice(2, 5): ${nums.slice(2, 5)}")

  // grouped / sliding
  println(s"grouped(3): ${nums.grouped(3).toList}")
  println(s"sliding(3): ${nums.sliding(3).toList}")

  // zip / unzip
  val names = List("Alice", "Bob", "Charlie")
  val ages = List(25, 30, 35)
  val zipped = names.zip(ages)
  println(s"zip: $zipped")

  val (unzippedNames, unzippedAges) = zipped.unzip
  println(s"unzip: names=$unzippedNames, ages=$unzippedAges")

  // zipWithIndex
  val indexed = names.zipWithIndex
  println(s"zipWithIndex: $indexed")

  // partition: 分成两组
  val (under5, over5) = nums.partition(_ < 5)
  println(s"partition(_ < 5): under5=$under5, over5=$over5")

  // groupBy: 分组
  val byEvenOdd = nums.groupBy(_ % 2 == 0)
  println(s"groupBy(_ % 2 == 0): $byEvenOdd")

  // ============================================
  // 8. 排序
  // ============================================
  println("\n--- 8. 排序 ---")

  val unsorted = List(3, 1, 4, 1, 5, 9, 2, 6)
  println(s"sorted: ${unsorted.sorted}")
  println(s"sortWith(_ > _): ${unsorted.sortWith(_ > _)}")

  case class Person(name: String, age: Int)
  val people = List(Person("Alice", 30), Person("Bob", 25), Person("Charlie", 35))
  val sortedByAge = people.sortBy(_.age)
  println(s"sortBy(_.age): $sortedByAge")

  // ============================================
  // 9. 可变集合
  // ============================================
  println("\n--- 9. 可变集合 ---")

  import scala.collection.mutable

  // 可变 ArrayBuffer
  val buffer = mutable.ArrayBuffer(1, 2, 3)
  buffer += 4              // 添加元素
  buffer ++= List(5, 6)    // 添加多个元素
  buffer -= 1              // 移除元素
  println(s"ArrayBuffer: $buffer")

  // 可变 Map
  val mutableMap = mutable.Map("a" -> 1, "b" -> 2)
  mutableMap("c") = 3      // 添加
  mutableMap("a") = 10     // 更新
  mutableMap -= "b"        // 移除
  println(s"mutable.Map: $mutableMap")

  // 可变 Set
  val mutableSet = mutable.Set(1, 2, 3)
  mutableSet += 4
  mutableSet -= 1
  println(s"mutable.Set: $mutableSet")

  // ============================================
  // 10. for 推导式
  // ============================================
  println("\n--- 10. for 推导式 ---")

  // 基本 for 推导
  val squares = for (i <- 1 to 5) yield i * i
  println(s"平方: $squares")

  // 多重循环
  val combinations = for {
    i <- 1 to 3
    j <- 1 to 3
    if i != j
  } yield (i, j)
  println(s"组合 (i != j): $combinations")

  // 与 Option 配合
  val maybeNumbers = List(Some(1), None, Some(3), None, Some(5))
  val extracted = for {
    maybeNum <- maybeNumbers
    num <- maybeNum
  } yield num * 2
  println(s"Option 推导: $extracted")

  println("\n" + "=" * 50)
  println("第五课完成！运行 L06_Advanced 继续学习")
  println("=" * 50)
}
