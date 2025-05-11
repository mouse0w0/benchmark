package benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/*
# JMH version: 1.37
# VM version: JDK 21.0.3, Java HotSpot(TM) 64-Bit Server VM, 21.0.3+7-LTS-152
# VM invoker: C:\Program Files\jdk-21\bin\java.exe
# VM options: -Dfile.encoding=UTF-8 -javaagent:C:\Program Files\IntelliJ IDEA\lib\idea_rt.jar=2611 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 2 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: benchmark.IntBenchmark.parseUnsignedLong

Benchmark                       Mode  Cnt   Score   Error  Units
IntBenchmark.parseInt           avgt    2   9.761          ns/op
IntBenchmark.parseLong          avgt    2  10.015          ns/op
IntBenchmark.parseUnsignedInt   avgt    2   9.981          ns/op
IntBenchmark.parseUnsignedLong  avgt    2   9.975          ns/op
*/

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class IntBenchmark {
    private String s = "7fffffff";

    @Benchmark
    public int parseInt() {
        return Integer.parseInt(s, 16);
    }

    @Benchmark
    public int parseUnsignedInt() {
        return Integer.parseUnsignedInt(s, 16);
    }

    @Benchmark
    public int parseLong() {
        return (int) Long.parseLong(s, 16);
    }

    @Benchmark
    public int parseUnsignedLong() {
        return (int) Long.parseUnsignedLong(s, 16);
    }
}
