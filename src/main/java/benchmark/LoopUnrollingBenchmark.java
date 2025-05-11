package benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/*
# JMH version: 1.37
# VM version: JDK 21.0.3, Java HotSpot(TM) 64-Bit Server VM, 21.0.3+7-LTS-152
# VM invoker: C:\Program Files\jdk-21\bin\java.exe
# VM options: -Dfile.encoding=UTF-8 -javaagent:C:\Program Files\IntelliJ IDEA\lib\idea_rt.jar=9241 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 2 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op

Benchmark                      Mode  Cnt     Score   Error  Units
LoopUnrollingBenchmark.loop    avgt    2  3703.977          ns/op
LoopUnrollingBenchmark.loop2   avgt    2  1894.040          ns/op
LoopUnrollingBenchmark.loop4   avgt    2  1306.569          ns/op
LoopUnrollingBenchmark.loop8   avgt    2  1152.607          ns/op
LoopUnrollingBenchmark.loop16  avgt    2  1890.463          ns/op
*/

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LoopUnrollingBenchmark {
    private int n = 16000;

    @Benchmark
    public int loop() {
        int fact = 0;
        for (int i = 1; i <= n; i++) {
            fact += i;
        }
        return fact;
    }

    @Benchmark
    public int loop2() {
        int fact0 = 0, fact1 = 0;
        for (int i = 1; i <= n; i += 2) {
            fact0 += i;
            fact1 += i + 1;
        }
        return fact0 + fact1;
    }

    @Benchmark
    public int loop4() {
        int fact0 = 0, fact1 = 0, fact2 = 0, fact3 = 0;
        for (int i = 1; i <= n; i += 4) {
            fact0 += i;
            fact1 += i + 1;
            fact2 += i + 2;
            fact3 += i + 3;
        }
        return fact0 + fact1 + fact2 + fact3;
    }

    @Benchmark
    public int loop8() {
        int fact0 = 0, fact1 = 0, fact2 = 0, fact3 = 0, fact4 = 0, fact5 = 0, fact6 = 0, fact7 = 0;
        for (int i = 1; i <= n; i += 8) {
            fact0 += i;
            fact1 += i + 1;
            fact2 += i + 2;
            fact3 += i + 3;
            fact4 += i + 4;
            fact5 += i + 5;
            fact6 += i + 6;
            fact7 += i + 7;
        }
        return fact0 + fact1 + fact2 + fact3 + fact4 + fact5 + fact6 + fact7;
    }

    @Benchmark
    public int loop16() {
        int fact0 = 0, fact1 = 0, fact2 = 0, fact3 = 0,
                fact4 = 0, fact5 = 0, fact6 = 0, fact7 = 0,
                fact8 = 0, fact9 = 0, fact10 = 0, fact11 = 0,
                fact12 = 0, fact13 = 0, fact14 = 0, fact15 = 0;
        for (int i = 1; i <= n; i += 16) {
            fact0 += i;
            fact1 += i + 1;
            fact2 += i + 2;
            fact3 += i + 3;
            fact4 += i + 4;
            fact4 += i + 4;
            fact5 += i + 5;
            fact6 += i + 6;
            fact7 += i + 7;
            fact8 += i + 8;
            fact9 += i + 9;
            fact10 += i + 10;
            fact11 += i + 11;
            fact12 += i + 12;
            fact13 += i + 13;
            fact14 += i + 14;
            fact15 += i + 15;
        }
        return fact0 + fact1 + fact2 + fact3
                + fact4 + fact5 + fact6 + fact7
                + fact8 + fact9 + fact10 + fact11
                + fact12 + fact13 + fact14 + fact15;
    }
}
