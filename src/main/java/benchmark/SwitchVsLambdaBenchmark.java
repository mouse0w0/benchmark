package benchmark;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

/*
# JMH version: 1.37
# VM version: JDK 21.0.3, Java HotSpot(TM) 64-Bit Server VM, 21.0.3+7-LTS-152
# VM invoker: C:\Program Files\jdk-21\bin\java.exe
# VM options: -Dfile.encoding=UTF-8 -javaagent:C:\Program Files\IntelliJ IDEA\lib\idea_rt.jar=2904 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 2 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: benchmark.SwitchVsLambdaBenchmark.switchTest

Benchmark                           Mode  Cnt  Score   Error  Units
SwitchVsLambdaBenchmark.lambdaTest  avgt    2  1.529          ns/op
SwitchVsLambdaBenchmark.switchTest  avgt    2  0.439          ns/op
*/

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SwitchVsLambdaBenchmark {
    private enum Color {
        RED,
        GREEN,
        BLUE
    }

    private final Consumer<Blackhole> red = this::red;
    private final Consumer<Blackhole> green = this::green;
    private final Consumer<Blackhole> blue = this::blue;

    @Benchmark
    public void switchTest(Blackhole blackhole) {
        doSwitch(Color.RED, blackhole);
        doSwitch(Color.GREEN, blackhole);
        doSwitch(Color.BLUE, blackhole);
    }

    @Benchmark
    public void lambdaTest(Blackhole blackhole) {
        red.accept(blackhole);
        green.accept(blackhole);
        blue.accept(blackhole);
    }

    private void doSwitch(Color color, Blackhole blackhole) {
        switch (color) {
            case RED:
                red(blackhole);
                break;
            case BLUE:
                blue(blackhole);
                break;
            case GREEN:
                green(blackhole);
                break;
            default:
                break;
        }
    }

    private void red(Blackhole blackhole) {
        blackhole.consume(0xFF0000);
    }

    private void green(Blackhole blackhole) {
        blackhole.consume(0x00FF00);
    }

    private void blue(Blackhole blackhole) {
        blackhole.consume(0x0000FF);
    }
}
