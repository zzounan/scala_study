# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Scala/Java 和 Spark 学习项目，包含从基础到实战的完整教程。支持 Scala 和 Java 两种语言。

## Build Commands

```bash
# sbt (Scala + Java)
sbt compile                              # 编译项目
sbt "runMain lessons.L01_BasicSyntax"    # 运行 Scala 课程
sbt "runMain spark.S01_RDDBasics"        # 运行 Scala Spark 课程
sbt "runMain spark.J01_RDDBasics"        # 运行 Java Spark 课程
sbt console                              # 启动 Scala REPL

# Maven (Java)
mvn compile                                            # 编译 Java 代码
mvn exec:java -Dexec.mainClass="lessons.JL01_BasicSyntax"  # 运行 Java 课程
mvn exec:java -Dexec.mainClass="spark.J01_RDDBasics"       # 运行 Java Spark 课程
```

## Project Structure

```
src/main/
├── scala/                          # Scala 源码
│   ├── lessons/                    # Scala 基础教程
│   │   ├── L01_BasicSyntax.scala       # 变量、类型、控制结构
│   │   ├── L02_Functions.scala         # 高阶函数、柯里化、递归
│   │   ├── L03_OOP.scala               # 类、Trait、Case Class
│   │   ├── L04_PatternMatching.scala   # 模式匹配、提取器
│   │   ├── L05_Collections.scala       # 集合操作、高阶函数
│   │   ├── L06_Advanced.scala          # Option、Either、隐式转换
│   │   └── L07_Practice.scala          # 综合练习：任务管理系统
│   │
│   └── spark/                      # Spark 教程 (Scala)
│       ├── S01_RDDBasics.scala         # RDD 创建与操作
│       ├── S02_SparkSQL.scala          # DataFrame、SQL、窗口函数
│       ├── S03_Practice.scala          # 实战：电商数据分析
│       └── S04_WordCount.scala         # 经典案例：词频统计
│
└── java/                           # Java 源码
    ├── lessons/                    # Java 基础教程（对比 Scala）
    │   ├── JL01_BasicSyntax.java       # 变量、类型、控制结构
    │   ├── JL02_Functions.java         # Lambda、函数式接口
    │   ├── JL03_OOP.java               # 类、接口、继承
    │   ├── JL04_PatternMatching.java   # instanceof、正则匹配
    │   ├── JL05_Collections.java       # 集合、Stream API
    │   ├── JL06_Advanced.java          # Optional、泛型、JTry
    │   └── JL07_Practice.java          # 综合练习：任务管理系统
    │
    └── spark/                      # Spark 教程 (Java)
        ├── J01_RDDBasics.java          # RDD 创建与操作
        ├── J02_SparkSQL.java           # DataFrame、SQL、窗口函数
        ├── J03_Practice.java           # 实战：电商数据分析
        └── J04_WordCount.java          # 经典案例：词频统计
```

## Learning Path

### Scala 学习路径

**基础 (按顺序学习):**
```bash
sbt "runMain lessons.L01_BasicSyntax"
sbt "runMain lessons.L02_Functions"
sbt "runMain lessons.L03_OOP"
sbt "runMain lessons.L04_PatternMatching"
sbt "runMain lessons.L05_Collections"
sbt "runMain lessons.L06_Advanced"
sbt "runMain lessons.L07_Practice"
```

**Spark 进阶:**
```bash
sbt "runMain spark.S01_RDDBasics"
sbt "runMain spark.S02_SparkSQL"
sbt "runMain spark.S03_Practice"
sbt "runMain spark.S04_WordCount"
```

### Java 学习路径

**基础 (按顺序学习):**
```bash
mvn exec:java -Dexec.mainClass="lessons.JL01_BasicSyntax"
mvn exec:java -Dexec.mainClass="lessons.JL02_Functions"
mvn exec:java -Dexec.mainClass="lessons.JL03_OOP"
mvn exec:java -Dexec.mainClass="lessons.JL04_PatternMatching"
mvn exec:java -Dexec.mainClass="lessons.JL05_Collections"
mvn exec:java -Dexec.mainClass="lessons.JL06_Advanced"
mvn exec:java -Dexec.mainClass="lessons.JL07_Practice"
```

**Spark 进阶:**
```bash
mvn exec:java -Dexec.mainClass="spark.J01_RDDBasics"
mvn exec:java -Dexec.mainClass="spark.J02_SparkSQL"
mvn exec:java -Dexec.mainClass="spark.J03_Practice"
mvn exec:java -Dexec.mainClass="spark.J04_WordCount"
```

## Scala vs Java 对比

| 特性 | Scala | Java |
|------|-------|------|
| 不可变变量 | `val` | `final` |
| 可变变量 | `var` | 普通变量 |
| 函数类型 | `(A, B) => C` | `BiFunction<A, B, C>` |
| 空值处理 | `Option[T]` | `Optional<T>` |
| 模式匹配 | `match/case` | `switch` + `instanceof` |
| 集合操作 | `.map/.filter` | Stream API |
| 数据类 | `case class` | `record` (Java 16+) |
| 混入 | `trait` | `interface` (default 方法) |
