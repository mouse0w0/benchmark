package benchmark;

import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/*
# JMH version: 1.37
# VM version: JDK 21.0.3, Java HotSpot(TM) 64-Bit Server VM, 21.0.3+7-LTS-152
# VM invoker: C:\Program Files\jdk-21\bin\java.exe
# VM options: -Dfile.encoding=UTF-8 -javaagent:C:\Program Files\IntelliJ IDEA\lib\idea_rt.jar=2165 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 2 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: benchmark.CreateDirectoriesBenchmark.createDirectories

Benchmark                                     Mode  Cnt       Score   Error  Units
CreateDirectoriesBenchmark.createDirectories  avgt    2  124372.019          ns/op
CreateDirectoriesBenchmark.notExists          avgt    2   58139.716          ns/op
*/

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class CreateDirectoriesBenchmark {
    private Path path;

    @Setup
    public void setup() {
        try {
            path = Files.createTempDirectory("temp");
        } catch (IOException ignored) {
        }
    }

    @Benchmark
    public boolean notExists() {
        return Files.notExists(path);
    }

    @Benchmark
    public boolean createDirectories() {
        try {
            Files.createDirectories(path);
            return true;
        } catch (IOException ignore) {
            return false;
        }
    }
}
