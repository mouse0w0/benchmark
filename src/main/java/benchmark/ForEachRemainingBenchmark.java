package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.IntUnaryOperator;

/*
# JMH version: 1.37
# VM version: JDK 21.0.7, OpenJDK 64-Bit Server VM, 21.0.7+6-LTS
# VM invoker: C:\Program Files\Zulu\zulu-21\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.1.1.1\lib\idea_rt.jar=34123 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 3 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op

Benchmark                       (max)  Mode  Cnt     Score      Error  Units
ForEachRemainingBenchmark.now      10  avgt    5     2.440 ±    0.015  ns/op
ForEachRemainingBenchmark.now    1000  avgt    5    38.312 ±    0.738  ns/op
ForEachRemainingBenchmark.now  100000  avgt    5  5388.858 ± 1625.980  ns/op
ForEachRemainingBenchmark.old      10  avgt    5     5.965 ±    0.414  ns/op
ForEachRemainingBenchmark.old    1000  avgt    5    66.950 ±    5.689  ns/op
ForEachRemainingBenchmark.old  100000  avgt    5  6804.215 ±  208.575  ns/op
*/

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ForEachRemainingBenchmark {
    @Param({"10", "1000", "100000"})
    private int max;
    private int from;
    private int to;
    private int[] a;

    @Setup
    public void setup() {
        a = new int[max];
        Arrays.setAll(a, IntUnaryOperator.identity());
        from = max >> 2;
        to = max - from;
    }

    private int pos;
    private int last;

    @Benchmark
    public void old(Blackhole blackhole) {
        pos = 0;
        last = pos - 1;

        final int[] a = this.a;
        final int max = to - from;
        while (pos < max) {
            blackhole.consume(a[from + (last = pos++)]);
        }
        blackhole.consume(pos);
        blackhole.consume(last);
    }

    @Benchmark
    public void now(Blackhole blackhole) {
        pos = 0;
        last = pos - 1;

        final int[] a = this.a;
        final int from = this.from, to = this.to;
        for (int i = from + pos; i < to; i++) {
            blackhole.consume(a[i]);
        }

        final int max = to - from;
        pos = max;
        last = max - 1;
        blackhole.consume(pos);
        blackhole.consume(last);
    }
}