package spark;

import org.apache.spark.sql.*;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import static org.apache.spark.sql.functions.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Spark 第三课：实战案例 (Java 版)
 *
 * 案例：电商数据分析
 * - 订单分析
 * - 用户行为分析
 * - 销售报表
 */
public class J03_Practice {

    public static class User implements Serializable {
        private int userId;
        private String userName;
        private int age;
        private String gender;
        private String city;

        public User() {}
        public User(int userId, String userName, int age, String gender, String city) {
            this.userId = userId; this.userName = userName; this.age = age;
            this.gender = gender; this.city = city;
        }
        public int getUserId() { return userId; }
        public void setUserId(int userId) { this.userId = userId; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
    }

    public static class Product implements Serializable {
        private int productId;
        private String productName;
        private String category;
        private double price;

        public Product() {}
        public Product(int productId, String productName, String category, double price) {
            this.productId = productId; this.productName = productName;
            this.category = category; this.price = price;
        }
        public int getProductId() { return productId; }
        public void setProductId(int productId) { this.productId = productId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }

    public static class Order implements Serializable {
        private int orderId;
        private int userId;
        private int productId;
        private int quantity;
        private String orderDate;

        public Order() {}
        public Order(int orderId, int userId, int productId, int quantity, String orderDate) {
            this.orderId = orderId; this.userId = userId; this.productId = productId;
            this.quantity = quantity; this.orderDate = orderDate;
        }
        public int getOrderId() { return orderId; }
        public void setOrderId(int orderId) { this.orderId = orderId; }
        public int getUserId() { return userId; }
        public void setUserId(int userId) { this.userId = userId; }
        public int getProductId() { return productId; }
        public void setProductId(int productId) { this.productId = productId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public String getOrderDate() { return orderDate; }
        public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
    }

    public static void main(String[] args) {

        System.out.println("============================================================");
        System.out.println("Spark 第三课：实战案例 - 电商数据分析 (Java 版)");
        System.out.println("============================================================");

        SparkSession spark = SparkSession.builder()
                .appName("E-commerce Analytics Java")
                .master("local[*]")
                .config("spark.sql.shuffle.partitions", "4")
                .getOrCreate();

        spark.sparkContext().setLogLevel("WARN");

        try {
            // ============================================
            // 模拟数据
            // ============================================

            // 用户数据
            List<User> userList = Arrays.asList(
                    new User(1, "张三", 25, "M", "北京"),
                    new User(2, "李四", 32, "F", "上海"),
                    new User(3, "王五", 28, "M", "广州"),
                    new User(4, "赵六", 35, "F", "北京"),
                    new User(5, "钱七", 22, "M", "深圳"),
                    new User(6, "孙八", 29, "F", "上海"),
                    new User(7, "周九", 31, "M", "杭州"),
                    new User(8, "吴十", 27, "F", "成都")
            );
            Dataset<Row> users = spark.createDataFrame(userList, User.class);

            // 商品数据
            List<Product> productList = Arrays.asList(
                    new Product(101, "iPhone 15", "手机", 6999.0),
                    new Product(102, "MacBook Pro", "电脑", 14999.0),
                    new Product(103, "AirPods Pro", "配件", 1899.0),
                    new Product(104, "iPad Air", "平板", 4799.0),
                    new Product(105, "华为 Mate 60", "手机", 5999.0),
                    new Product(106, "ThinkPad X1", "电脑", 9999.0),
                    new Product(107, "小米手环", "配件", 269.0),
                    new Product(108, "Kindle", "阅读器", 999.0)
            );
            Dataset<Row> products = spark.createDataFrame(productList, Product.class);

            // 订单数据
            List<Order> orderList = Arrays.asList(
                    new Order(1001, 1, 101, 1, "2024-01-15"),
                    new Order(1002, 1, 103, 2, "2024-01-15"),
                    new Order(1003, 2, 102, 1, "2024-01-16"),
                    new Order(1004, 3, 101, 1, "2024-01-17"),
                    new Order(1005, 4, 104, 1, "2024-01-18"),
                    new Order(1006, 2, 103, 1, "2024-01-19"),
                    new Order(1007, 5, 105, 1, "2024-01-20"),
                    new Order(1008, 1, 107, 3, "2024-01-21"),
                    new Order(1009, 6, 102, 1, "2024-01-22"),
                    new Order(1010, 3, 106, 1, "2024-01-23"),
                    new Order(1011, 7, 101, 2, "2024-02-01"),
                    new Order(1012, 8, 108, 1, "2024-02-02"),
                    new Order(1013, 2, 104, 1, "2024-02-03"),
                    new Order(1014, 4, 105, 1, "2024-02-05"),
                    new Order(1015, 5, 103, 2, "2024-02-07"),
                    new Order(1016, 1, 102, 1, "2024-02-10"),
                    new Order(1017, 6, 107, 2, "2024-02-12"),
                    new Order(1018, 3, 103, 1, "2024-02-15"),
                    new Order(1019, 7, 104, 1, "2024-02-18"),
                    new Order(1020, 8, 101, 1, "2024-02-20")
            );
            Dataset<Row> orders = spark.createDataFrame(orderList, Order.class);

            // 注册临时视图
            users.createOrReplaceTempView("users");
            products.createOrReplaceTempView("products");
            orders.createOrReplaceTempView("orders");

            // ============================================
            // 案例1：销售总览
            // ============================================
            System.out.println("\n==================================================");
            System.out.println("案例1：销售总览");
            System.out.println("==================================================");

            // 计算订单详情
            Dataset<Row> orderDetails = orders
                    .join(products, orders.col("productId").equalTo(products.col("productId")))
                    .withColumn("amount", col("quantity").multiply(col("price")));

            orderDetails.createOrReplaceTempView("order_details");

            System.out.println("\n订单详情:");
            orderDetails.select("orderId", "productName", "category", "quantity", "price", "amount")
                    .show(10);

            // 销售汇总
            System.out.println("\n销售汇总:");
            orderDetails.agg(
                    countDistinct("orderId").as("订单数"),
                    sum("quantity").as("商品数量"),
                    sum("amount").as("销售总额"),
                    avg("amount").as("平均订单金额")
            ).show();

            // ============================================
            // 案例2：商品销售排行
            // ============================================
            System.out.println("\n==================================================");
            System.out.println("案例2：商品销售排行");
            System.out.println("==================================================");

            Dataset<Row> productRanking = orderDetails
                    .groupBy(orders.col("productId"), col("productName"), col("category"))
                    .agg(
                            sum("quantity").as("销量"),
                            sum("amount").as("销售额"),
                            countDistinct("orderId").as("订单数")
                    )
                    .orderBy(col("销售额").desc());

            System.out.println("\n商品销售排行（按销售额）:");
            productRanking.show();

            // 按类别统计
            System.out.println("\n按类别统计:");
            orderDetails
                    .groupBy("category")
                    .agg(
                            sum("quantity").as("销量"),
                            sum("amount").as("销售额"),
                            countDistinct(orders.col("productId")).as("商品种类")
                    )
                    .orderBy(col("销售额").desc())
                    .show();

            // ============================================
            // 案例3：用户消费分析
            // ============================================
            System.out.println("\n==================================================");
            System.out.println("案例3：用户消费分析");
            System.out.println("==================================================");

            Dataset<Row> userStats = orderDetails
                    .join(users, orderDetails.col("userId").equalTo(users.col("userId")))
                    .groupBy(orders.col("userId"), col("userName"), col("city"), col("gender"))
                    .agg(
                            countDistinct("orderId").as("订单数"),
                            sum("amount").as("消费总额"),
                            avg("amount").as("平均订单金额"),
                            min("orderDate").as("首次购买"),
                            max("orderDate").as("最近购买")
                    )
                    .orderBy(col("消费总额").desc());

            System.out.println("\n用户消费排行:");
            userStats.show();

            // 用户分层
            System.out.println("\n用户价值分层:");
            userStats
                    .withColumn("用户等级",
                            when(col("消费总额").geq(15000), "高价值")
                                    .when(col("消费总额").geq(8000), "中价值")
                                    .otherwise("普通"))
                    .groupBy("用户等级")
                    .agg(
                            count("*").as("用户数"),
                            sum("消费总额").as("总消费"),
                            avg("消费总额").as("平均消费")
                    )
                    .orderBy(col("总消费").desc())
                    .show();

            // ============================================
            // 案例4：时间维度分析
            // ============================================
            System.out.println("\n==================================================");
            System.out.println("案例4：时间维度分析");
            System.out.println("==================================================");

            Dataset<Row> timeAnalysis = orderDetails
                    .withColumn("order_date", to_date(col("orderDate")))
                    .withColumn("year_month", date_format(col("order_date"), "yyyy-MM"))
                    .withColumn("day_of_week", dayofweek(col("order_date")));

            // 月度销售趋势
            System.out.println("\n月度销售趋势:");
            timeAnalysis
                    .groupBy("year_month")
                    .agg(
                            countDistinct("orderId").as("订单数"),
                            sum("amount").as("销售额")
                    )
                    .orderBy("year_month")
                    .show();

            // 星期销售分布
            System.out.println("\n星期销售分布 (1=周日, 7=周六):");
            timeAnalysis
                    .groupBy("day_of_week")
                    .agg(
                            countDistinct("orderId").as("订单数"),
                            sum("amount").as("销售额")
                    )
                    .orderBy("day_of_week")
                    .show();

            // ============================================
            // 案例5：窗口函数应用
            // ============================================
            System.out.println("\n==================================================");
            System.out.println("案例5：窗口函数应用");
            System.out.println("==================================================");

            WindowSpec userWindow = Window.partitionBy(orders.col("userId")).orderBy(col("orderDate"));

            System.out.println("\n用户购买序列:");
            orderDetails
                    .select(orders.col("userId"), col("productName"), col("orderDate"), col("amount"))
                    .withColumn("购买序号", row_number().over(userWindow))
                    .withColumn("累计消费", sum("amount").over(userWindow))
                    .orderBy(orders.col("userId"), col("orderDate"))
                    .show(15);

            // ============================================
            // 案例6：复杂 SQL 分析
            // ============================================
            System.out.println("\n==================================================");
            System.out.println("案例6：复杂 SQL 分析");
            System.out.println("==================================================");

            System.out.println("\n城市消费能力分析:");
            spark.sql(
                    "WITH user_consumption AS (" +
                    "  SELECT u.city, u.userId, SUM(od.quantity * p.price) as total_amount " +
                    "  FROM orders od " +
                    "  JOIN users u ON od.userId = u.userId " +
                    "  JOIN products p ON od.productId = p.productId " +
                    "  GROUP BY u.city, u.userId" +
                    ") " +
                    "SELECT city, COUNT(DISTINCT userId) as user_count, " +
                    "       SUM(total_amount) as total_spend, " +
                    "       AVG(total_amount) as avg_spend, " +
                    "       MAX(total_amount) as max_spend, " +
                    "       MIN(total_amount) as min_spend " +
                    "FROM user_consumption " +
                    "GROUP BY city " +
                    "ORDER BY total_spend DESC"
            ).show();

            System.out.println("\n============================================================");
            System.out.println("恭喜！Spark 实战课程完成！");
            System.out.println("============================================================");

        } finally {
            spark.stop();
        }
    }
}
