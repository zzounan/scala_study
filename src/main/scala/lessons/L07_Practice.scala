package lessons

/**
 * 第七课：综合练习
 *
 * 这个文件包含一个完整的小项目：简单的任务管理系统
 * 综合运用前面学到的所有知识
 */
object L07_Practice extends App {

  println("=" * 50)
  println("第七课：综合练习 - 任务管理系统")
  println("=" * 50)

  // ============================================
  // 1. 数据模型定义（Case Class）
  // ============================================

  // 任务状态（密封特质 + Case Object）
  sealed trait TaskStatus
  case object Todo extends TaskStatus
  case object InProgress extends TaskStatus
  case object Done extends TaskStatus

  // 任务优先级
  sealed trait Priority extends Ordered[Priority] {
    def level: Int
    def compare(that: Priority): Int = this.level - that.level
  }
  case object Low extends Priority { val level = 1 }
  case object Medium extends Priority { val level = 2 }
  case object High extends Priority { val level = 3 }

  // 任务（Case Class）
  case class Task(
    id: Int,
    title: String,
    description: String,
    status: TaskStatus = Todo,
    priority: Priority = Medium
  )

  // ============================================
  // 2. 任务仓库（面向对象 + 函数式）
  // ============================================

  class TaskRepository {
    private var tasks: Map[Int, Task] = Map.empty
    private var nextId: Int = 1

    // 添加任务
    def add(title: String, description: String, priority: Priority = Medium): Task = {
      val task = Task(nextId, title, description, priority = priority)
      tasks = tasks + (nextId -> task)
      nextId += 1
      task
    }

    // 获取任务
    def get(id: Int): Option[Task] = tasks.get(id)

    // 获取所有任务
    def getAll: List[Task] = tasks.values.toList.sortBy(_.id)

    // 更新任务状态
    def updateStatus(id: Int, status: TaskStatus): Option[Task] = {
      tasks.get(id).map { task =>
        val updated = task.copy(status = status)
        tasks = tasks + (id -> updated)
        updated
      }
    }

    // 删除任务
    def delete(id: Int): Boolean = {
      val exists = tasks.contains(id)
      tasks = tasks - id
      exists
    }

    // 按状态过滤
    def filterByStatus(status: TaskStatus): List[Task] = {
      tasks.values.filter(_.status == status).toList
    }

    // 按优先级过滤
    def filterByPriority(priority: Priority): List[Task] = {
      tasks.values.filter(_.priority == priority).toList
    }

    // 统计
    def statistics: Map[TaskStatus, Int] = {
      tasks.values.groupBy(_.status).map { case (status, ts) =>
        status -> ts.size
      }
    }
  }

  // ============================================
  // 3. 任务格式化（隐式类扩展）
  // ============================================

  implicit class TaskFormatter(task: Task) {
    def format: String = {
      val statusIcon = task.status match {
        case Todo => "[ ]"
        case InProgress => "[~]"
        case Done => "[x]"
      }
      val priorityIcon = task.priority match {
        case High => "!!!"
        case Medium => "!!"
        case Low => "!"
      }
      s"$statusIcon $priorityIcon #${task.id} ${task.title}"
    }

    def formatDetailed: String = {
      s"""
         |任务 #${task.id}
         |  标题: ${task.title}
         |  描述: ${task.description}
         |  状态: ${task.status}
         |  优先级: ${task.priority}
       """.stripMargin.trim
    }
  }

  // ============================================
  // 4. 任务操作结果（Either 错误处理）
  // ============================================

  sealed trait TaskError
  case class TaskNotFound(id: Int) extends TaskError
  case class InvalidOperation(message: String) extends TaskError

  type TaskResult[T] = Either[TaskError, T]

  class TaskService(repo: TaskRepository) {

    def createTask(title: String, description: String, priority: Priority): TaskResult[Task] = {
      if (title.isEmpty) Left(InvalidOperation("标题不能为空"))
      else Right(repo.add(title, description, priority))
    }

    def startTask(id: Int): TaskResult[Task] = {
      repo.get(id) match {
        case None => Left(TaskNotFound(id))
        case Some(task) if task.status == Done =>
          Left(InvalidOperation("已完成的任务不能重新开始"))
        case Some(_) =>
          repo.updateStatus(id, InProgress).toRight(TaskNotFound(id))
      }
    }

    def completeTask(id: Int): TaskResult[Task] = {
      repo.get(id) match {
        case None => Left(TaskNotFound(id))
        case Some(task) if task.status == Todo =>
          Left(InvalidOperation("请先开始任务"))
        case Some(_) =>
          repo.updateStatus(id, Done).toRight(TaskNotFound(id))
      }
    }

    def deleteTask(id: Int): TaskResult[Unit] = {
      if (repo.delete(id)) Right(())
      else Left(TaskNotFound(id))
    }
  }

  // ============================================
  // 5. 演示
  // ============================================

  println("\n--- 创建任务仓库和服务 ---")
  val repo = new TaskRepository
  val service = new TaskService(repo)

  println("\n--- 添加任务 ---")
  val task1 = repo.add("学习 Scala 基础", "完成前三课的学习", High)
  val task2 = repo.add("练习模式匹配", "完成第四课的练习")
  val task3 = repo.add("学习集合操作", "熟悉 map/filter/fold", Low)

  println(task1.format)
  println(task2.format)
  println(task3.format)

  println("\n--- 开始第一个任务 ---")
  service.startTask(1) match {
    case Right(task) => println(s"已开始: ${task.format}")
    case Left(error) => println(s"错误: $error")
  }

  println("\n--- 完成第一个任务 ---")
  service.completeTask(1) match {
    case Right(task) => println(s"已完成: ${task.format}")
    case Left(error) => println(s"错误: $error")
  }

  println("\n--- 尝试完成未开始的任务 ---")
  service.completeTask(2) match {
    case Right(task) => println(s"已完成: ${task.format}")
    case Left(InvalidOperation(msg)) => println(s"操作无效: $msg")
    case Left(TaskNotFound(id)) => println(s"任务不存在: #$id")
  }

  println("\n--- 所有任务列表 ---")
  repo.getAll.foreach(t => println(t.format))

  println("\n--- 任务统计 ---")
  val stats = repo.statistics
  println(s"  待办: ${stats.getOrElse(Todo, 0)}")
  println(s"  进行中: ${stats.getOrElse(InProgress, 0)}")
  println(s"  已完成: ${stats.getOrElse(Done, 0)}")

  println("\n--- 高优先级任务 ---")
  repo.filterByPriority(High).foreach(t => println(t.format))

  println("\n--- 任务详情 ---")
  repo.get(1).foreach(t => println(t.formatDetailed))

  // ============================================
  // 6. 使用高阶函数处理任务
  // ============================================
  println("\n--- 高阶函数示例 ---")

  // 批量操作
  val allTasks = repo.getAll

  // 统计各优先级任务数
  val byPriority = allTasks.groupBy(_.priority)
  println("按优先级分组:")
  byPriority.foreach { case (priority, tasks) =>
    println(s"  $priority: ${tasks.length} 个任务")
  }

  // 查找未完成的高优先级任务
  val urgentTasks = allTasks
    .filter(t => t.priority == High && t.status != Done)
  println(s"紧急未完成任务: ${urgentTasks.length} 个")

  // 任务标题列表
  val titles = allTasks.map(_.title)
  println(s"所有标题: ${titles.mkString(", ")}")

  // 检查是否有进行中的任务
  val hasInProgress = allTasks.exists(_.status == InProgress)
  println(s"有进行中的任务: $hasInProgress")

  // 检查是否所有任务都已完成
  val allDone = allTasks.forall(_.status == Done)
  println(s"所有任务已完成: $allDone")

  println("\n" + "=" * 50)
  println("恭喜！你已完成所有 Scala 基础课程！")
  println("=" * 50)
  println("""
    |后续学习建议：
    |1. 深入学习 Scala 标准库
    |2. 学习 Cats / ZIO 等函数式编程库
    |3. 学习 Akka 进行并发编程
    |4. 学习 Play Framework 进行 Web 开发
    |5. 学习 Apache Spark 进行大数据处理
  """.stripMargin)
}
