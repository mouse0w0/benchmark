package benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/*
# JMH version: 1.37
# VM version: JDK 21.0.7, OpenJDK 64-Bit Server VM, 21.0.7+6-LTS
# VM invoker: C:\Program Files\Zulu\zulu-21\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.1.1.1\lib\idea_rt.jar=38412 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 2 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op

Benchmark                                        (size)  Mode  Cnt        Score   Error  Units
ListIterationBenchmark.enhancedForLoop              100  avgt    2       27.451          ns/op
ListIterationBenchmark.enhancedForLoop            10000  avgt    2     2837.078          ns/op
ListIterationBenchmark.enhancedForLoop          1000000  avgt    2   646057.521          ns/op
ListIterationBenchmark.forEachLambda                100  avgt    2       26.484          ns/op
ListIterationBenchmark.forEachLambda              10000  avgt    2     2762.896          ns/op
ListIterationBenchmark.forEachLambda            1000000  avgt    2   683335.225          ns/op
ListIterationBenchmark.forLoop                      100  avgt    2       25.883          ns/op
ListIterationBenchmark.forLoop                    10000  avgt    2     2764.192          ns/op
ListIterationBenchmark.forLoop                  1000000  avgt    2   719334.026          ns/op
ListIterationBenchmark.indexedForLoopWithCache      100  avgt    2       27.299          ns/op
ListIterationBenchmark.indexedForLoopWithCache    10000  avgt    2     2924.778          ns/op
ListIterationBenchmark.indexedForLoopWithCache  1000000  avgt    2   754722.253          ns/op
ListIterationBenchmark.iterator                     100  avgt    2       28.930          ns/op
ListIterationBenchmark.iterator                   10000  avgt    2     2912.615          ns/op
ListIterationBenchmark.iterator                 1000000  avgt    2   765891.483          ns/op
ListIterationBenchmark.parallelStreamReduce         100  avgt    2     9666.091          ns/op
ListIterationBenchmark.parallelStreamReduce       10000  avgt    2    12840.547          ns/op
ListIterationBenchmark.parallelStreamReduce     1000000  avgt    2   575015.648          ns/op
ListIterationBenchmark.parallelStreamSum            100  avgt    2     9459.052          ns/op
ListIterationBenchmark.parallelStreamSum          10000  avgt    2     8837.630          ns/op
ListIterationBenchmark.parallelStreamSum        1000000  avgt    2   104383.981          ns/op
ListIterationBenchmark.streamReduce                 100  avgt    2      221.675          ns/op
ListIterationBenchmark.streamReduce               10000  avgt    2    20280.236          ns/op
ListIterationBenchmark.streamReduce             1000000  avgt    2  2424372.019          ns/op
ListIterationBenchmark.streamSum                    100  avgt    2       40.659          ns/op
ListIterationBenchmark.streamSum                  10000  avgt    2     5397.717          ns/op
ListIterationBenchmark.streamSum                1000000  avgt    2   777435.389          ns/op
 */

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ListIterationBenchmark {
    @Param({"100", "10000", "1000000"})
    private int size;

    private List<Integer> list;

    @Setup
    public void setup() {
        list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
    }

    @Benchmark
    public int forLoop() {
        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i);
        }
        return sum;
    }

    @Benchmark
    public int enhancedForLoop() {
        int sum = 0;
        for (Integer value : list) {
            sum += value;
        }
        return sum;
    }

    @Benchmark
    public int forEachLambda() {
        int[] sum = {0};
        list.forEach(value -> sum[0] += value);
        return sum[0];
    }

    @Benchmark
    public int iterator() {
        int sum = 0;
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            sum += iterator.next();
        }
        return sum;
    }

    @Benchmark
    public int streamReduce() {
        return list.stream().reduce(0, Integer::sum);
    }

    @Benchmark
    public int parallelStreamReduce() {
        return list.parallelStream().reduce(0, Integer::sum);
    }

    @Benchmark
    public int streamSum() {
        return list.stream().mapToInt(Integer::intValue).sum();
    }

    @Benchmark
    public int parallelStreamSum() {
        return list.parallelStream().mapToInt(Integer::intValue).sum();
    }

    @Benchmark
    public int indexedForLoopWithCache() {
        int sum = 0;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            sum += list.get(i);
        }
        return sum;
    }
}