package benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/*
# JMH version: 1.37
# VM version: JDK 25, OpenJDK 64-Bit Server VM, 25+36-LTS
# VM invoker: C:\Program Files\Zulu\zulu-25\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.1.1.1\lib\idea_rt.jar=41138 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 2 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op

Benchmark                                              Mode  Cnt     Score   Error   Units
RecordVsBeanBenchmark.createBean                       avgt    2     2.356           ns/op
RecordVsBeanBenchmark.createBean:gc.alloc.rate         avgt    2  9714.671          MB/sec
RecordVsBeanBenchmark.createBean:gc.alloc.rate.norm    avgt    2    24.000            B/op
RecordVsBeanBenchmark.createBean:gc.count              avgt    2   235.000          counts
RecordVsBeanBenchmark.createBean:gc.time               avgt    2   406.000              ms
RecordVsBeanBenchmark.createRecord                     avgt    2     2.358           ns/op
RecordVsBeanBenchmark.createRecord:gc.alloc.rate       avgt    2  9705.255          MB/sec
RecordVsBeanBenchmark.createRecord:gc.alloc.rate.norm  avgt    2    24.000            B/op
RecordVsBeanBenchmark.createRecord:gc.count            avgt    2   234.000          counts
RecordVsBeanBenchmark.createRecord:gc.time             avgt    2   389.000              ms

Benchmark                               Mode  Cnt  Score   Error  Units
RecordVsBeanBenchmark.accessBean        avgt   10  5.903 ± 0.041  ns/op
RecordVsBeanBenchmark.accessRecord      avgt   10  5.849 ± 0.134  ns/op
RecordVsBeanBenchmark.createBean        avgt   10  2.398 ± 0.014  ns/op
RecordVsBeanBenchmark.createRecord      avgt   10  2.390 ± 0.035  ns/op
RecordVsBeanBenchmark.equalsBeanDiff    avgt   10  0.515 ± 0.010  ns/op
RecordVsBeanBenchmark.equalsBeanSame    avgt   10  0.728 ± 0.007  ns/op
RecordVsBeanBenchmark.equalsRecordDiff  avgt   10  0.509 ± 0.017  ns/op
RecordVsBeanBenchmark.equalsRecordSame  avgt   10  0.646 ± 0.005  ns/op
RecordVsBeanBenchmark.hashCodeBean      avgt   10  0.583 ± 0.017  ns/op
RecordVsBeanBenchmark.hashCodeRecord    avgt   10  0.588 ± 0.005  ns/op
RecordVsBeanBenchmark.toStringBean      avgt   10  9.732 ± 0.110  ns/op
RecordVsBeanBenchmark.toStringRecord    avgt   10  9.665 ± 0.160  ns/op
 */

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 3)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class RecordVsBeanBenchmark {
    public static class PersonBean {
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

    public record PersonRecord(String name, int age) {}

    private PersonBean bean;
    private PersonRecord record;
    private PersonBean beanSame;
    private PersonRecord recordSame;
    private PersonBean beanDiff;
    private PersonRecord recordDiff;

    @Setup
    public void setup() {
        bean = new PersonBean("Alice", 30);
        record = new PersonRecord("Alice", 30);
        beanSame = new PersonBean("Alice", 30);
        recordSame = new PersonRecord("Alice", 30);
        beanDiff = new PersonBean("Bob", 25);
        recordDiff = new PersonRecord("Bob", 25);
    }

    @Benchmark
    public PersonBean createBean() {
        return new PersonBean("Alice", 30);
    }

    @Benchmark
    public PersonRecord createRecord() {
        return new PersonRecord("Alice", 30);
    }

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
