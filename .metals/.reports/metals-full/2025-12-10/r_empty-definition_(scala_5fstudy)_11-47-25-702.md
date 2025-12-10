file://<WORKSPACE>/src/main/scala/spark/S02_SparkSQL.scala
empty definition using pc, found symbol in pc: 
semanticdb not found
empty definition using fallback
non-local guesses:
	 -org/apache/spark/sql/SparkSession.
	 -org/apache/spark/sql/types/SparkSession.
	 -spark/implicits/SparkSession.
	 -SparkSession.
	 -scala/Predef.SparkSession.
offset: 789
uri: file://<WORKSPACE>/src/main/scala/spark/S02_SparkSQL.scala
text:
```scala
package spark

import org.apache.spark.sql.{SparkSession, functions => F}
import org.apache.spark.sql.types._

/**
 * Spark 第二课：Spark SQL 和 DataFrame
 *
 * 学习目标：
 * - SparkSession
 * - DataFrame 创建与操作
 * - SQL 查询
 * - 常用函数
 * - 数据类型转换
 */

// Case Class 必须定义在 object 外部
case class Person(id: Int, name: String, age: Int, city: String)
case class Sales(product: String, amount: Double)
case class Department(id: Int, dept: String)

object S02_SparkSQL {

  def main(args: Array[String]): Unit = {

    println("=" * 50)
    println("Spark 第二课：Spark SQL 和 DataFrame")
    println("=" * 50)

    // ============================================
    // 1. 创建 SparkSession
    // ============================================
    println("\n--- 1. 创建 SparkSession ---")

    val spark = SparkSess@@ion.builder()
      .appName("Spark SQL Basics")
      .master("local[*]")
      .config("spark.sql.shuffle.partitions", "4")  // 减少 shuffle 分区数
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    import spark.implicits._  // 启用隐式转换

    println(s"Spark 版本: ${spark.version}")

    try {
      // ============================================
      // 2. 创建 DataFrame
      // ============================================
      println("\n--- 2. 创建 DataFrame ---")

      // 方式1：从序列创建
      val df1 = Seq(
        (1, "Alice", 25),
        (2, "Bob", 30),
        (3, "Charlie", 35)
      ).toDF("id", "name", "age")

      println("从序列创建:")
      df1.show()

      // 方式2：从 Case Class 创建（推荐）
      val people = Seq(
        Person(1, "Alice", 25, "Beijing"),
        Person(2, "Bob", 30, "Shanghai"),
        Person(3, "Charlie", 35, "Guangzhou"),
        Person(4, "David", 28, "Beijing"),
        Person(5, "Eve", 32, "Shanghai")
      )
      val df2 = people.toDF()

      println("从 Case Class 创建:")
      df2.show()

      // 方式3：使用 createDataFrame 指定 Schema
      val data = Seq(
        (1, "Product A", 100.0),
        (2, "Product B", 200.0),
        (3, "Product C", 150.0)
      )
      val schema = StructType(Seq(
        StructField("id", IntegerType, nullable = false),
        StructField("name", StringType, nullable = true),
        StructField("price", DoubleType, nullable = true)
      ))
      val df3 = spark.createDataFrame(
        spark.sparkContext.parallelize(data).map(t => org.apache.spark.sql.Row(t._1, t._2, t._3)),
        schema
      )

      println("指定 Schema:")
      df3.printSchema()

      // ============================================
      // 3. DataFrame 基本操作
      // ============================================
      println("\n--- 3. DataFrame 基本操作 ---")

      val df = df2  // 使用 Person DataFrame

      // 查看 Schema
      println("Schema:")
      df.printSchema()

      // 查看数据
      println("显示前 3 行:")
      df.show(3)

      // 统计信息
      println("统计信息:")
      df.describe("age").show()

      // 选择列
      println("select name, age:")
      df.select("name", "age").show()

      // 使用 $ 或 col
      println("使用 $ 选择:")
      df.select($"name", $"age" + 1 as "next_age").show()

      // 过滤
      println("filter(age > 28):")
      df.filter($"age" > 28).show()

      // where (等同于 filter)
      println("where(city = 'Beijing'):")
      df.where($"city" === "Beijing").show()

      // 排序
      println("orderBy(age desc):")
      df.orderBy($"age".desc).show()

      // 去重
      println("distinct cities:")
      df.select("city").distinct().show()

      // ============================================
      // 4. 聚合操作
      // ============================================
      println("\n--- 4. 聚合操作 ---")

      // 简单聚合
      println("count, avg, max, min:")
      df.agg(
        F.count("*").as("total"),
        F.avg("age").as("avg_age"),
        F.max("age").as("max_age"),
        F.min("age").as("min_age")
      ).show()

      // 分组聚合
      println("按城市分组统计:")
      df.groupBy("city")
        .agg(
          F.count("*").as("count"),
          F.avg("age").as("avg_age"),
          F.collect_list("name").as("names")
        )
        .orderBy($"count".desc)
        .show(false)

      // ============================================
      // 5. SQL 查询
      // ============================================
      println("\n--- 5. SQL 查询 ---")

      // 注册临时视图
      df.createOrReplaceTempView("people")

      // 执行 SQL
      println("SQL 查询:")
      spark.sql("""
        SELECT city, COUNT(*) as count, AVG(age) as avg_age
        FROM people
        GROUP BY city
        ORDER BY count DESC
      """).show()

      // 复杂查询
      println("复杂 SQL:")
      spark.sql("""
        SELECT
          name,
          age,
          city,
          CASE
            WHEN age < 28 THEN '青年'
            WHEN age < 33 THEN '中青年'
            ELSE '中年'
          END as age_group
        FROM people
        WHERE city IN ('Beijing', 'Shanghai')
      """).show()

      // ============================================
      // 6. 常用函数
      // ============================================
      println("\n--- 6. 常用函数 ---")

      // 字符串函数
      println("字符串函数:")
      df.select(
        $"name",
        F.upper($"name").as("upper_name"),
        F.lower($"name").as("lower_name"),
        F.length($"name").as("name_length"),
        F.concat($"name", F.lit(" from "), $"city").as("full_info")
      ).show()

      // 数值函数
      val salesDf = Seq(
        Sales("A", 123.456),
        Sales("B", 789.123),
        Sales("C", 456.789)
      ).toDF()

      println("数值函数:")
      salesDf.select(
        $"product",
        $"amount",
        F.round($"amount", 2).as("rounded"),
        F.ceil($"amount").as("ceil"),
        F.floor($"amount").as("floor")
      ).show()

      // 日期函数
      println("日期函数:")
      val dateDf = Seq("2024-01-15", "2024-06-20", "2024-12-25").toDF("date_str")
      dateDf.select(
        $"date_str",
        F.to_date($"date_str").as("date"),
        F.year(F.to_date($"date_str")).as("year"),
        F.month(F.to_date($"date_str")).as("month"),
        F.dayofmonth(F.to_date($"date_str")).as("day"),
        F.dayofweek(F.to_date($"date_str")).as("dayofweek")
      ).show()

      // 条件函数
      println("条件函数:")
      df.select(
        $"name",
        $"age",
        F.when($"age" < 30, "young")
          .when($"age" < 35, "middle")
          .otherwise("senior").as("category")
      ).show()

      // ============================================
      // 7. Join 操作
      // ============================================
      println("\n--- 7. Join 操作 ---")

      val deptDf = Seq(
        Department(1, "Engineering"),
        Department(2, "Marketing"),
        Department(4, "Sales")
      ).toDF()

      println("Employees:")
      df.select("id", "name").show()
      println("Departments:")
      deptDf.show()

      // Inner Join
      println("Inner Join:")
      df.join(deptDf, Seq("id"), "inner")
        .select("id", "name", "dept")
        .show()

      // Left Join
      println("Left Join:")
      df.join(deptDf, Seq("id"), "left")
        .select("id", "name", "dept")
        .show()

      // Right Join
      println("Right Join:")
      df.join(deptDf, Seq("id"), "right")
        .select("id", "name", "dept")
        .show()

      // ============================================
      // 8. 窗口函数
      // ============================================
      println("\n--- 8. 窗口函数 ---")

      import org.apache.spark.sql.expressions.Window

      // 按城市分组的窗口
      val cityWindow = Window.partitionBy("city").orderBy($"age".desc)

      println("窗口函数示例:")
      df.select(
        $"name",
        $"city",
        $"age",
        F.row_number().over(cityWindow).as("rank"),
        F.avg($"age").over(Window.partitionBy("city")).as("city_avg_age"),
        F.lag($"name", 1).over(cityWindow).as("prev_person")
      ).show()

      // ============================================
      // 9. UDF (用户自定义函数)
      // ============================================
      println("\n--- 9. UDF ---")

      // 定义 UDF
      val ageCategory = F.udf((age: Int) => {
        if (age < 28) "青年"
        else if (age < 35) "中年"
        else "成熟"
      })

      // 使用 UDF
      println("使用 UDF:")
      df.select(
        $"name",
        $"age",
        ageCategory($"age").as("category")
      ).show()

      // 注册 UDF 供 SQL 使用
      spark.udf.register("age_category", (age: Int) => {
        if (age < 28) "青年" else if (age < 35) "中年" else "成熟"
      })

      println("SQL 中使用 UDF:")
      spark.sql("SELECT name, age, age_category(age) as category FROM people").show()

      // ============================================
      // 10. DataFrame 与 Dataset
      // ============================================
      println("\n--- 10. Dataset ---")

      // Dataset 是类型安全的 DataFrame
      val personDs = df.as[Person]

      println("Dataset 类型安全操作:")
      personDs
        .filter(_.age > 25)
        .map(p => s"${p.name} is ${p.age} years old")
        .show()

      // Dataset 强类型 API
      println("Dataset 聚合:")
      personDs
        .groupByKey(_.city)
        .count()
        .show()

      println("\n" + "=" * 50)
      println("Spark SQL 完成！运行 S03_Practice 继续学习")
      println("=" * 50)

    } finally {
      spark.stop()
    }
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 