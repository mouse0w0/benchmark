package benchmark;

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