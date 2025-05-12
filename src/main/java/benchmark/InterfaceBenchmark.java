package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/*
# JMH version: 1.37
# VM version: JDK 21.0.3, Java HotSpot(TM) 64-Bit Server VM, 21.0.3+7-LTS-152
# VM invoker: C:\Program Files\jdk-21\bin\java.exe
# VM options: -Dfile.encoding=UTF-8 -javaagent:C:\Program Files\IntelliJ IDEA\lib\idea_rt.jar=3480 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 3 iterations, 10 s each
# Measurement: 10 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op

Benchmark                      Mode  Cnt  Score   Error  Units
InterfaceBenchmark.interface1  avgt   10  0.542 ± 0.006  ns/op
InterfaceBenchmark.interface2  avgt   10  0.539 ± 0.001  ns/op
*/

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 3)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class InterfaceBenchmark {
    interface Interface1 {
        void foo(Blackhole blackhole);
    }

    interface Interface2 {
        int value();

        default void foo(Blackhole blackhole) {
            blackhole.consume(value());
        }
    }

    class Class1 implements Interface1 {
        private int value;

        Class1(int value) {
            this.value = value;
        }

        @Override
        public void foo(Blackhole blackhole) {
            blackhole.consume(value);
        }
    }

    class Class2 implements Interface2 {
        private int value;

        Class2(int value) {
            this.value = value;
        }

        @Override
        public int value() {
            return value;
        }
    }

    private Class1 class1;
    private Class2 class2;

    @Setup
    public void setup() {
        class1 = new Class1(42);
        class2 = new Class2(42);
    }

    @Benchmark
    public void interface1(Blackhole blackhole) {
        class1.foo(blackhole);
    }

    @Benchmark
    public void interface2(Blackhole blackhole) {
        class2.foo(blackhole);
    }
}
