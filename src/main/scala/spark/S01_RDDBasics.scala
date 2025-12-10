package spark

import org.apache.spark.{SparkConf, SparkContext}

/**
 * Spark 第一课：RDD 基础
 *
 * 学习目标：
 * - 理解 RDD（弹性分布式数据集）
 * - 创建 RDD
 * - Transformation 和 Action 操作
 * - RDD 持久化
 */
object S01_RDDBasics {

  def main(args: Array[String]): Unit = {

    println("=" * 50)
    println("Spark 第一课：RDD 基础")
    println("=" * 50)

    // ============================================
    // 1. 创建 SparkContext
    // ============================================
    println("\n--- 1. 创建 SparkContext ---")

    val conf = new SparkConf()
      .setAppName("RDD Basics")
      .setMaster("local[*]")  // 本地模式，使用所有可用核心

    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")  // 减少日志输出

    println(s"Spark 版本: ${sc.version}")
    println(s"应用名称: ${sc.appName}")

    try {
      // ============================================
      // 2. 创建 RDD
      // ============================================
      println("\n--- 2. 创建 RDD ---")

      // 方式1：从集合创建（parallelize）
      val numbersRDD = sc.parallelize(1 to 10)
      println(s"从集合创建: ${numbersRDD.collect().mkString(", ")}")

      // 方式2：指定分区数
      val partitionedRDD = sc.parallelize(1 to 10, 3)
      println(s"分区数: ${partitionedRDD.getNumPartitions}")

      // 方式3：从数组创建
      val wordsRDD = sc.parallelize(Seq("hello", "world", "spark", "scala"))
      println(s"单词: ${wordsRDD.collect().mkString(", ")}")

      // ============================================
      // 3. Transformation 操作（惰性求值）
      // ============================================
      println("\n--- 3. Transformation 操作 ---")

      // map: 对每个元素应用函数
      val doubled = numbersRDD.map(_ * 2)
      println(s"map(_ * 2): ${doubled.collect().mkString(", ")}")

      // filter: 过滤元素
      val evens = numbersRDD.filter(_ % 2 == 0)
      println(s"filter(_ % 2 == 0): ${evens.collect().mkString(", ")}")

      // flatMap: 一对多映射
      val chars = wordsRDD.flatMap(_.toList)
      println(s"flatMap(_.toList): ${chars.collect().mkString(", ")}")

      // distinct: 去重
      val withDups = sc.parallelize(Seq(1, 2, 2, 3, 3, 3))
      println(s"distinct: ${withDups.distinct().collect().mkString(", ")}")

      // ============================================
      // 4. Action 操作（触发计算）
      // ============================================
      println("\n--- 4. Action 操作 ---")

      // collect: 收集所有元素到驱动程序
      val collected = numbersRDD.collect()
      println(s"collect: ${collected.mkString(", ")}")

      // count: 计数
      println(s"count: ${numbersRDD.count()}")

      // first: 第一个元素
      println(s"first: ${numbersRDD.first()}")

      // take: 取前 n 个
      println(s"take(3): ${numbersRDD.take(3).mkString(", ")}")

      // reduce: 聚合
      val sum = numbersRDD.reduce(_ + _)
      println(s"reduce(_ + _): $sum")

      // fold: 带初始值的聚合
      val sumWithInit = numbersRDD.fold(100)(_ + _)
      println(s"fold(100)(_ + _): $sumWithInit")

      // aggregate: 复杂聚合（分区内 + 分区间）
      val (total, count) = numbersRDD.aggregate((0, 0))(
        (acc, value) => (acc._1 + value, acc._2 + 1),  // 分区内
        (acc1, acc2) => (acc1._1 + acc2._1, acc1._2 + acc2._2)  // 分区间
      )
      println(s"aggregate (sum, count): ($total, $count), avg = ${total.toDouble / count}")

      // ============================================
      // 5. Key-Value RDD 操作
      // ============================================
      println("\n--- 5. Key-Value RDD 操作 ---")

      val words = sc.parallelize(Seq(
        "hello world",
        "hello spark",
        "spark is awesome",
        "hello scala"
      ))

      // 词频统计（经典 WordCount）
      val wordCounts = words
        .flatMap(_.split(" "))
        .map(word => (word, 1))
        .reduceByKey(_ + _)

      println("词频统计:")
      wordCounts.collect().sortBy(-_._2).foreach { case (word, count) =>
        println(s"  $word: $count")
      }

      // groupByKey: 按键分组
      val pairRDD = sc.parallelize(Seq(
        ("a", 1), ("b", 2), ("a", 3), ("b", 4), ("c", 5)
      ))
      val grouped = pairRDD.groupByKey()
      println("groupByKey:")
      grouped.collect().foreach { case (key, values) =>
        println(s"  $key: ${values.mkString(", ")}")
      }

      // sortByKey: 按键排序
      println(s"sortByKey: ${pairRDD.sortByKey().collect().mkString(", ")}")

      // keys 和 values
      println(s"keys: ${pairRDD.keys.collect().mkString(", ")}")
      println(s"values: ${pairRDD.values.collect().mkString(", ")}")

      // ============================================
      // 6. 集合操作
      // ============================================
      println("\n--- 6. 集合操作 ---")

      val rdd1 = sc.parallelize(1 to 5)
      val rdd2 = sc.parallelize(3 to 7)

      println(s"rdd1: ${rdd1.collect().mkString(", ")}")
      println(s"rdd2: ${rdd2.collect().mkString(", ")}")
      println(s"union: ${rdd1.union(rdd2).collect().mkString(", ")}")
      println(s"intersection: ${rdd1.intersection(rdd2).collect().mkString(", ")}")
      println(s"subtract: ${rdd1.subtract(rdd2).collect().mkString(", ")}")
      println(s"cartesian: ${rdd1.take(2).mkString(",")} x ${rdd2.take(2).mkString(",")} = ${rdd1.cartesian(rdd2).take(4).mkString(", ")}")

      // ============================================
      // 7. RDD 持久化
      // ============================================
      println("\n--- 7. RDD 持久化 ---")

      import org.apache.spark.storage.StorageLevel

      val expensiveRDD = numbersRDD
        .map { n =>
          // 模拟耗时操作
          n * n
        }
        .cache()  // 等同于 persist(StorageLevel.MEMORY_ONLY)

      // 第一次触发计算并缓存
      println(s"第一次 count: ${expensiveRDD.count()}")
      // 第二次直接从缓存读取
      println(s"第二次 count: ${expensiveRDD.count()}")

      // 不同的存储级别
      println("\n存储级别:")
      println("  MEMORY_ONLY: 仅内存")
      println("  MEMORY_AND_DISK: 内存+磁盘")
      println("  MEMORY_ONLY_SER: 内存（序列化）")
      println("  DISK_ONLY: 仅磁盘")

      // 释放缓存
      expensiveRDD.unpersist()

      // ============================================
      // 8. 分区操作
      // ============================================
      println("\n--- 8. 分区操作 ---")

      val dataRDD = sc.parallelize(1 to 12, 4)
      println(s"原始分区数: ${dataRDD.getNumPartitions}")

      // glom: 查看每个分区的内容
      println("各分区内容:")
      dataRDD.glom().collect().zipWithIndex.foreach { case (arr, idx) =>
        println(s"  分区 $idx: ${arr.mkString(", ")}")
      }

      // repartition: 重新分区（涉及 shuffle）
      val repartitioned = dataRDD.repartition(2)
      println(s"repartition(2) 后分区数: ${repartitioned.getNumPartitions}")

      // coalesce: 减少分区（避免 shuffle）
      val coalesced = dataRDD.coalesce(2)
      println(s"coalesce(2) 后分区数: ${coalesced.getNumPartitions}")

      // mapPartitions: 按分区处理
      val partitionSums = dataRDD.mapPartitions { iter =>
        Iterator(iter.sum)
      }
      println(s"各分区求和: ${partitionSums.collect().mkString(", ")}")

      // mapPartitionsWithIndex: 带分区索引
      val withPartitionId = dataRDD.mapPartitionsWithIndex { (idx, iter) =>
        iter.map(x => s"P$idx:$x")
      }
      println(s"带分区ID: ${withPartitionId.collect().mkString(", ")}")

      println("\n" + "=" * 50)
      println("RDD 基础完成！运行 S02_SparkSQL 继续学习")
      println("=" * 50)

    } finally {
      sc.stop()
    }
  }
}
