package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/*
# JMH version: 1.37
# VM version: JDK 25, Java HotSpot(TM) 64-Bit Server VM, 25+37-LTS-3491
# VM invoker: C:\Program Files\jdk-25\bin\java.exe
# VM options: -Dfile.encoding=UTF-8 -javaagent:C:\Program Files\IntelliJ IDEA\lib\idea_rt.jar=2032 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 3 iterations, 10 s each
# Measurement: 10 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op

Benchmark                                      Mode  Cnt  Score   Error  Units
NullCheckBenchmark.explicitNullCheck           avgt   10  0.822 ± 0.030  ns/op
NullCheckBenchmark.explicitNullCheckNullValue  avgt   10  0.447 ± 0.006  ns/op
NullCheckBenchmark.noNullCheck                 avgt   10  0.782 ± 0.007  ns/op
NullCheckBenchmark.optionalNullCheck           avgt   10  1.191 ± 0.022  ns/op
NullCheckBenchmark.optionalNullCheckNullValue  avgt   10  0.561 ± 0.014  ns/op
NullCheckBenchmark.requireNonNull              avgt   10  0.834 ± 0.052  ns/op
NullCheckBenchmark.ternaryNullCheck            avgt   10  0.786 ± 0.029  ns/op
NullCheckBenchmark.ternaryNullCheckNullValue   avgt   10  0.448 ± 0.004  ns/op
*/

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 3)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class NullCheckBenchmark {

    private String value;
    private String nullValue;

    @Setup
    public void setup() {
        value = "test";
        nullValue = null;
    }

    @Benchmark
    public void explicitNullCheck(Blackhole blackhole) {
        if (value != null) {
            blackhole.consume(value.length());
        }
    }

    @Benchmark
    public void noNullCheck(Blackhole blackhole) {
        blackhole.consume(value.length());
    }

    @Benchmark
    public void requireNonNull(Blackhole blackhole) {
        String v = java.util.Objects.requireNonNull(value);
        blackhole.consume(v.length());
    }

    @Benchmark
    public void ternaryNullCheck(Blackhole blackhole) {
        blackhole.consume(value != null ? value.length() : 0);
    }

    @Benchmark
    public void optionalNullCheck(Blackhole blackhole) {
        blackhole.consume(java.util.Optional.ofNullable(value).map(String::length).orElse(0));
    }

    @Benchmark
    public void explicitNullCheckNullValue(Blackhole blackhole) {
        if (nullValue != null) {
            blackhole.consume(nullValue.length());
        }
    }

    @Benchmark
    public void ternaryNullCheckNullValue(Blackhole blackhole) {
        blackhole.consume(nullValue != null ? nullValue.length() : 0);
    }

    @Benchmark
    public void optionalNullCheckNullValue(Blackhole blackhole) {
        blackhole.consume(java.util.Optional.ofNullable(nullValue).map(String::length).orElse(0));
    }
}
