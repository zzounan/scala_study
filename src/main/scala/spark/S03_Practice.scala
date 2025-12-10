package spark

import org.apache.spark.sql.{SparkSession, functions => F}
import org.apache.spark.sql.expressions.Window

/**
 * Spark 第三课：实战案例
 *
 * 案例：电商数据分析
 * - 订单分析
 * - 用户行为分析
 * - 销售报表
 */

// Case Class 必须定义在 object 外部
case class User(userId: Int, userName: String, age: Int, gender: String, city: String)
case class Product(productId: Int, productName: String, category: String, price: Double)
case class Order(orderId: Int, userId: Int, productId: Int, quantity: Int, orderDate: String)

object S03_Practice {

  def main(args: Array[String]): Unit = {

    println("=" * 60)
    println("Spark 第三课：实战案例 - 电商数据分析")
    println("=" * 60)

    val spark = SparkSession.builder()
      .appName("E-commerce Analytics")
      .master("local[*]")
      .config("spark.sql.shuffle.partitions", "4")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    import spark.implicits._

    try {
      // ============================================
      // 模拟数据
      // ============================================

      // 用户数据
      val users = Seq(
        User(1, "张三", 25, "M", "北京"),
        User(2, "李四", 32, "F", "上海"),
        User(3, "王五", 28, "M", "广州"),
        User(4, "赵六", 35, "F", "北京"),
        User(5, "钱七", 22, "M", "深圳"),
        User(6, "孙八", 29, "F", "上海"),
        User(7, "周九", 31, "M", "杭州"),
        User(8, "吴十", 27, "F", "成都")
      ).toDF()

      // 商品数据
      val products = Seq(
        Product(101, "iPhone 15", "手机", 6999.0),
        Product(102, "MacBook Pro", "电脑", 14999.0),
        Product(103, "AirPods Pro", "配件", 1899.0),
        Product(104, "iPad Air", "平板", 4799.0),
        Product(105, "华为 Mate 60", "手机", 5999.0),
        Product(106, "ThinkPad X1", "电脑", 9999.0),
        Product(107, "小米手环", "配件", 269.0),
        Product(108, "Kindle", "阅读器", 999.0)
      ).toDF()

      // 订单数据
      val orders = Seq(
        Order(1001, 1, 101, 1, "2024-01-15"),
        Order(1002, 1, 103, 2, "2024-01-15"),
        Order(1003, 2, 102, 1, "2024-01-16"),
        Order(1004, 3, 101, 1, "2024-01-17"),
        Order(1005, 4, 104, 1, "2024-01-18"),
        Order(1006, 2, 103, 1, "2024-01-19"),
        Order(1007, 5, 105, 1, "2024-01-20"),
        Order(1008, 1, 107, 3, "2024-01-21"),
        Order(1009, 6, 102, 1, "2024-01-22"),
        Order(1010, 3, 106, 1, "2024-01-23"),
        Order(1011, 7, 101, 2, "2024-02-01"),
        Order(1012, 8, 108, 1, "2024-02-02"),
        Order(1013, 2, 104, 1, "2024-02-03"),
        Order(1014, 4, 105, 1, "2024-02-05"),
        Order(1015, 5, 103, 2, "2024-02-07"),
        Order(1016, 1, 102, 1, "2024-02-10"),
        Order(1017, 6, 107, 2, "2024-02-12"),
        Order(1018, 3, 103, 1, "2024-02-15"),
        Order(1019, 7, 104, 1, "2024-02-18"),
        Order(1020, 8, 101, 1, "2024-02-20")
      ).toDF()

      // 注册临时视图
      users.createOrReplaceTempView("users")
      products.createOrReplaceTempView("products")
      orders.createOrReplaceTempView("orders")

      // ============================================
      // 案例1：销售总览
      // ============================================
      println("\n" + "=" * 50)
      println("案例1：销售总览")
      println("=" * 50)

      // 计算订单详情（关联商品信息）
      val orderDetails = orders
        .join(products, Seq("productId"))
        .withColumn("amount", $"quantity" * $"price")

      orderDetails.createOrReplaceTempView("order_details")

      println("\n订单详情:")
      orderDetails.select("orderId", "productName", "category", "quantity", "price", "amount")
        .show(10)

      // 销售汇总
      println("\n销售汇总:")
      orderDetails.agg(
        F.countDistinct("orderId").as("订单数"),
        F.sum("quantity").as("商品数量"),
        F.sum("amount").as("销售总额"),
        F.avg("amount").as("平均订单金额")
      ).show()

      // ============================================
      // 案例2：商品销售排行
      // ============================================
      println("\n" + "=" * 50)
      println("案例2：商品销售排行")
      println("=" * 50)

      val productRanking = orderDetails
        .groupBy("productId", "productName", "category")
        .agg(
          F.sum("quantity").as("销量"),
          F.sum("amount").as("销售额"),
          F.countDistinct("orderId").as("订单数")
        )
        .orderBy($"销售额".desc)

      println("\n商品销售排行（按销售额）:")
      productRanking.show()

      // 按类别统计
      println("\n按类别统计:")
      orderDetails
        .groupBy("category")
        .agg(
          F.sum("quantity").as("销量"),
          F.sum("amount").as("销售额"),
          F.countDistinct("productId").as("商品种类")
        )
        .orderBy($"销售额".desc)
        .show()

      // ============================================
      // 案例3：用户消费分析
      // ============================================
      println("\n" + "=" * 50)
      println("案例3：用户消费分析")
      println("=" * 50)

      val userStats = orderDetails
        .join(users, Seq("userId"))
        .groupBy("userId", "userName", "city", "gender")
        .agg(
          F.countDistinct("orderId").as("订单数"),
          F.sum("amount").as("消费总额"),
          F.avg("amount").as("平均订单金额"),
          F.min("orderDate").as("首次购买"),
          F.max("orderDate").as("最近购买")
        )
        .orderBy($"消费总额".desc)

      println("\n用户消费排行:")
      userStats.show()

      // 用户分层（RFM 简化版）
      println("\n用户价值分层:")
      userStats
        .withColumn("用户等级",
          F.when($"消费总额" >= 15000, "高价值")
            .when($"消费总额" >= 8000, "中价值")
            .otherwise("普通"))
        .groupBy("用户等级")
        .agg(
          F.count("*").as("用户数"),
          F.sum("消费总额").as("总消费"),
          F.avg("消费总额").as("平均消费")
        )
        .orderBy($"总消费".desc)
        .show()

      // ============================================
      // 案例4：时间维度分析
      // ============================================
      println("\n" + "=" * 50)
      println("案例4：时间维度分析")
      println("=" * 50)

      val timeAnalysis = orderDetails
        .withColumn("order_date", F.to_date($"orderDate"))
        .withColumn("year_month", F.date_format($"order_date", "yyyy-MM"))
        .withColumn("day_of_week", F.dayofweek($"order_date"))

      // 月度销售趋势
      println("\n月度销售趋势:")
      timeAnalysis
        .groupBy("year_month")
        .agg(
          F.countDistinct("orderId").as("订单数"),
          F.sum("amount").as("销售额")
        )
        .orderBy("year_month")
        .show()

      // 星期几销售分布
      println("\n星期销售分布 (1=周日, 7=周六):")
      timeAnalysis
        .groupBy("day_of_week")
        .agg(
          F.countDistinct("orderId").as("订单数"),
          F.sum("amount").as("销售额")
        )
        .orderBy("day_of_week")
        .show()

      // ============================================
      // 案例5：窗口函数应用
      // ============================================
      println("\n" + "=" * 50)
      println("案例5：窗口函数应用")
      println("=" * 50)

      // 用户购买排名
      val userWindow = Window.partitionBy("userId").orderBy($"orderDate")
      val categoryWindow = Window.partitionBy("category").orderBy($"amount".desc)

      println("\n用户购买序列:")
      orderDetails
        .select("userId", "productName", "orderDate", "amount")
        .withColumn("购买序号", F.row_number().over(userWindow))
        .withColumn("累计消费", F.sum("amount").over(userWindow))
        .orderBy("userId", "orderDate")
        .show(15)

      // 类别内排名
      println("\n各类别销售排名:")
      orderDetails
        .groupBy("productId", "productName", "category")
        .agg(F.sum("amount").as("销售额"))
        .withColumn("类别排名", F.rank().over(categoryWindow))
        .orderBy("category", "类别排名")
        .show()

      // ============================================
      // 案例6：复杂 SQL 分析
      // ============================================
      println("\n" + "=" * 50)
      println("案例6：复杂 SQL 分析")
      println("=" * 50)

      println("\n城市消费能力分析:")
      spark.sql("""
        WITH user_consumption AS (
          SELECT
            u.city,
            u.userId,
            SUM(od.quantity * p.price) as total_amount
          FROM orders od
          JOIN users u ON od.userId = u.userId
          JOIN products p ON od.productId = p.productId
          GROUP BY u.city, u.userId
        )
        SELECT
          city as 城市,
          COUNT(DISTINCT userId) as 用户数,
          SUM(total_amount) as 总消费,
          AVG(total_amount) as 人均消费,
          MAX(total_amount) as 最高消费,
          MIN(total_amount) as 最低消费
        FROM user_consumption
        GROUP BY city
        ORDER BY 总消费 DESC
      """).show()

      println("\n用户复购分析:")
      spark.sql("""
        SELECT
          CASE
            WHEN order_count = 1 THEN '一次购买'
            WHEN order_count = 2 THEN '二次复购'
            ELSE '多次复购'
          END as 购买频次,
          COUNT(*) as 用户数,
          SUM(total_amount) as 总消费
        FROM (
          SELECT
            userId,
            COUNT(DISTINCT orderId) as order_count,
            SUM(quantity * price) as total_amount
          FROM order_details
          GROUP BY userId
        )
        GROUP BY
          CASE
            WHEN order_count = 1 THEN '一次购买'
            WHEN order_count = 2 THEN '二次复购'
            ELSE '多次复购'
          END
        ORDER BY 用户数 DESC
      """).show()

      // ============================================
      // 案例7：数据导出示例
      // ============================================
      println("\n" + "=" * 50)
      println("案例7：数据导出（示例代码）")
      println("=" * 50)

      println("""
        |// 导出为 CSV
        |df.write
        |  .option("header", "true")
        |  .mode("overwrite")
        |  .csv("output/report.csv")
        |
        |// 导出为 Parquet（推荐）
        |df.write
        |  .mode("overwrite")
        |  .parquet("output/report.parquet")
        |
        |// 导出为 JSON
        |df.write
        |  .mode("overwrite")
        |  .json("output/report.json")
        |
        |// 分区导出
        |df.write
        |  .partitionBy("year", "month")
        |  .parquet("output/partitioned")
      """.stripMargin)

      println("\n" + "=" * 60)
      println("恭喜！Spark 实战课程完成！")
      println("=" * 60)
      println("""
        |学习建议：
        |1. 尝试修改参数，观察结果变化
        |2. 添加自己的分析维度
        |3. 处理真实数据集（如 Kaggle 数据）
        |4. 学习 Spark Streaming 实时处理
        |5. 了解 Spark MLlib 机器学习
      """.stripMargin)

    } finally {
      spark.stop()
    }
  }
}
