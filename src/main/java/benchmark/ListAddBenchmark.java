package benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
# JMH version: 1.37
# VM version: JDK 21.0.7, OpenJDK 64-Bit Server VM, 21.0.7+6-LTS
# VM invoker: C:\Program Files\Zulu\zulu-21\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.1.1.1\lib\idea_rt.jar=40236 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 2 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op

Benchmark                 (count)  Mode  Cnt     Score   Error  Units
ListAddBenchmark.add            0  avgt    2     6.085          ns/op
ListAddBenchmark.add            1  avgt    2    10.787          ns/op
ListAddBenchmark.add           10  avgt    2    24.185          ns/op
ListAddBenchmark.add          100  avgt    2   264.447          ns/op
ListAddBenchmark.add         1000  avgt    2  2149.941          ns/op
ListAddBenchmark.lazyAdd        0  avgt    2     0.361          ns/op
ListAddBenchmark.lazyAdd        1  avgt    2     9.391          ns/op
ListAddBenchmark.lazyAdd       10  avgt    2    61.358          ns/op
ListAddBenchmark.lazyAdd      100  avgt    2   670.123          ns/op
ListAddBenchmark.lazyAdd     1000  avgt    2  6778.650          ns/op
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
    public List<Object> add() {
        list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(e);
        }
        return list;
    }

    @Benchmark
    public List<Object> lazyAdd() {
        list = null;
        for (int i = 0; i < count; i++) {
            list = add(list, e);
        }
        return list;
    }

    public static <E> List<E> add(List<E> list, E e) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(e);
        return list;
    }
}
