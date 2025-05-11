package benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/*
# JMH version: 1.37
# VM version: JDK 21.0.3, Java HotSpot(TM) 64-Bit Server VM, 21.0.3+7-LTS-152
# VM invoker: C:\Program Files\jdk-21\bin\java.exe
# VM options: -Dfile.encoding=UTF-8 -javaagent:C:\Program Files\IntelliJ IDEA\lib\idea_rt.jar=1604 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 2 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: benchmark.ColorBenchmark.test

Benchmark             Mode  Cnt   Score   Error  Units
ColorBenchmark.test   avgt    2  37.801          ns/op
ColorBenchmark.test2  avgt    2  12.678          ns/op
*/

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ColorBenchmark {
    private String color = "ffffffff";

    @Benchmark
    public Color test() {
        int r, g, b, a;
        r = Integer.parseInt(color.substring(0, 2), 16);
        g = Integer.parseInt(color.substring(2, 4), 16);
        b = Integer.parseInt(color.substring(4, 6), 16);
        a = Integer.parseInt(color.substring(6, 8), 16);
        return new Color(r, g, b, a);
    }

    @Benchmark
    public Color test2() {
        int c = Integer.parseUnsignedInt(color, 16);
        return new Color((c >> 24) & 0xff, (c >> 16) & 0xff, (c >> 8) & 0xff, c & 0xff);
    }

    public static void main(String[] args) {
        System.out.println(new ColorBenchmark().test());
        System.out.println(new ColorBenchmark().test2());
    }

    public static final class Color {
        int r, g, b, a;

        public Color(int r, int g, int b, int a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        @Override
        public String toString() {
            return "Color{" +
                    "r=" + r +
                    ", g=" + g +
                    ", b=" + b +
                    ", a=" + a +
                    '}';
        }
    }
}
