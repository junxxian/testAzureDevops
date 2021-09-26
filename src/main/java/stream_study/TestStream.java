package stream_study;

import entity.Student;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestStream {

    /**
     * 流创建方法
     * 1.1 使用Collection下的 stream() 和 parallelStream() 方法
     */
    @Test
    public void create1(){
        List<String> strings = new ArrayList<>();
        //创建一个顺序流
        Stream<String> stream = strings.stream();
        //创建一个并行流
        Stream<String> parallelStream = strings.parallelStream();
    }

    /**
     * 1.2 使用Arrays中的stream()方法，将数组转成流
     */
    @Test
    public void create2(){
        Integer[] integers = new Integer[10];
        Stream<Integer> stream = Arrays.stream(integers);
    }

    /**
     * 1.3 使用Stream中的静态方法：of()、iterate()、generate()
     */
    @Test
    public void create3(){
        //of()
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6);

        //iterate()
        Stream<Integer> stream2 = Stream.iterate(0, x -> x + 2).limit(5);
        stream2.forEach(System.out::println);

        //generate()
        Stream<Double> stream3 = Stream.generate(Math::random).limit(3);
        stream3.forEach(System.out::println);
    }

    /**
     * 1.4 使用BufferedReader.lines()方法，将每行内容转成流
     */
    @Test
    public void create4() throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("H:\\学习记录\\一次构建到处运行步骤.txt"));
        List<String> collect = bufferedReader.lines().collect(Collectors.toList());
        collect.forEach(System.out::println);
    }

    /**
     * 1.5 使用Pattern.splitAsStream()方法，将字符串分隔成流
     */
    @Test
    public void create5(){
        Pattern pattern = Pattern.compile(",");
        Stream<String> stringStream = pattern.splitAsStream("a,b,c,d");
//        stringStream.forEach(System.out::println);
        List<String> collect = stringStream.collect(Collectors.toList());
        collect.forEach(System.out::println);
    }

    /**
     * 2.流的中间操作
     * 2.1 筛选与切片
     *         filter：过滤流中的某些元素
     *         limit(n)：获取n个元素
     *         skip(n)：跳过n元素，配合limit(n)可实现分页
     *         distinct：通过流中元素的 hashCode() 和 equals() 去除重复元素
     */
    @Test
    public void operate1(){
        Stream<Integer> stream = Stream.of(2, 3, 4, 5, 6, 5, 5, 10, 12, 13);
        stream.filter(a -> a > 4) //5, 6, 5, 5, 10, 12, 13
                .distinct() //5, 6, 10, 12, 13
                .skip(2) //10, 12, 13
                .limit(2) //10, 12
                .forEach(System.out::println);
    }

    /**
     * 2.2 映射
     *         map：接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素。
     *         flatMap：接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流。
     */
    @Test
    public void operate2(){
        List<String> list = Arrays.asList("a,b,c", "1,2,3");
        // 将每个元素转成一个新的且不带逗号的元素
        Stream<String> s1 = list.stream().map(a -> a.replaceAll(",", ""));
        s1.forEach(System.out::println);

        // 将每个元素转换成一个stream
        Stream<String> s2 = list.stream().flatMap(s -> {
            String[] split = s.split(",");
            Stream<String> stream = Arrays.stream(split);
            return stream;
        });
        List<String> collect = s2.collect(Collectors.toList());
        s2.forEach(System.out::println);

        //个人demo
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "小明"));
        students.add(new Student(2, "小红"));
        Stream<Student> stream = students.stream().map(x -> {
            Student student = new Student();
            student.setAge(x.getAge() + 100);
            student.setName(x.getName());
            return student;
        });
        stream.forEach(System.out::println);
    }

    /**
     * 2.3 排序
     *         sorted()：自然排序，流中元素需实现Comparable接口
     *         sorted(Comparator com)：定制排序，自定义Comparator排序器
     */
    @Test
    public void operate3(){
        List<String> list = Arrays.asList("aa", "cc", "ff");
        // String 类自身已实现Comparable接口
        list.stream().sorted().forEach(System.out::println);

        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "叶倩文"));
        students.add(new Student(2, "王菲"));
        students.add(new Student(3, "梅艳芳"));
        students.add(new Student(4, "叶倩文"));
        // 自定义排序：先按姓名升序，姓名相同则按年龄升序
        students.stream().sorted((s1,s2) -> {
            if (s1.getName().equals(s2.getName())){
                return s1.getAge() - s2.getAge();
            }else{
                return s1.getName().compareTo(s2.getName());
            }
        }).forEach(System.out::println);
    }

    /**
     * 2.4 消费
     *         peek：如同于map，能得到流中的每一个元素。但map接收的是一个Function表达式，有返回值；而peek接收的是Consumer表达式，没有返回值。
     */
    @Test
    public void operate4(){
        Student s1 = new Student(1, "zhangsan");
        Student s2 = new Student(2, "lisi");
        List<Student> students = Arrays.asList(s1, s2);
        students.stream().peek(s -> {
            s.setAge(12);
        }).forEach(System.out::println);
    }

    /**
     * 3. 流的终止操作
     *
     * 3.1 匹配、聚合操作
     *         allMatch：接收一个 Predicate 函数，当流中每个元素都符合该断言时才返回true，否则返回false
     *         noneMatch：接收一个 Predicate 函数，当流中每个元素都不符合该断言时才返回true，否则返回false
     *         anyMatch：接收一个 Predicate 函数，只要流中有一个元素满足该断言则返回true，否则返回false
     *         findFirst：返回流中第一个元素
     *         findAny：返回流中的任意元素
     *         count：返回流中元素的总个数
     *         max：返回流中元素最大值
     *         min：返回流中元素最小值
     */
    @Test
    public void terminate1(){
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        boolean allMatch = list.stream().allMatch(e -> e > 5); //false
        System.out.println("allMatch: " + allMatch);
        boolean noneMatch = list.stream().noneMatch(e -> e > 5); //true
        System.out.println("noneMatch: " + noneMatch);
        boolean anyMatch = list.stream().anyMatch(e -> e > 4); //true
        System.out.println("anyMatch: " + anyMatch);

        Integer findFirst = list.stream().findFirst().get(); //1
        System.out.println("findFirst: " + findFirst);
        Integer findAny = list.stream().findAny().get(); //1
        System.out.println("findAny: " + findAny);

        long count = list.stream().count(); //5
        System.out.println("count: " + count);
        Integer max = list.stream().max(Integer::compareTo).get(); //5
        System.out.println("max: " + max);
        Integer min = list.stream().min(Integer::compareTo).get(); //1
        System.out.println("min: " + min);
    }

    /**
     * 3.2 规约操作
     *              1. Optional<T> reduce(BinaryOperator<T> accumulator)：第一次执行时，accumulator函数的第一个参数为流中的第一个元素，
     *         第二个参数为流中元素的第二个元素；第二次执行时，第一个参数为第一次函数执行的结果，第二个参数为流中的第三个元素；依次类推。
     *              2. T reduce(T identity, BinaryOperator<T> accumulator)：流程跟上面一样，只是第一次执行时，
     *         accumulator函数的第一个参数为identity，而第二个参数为流中的第一个元素。
     *              <U> U reduce(U identity,BiFunction<U, ? super T, U> accumulator,BinaryOperator<U> combiner)：
     *         在串行流(stream)中，该方法跟第二个方法一样，即第三个参数combiner不会起作用。
     *         在并行流(parallelStream)中,我们知道流被fork join出多个线程进行执行，
     *         此时每个线程的执行流程就跟第二个方法reduce(identity,accumulator)一样，
     *         而第三个参数combiner函数，则是将每个线程的执行结果当成一个新的流，然后使用第一个方法reduce(accumulator)流程进行规约。
     */
    @Test
    public void terminate2(){
        //经过测试，当元素个数小于24时，并行时线程数等于元素个数，当大于等于24时，并行时线程数为16
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24);
        List<Integer> list2 = Arrays.asList(3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24);
        int sum =-1;
        for (Integer integer : list2) {
            sum -= integer;
        }
        System.out.println(sum);
        //1
        Integer v1 = list.stream().reduce((x1, x2) -> x1 - x2).get();
        System.out.println(v1);

        //2
        Integer v2 = list.stream().reduce(10, (x1, x2) -> x1 - x2);
        System.out.println(v2);

        int su =10;
        for (Integer integer : list) {
            su -= integer;
        }
        System.out.println(su);

        //3
        Integer v3 = list.stream().reduce(0,
                (x1, x2) -> {
                    System.out.println("stream accumulator: x1:" + x1 + "  x2:" + x2);
                    return x1 - x2;
                },
                (x1, x2) -> {
                    System.out.println("stream combiner: x1:" + x1 + "  x2:" + x2);
                    return x1 * x2;
                });
        System.out.println(v3); // -300

        //4
        Integer v4 = list.parallelStream().reduce(0,
                (x1, x2) -> {
                    System.out.println("parallelStream accumulator: x1:" + x1 + "  x2:" + x2);
                    return x1 - x2;
                },
                (x1, x2) -> {
                    System.out.println("parallelStream combiner: x1:" + x1 + "  x2:" + x2);
                    return x1 * x2;
                });
        System.out.println(v4);
    }

    /**
     * 3.3 收集操作
     *         collect：接收一个Collector实例，将流中元素收集成另外一个数据结构。
     *         Collector<T, A, R> 是一个接口，有以下5个抽象方法：
     *             Supplier<A> supplier()：创建一个结果容器A
     *             BiConsumer<A, T> accumulator()：消费型接口，第一个参数为容器A，第二个参数为流中元素T。
     *             BinaryOperator<A> combiner()：函数接口，该参数的作用跟上一个方法(reduce)中的combiner参数一样，将并行流中各                                                                 个子进程的运行结果(accumulator函数操作后的容器A)进行合并。
     *             Function<A, R> finisher()：函数式接口，参数为：容器A，返回类型为：collect方法最终想要的结果R。
     *             Set<Characteristics> characteristics()：返回一个不可变的Set集合，用来表明该Collector的特征。有以下三个特征：
     *                 CONCURRENT：表示此收集器支持并发。（官方文档还有其他描述，暂时没去探索，故不作过多翻译）
     *                 UNORDERED：表示该收集操作不会保留流中元素原有的顺序。
     *                 IDENTITY_FINISH：表示finisher参数只是标识而已，可忽略
     *
     *  3.3.1 Collector 工具库：Collectors
     */
    @Test
    public void terminate3(){
        Student xh = new Student(1, "xh");
        Student xm = new Student(2, "xm");
        Student xk = new Student(3, "xk");

        List<Student> students = Arrays.asList(xh, xm, xk);

        //转成list
        List<Integer> collect = students.stream().map(Student::getAge).collect(Collectors.toList());
        System.out.println("collect: " + collect);

        //转成set
        Set<String> collect1 = students.stream().map(Student::getName).collect(Collectors.toSet());
        System.out.println("collect1： " + collect1);

        //转成map,注：key不能重复，否则报错
        Map<String, Integer> collect2 = students.stream().collect(Collectors.toMap(Student::getName, Student::getAge));
        System.out.println("collect2: " + collect2);

        //字符串分隔符连接
        String collect3 = students.stream().map(Student::getName).collect(Collectors.joining(",", "(", ")"));
        System.out.println("collect3: " + collect3);

        //聚合操作
        //1.学生总数
        Long collect4 = students.stream().collect(Collectors.counting());
        System.out.println("collect4: " + collect4);

        //2. 最大年龄
        Integer collect5 = students.stream().map(Student::getAge).collect(Collectors.maxBy(Integer::compareTo)).get();
        System.out.println("collect5: " + collect5);

        //3. 所有人的年龄
        Integer collect6 = students.stream().collect(Collectors.summingInt(Student::getAge));
        System.out.println("collect6: " + collect6);

        //4. 平均年龄
        Double collect7 = students.stream().collect(Collectors.averagingInt(Student::getAge));
        System.out.println("collect7: "  + collect7);

        //5. 分组
        Map<Integer, List<Student>> collect8 = students.stream().collect(Collectors.groupingBy(Student::getAge));
        System.out.println("collect8: " + collect8);

        //6. 多重分组，先根据年龄分，再根据名字分组
        Map<Integer, Map<String, List<Student>>> collect9 = students.stream().collect(Collectors.groupingBy(Student::getAge, Collectors.groupingBy(Student::getName)));
        System.out.println("collect9: " + collect9);

        //7. 分区--分成两部分，一部分大于2岁，一部分小于等于2岁
        Map<Boolean, List<Student>> collect10 = students.stream().collect(Collectors.partitioningBy(v -> v.getAge() > 2));
        System.out.println("collect10: " + collect10);

        //8. 规约
        Integer allAge = students.stream().map(Student::getAge).collect(Collectors.reducing(Integer::sum)).get();
        System.out.println("allAge: " + allAge);

    }




    @Test
    public void test133(){
        List<Student> students = new ArrayList<>();
        students.add(new Student(1,"小明"));
        students.add(new Student(2,"小玲"));
        students.add(new Student(3,"小花"));
        students.add(new Student(4,"小丑"));
        students.add(new Student(5,"小美"));
//        List<Student> collect = students.stream().filter(student -> student.getName().equals("小明")).distinct().limit(3).collect(Collectors.toList());
//        students.stream().filter(student -> student.getName().equals("小明")).distinct().limit(3).
//                forEach(student -> System.out.println(student.getName()));
//        System.out.println(collect);
    }
}
