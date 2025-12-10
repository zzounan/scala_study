package spark

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.{functions => F}

/**
 * Spark 经典案例：WordCount
 *
 * 展示 RDD 和 DataFrame 两种实现方式
 */
object S04_WordCount {

  def main(args: Array[String]): Unit = {

    println("=" * 50)
    println("Spark 经典案例：WordCount")
    println("=" * 50)

    val spark = SparkSession.builder()
      .appName("Word Count")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    import spark.implicits._

    try {
      // 示例文本
      val text = """
        |Scala is a modern programming language
        |Scala combines object-oriented and functional programming
        |Spark is built with Scala
        |Spark is fast and powerful
        |Learning Scala and Spark is fun
        |Scala runs on the JVM
        |Spark can process big data
        |Big data analytics with Spark is amazing
      """.stripMargin

      val lines = text.split("\n").filter(_.nonEmpty)

      // ============================================
      // 方式1：使用 RDD
      // ============================================
      println("\n--- 方式1：RDD 实现 ---")

      val rddResult = sc.parallelize(lines)
        .flatMap(_.toLowerCase.split("\\s+"))     // 分词
        .filter(_.nonEmpty)                        // 过滤空字符串
        .map(word => (word, 1))                    // 映射为 (word, 1)
        .reduceByKey(_ + _)                        // 按 key 聚合
        .sortBy(-_._2)                             // 按计数降序排序

      println("RDD 词频统计 Top 10:")
      rddResult.take(10).foreach { case (word, count) =>
        println(f"  $word%-15s : $count")
      }

      // ============================================
      // 方式2：使用 DataFrame
      // ============================================
      println("\n--- 方式2：DataFrame 实现 ---")

      val dfResult = lines.toSeq.toDF("line")
        .withColumn("word", F.explode(F.split(F.lower($"line"), "\\s+")))
        .filter($"word" =!= "")
        .groupBy("word")
        .count()
        .orderBy($"count".desc)

      println("DataFrame 词频统计 Top 10:")
      dfResult.show(10)

      // ============================================
      // 方式3：使用 SQL
      // ============================================
      println("\n--- 方式3：SQL 实现 ---")

      lines.toSeq.toDF("line").createOrReplaceTempView("text_lines")

      val sqlResult = spark.sql("""
        SELECT word, COUNT(*) as count
        FROM (
          SELECT EXPLODE(SPLIT(LOWER(line), '\\s+')) as word
          FROM text_lines
        )
        WHERE word != ''
        GROUP BY word
        ORDER BY count DESC
        LIMIT 10
      """)

      println("SQL 词频统计 Top 10:")
      sqlResult.show()

      // ============================================
      // 扩展：词频分布统计
      // ============================================
      println("\n--- 扩展：词频分布 ---")

      val wordStats = dfResult.agg(
        F.count("*").as("不同单词数"),
        F.sum("count").as("总词数"),
        F.avg("count").as("平均词频"),
        F.max("count").as("最高词频"),
        F.min("count").as("最低词频")
      )
      wordStats.show()

      // 词频分布
      println("词频分布:")
      dfResult
        .withColumn("频次区间",
          F.when($"count" === 1, "出现1次")
            .when($"count" <= 3, "出现2-3次")
            .otherwise("出现3次以上"))
        .groupBy("频次区间")
        .agg(F.count("*").as("单词数"))
        .orderBy($"单词数".desc)
        .show()

      println("\n" + "=" * 50)
      println("WordCount 完成！")
      println("=" * 50)

    } finally {
      spark.stop()
    }
  }
}
