package spark;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.*;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Spark 经典案例：WordCount (Java 版)
 *
 * 展示 RDD 和 DataFrame 两种实现方式
 */
public class J04_WordCount {

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("Spark 经典案例：WordCount (Java 版)");
        System.out.println("==================================================");

        SparkSession spark = SparkSession.builder()
                .appName("Word Count Java")
                .master("local[*]")
                .getOrCreate();

        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
        sc.setLogLevel("WARN");

        try {
            // 示例文本
            String[] lines = {
                    "Scala is a modern programming language",
                    "Scala combines object-oriented and functional programming",
                    "Spark is built with Scala",
                    "Spark is fast and powerful",
                    "Learning Scala and Spark is fun",
                    "Scala runs on the JVM",
                    "Spark can process big data",
                    "Big data analytics with Spark is amazing"
            };

            // ============================================
            // 方式1：使用 RDD
            // ============================================
            System.out.println("\n--- 方式1：RDD 实现 ---");

            JavaRDD<String> linesRDD = sc.parallelize(Arrays.asList(lines));

            JavaPairRDD<String, Integer> wordCounts = linesRDD
                    .flatMap(line -> Arrays.asList(line.toLowerCase().split("\\s+")).iterator())
                    .filter(word -> !word.isEmpty())
                    .mapToPair(word -> new Tuple2<>(word, 1))
                    .reduceByKey((a, b) -> a + b);

            // 收集并排序（collect返回不可修改列表，需包装成ArrayList）
            List<Tuple2<String, Integer>> rddResult = new java.util.ArrayList<>(wordCounts.collect());
            rddResult.sort((a, b) -> b._2() - a._2());

            System.out.println("RDD 词频统计 Top 10:");
            rddResult.stream().limit(10).forEach(t ->
                    System.out.printf("  %-15s : %d%n", t._1(), t._2()));

            // ============================================
            // 方式2：使用 DataFrame
            // ============================================
            System.out.println("\n--- 方式2：DataFrame 实现 ---");

            Dataset<Row> linesDf = spark.createDataset(Arrays.asList(lines),
                    org.apache.spark.sql.Encoders.STRING()).toDF("line");

            Dataset<Row> dfResult = linesDf
                    .withColumn("word", explode(split(lower(col("line")), "\\s+")))
                    .filter(col("word").notEqual(""))
                    .groupBy("word")
                    .count()
                    .orderBy(col("count").desc());

            System.out.println("DataFrame 词频统计 Top 10:");
            dfResult.show(10);

            // ============================================
            // 方式3：使用 SQL
            // ============================================
            System.out.println("\n--- 方式3：SQL 实现 ---");

            linesDf.createOrReplaceTempView("text_lines");

            Dataset<Row> sqlResult = spark.sql(
                    "SELECT word, COUNT(*) as count " +
                    "FROM (" +
                    "  SELECT EXPLODE(SPLIT(LOWER(line), '\\\\s+')) as word " +
                    "  FROM text_lines" +
                    ") " +
                    "WHERE word != '' " +
                    "GROUP BY word " +
                    "ORDER BY count DESC " +
                    "LIMIT 10"
            );

            System.out.println("SQL 词频统计 Top 10:");
            sqlResult.show();

            // ============================================
            // 扩展：词频分布统计
            // ============================================
            System.out.println("\n--- 扩展：词频分布 ---");

            Dataset<Row> wordStats = dfResult.agg(
                    count("*").as("不同单词数"),
                    sum("count").as("总词数"),
                    avg("count").as("平均词频"),
                    max("count").as("最高词频"),
                    min("count").as("最低词频")
            );
            wordStats.show();

            // 词频分布
            System.out.println("词频分布:");
            dfResult
                    .withColumn("频次区间",
                            when(col("count").equalTo(1), "出现1次")
                                    .when(col("count").leq(3), "出现2-3次")
                                    .otherwise("出现3次以上"))
                    .groupBy("频次区间")
                    .agg(count("*").as("单词数"))
                    .orderBy(col("单词数").desc())
                    .show();

            System.out.println("\n==================================================");
            System.out.println("WordCount 完成！");
            System.out.println("==================================================");

        } finally {
            spark.stop();
        }
    }
}
