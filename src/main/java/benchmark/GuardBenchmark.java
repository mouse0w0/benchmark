package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/*
# JMH version: 1.37
# VM version: JDK 21.0.3, Java HotSpot(TM) 64-Bit Server VM, 21.0.3+7-LTS-152
# VM invoker: C:\Program Files\jdk-21\bin\java.exe
# VM options: -Dfile.encoding=UTF-8 -javaagent:C:\Program Files\IntelliJ IDEA\lib\idea_rt.jar=4646 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 3 iterations, 10 s each
# Measurement: 10 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: benchmark.GuardBenchmark.withoutGuard

Benchmark                    Mode  Cnt  Score   Error  Units
GuardBenchmark.guard         avgt   10  2.400 ± 0.015  ns/op
GuardBenchmark.override      avgt   10  2.396 ± 0.007  ns/op
GuardBenchmark.withoutGuard  avgt   10  2.402 ± 0.014  ns/op
*/

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 3)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class GuardBenchmark {

    static class Node {
        boolean hasChildren() {
            return false;
        }

        void visitNode(Blackhole blackhole) {
            blackhole.consume(this);

            if (hasChildren()) {
                visitChildren(blackhole);
            }
        }

        void visitNode2(Blackhole blackhole) {
            blackhole.consume(this);

            visitChildren(blackhole);
        }

        void visitNode3(Blackhole blackhole) {
            blackhole.consume(this);
        }

        void visitChildren(Blackhole blackhole) {
        }
    }

    static class Parent extends Node {
        private final Node[] children;

        public Parent(Node... children) {
            this.children = children;
        }

        boolean hasChildren() {
            return true;
        }

        @Override
        void visitChildren(Blackhole blackhole) {
            for (Node child : children) {
                blackhole.consume(child);
            }
        }

        @Override
        void visitNode3(Blackhole blackhole) {
            super.visitNode3(blackhole);

            for (Node child : children) {
                blackhole.consume(child);
            }
        }
    }

    private Parent p;

    @Setup
    public void setup() {
        Node a = new Node();
        Node b = new Node();
        Node c = new Node();
        p = new Parent(a, b, c);
    }

    @Benchmark
    public void guard(Blackhole blackhole) {
        p.visitNode(blackhole);
    }

    @Benchmark
    public void withoutGuard(Blackhole blackhole) {
        p.visitNode2(blackhole);
    }

    @Benchmark
    public void override(Blackhole blackhole) {
        p.visitNode3(blackhole);
    }
}
