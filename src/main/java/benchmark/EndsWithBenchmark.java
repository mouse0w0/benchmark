package benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/*
# JMH version: 1.37
# VM version: JDK 21.0.7, OpenJDK 64-Bit Server VM, 21.0.7+6-LTS
# VM invoker: C:\Program Files\Zulu\zulu-21\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.1.1.1\lib\idea_rt.jar=37623 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 2 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op

Benchmark                   (fileName)  Mode  Cnt   Score   Error  Units
EndsWithBenchmark.endsWith   test.json  avgt    2   5.102          ns/op
EndsWithBenchmark.endsWith  test.jsonc  avgt    2   6.069          ns/op
EndsWithBenchmark.endsWith  test.json5  avgt    2   7.584          ns/op
EndsWithBenchmark.endsWith   test.yaml  avgt    2   5.336          ns/op
EndsWithBenchmark.optimize   test.json  avgt    2   4.203          ns/op
EndsWithBenchmark.optimize  test.jsonc  avgt    2   4.522          ns/op
EndsWithBenchmark.optimize  test.json5  avgt    2   4.611          ns/op
EndsWithBenchmark.optimize   test.yaml  avgt    2   2.753          ns/op
EndsWithBenchmark.pattern    test.json  avgt    2  34.550          ns/op
EndsWithBenchmark.pattern   test.jsonc  avgt    2  41.077          ns/op
EndsWithBenchmark.pattern   test.json5  avgt    2  40.198          ns/op
EndsWithBenchmark.pattern    test.yaml  avgt    2  32.442          ns/op
*/

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class EndsWithBenchmark {
    private static final Pattern JSON_FILE_PATTERN = Pattern.compile(".*\\.json[c5]?$", Pattern.CASE_INSENSITIVE);

    @Param({"test.json", "test.jsonc", "test.json5", "test.yaml"})
    private String fileName;

    @Benchmark
    public boolean endsWith() {
        return endsWith(fileName);
    }

    public static boolean endsWith(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".json") || lower.endsWith(".jsonc") || lower.endsWith(".json5");
    }

    @Benchmark
    public boolean pattern() {
        return pattern(fileName);
    }

    public static boolean pattern(String fileName) {
        return JSON_FILE_PATTERN.matcher(fileName).matches();
    }

    @Benchmark
    public boolean optimize() {
        return optimize(fileName);
    }

    public static boolean optimize(String fileName) {
        if (fileName.length() > 4 && fileName.charAt(fileName.length() - 5) == '.') {
            return fileName.regionMatches(true, fileName.length() - 4, "json", 0, 4);
        } else if (fileName.length() > 5 && fileName.charAt(fileName.length() - 6) == '.') {
            char lastChar = fileName.charAt(fileName.length() - 1);
            return (lastChar == 'c' || lastChar == 'C' || lastChar == '5') &&
                    fileName.regionMatches(true, fileName.length() - 5, "json", 0, 4);
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(optimize("test.json"));
        System.out.println(optimize("test.jsonc"));
        System.out.println(optimize("test.json5"));
        System.out.println(optimize("test.yaml"));
    }
}
