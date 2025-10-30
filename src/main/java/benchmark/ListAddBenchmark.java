package benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
# JMH version: 1.37
# VM version: JDK 21.0.7, OpenJDK 64-Bit Server VM, 21.0.7+6-LTS
# VM invoker: C:\Program Files\Zulu\zulu-21\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.1.1.1\lib\idea_rt.jar=37789 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 2 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op

Benchmark                 (count)  Mode  Cnt     Score   Error  Units
ListAddBenchmark.add            0  avgt    2     6.264          ns/op
ListAddBenchmark.add            1  avgt    2    11.067          ns/op
ListAddBenchmark.add           10  avgt    2    25.978          ns/op
ListAddBenchmark.add          100  avgt    2   263.548          ns/op
ListAddBenchmark.add         1000  avgt    2  2149.510          ns/op
ListAddBenchmark.lazyAdd        0  avgt    2     0.389          ns/op
ListAddBenchmark.lazyAdd        1  avgt    2     9.578          ns/op
ListAddBenchmark.lazyAdd       10  avgt    2    62.864          ns/op
ListAddBenchmark.lazyAdd      100  avgt    2   675.881          ns/op
ListAddBenchmark.lazyAdd     1000  avgt    2  6942.402          ns/op
*/

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ListAddBenchmark {
    private static final Object e = new Object();

    @Param({"0", "1", "10", "100", "1000"})
    private int count;

    private List<Object> list;

    @Benchmark
    public void add() {
        list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(e);
        }
    }

    @Benchmark
    public void lazyAdd() {
        list = null;
        for (int i = 0; i < count; i++) {
            list = add(list, e);
        }
    }

    public static <E> List<E> add(List<E> list, E e) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(e);
        return list;
    }
}
