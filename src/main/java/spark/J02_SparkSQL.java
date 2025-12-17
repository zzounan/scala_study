package spark;

import org.apache.spark.sql.*;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import org.apache.spark.sql.types.*;
import static org.apache.spark.sql.functions.*;
import org.apache.spark.api.java.function.MapFunction;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Spark 第二课：Spark SQL 和 DataFrame (Java 版)
 *
 * 学习目标：
 * - SparkSession
 * - DataFrame 创建与操作
 * - SQL 查询
 * - 常用函数
 * - 数据类型转换
 */
public class J02_SparkSQL {

    // JavaBean 类必须是 public static 且实现 Serializable
    public static class Person implements Serializable {
        private int id;
        private String name;
        private int age;
        private String city;

        public Person() {}
        public Person(int id, String name, int age, String city) {
            this.id = id; this.name = name; this.age = age; this.city = city;
        }
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
    }

    public static class Sales implements Serializable {
        private String product;
        private double amount;

        public Sales() {}
        public Sales(String product, double amount) {
            this.product = product; this.amount = amount;
        }
        public String getProduct() { return product; }
        public void setProduct(String product) { this.product = product; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
    }

    public static class Department implements Serializable {
        private int id;
        private String dept;

        public Department() {}
        public Department(int id, String dept) {
            this.id = id; this.dept = dept;
        }
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getDept() { return dept; }
        public void setDept(String dept) { this.dept = dept; }
    }

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("Spark 第二课：Spark SQL 和 DataFrame (Java 版)");
        System.out.println("==================================================");

        // ============================================
        // 1. 创建 SparkSession
        // ============================================
        System.out.println("\n--- 1. 创建 SparkSession ---");

        SparkSession spark = SparkSession.builder()
                .appName("Spark SQL Basics Java")
                .master("local[*]")
                .config("spark.sql.shuffle.partitions", "4")
                .getOrCreate();

        spark.sparkContext().setLogLevel("WARN");

        System.out.println("Spark 版本: " + spark.version());

        try {
            // ============================================
            // 2. 创建 DataFrame
            // ============================================
            System.out.println("\n--- 2. 创建 DataFrame ---");

            // 方式1：从 JavaBean 列表创建
            List<Person> people = Arrays.asList(
                    new Person(1, "Alice", 25, "Beijing"),
                    new Person(2, "Bob", 30, "Shanghai"),
                    new Person(3, "Charlie", 35, "Guangzhou"),
                    new Person(4, "David", 28, "Beijing"),
                    new Person(5, "Eve", 32, "Shanghai")
            );
            Dataset<Row> df = spark.createDataFrame(people, Person.class);

            System.out.println("从 JavaBean 创建:");
            df.show();

            // 方式2：使用 RowFactory 和 Schema
            StructType schema = new StructType(new StructField[]{
                    new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
                    new StructField("name", DataTypes.StringType, true, Metadata.empty()),
                    new StructField("price", DataTypes.DoubleType, true, Metadata.empty())
            });

            List<Row> productData = Arrays.asList(
                    RowFactory.create(1, "Product A", 100.0),
                    RowFactory.create(2, "Product B", 200.0),
                    RowFactory.create(3, "Product C", 150.0)
            );
            Dataset<Row> productDf = spark.createDataFrame(productData, schema);

            System.out.println("指定 Schema:");
            productDf.printSchema();

            // ============================================
            // 3. DataFrame 基本操作
            // ============================================
            System.out.println("\n--- 3. DataFrame 基本操作 ---");

            // 查看 Schema
            System.out.println("Schema:");
            df.printSchema();

            // 查看数据
            System.out.println("显示前 3 行:");
            df.show(3);

            // 统计信息
            System.out.println("统计信息:");
            df.describe("age").show();

            // 选择列
            System.out.println("select name, age:");
            df.select("name", "age").show();

            // 使用 col
            System.out.println("使用 col 选择:");
            df.select(col("name"), col("age").plus(1).as("next_age")).show();

            // 过滤
            System.out.println("filter(age > 28):");
            df.filter(col("age").gt(28)).show();

            // where
            System.out.println("where(city = 'Beijing'):");
            df.where(col("city").equalTo("Beijing")).show();

            // 排序
            System.out.println("orderBy(age desc):");
            df.orderBy(col("age").desc()).show();

            // 去重
            System.out.println("distinct cities:");
            df.select("city").distinct().show();

            // ============================================
            // 4. 聚合操作
            // ============================================
            System.out.println("\n--- 4. 聚合操作 ---");

            // 简单聚合
            System.out.println("count, avg, max, min:");
            df.agg(
                    count("*").as("total"),
                    avg("age").as("avg_age"),
                    max("age").as("max_age"),
                    min("age").as("min_age")
            ).show();

            // 分组聚合
            System.out.println("按城市分组统计:");
            df.groupBy("city")
                    .agg(
                            count("*").as("count"),
                            avg("age").as("avg_age"),
                            collect_list("name").as("names")
                    )
                    .orderBy(col("count").desc())
                    .show(false);

            // ============================================
            // 5. SQL 查询
            // ============================================
            System.out.println("\n--- 5. SQL 查询 ---");

            // 注册临时视图
            df.createOrReplaceTempView("people");

            // 执行 SQL
            System.out.println("SQL 查询:");
            spark.sql(
                    "SELECT city, COUNT(*) as count, AVG(age) as avg_age " +
                    "FROM people " +
                    "GROUP BY city " +
                    "ORDER BY count DESC"
            ).show();

            // 复杂查询
            System.out.println("复杂 SQL:");
            spark.sql(
                    "SELECT name, age, city, " +
                    "CASE " +
                    "  WHEN age < 28 THEN '青年' " +
                    "  WHEN age < 33 THEN '中青年' " +
                    "  ELSE '中年' " +
                    "END as age_group " +
                    "FROM people " +
                    "WHERE city IN ('Beijing', 'Shanghai')"
            ).show();

            // ============================================
            // 6. 常用函数
            // ============================================
            System.out.println("\n--- 6. 常用函数 ---");

            // 字符串函数
            System.out.println("字符串函数:");
            df.select(
                    col("name"),
                    upper(col("name")).as("upper_name"),
                    lower(col("name")).as("lower_name"),
                    length(col("name")).as("name_length"),
                    concat(col("name"), lit(" from "), col("city")).as("full_info")
            ).show();

            // 数值函数
            List<Sales> salesList = Arrays.asList(
                    new Sales("A", 123.456),
                    new Sales("B", 789.123),
                    new Sales("C", 456.789)
            );
            Dataset<Row> salesDf = spark.createDataFrame(salesList, Sales.class);

            System.out.println("数值函数:");
            salesDf.select(
                    col("product"),
                    col("amount"),
                    round(col("amount"), 2).as("rounded"),
                    ceil(col("amount")).as("ceil"),
                    floor(col("amount")).as("floor")
            ).show();

            // 日期函数
            System.out.println("日期函数:");
            List<Row> dateData = Arrays.asList(
                    RowFactory.create("2024-01-15"),
                    RowFactory.create("2024-06-20"),
                    RowFactory.create("2024-12-25")
            );
            StructType dateSchema = new StructType(new StructField[]{
                    new StructField("date_str", DataTypes.StringType, true, Metadata.empty())
            });
            Dataset<Row> dateDf = spark.createDataFrame(dateData, dateSchema);

            dateDf.select(
                    col("date_str"),
                    to_date(col("date_str")).as("date"),
                    year(to_date(col("date_str"))).as("year"),
                    month(to_date(col("date_str"))).as("month"),
                    dayofmonth(to_date(col("date_str"))).as("day"),
                    dayofweek(to_date(col("date_str"))).as("dayofweek")
            ).show();

            // 条件函数
            System.out.println("条件函数:");
            df.select(
                    col("name"),
                    col("age"),
                    when(col("age").lt(30), "young")
                            .when(col("age").lt(35), "middle")
                            .otherwise("senior").as("category")
            ).show();

            // ============================================
            // 7. Join 操作
            // ============================================
            System.out.println("\n--- 7. Join 操作 ---");

            List<Department> deptList = Arrays.asList(
                    new Department(1, "Engineering"),
                    new Department(2, "Marketing"),
                    new Department(4, "Sales")
            );
            Dataset<Row> deptDf = spark.createDataFrame(deptList, Department.class);

            System.out.println("Employees:");
            df.select("id", "name").show();
            System.out.println("Departments:");
            deptDf.show();

            // Inner Join
            System.out.println("Inner Join:");
            df.join(deptDf, df.col("id").equalTo(deptDf.col("id")), "inner")
                    .select(df.col("id"), df.col("name"), deptDf.col("dept"))
                    .show();

            // Left Join
            System.out.println("Left Join:");
            df.join(deptDf, df.col("id").equalTo(deptDf.col("id")), "left")
                    .select(df.col("id"), df.col("name"), deptDf.col("dept"))
                    .show();

            // ============================================
            // 8. 窗口函数
            // ============================================
            System.out.println("\n--- 8. 窗口函数 ---");

            // 按城市分组的窗口
            WindowSpec cityWindow = Window.partitionBy("city").orderBy(col("age").desc());

            System.out.println("窗口函数示例:");
            df.select(
                    col("name"),
                    col("city"),
                    col("age"),
                    row_number().over(cityWindow).as("rank"),
                    avg("age").over(Window.partitionBy("city")).as("city_avg_age"),
                    lag("name", 1).over(cityWindow).as("prev_person")
            ).show();

            // ============================================
            // 9. UDF (用户自定义函数)
            // ============================================
            System.out.println("\n--- 9. UDF ---");

            // 注册 UDF
            spark.udf().register("age_category", (Integer age) -> {
                if (age < 28) return "青年";
                else if (age < 35) return "中年";
                else return "成熟";
            }, DataTypes.StringType);

            System.out.println("SQL 中使用 UDF:");
            spark.sql("SELECT name, age, age_category(age) as category FROM people").show();

            System.out.println("\n==================================================");
            System.out.println("Spark SQL 完成！运行 J03_Practice 继续学习");
            System.out.println("==================================================");

        } finally {
            spark.stop();
        }
    }
}
