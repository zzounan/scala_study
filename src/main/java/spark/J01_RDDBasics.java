package spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Spark 第一课：RDD 基础 (Java 版)
 *
 * 学习目标：
 * - 理解 RDD（弹性分布式数据集）
 * - 创建 RDD
 * - Transformation 和 Action 操作
 * - RDD 持久化
 */
public class J01_RDDBasics {

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("Spark 第一课：RDD 基础 (Java 版)");
        System.out.println("==================================================");

        // ============================================
        // 1. 创建 SparkContext
        // ============================================
        System.out.println("\n--- 1. 创建 SparkContext ---");

        SparkConf conf = new SparkConf()
                .setAppName("RDD Basics Java")
                .setMaster("local[*]");  // 本地模式，使用所有可用核心

        JavaSparkContext sc = new JavaSparkContext(conf);
        sc.setLogLevel("WARN");  // 减少日志输出

        System.out.println("Spark 版本: " + sc.version());
        System.out.println("应用名称: " + sc.appName());

        try {
            // ============================================
            // 2. 创建 RDD
            // ============================================
            System.out.println("\n--- 2. 创建 RDD ---");

            // 方式1：从集合创建（parallelize）
            List<Integer> numbers = IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList());
            JavaRDD<Integer> numbersRDD = sc.parallelize(numbers);
            System.out.println("从集合创建: " + numbersRDD.collect());

            // 方式2：指定分区数
            JavaRDD<Integer> partitionedRDD = sc.parallelize(numbers, 3);
            System.out.println("分区数: " + partitionedRDD.getNumPartitions());

            // 方式3：从数组创建
            JavaRDD<String> wordsRDD = sc.parallelize(Arrays.asList("hello", "world", "spark", "scala"));
            System.out.println("单词: " + wordsRDD.collect());

            // ============================================
            // 3. Transformation 操作（惰性求值）
            // ============================================
            System.out.println("\n--- 3. Transformation 操作 ---");

            // map: 对每个元素应用函数
            JavaRDD<Integer> doubled = numbersRDD.map(n -> n * 2);
            System.out.println("map(n -> n * 2): " + doubled.collect());

            // filter: 过滤元素
            JavaRDD<Integer> evens = numbersRDD.filter(n -> n % 2 == 0);
            System.out.println("filter(n -> n % 2 == 0): " + evens.collect());

            // flatMap: 一对多映射
            JavaRDD<Character> chars = wordsRDD.flatMap(word -> {
                char[] charArray = word.toCharArray();
                Character[] characters = new Character[charArray.length];
                for (int i = 0; i < charArray.length; i++) {
                    characters[i] = charArray[i];
                }
                return Arrays.asList(characters).iterator();
            });
            System.out.println("flatMap(word -> chars): " + chars.collect());

            // distinct: 去重
            JavaRDD<Integer> withDups = sc.parallelize(Arrays.asList(1, 2, 2, 3, 3, 3));
            System.out.println("distinct: " + withDups.distinct().collect());

            // ============================================
            // 4. Action 操作（触发计算）
            // ============================================
            System.out.println("\n--- 4. Action 操作 ---");

            // collect: 收集所有元素到驱动程序
            List<Integer> collected = numbersRDD.collect();
            System.out.println("collect: " + collected);

            // count: 计数
            System.out.println("count: " + numbersRDD.count());

            // first: 第一个元素
            System.out.println("first: " + numbersRDD.first());

            // take: 取前 n 个
            System.out.println("take(3): " + numbersRDD.take(3));

            // reduce: 聚合
            int sum = numbersRDD.reduce((a, b) -> a + b);
            System.out.println("reduce((a, b) -> a + b): " + sum);

            // fold: 带初始值的聚合
            int sumWithInit = numbersRDD.fold(100, (a, b) -> a + b);
            System.out.println("fold(100, (a, b) -> a + b): " + sumWithInit);

            // aggregate: 复杂聚合（分区内 + 分区间）
            Tuple2<Integer, Integer> result = numbersRDD.aggregate(
                    new Tuple2<>(0, 0),
                    (acc, value) -> new Tuple2<>(acc._1() + value, acc._2() + 1),  // 分区内
                    (acc1, acc2) -> new Tuple2<>(acc1._1() + acc2._1(), acc1._2() + acc2._2())  // 分区间
            );
            System.out.println("aggregate (sum, count): (" + result._1() + ", " + result._2() +
                    "), avg = " + ((double) result._1() / result._2()));

            // ============================================
            // 5. Key-Value RDD 操作
            // ============================================
            System.out.println("\n--- 5. Key-Value RDD 操作 ---");

            JavaRDD<String> sentences = sc.parallelize(Arrays.asList(
                    "hello world",
                    "hello spark",
                    "spark is awesome",
                    "hello scala"
            ));

            // 词频统计（经典 WordCount）
            JavaPairRDD<String, Integer> wordCounts = sentences
                    .flatMap(line -> Arrays.asList(line.split(" ")).iterator())
                    .mapToPair(word -> new Tuple2<>(word, 1))
                    .reduceByKey((a, b) -> a + b);

            System.out.println("词频统计:");
            List<Tuple2<String, Integer>> sortedCounts = new java.util.ArrayList<>(wordCounts.collect());
            sortedCounts.sort((a, b) -> b._2() - a._2());
            for (Tuple2<String, Integer> wc : sortedCounts) {
                System.out.println("  " + wc._1() + ": " + wc._2());
            }

            // groupByKey: 按键分组
            JavaPairRDD<String, Integer> pairRDD = sc.parallelizePairs(Arrays.asList(
                    new Tuple2<>("a", 1),
                    new Tuple2<>("b", 2),
                    new Tuple2<>("a", 3),
                    new Tuple2<>("b", 4),
                    new Tuple2<>("c", 5)
            ));
            JavaPairRDD<String, Iterable<Integer>> grouped = pairRDD.groupByKey();
            System.out.println("groupByKey:");
            for (Tuple2<String, Iterable<Integer>> entry : grouped.collect()) {
                System.out.println("  " + entry._1() + ": " + entry._2());
            }

            // sortByKey: 按键排序
            System.out.println("sortByKey: " + pairRDD.sortByKey().collect());

            // keys 和 values
            System.out.println("keys: " + pairRDD.keys().collect());
            System.out.println("values: " + pairRDD.values().collect());

            // ============================================
            // 6. 集合操作
            // ============================================
            System.out.println("\n--- 6. 集合操作 ---");

            List<Integer> list1 = IntStream.rangeClosed(1, 5).boxed().collect(Collectors.toList());
            List<Integer> list2 = IntStream.rangeClosed(3, 7).boxed().collect(Collectors.toList());
            JavaRDD<Integer> rdd1 = sc.parallelize(list1);
            JavaRDD<Integer> rdd2 = sc.parallelize(list2);

            System.out.println("rdd1: " + rdd1.collect());
            System.out.println("rdd2: " + rdd2.collect());
            System.out.println("union: " + rdd1.union(rdd2).collect());
            System.out.println("intersection: " + rdd1.intersection(rdd2).collect());
            System.out.println("subtract: " + rdd1.subtract(rdd2).collect());
            System.out.println("cartesian (部分): " + rdd1.cartesian(rdd2).take(4));

            // ============================================
            // 7. RDD 持久化
            // ============================================
            System.out.println("\n--- 7. RDD 持久化 ---");

            JavaRDD<Integer> expensiveRDD = numbersRDD
                    .map(n -> {
                        // 模拟耗时操作
                        return n * n;
                    })
                    .cache();  // 等同于 persist(StorageLevel.MEMORY_ONLY)

            // 第一次触发计算并缓存
            System.out.println("第一次 count: " + expensiveRDD.count());
            // 第二次直接从缓存读取
            System.out.println("第二次 count: " + expensiveRDD.count());

            // 不同的存储级别
            System.out.println("\n存储级别:");
            System.out.println("  MEMORY_ONLY: 仅内存");
            System.out.println("  MEMORY_AND_DISK: 内存+磁盘");
            System.out.println("  MEMORY_ONLY_SER: 内存（序列化）");
            System.out.println("  DISK_ONLY: 仅磁盘");

            // 释放缓存
            expensiveRDD.unpersist();

            // ============================================
            // 8. 分区操作
            // ============================================
            System.out.println("\n--- 8. 分区操作 ---");

            List<Integer> data = IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toList());
            JavaRDD<Integer> dataRDD = sc.parallelize(data, 4);
            System.out.println("原始分区数: " + dataRDD.getNumPartitions());

            // glom: 查看每个分区的内容
            System.out.println("各分区内容:");
            List<List<Integer>> partitions = dataRDD.glom().collect();
            for (int i = 0; i < partitions.size(); i++) {
                System.out.println("  分区 " + i + ": " + partitions.get(i));
            }

            // repartition: 重新分区（涉及 shuffle）
            JavaRDD<Integer> repartitioned = dataRDD.repartition(2);
            System.out.println("repartition(2) 后分区数: " + repartitioned.getNumPartitions());

            // coalesce: 减少分区（避免 shuffle）
            JavaRDD<Integer> coalesced = dataRDD.coalesce(2);
            System.out.println("coalesce(2) 后分区数: " + coalesced.getNumPartitions());

            // mapPartitions: 按分区处理
            JavaRDD<Integer> partitionSums = dataRDD.mapPartitions(iter -> {
                int partitionSum = 0;
                while (iter.hasNext()) {
                    partitionSum += iter.next();
                }
                return Arrays.asList(partitionSum).iterator();
            });
            System.out.println("各分区求和: " + partitionSums.collect());

            // mapPartitionsWithIndex: 带分区索引
            JavaRDD<String> withPartitionId = dataRDD.mapPartitionsWithIndex((idx, iter) -> {
                List<String> results = new java.util.ArrayList<>();
                while (iter.hasNext()) {
                    results.add("P" + idx + ":" + iter.next());
                }
                return results.iterator();
            }, false);
            System.out.println("带分区ID: " + withPartitionId.collect());

            System.out.println("\n==================================================");
            System.out.println("RDD 基础完成！运行 S02_SparkSQL 继续学习");
            System.out.println("==================================================");

        } finally {
            sc.stop();
        }
    }
}
