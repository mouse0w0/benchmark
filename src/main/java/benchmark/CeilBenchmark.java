package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/*
# JMH version: 1.37
# VM version: JDK 21.0.3, Java HotSpot(TM) 64-Bit Server VM, 21.0.3+7-LTS-152
# VM invoker: C:\Program Files\jdk-21\bin\java.exe
# VM options: -Dfile.encoding=UTF-8 -javaagent:C:\Program Files\IntelliJ IDEA\lib\idea_rt.jar=1823 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 2 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: benchmark.CeilBenchmark.mathCeil

Benchmark               Mode  Cnt     Score   Error  Units
CeilBenchmark.mathCeil  avgt    2  5191.718          ns/op
CeilBenchmark.myCeil    avgt    2  2257.881          ns/op
*/

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class CeilBenchmark {
    private static final float START = (float) Math.PI * -1000f;
    private static final float END = (float) Math.PI * 1000f;
    private static final float STEP = (float) Math.PI;

    @Benchmark
    public void mathCeil(Blackhole blackhole) {
        for (float f = START; f < END; f += STEP) {
            blackhole.consume(Math.ceil(f));
        }
    }

    @Benchmark
    public void myCeil(Blackhole blackhole) {
        for (float f = START; f < END; f += STEP) {
            blackhole.consume(ceil(f));
        }
    }

    private static int ceil(float f) {
        int i = (int) f;
        return f > i ? i + 1 : i;
    }
}
