package benchmark;

import org.bukkit.configuration.file.YamlConfiguration;
import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
# JMH version: 1.37
# VM version: JDK 21.0.3, Java HotSpot(TM) 64-Bit Server VM, 21.0.3+7-LTS-152
# VM invoker: C:\Program Files\jdk-21\bin\java.exe
# VM options: -Dfile.encoding=UTF-8 -javaagent:C:\Program Files\IntelliJ IDEA\lib\idea_rt.jar=3115 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 2 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: benchmark.YamlBenchmark.getMap

Benchmark                Mode  Cnt    Score   Error  Units
YamlBenchmark.cache      avgt    2    0.436          ns/op
YamlBenchmark.getConfig  avgt    2  112.838          ns/op
YamlBenchmark.getMap     avgt    2    5.166          ns/op
*/

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class YamlBenchmark {
    private YamlConfiguration cfg;
    private Map<String, Object> map;
    private String cacheValue;

    @Setup
    public void setup() {
        cfg = new YamlConfiguration();
        cfg.set(String.valueOf("red.green.blue".toCharArray()), "Hello world!");
        map = new HashMap<>();
        map.put(String.valueOf("red.green.blue".toCharArray()), "Hello world!");
        cacheValue = cfg.getString("red.green.blue");
    }

    @Benchmark
    public String getConfig() {
        return cfg.getString("red.green.blue");
    }

    @Benchmark
    public String getMap() {
        return (String) map.get("red.green.blue");
    }

    @Benchmark
    public String cache() {
        return cacheValue;
    }
}
