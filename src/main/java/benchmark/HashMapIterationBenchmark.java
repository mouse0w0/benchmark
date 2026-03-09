package benchmark;

/*
# JMH version: 1.37
# VM version: JDK 21.0.7, OpenJDK 64-Bit Server VM, 21.0.7+6-LTS
# VM invoker: C:\Program Files\Zulu\zulu-21\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.1.1.1\lib\idea_rt.jar=39910 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 2 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op

Benchmark                                           (size)  Mode  Cnt        Score   Error  Units
HashMapIterationBenchmark.enhancedForLoopEntrySet      100  avgt    2      252.719          ns/op
HashMapIterationBenchmark.enhancedForLoopEntrySet    10000  avgt    2    23968.971          ns/op
HashMapIterationBenchmark.enhancedForLoopEntrySet  1000000  avgt    2  4971493.821          ns/op
HashMapIterationBenchmark.enhancedForLoopKeySet        100  avgt    2      389.952          ns/op
HashMapIterationBenchmark.enhancedForLoopKeySet      10000  avgt    2    46463.341          ns/op
HashMapIterationBenchmark.enhancedForLoopKeySet    1000000  avgt    2  7416727.311          ns/op
HashMapIterationBenchmark.enhancedForLoopValues        100  avgt    2      248.521          ns/op
HashMapIterationBenchmark.enhancedForLoopValues      10000  avgt    2    23205.976          ns/op
HashMapIterationBenchmark.enhancedForLoopValues    1000000  avgt    2  5022301.574          ns/op
HashMapIterationBenchmark.forEachLambdaEntrySet        100  avgt    2      252.519          ns/op
HashMapIterationBenchmark.forEachLambdaEntrySet      10000  avgt    2    22972.075          ns/op
HashMapIterationBenchmark.forEachLambdaEntrySet    1000000  avgt    2  4602344.376          ns/op
HashMapIterationBenchmark.forEachLambdaKeys            100  avgt    2      281.091          ns/op
HashMapIterationBenchmark.forEachLambdaKeys          10000  avgt    2    26713.498          ns/op
HashMapIterationBenchmark.forEachLambdaKeys        1000000  avgt    2  5512442.427          ns/op
HashMapIterationBenchmark.forEachLambdaValues          100  avgt    2      249.290          ns/op
HashMapIterationBenchmark.forEachLambdaValues        10000  avgt    2    22790.439          ns/op
HashMapIterationBenchmark.forEachLambdaValues      1000000  avgt    2  4284063.407          ns/op
HashMapIterationBenchmark.iteratorEntrySet             100  avgt    2      245.894          ns/op
HashMapIterationBenchmark.iteratorEntrySet           10000  avgt    2    24191.368          ns/op
HashMapIterationBenchmark.iteratorEntrySet         1000000  avgt    2  4909444.531          ns/op
HashMapIterationBenchmark.iteratorKeySet               100  avgt    2      447.188          ns/op
HashMapIterationBenchmark.iteratorKeySet             10000  avgt    2    42036.269          ns/op
HashMapIterationBenchmark.iteratorKeySet           1000000  avgt    2  8288407.564          ns/op
HashMapIterationBenchmark.iteratorValues               100  avgt    2      246.993          ns/op
HashMapIterationBenchmark.iteratorValues             10000  avgt    2    24148.230          ns/op
HashMapIterationBenchmark.iteratorValues           1000000  avgt    2  4893407.868          ns/op
HashMapIterationBenchmark.parallelStreamEntrySet       100  avgt    2     8819.510          ns/op
HashMapIterationBenchmark.parallelStreamEntrySet     10000  avgt    2    11850.092          ns/op
HashMapIterationBenchmark.parallelStreamEntrySet   1000000  avgt    2   966015.406          ns/op
HashMapIterationBenchmark.streamEntrySet               100  avgt    2      765.049          ns/op
HashMapIterationBenchmark.streamEntrySet             10000  avgt    2    57944.911          ns/op
HashMapIterationBenchmark.streamEntrySet           1000000  avgt    2  6965353.433          ns/op
HashMapIterationBenchmark.streamKeySet                 100  avgt    2      705.981          ns/op
HashMapIterationBenchmark.streamKeySet               10000  avgt    2    58107.621          ns/op
HashMapIterationBenchmark.streamKeySet             1000000  avgt    2  6982288.522          ns/op
HashMapIterationBenchmark.streamValues                 100  avgt    2      759.331          ns/op
HashMapIterationBenchmark.streamValues               10000  avgt    2    57302.024          ns/op
HashMapIterationBenchmark.streamValues             1000000  avgt    2  7059124.904          ns/op
 */

import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class HashMapIterationBenchmark {
    @Param({"100", "10000", "1000000"})
    private int size;

    private Map<Integer, Integer> map;

    @Setup
    public void setup() {
        map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            map.put(i, i * 2);
        }
    }

    @Benchmark
    public long forEachLambdaEntrySet() {
        long[] sum = {0};
        map.forEach((key, value) -> sum[0] += value);
        return sum[0];
    }

    @Benchmark
    public long enhancedForLoopEntrySet() {
        long sum = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            sum += entry.getValue();
        }
        return sum;
    }

    @Benchmark
    public long enhancedForLoopKeySet() {
        long sum = 0;
        for (Integer key : map.keySet()) {
            sum += map.get(key);
        }
        return sum;
    }

    @Benchmark
    public long enhancedForLoopValues() {
        long sum = 0;
        for (Integer value : map.values()) {
            sum += value;
        }
        return sum;
    }

    @Benchmark
    public long iteratorEntrySet() {
        long sum = 0;
        Iterator<Map.Entry<Integer, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            sum += iterator.next().getValue();
        }
        return sum;
    }

    @Benchmark
    public long iteratorKeySet() {
        long sum = 0;
        Iterator<Integer> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            sum += map.get(iterator.next());
        }
        return sum;
    }

    @Benchmark
    public long iteratorValues() {
        long sum = 0;
        Iterator<Integer> iterator = map.values().iterator();
        while (iterator.hasNext()) {
            sum += iterator.next();
        }
        return sum;
    }

    @Benchmark
    public long streamEntrySet() {
        return map.entrySet().stream().mapToLong(Map.Entry::getValue).sum();
    }

    @Benchmark
    public long parallelStreamEntrySet() {
        return map.entrySet().parallelStream().mapToLong(Map.Entry::getValue).sum();
    }

    @Benchmark
    public long streamKeySet() {
        return map.keySet().stream().mapToLong(key -> map.get(key)).sum();
    }

    @Benchmark
    public long streamValues() {
        return map.values().stream().mapToLong(Integer::longValue).sum();
    }

    @Benchmark
    public long forEachLambdaValues() {
        long[] sum = {0};
        map.values().forEach(value -> sum[0] += value);
        return sum[0];
    }

    @Benchmark
    public long forEachLambdaKeys() {
        long[] sum = {0};
        map.keySet().forEach(key -> sum[0] += map.get(key));
        return sum[0];
    }
}