package benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/*
# JMH version: 1.37
# VM version: JDK 21.0.3, Java HotSpot(TM) 64-Bit Server VM, 21.0.3+7-LTS-152
# VM invoker: C:\Program Files\jdk-21\bin\java.exe
# VM options: -Dfile.encoding=UTF-8 -javaagent:C:\Program Files\IntelliJ IDEA\lib\idea_rt.jar=10740 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 2 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op

Benchmark                         Mode  Cnt     Score   Error  Units
CallerClassBenchmark.stackWalker  avgt    2  1105.414          ns/op
CallerClassBenchmark.thread       avgt    2  6154.204          ns/op
CallerClassBenchmark.throwable    avgt    2  6628.387          ns/op
*/

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class CallerClassBenchmark {

    private StackWalker stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    @Benchmark
    public Class<?> stackWalker() {
        return stackWalker.getCallerClass();
    }

    @Benchmark
    public Class<?> thread() {
        try {
            return Class.forName(Thread.currentThread().getStackTrace()[1].getClassName());
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        }
    }

    @Benchmark
    public Class<?> throwable() {
        try {
            return Class.forName(new Throwable().getStackTrace()[1].getClassName());
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        }
    }
}
