package benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/*
# JMH version: 1.37
# VM version: JDK 21.0.7, OpenJDK 64-Bit Server VM, 21.0.7+6-LTS
# VM invoker: C:\Program Files\Zulu\zulu-21\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.1.1.1\lib\idea_rt.jar=41270 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 2 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op

Benchmark                              (arg1)  (arg2)  (arg3)  (arg4)  (arg5)  (arg6)  (arg7)  (arg8)  (arg9)  Mode  Cnt  Score   Error  Units
VarargsBenchmark.nine                       1       2       3       4       5       6       7       8       9  avgt    2  0.889          ns/op
VarargsBenchmark.one                        1       2       3       4       5       6       7       8       9  avgt    2  0.297          ns/op
VarargsBenchmark.requiredOne                1       2       3       4       5       6       7       8       9  avgt    2  0.311          ns/op
VarargsBenchmark.requiredOneMultiArgs       1       2       3       4       5       6       7       8       9  avgt    2  5.049          ns/op
VarargsBenchmark.varargs                    1       2       3       4       5       6       7       8       9  avgt    2  0.293          ns/op
VarargsBenchmark.varargsMultiArgs           1       2       3       4       5       6       7       8       9  avgt    2  5.924          ns/op
*/

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class VarargsBenchmark {

    @Param({"1"})
    public int arg1;
    
    @Param({"2"})
    public int arg2;
    
    @Param({"3"})
    public int arg3;
    
    @Param({"4"})
    public int arg4;
    
    @Param({"5"})
    public int arg5;
    
    @Param({"6"})
    public int arg6;
    
    @Param({"7"})
    public int arg7;
    
    @Param({"8"})
    public int arg8;
    
    @Param({"9"})
    public int arg9;

    @Benchmark
    public int one() {
        return one(arg1);
    }

    @Benchmark
    public int requiredOne() {
        return requiredOne(arg1);
    }

    @Benchmark
    public int varargs() {
        return varargs(arg1);
    }

    @Benchmark
    public int nine() {
        return nine(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    }

    @Benchmark
    public int requiredOneMultiArgs() {
        return requiredOne(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    }

    @Benchmark
    public int varargsMultiArgs() {
        return varargs(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    }

    private int one(int i) {
        return i;
    }

    private int nine(int i, int j, int k, int l, int m, int n, int o, int p, int q) {
        return i + j + k + l + m + n + o + p + q;
    }

    private int requiredOne(int i, int... j) {
        int result = i;
        for (int k : j) {
            result += k;
        }
        return result;
    }

    private int varargs(int... i) {
        int result = 0;
        for (int j : i) {
            result += j;
        }
        return result;
    }
}