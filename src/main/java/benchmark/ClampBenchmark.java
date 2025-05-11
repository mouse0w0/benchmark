package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/*
# JMH version: 1.37
# VM version: JDK 21.0.3, Java HotSpot(TM) 64-Bit Server VM, 21.0.3+7-LTS-152
# VM invoker: C:\Program Files\jdk-21\bin\java.exe
# VM options: -Dfile.encoding=UTF-8 -javaagent:C:\Program Files\IntelliJ IDEA\lib\idea_rt.jar=1976 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 2 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: benchmark.ClampBenchmark.optimize

Benchmark                Mode  Cnt   Score   Error  Units
ClampBenchmark.optimize  avgt    2  50.198          ns/op
ClampBenchmark.plain     avgt    2  74.887          ns/op
*/

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ClampBenchmark {

    @Benchmark
    public void plain(Blackhole blackhole) {
        for (int i = 0; i <= 256; i++) {
            blackhole.consume(clamp(i, 0, 255));
        }
    }

    @Benchmark
    public void optimize(Blackhole blackhole) {
        for (int i = 0; i <= 256; i++) {
            blackhole.consume(clampOptimize(i));
        }
    }

    public static int clamp(int n, int min, int max) {
        return Math.max(min, Math.min(n, max));
    }

    public static int clampOptimize(int n) {
        if ((n & ~255) != 0) {
            n = ((~n) >> 31) & 255;
        }
        return n;
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 256; i++) {
            System.out.println(clamp(i, 0, 255));
            System.out.println(clampOptimize(i));
        }
    }
}
