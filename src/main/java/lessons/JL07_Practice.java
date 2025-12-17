package lessons;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 第七课：综合练习 (Java 版)
 *
 * 这个文件包含一个完整的小项目：简单的任务管理系统
 * 综合运用前面学到的所有知识
 */
public class JL07_Practice {

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("第七课：综合练习 - 任务管理系统 (Java 版)");
        System.out.println("==================================================");

        // ============================================
        // 演示
        // ============================================

        System.out.println("\n--- 创建任务仓库和服务 ---");
        TaskRepository repo = new TaskRepository();
        TaskService service = new TaskService(repo);

        System.out.println("\n--- 添加任务 ---");
        Task task1 = repo.add("学习 Scala 基础", "完成前三课的学习", TaskPriority.HIGH);
        Task task2 = repo.add("练习模式匹配", "完成第四课的练习", TaskPriority.MEDIUM);
        Task task3 = repo.add("学习集合操作", "熟悉 map/filter/fold", TaskPriority.LOW);

        System.out.println(TaskFormatter.format(task1));
        System.out.println(TaskFormatter.format(task2));
        System.out.println(TaskFormatter.format(task3));

        System.out.println("\n--- 开始第一个任务 ---");
        TaskResult<Task> startResult = service.startTask(1);
        if (startResult.isSuccess()) {
            System.out.println("已开始: " + TaskFormatter.format(startResult.getValue()));
        } else {
            System.out.println("错误: " + startResult.getError());
        }

        System.out.println("\n--- 完成第一个任务 ---");
        TaskResult<Task> completeResult = service.completeTask(1);
        if (completeResult.isSuccess()) {
            System.out.println("已完成: " + TaskFormatter.format(completeResult.getValue()));
        } else {
            System.out.println("错误: " + completeResult.getError());
        }

        System.out.println("\n--- 尝试完成未开始的任务 ---");
        TaskResult<Task> failResult = service.completeTask(2);
        if (failResult.isSuccess()) {
            System.out.println("已完成: " + TaskFormatter.format(failResult.getValue()));
        } else {
            System.out.println("操作无效: " + failResult.getError().getMessage());
        }

        System.out.println("\n--- 所有任务列表 ---");
        for (Task t : repo.getAll()) {
            System.out.println(TaskFormatter.format(t));
        }

        System.out.println("\n--- 任务统计 ---");
        Map<TaskStatus, Long> stats = repo.statistics();
        System.out.println("  待办: " + stats.getOrDefault(TaskStatus.TODO, 0L));
        System.out.println("  进行中: " + stats.getOrDefault(TaskStatus.IN_PROGRESS, 0L));
        System.out.println("  已完成: " + stats.getOrDefault(TaskStatus.DONE, 0L));

        System.out.println("\n--- 高优先级任务 ---");
        for (Task t : repo.filterByPriority(TaskPriority.HIGH)) {
            System.out.println(TaskFormatter.format(t));
        }

        System.out.println("\n--- 任务详情 ---");
        repo.get(1).ifPresent(t -> System.out.println(TaskFormatter.formatDetailed(t)));

        // ============================================
        // 使用高阶函数处理任务
        // ============================================
        System.out.println("\n--- 高阶函数示例 ---");

        List<Task> allTasks = repo.getAll();

        // 统计各优先级任务数
        Map<TaskPriority, List<Task>> byPriority = allTasks.stream()
                .collect(Collectors.groupingBy(Task::getPriority));
        System.out.println("按优先级分组:");
        for (Map.Entry<TaskPriority, List<Task>> entry : byPriority.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue().size() + " 个任务");
        }

        // 查找未完成的高优先级任务
        List<Task> urgentTasks = allTasks.stream()
                .filter(t -> t.getPriority() == TaskPriority.HIGH && t.getStatus() != TaskStatus.DONE)
                .collect(Collectors.toList());
        System.out.println("紧急未完成任务: " + urgentTasks.size() + " 个");

        // 任务标题列表
        String titles = allTasks.stream()
                .map(Task::getTitle)
                .collect(Collectors.joining(", "));
        System.out.println("所有标题: " + titles);

        // 检查是否有进行中的任务
        boolean hasInProgress = allTasks.stream()
                .anyMatch(t -> t.getStatus() == TaskStatus.IN_PROGRESS);
        System.out.println("有进行中的任务: " + hasInProgress);

        // 检查是否所有任务都已完成
        boolean allDone = allTasks.stream()
                .allMatch(t -> t.getStatus() == TaskStatus.DONE);
        System.out.println("所有任务已完成: " + allDone);

        System.out.println("\n==================================================");
        System.out.println("恭喜！你已完成所有 Java 基础课程！");
        System.out.println("==================================================");
        System.out.println("\n后续学习建议:");
        System.out.println("1. 深入学习 Java Stream API");
        System.out.println("2. 学习 Java 并发编程");
        System.out.println("3. 学习 Spring Boot 框架");
        System.out.println("4. 学习 Apache Spark (Java API)");
        System.out.println("5. 对比学习 Scala 语言");
    }
}

// ============================================
// 1. 数据模型定义
// ============================================

enum TaskStatus {
    TODO, IN_PROGRESS, DONE
}

enum TaskPriority {
    LOW(1), MEDIUM(2), HIGH(3);

    private final int level;

    TaskPriority(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}

class Task {
    private final int id;
    private final String title;
    private final String description;
    private TaskStatus status;
    private final TaskPriority priority;

    public Task(int id, String title, String description, TaskStatus status, TaskPriority priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }
    public TaskPriority getPriority() { return priority; }

    // 返回新的 Task（不可变风格）
    public Task withStatus(TaskStatus newStatus) {
        return new Task(id, title, description, newStatus, priority);
    }
}

// ============================================
// 2. 任务仓库
// ============================================

class TaskRepository {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private int nextId = 1;

    public Task add(String title, String description, TaskPriority priority) {
        Task task = new Task(nextId, title, description, TaskStatus.TODO, priority);
        tasks.put(nextId, task);
        nextId++;
        return task;
    }

    public Optional<Task> get(int id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public List<Task> getAll() {
        return tasks.values().stream()
                .sorted(Comparator.comparingInt(Task::getId))
                .collect(Collectors.toList());
    }

    public Optional<Task> updateStatus(int id, TaskStatus status) {
        Task task = tasks.get(id);
        if (task != null) {
            Task updated = task.withStatus(status);
            tasks.put(id, updated);
            return Optional.of(updated);
        }
        return Optional.empty();
    }

    public boolean delete(int id) {
        return tasks.remove(id) != null;
    }

    public List<Task> filterByStatus(TaskStatus status) {
        return tasks.values().stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Task> filterByPriority(TaskPriority priority) {
        return tasks.values().stream()
                .filter(t -> t.getPriority() == priority)
                .collect(Collectors.toList());
    }

    public Map<TaskStatus, Long> statistics() {
        return tasks.values().stream()
                .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));
    }
}

// ============================================
// 3. 任务格式化器
// ============================================

class TaskFormatter {
    public static String format(Task task) {
        String statusIcon;
        switch (task.getStatus()) {
            case TODO: statusIcon = "[ ]"; break;
            case IN_PROGRESS: statusIcon = "[~]"; break;
            case DONE: statusIcon = "[x]"; break;
            default: statusIcon = "[?]";
        }

        String priorityIcon;
        switch (task.getPriority()) {
            case HIGH: priorityIcon = "!!!"; break;
            case MEDIUM: priorityIcon = "!!"; break;
            case LOW: priorityIcon = "!"; break;
            default: priorityIcon = "?";
        }

        return statusIcon + " " + priorityIcon + " #" + task.getId() + " " + task.getTitle();
    }

    public static String formatDetailed(Task task) {
        return "任务 #" + task.getId() + "\n" +
                "  标题: " + task.getTitle() + "\n" +
                "  描述: " + task.getDescription() + "\n" +
                "  状态: " + task.getStatus() + "\n" +
                "  优先级: " + task.getPriority();
    }
}

// ============================================
// 4. 任务操作结果（类似 Either）
// ============================================

class TaskError {
    private final String message;

    public TaskError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static TaskError taskNotFound(int id) {
        return new TaskError("任务不存在: #" + id);
    }

    public static TaskError invalidOperation(String message) {
        return new TaskError(message);
    }
}

class TaskResult<T> {
    private final T value;
    private final TaskError error;
    private final boolean success;

    private TaskResult(T value, TaskError error, boolean success) {
        this.value = value;
        this.error = error;
        this.success = success;
    }

    public static <T> TaskResult<T> success(T value) {
        return new TaskResult<>(value, null, true);
    }

    public static <T> TaskResult<T> failure(TaskError error) {
        return new TaskResult<>(null, error, false);
    }

    public boolean isSuccess() { return success; }
    public T getValue() { return value; }
    public TaskError getError() { return error; }
}

// ============================================
// 5. 任务服务
// ============================================

class TaskService {
    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public TaskResult<Task> createTask(String title, String description, TaskPriority priority) {
        if (title == null || title.isEmpty()) {
            return TaskResult.failure(TaskError.invalidOperation("标题不能为空"));
        }
        return TaskResult.success(repo.add(title, description, priority));
    }

    public TaskResult<Task> startTask(int id) {
        Optional<Task> taskOpt = repo.get(id);
        if (!taskOpt.isPresent()) {
            return TaskResult.failure(TaskError.taskNotFound(id));
        }

        Task task = taskOpt.get();
        if (task.getStatus() == TaskStatus.DONE) {
            return TaskResult.failure(TaskError.invalidOperation("已完成的任务不能重新开始"));
        }

        Optional<Task> updated = repo.updateStatus(id, TaskStatus.IN_PROGRESS);
        return updated.map(TaskResult::success)
                .orElse(TaskResult.failure(TaskError.taskNotFound(id)));
    }

    public TaskResult<Task> completeTask(int id) {
        Optional<Task> taskOpt = repo.get(id);
        if (!taskOpt.isPresent()) {
            return TaskResult.failure(TaskError.taskNotFound(id));
        }

        Task task = taskOpt.get();
        if (task.getStatus() == TaskStatus.TODO) {
            return TaskResult.failure(TaskError.invalidOperation("请先开始任务"));
        }

        Optional<Task> updated = repo.updateStatus(id, TaskStatus.DONE);
        return updated.map(TaskResult::success)
                .orElse(TaskResult.failure(TaskError.taskNotFound(id)));
    }

    public TaskResult<Void> deleteTask(int id) {
        if (repo.delete(id)) {
            return TaskResult.success(null);
        }
        return TaskResult.failure(TaskError.taskNotFound(id));
    }
}
