package benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/*
# JMH version: 1.37
# VM version: JDK 25, OpenJDK 64-Bit Server VM, 25+36-LTS
# VM invoker: C:\Program Files\Zulu\zulu-25\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.1.1.1\lib\idea_rt.jar=33379 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 3 iterations, 10 s each
# Measurement: 10 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op

Benchmark                                          Mode  Cnt  Score   Error  Units
StaticFinalRecordVsBeanBenchmark.accessBean        avgt   10  5.499 ± 0.136  ns/op
StaticFinalRecordVsBeanBenchmark.accessRecord      avgt   10  4.690 ± 0.071  ns/op
StaticFinalRecordVsBeanBenchmark.equalsBeanDiff    avgt   10  0.371 ± 0.019  ns/op
StaticFinalRecordVsBeanBenchmark.equalsBeanSame    avgt   10  0.572 ± 0.011  ns/op
StaticFinalRecordVsBeanBenchmark.equalsRecordDiff  avgt   10  0.214 ± 0.001  ns/op
StaticFinalRecordVsBeanBenchmark.equalsRecordSame  avgt   10  0.216 ± 0.002  ns/op
StaticFinalRecordVsBeanBenchmark.hashCodeBean      avgt   10  0.439 ± 0.012  ns/op
StaticFinalRecordVsBeanBenchmark.hashCodeRecord    avgt   10  0.215 ± 0.002  ns/op
StaticFinalRecordVsBeanBenchmark.toStringBean      avgt   10  9.448 ± 0.257  ns/op
StaticFinalRecordVsBeanBenchmark.toStringRecord    avgt   10  8.791 ± 0.049  ns/op
 */

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 3)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class StaticFinalRecordVsBeanBenchmark {
    public static final class PersonBean {
        private final String name;
        private final int age;

        public PersonBean(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PersonBean that = (PersonBean) o;
            return age == that.age && name.equals(that.name);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + age;
            return result;
        }

        @Override
        public String toString() {
            return "PersonBean{name='" + name + "', age=" + age + '}';
        }
    }

    public record PersonRecord(String name, int age) {
    }

    private static final String name = "alice";
    private static final int age = 30;
    private static final PersonBean bean = new PersonBean("Alice", 30);
    private static final PersonRecord record = new PersonRecord("Alice", 30);
    private static final PersonBean beanSame = new PersonBean("Alice", 30);
    private static final PersonRecord recordSame = new PersonRecord("Alice", 30);
    private static final PersonBean beanDiff = new PersonBean("Bob", 25);
    private static final PersonRecord recordDiff = new PersonRecord("Bob", 25);

    @Benchmark
    public String accessBean() {
        return bean.getName() + bean.getAge();
    }

    @Benchmark
    public String accessRecord() {
        return record.name() + record.age();
    }

    @Benchmark
    public boolean equalsBeanSame() {
        return bean.equals(beanSame);
    }

    @Benchmark
    public boolean equalsRecordSame() {
        return record.equals(recordSame);
    }

    @Benchmark
    public boolean equalsBeanDiff() {
        return bean.equals(beanDiff);
    }

    @Benchmark
    public boolean equalsRecordDiff() {
        return record.equals(recordDiff);
    }

    @Benchmark
    public int hashCodeBean() {
        return bean.hashCode();
    }

    @Benchmark
    public int hashCodeRecord() {
        return record.hashCode();
    }

    @Benchmark
    public String toStringBean() {
        return bean.toString();
    }

    @Benchmark
    public String toStringRecord() {
        return record.toString();
    }
}
