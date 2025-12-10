# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Scala 和 Spark 学习项目，包含从基础到实战的完整教程。

## Build Commands

```bash
sbt compile                              # 编译项目（首次会下载依赖）
sbt "runMain lessons.L01_BasicSyntax"    # 运行 Scala 课程
sbt "runMain spark.S01_RDDBasics"        # 运行 Spark 课程
sbt console                              # 启动 REPL
```

## Project Structure

```
src/main/scala/
├── lessons/                    # Scala 基础教程
│   ├── L01_BasicSyntax.scala       # 变量、类型、控制结构
│   ├── L02_Functions.scala         # 高阶函数、柯里化、递归
│   ├── L03_OOP.scala               # 类、Trait、Case Class
│   ├── L04_PatternMatching.scala   # 模式匹配、提取器
│   ├── L05_Collections.scala       # 集合操作、高阶函数
│   ├── L06_Advanced.scala          # Option、Either、隐式转换
│   └── L07_Practice.scala          # 综合练习：任务管理系统
│
└── spark/                      # Spark 教程
    ├── S01_RDDBasics.scala         # RDD 创建与操作
    ├── S02_SparkSQL.scala          # DataFrame、SQL、窗口函数
    ├── S03_Practice.scala          # 实战：电商数据分析
    └── S04_WordCount.scala         # 经典案例：词频统计
```

## Learning Path

**Scala 基础 (按顺序学习):**
```bash
sbt "runMain lessons.L01_BasicSyntax"
sbt "runMain lessons.L02_Functions"
sbt "runMain lessons.L03_OOP"
sbt "runMain lessons.L04_PatternMatching"
sbt "runMain lessons.L05_Collections"
sbt "runMain lessons.L06_Advanced"
sbt "runMain lessons.L07_Practice"
```

**Spark 进阶 (完成 Scala 基础后):**
```bash
sbt "runMain spark.S01_RDDBasics"
sbt "runMain spark.S02_SparkSQL"
sbt "runMain spark.S03_Practice"
sbt "runMain spark.S04_WordCount"
```
