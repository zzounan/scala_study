package lessons;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 第五课：集合操作 (Java 版)
 *
 * 学习目标：
 * - List, Set, Map 基本操作
 * - Stream API（类似 Scala 高阶函数）
 * - 集合转换
 */
public class JL05_Collections {

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("第五课：集合操作 (Java 版)");
        System.out.println("==================================================");

        // ============================================
        // 1. List
        // ============================================
        System.out.println("\n--- 1. List ---");

        // 创建 List
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> empty = Collections.emptyList();
        List<Integer> range = IntStream.rangeClosed(1, 5).boxed().collect(Collectors.toList());
        List<String> filled = Collections.nCopies(5, "x");

        System.out.println("numbers: " + numbers);
        System.out.println("filled: " + filled);

        // List 操作
        System.out.println("get(0) [head]: " + numbers.get(0));
        System.out.println("subList(1, size) [tail]: " + numbers.subList(1, numbers.size()));
        System.out.println("get(size-1) [last]: " + numbers.get(numbers.size() - 1));
        System.out.println("size [length]: " + numbers.size());
        System.out.println("isEmpty: " + numbers.isEmpty());
        System.out.println("contains(3): " + numbers.contains(3));
        System.out.println("indexOf(3): " + numbers.indexOf(3));

        // 添加元素（创建新 List）
        List<Integer> prepended = new ArrayList<>();
        prepended.add(0);
        prepended.addAll(numbers);
        System.out.println("prepend 0: " + prepended);

        List<Integer> appended = new ArrayList<>(numbers);
        appended.add(6);
        System.out.println("append 6: " + appended);

        List<Integer> concatenated = new ArrayList<>(numbers);
        concatenated.addAll(Arrays.asList(6, 7, 8));
        System.out.println("concat [6,7,8]: " + concatenated);

        // ============================================
        // 2. Set（无重复元素）
        // ============================================
        System.out.println("\n--- 2. Set ---");

        Set<String> fruits = new HashSet<>(Arrays.asList("apple", "banana", "cherry"));
        Set<String> moreFruits = new HashSet<>(Arrays.asList("banana", "date", "elderberry"));

        System.out.println("fruits: " + fruits);
        System.out.println("contains banana: " + fruits.contains("banana"));

        Set<String> withDate = new HashSet<>(fruits);
        withDate.add("date");
        System.out.println("fruits + date: " + withDate);

        Set<String> withoutBanana = new HashSet<>(fruits);
        withoutBanana.remove("banana");
        System.out.println("fruits - banana: " + withoutBanana);

        // 集合运算
        Set<String> union = new HashSet<>(fruits);
        union.addAll(moreFruits);
        System.out.println("并集: " + union);

        Set<String> intersection = new HashSet<>(fruits);
        intersection.retainAll(moreFruits);
        System.out.println("交集: " + intersection);

        Set<String> difference = new HashSet<>(fruits);
        difference.removeAll(moreFruits);
        System.out.println("差集: " + difference);

        // ============================================
        // 3. Map（键值对）
        // ============================================
        System.out.println("\n--- 3. Map ---");

        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 95);
        scores.put("Bob", 87);
        scores.put("Charlie", 92);

        System.out.println("scores: " + scores);
        System.out.println("Alice的分数: " + scores.get("Alice"));
        System.out.println("安全获取: " + Optional.ofNullable(scores.get("David")));
        System.out.println("带默认值: " + scores.getOrDefault("David", 0));

        // 添加/更新
        Map<String, Integer> updatedScores = new HashMap<>(scores);
        updatedScores.put("David", 88);
        System.out.println("添加 David: " + updatedScores);

        Map<String, Integer> removedScores = new HashMap<>(scores);
        removedScores.remove("Bob");
        System.out.println("移除 Bob: " + removedScores);

        // 遍历
        System.out.println("遍历 Map:");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }

        // ============================================
        // 4. 常用高阶函数 (Stream API)
        // ============================================
        System.out.println("\n--- 4. Stream API 高阶函数 ---");

        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // map: 转换每个元素
        List<Integer> doubled = nums.stream()
                .map(n -> n * 2)
                .collect(Collectors.toList());
        System.out.println("map(n * 2): " + doubled);

        // filter: 过滤元素
        List<Integer> evens = nums.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        System.out.println("filter(n % 2 == 0): " + evens);

        // filterNot 等效
        List<Integer> odds = nums.stream()
                .filter(n -> n % 2 != 0)
                .collect(Collectors.toList());
        System.out.println("filter(n % 2 != 0): " + odds);

        // flatMap
        List<List<Integer>> nested = Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4),
                Arrays.asList(5)
        );
        List<Integer> flattened = nested.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        System.out.println("flatMap: " + flattened);

        List<String> words = Arrays.asList("hello", "world");
        List<Character> chars = words.stream()
                .flatMap(w -> w.chars().mapToObj(c -> (char) c))
                .collect(Collectors.toList());
        System.out.println("words.flatMap(toChars): " + chars);

        // reduce
        int sum = nums.stream().reduce(0, (a, b) -> a + b);
        int product = nums.stream().reduce(1, (a, b) -> a * b);
        System.out.println("reduce(+): " + sum);
        System.out.println("reduce(*): " + product);

        int sumWithInit = nums.stream().reduce(100, (a, b) -> a + b);
        System.out.println("reduce(100, +): " + sumWithInit);

        // ============================================
        // 5. 更多集合操作
        // ============================================
        System.out.println("\n--- 5. 更多集合操作 ---");

        // limit / skip (类似 take / drop)
        System.out.println("limit(3) [take]: " + nums.stream().limit(3).collect(Collectors.toList()));
        System.out.println("skip(3) [drop]: " + nums.stream().skip(3).collect(Collectors.toList()));

        // takeWhile / dropWhile (Java 9+, 这里用循环模拟)
        List<Integer> takeWhileLt5 = new ArrayList<>();
        for (int n : nums) {
            if (n >= 5) break;
            takeWhileLt5.add(n);
        }
        System.out.println("takeWhile(< 5): " + takeWhileLt5);

        // subList (类似 slice)
        System.out.println("subList(2, 5) [slice]: " + nums.subList(2, 5));

        // 分组
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        List<Integer> ages = Arrays.asList(25, 30, 35);

        // zip (手动实现)
        List<String> zipped = new ArrayList<>();
        for (int i = 0; i < Math.min(names.size(), ages.size()); i++) {
            zipped.add("(" + names.get(i) + ", " + ages.get(i) + ")");
        }
        System.out.println("zip: " + zipped);

        // zipWithIndex
        List<String> indexed = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            indexed.add("(" + names.get(i) + ", " + i + ")");
        }
        System.out.println("zipWithIndex: " + indexed);

        // partition
        Map<Boolean, List<Integer>> partitioned = nums.stream()
                .collect(Collectors.partitioningBy(n -> n < 5));
        System.out.println("partition(< 5): under5=" + partitioned.get(true) +
                ", over5=" + partitioned.get(false));

        // groupBy
        Map<Boolean, List<Integer>> grouped = nums.stream()
                .collect(Collectors.groupingBy(n -> n % 2 == 0));
        System.out.println("groupBy(% 2 == 0): " + grouped);

        // ============================================
        // 6. 排序
        // ============================================
        System.out.println("\n--- 6. 排序 ---");

        List<Integer> unsorted = Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6);

        List<Integer> sorted = unsorted.stream().sorted().collect(Collectors.toList());
        System.out.println("sorted: " + sorted);

        List<Integer> sortedDesc = unsorted.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        System.out.println("sorted desc: " + sortedDesc);

        // 自定义对象排序
        List<PersonData> people = Arrays.asList(
                new PersonData("Alice", 30),
                new PersonData("Bob", 25),
                new PersonData("Charlie", 35)
        );
        List<PersonData> sortedByAge = people.stream()
                .sorted(Comparator.comparingInt(p -> p.age))
                .collect(Collectors.toList());
        System.out.println("sortBy(age): " + sortedByAge);

        // ============================================
        // 7. 可变集合
        // ============================================
        System.out.println("\n--- 7. 可变集合 ---");

        // ArrayList (可变)
        ArrayList<Integer> buffer = new ArrayList<>(Arrays.asList(1, 2, 3));
        buffer.add(4);
        buffer.addAll(Arrays.asList(5, 6));
        buffer.remove(Integer.valueOf(1));
        System.out.println("ArrayList: " + buffer);

        // HashMap (可变)
        HashMap<String, Integer> mutableMap = new HashMap<>();
        mutableMap.put("a", 1);
        mutableMap.put("b", 2);
        mutableMap.put("c", 3);
        mutableMap.put("a", 10);  // 更新
        mutableMap.remove("b");
        System.out.println("HashMap: " + mutableMap);

        // HashSet (可变)
        HashSet<Integer> mutableSet = new HashSet<>(Arrays.asList(1, 2, 3));
        mutableSet.add(4);
        mutableSet.remove(1);
        System.out.println("HashSet: " + mutableSet);

        // ============================================
        // 8. Stream 综合示例
        // ============================================
        System.out.println("\n--- 8. Stream 综合示例 ---");

        // 类似 Scala 的 for 推导式
        List<Integer> squares = IntStream.rangeClosed(1, 5)
                .map(i -> i * i)
                .boxed()
                .collect(Collectors.toList());
        System.out.println("平方: " + squares);

        // 多重条件
        List<String> combinations = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (i != j) {
                    combinations.add("(" + i + ", " + j + ")");
                }
            }
        }
        System.out.println("组合 (i != j): " + combinations);

        // Optional 与 Stream
        List<Optional<Integer>> maybeNumbers = Arrays.asList(
                Optional.of(1), Optional.empty(), Optional.of(3),
                Optional.empty(), Optional.of(5)
        );
        List<Integer> extracted = maybeNumbers.stream()
                .filter(Optional::isPresent)
                .map(opt -> opt.get() * 2)
                .collect(Collectors.toList());
        System.out.println("Optional 提取并翻倍: " + extracted);

        System.out.println("\n==================================================");
        System.out.println("第五课完成！运行 JL06_Advanced 继续学习");
        System.out.println("==================================================");
    }
}

// 辅助类
class PersonData {
    final String name;
    final int age;

    PersonData(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person(" + name + ", " + age + ")";
    }
}
