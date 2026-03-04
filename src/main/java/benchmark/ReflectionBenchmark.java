package benchmark;

import org.openjdk.jmh.annotations.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/*
# JMH version: 1.37
# VM version: JDK 21.0.7, OpenJDK 64-Bit Server VM, 21.0.7+6-LTS
# VM invoker: C:\Program Files\Zulu\zulu-21\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.1.1.1\lib\idea_rt.jar=47798 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 2 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op

Benchmark                                                     Mode  Cnt   Score   Error  Units
ReflectionBenchmark.directCall                                avgt    2   0.296          ns/op
ReflectionBenchmark.getField                                  avgt    2   9.660          ns/op
ReflectionBenchmark.getMethod                                 avgt    2  15.652          ns/op
ReflectionBenchmark.lambdaGetValue                            avgt    2   0.477          ns/op
ReflectionBenchmark.lambdaSetValue                            avgt    2   0.420          ns/op
ReflectionBenchmark.methodHandleConstructorNewInstance        avgt    2   2.609          ns/op
ReflectionBenchmark.methodHandleGetValue                      avgt    2   2.270          ns/op
ReflectionBenchmark.methodHandleInvoke                        avgt    2   1.682          ns/op
ReflectionBenchmark.methodHandleInvokeAsType                  avgt    2   2.200          ns/op
ReflectionBenchmark.methodHandleInvokeBound                   avgt    2   1.533          ns/op
ReflectionBenchmark.methodHandleInvokeExact                   avgt    2   1.866          ns/op
ReflectionBenchmark.methodHandleSetValue                      avgt    2   1.790          ns/op
ReflectionBenchmark.methodInvoke                              avgt    2   3.889          ns/op
ReflectionBenchmark.reflectionConstructorNewInstance          avgt    2   2.719          ns/op
ReflectionBenchmark.reflectionFieldGet                        avgt    2   2.467          ns/op
ReflectionBenchmark.reflectionFieldSet                        avgt    2   2.742          ns/op
ReflectionBenchmark.reflectionMethodGetValue                  avgt    2   3.562          ns/op
ReflectionBenchmark.reflectionMethodSetValue                  avgt    2   3.404          ns/op
ReflectionBenchmark.reflectionNewInstance                     avgt    2  20.484          ns/op
ReflectionBenchmark.staticLambdaGetValue                      avgt    2   0.481          ns/op
ReflectionBenchmark.staticLambdaSetValue                      avgt    2   0.412          ns/op
ReflectionBenchmark.staticMethodHandleConstructorNewInstance  avgt    2   2.511          ns/op
ReflectionBenchmark.staticMethodHandleGetValue                avgt    2   1.908          ns/op
ReflectionBenchmark.staticMethodHandleInvoke                  avgt    2   0.281          ns/op
ReflectionBenchmark.staticMethodHandleInvokeAsType            avgt    2   1.726          ns/op
ReflectionBenchmark.staticMethodHandleInvokeBound             avgt    2   0.203          ns/op
ReflectionBenchmark.staticMethodHandleInvokeExact             avgt    2   0.276          ns/op
ReflectionBenchmark.staticMethodHandleSetValue                avgt    2   0.283          ns/op
ReflectionBenchmark.staticVarHandleGet                        avgt    2   5.788          ns/op
ReflectionBenchmark.staticVarHandleSet                        avgt    2   0.276          ns/op
ReflectionBenchmark.varHandleGet                              avgt    2   7.521          ns/op
ReflectionBenchmark.varHandleGetAcquire                       avgt    2   7.671          ns/op
ReflectionBenchmark.varHandleGetOpaque                        avgt    2   7.789          ns/op
ReflectionBenchmark.varHandleGetVolatile                      avgt    2   7.887          ns/op
ReflectionBenchmark.varHandleSet                              avgt    2   2.628          ns/op
ReflectionBenchmark.varHandleSetOpaque                        avgt    2   2.645          ns/op
ReflectionBenchmark.varHandleSetRelease                       avgt    2   2.634          ns/op
ReflectionBenchmark.varHandleSetVolatile                      avgt    2   5.817          ns/op
 */

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ReflectionBenchmark {

    static class TestClass {
        private int value = 42;

        public TestClass() {}

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int add(int a, int b) {
            return a + b;
        }
    }

    private TestClass instance = new TestClass();

    private static final MethodHandle STATIC_ADD_METHOD_HANDLE;
    private static final MethodHandle STATIC_ADD_METHOD_HANDLE_AS_TYPE;
    private static final MethodHandle STATIC_GET_VALUE_METHOD_HANDLE;
    private static final MethodHandle STATIC_SET_VALUE_METHOD_HANDLE;
    private static final VarHandle STATIC_VALUE_VAR_HANDLE;
    private static final MethodHandle STATIC_ADD_METHOD_HANDLE_BOUND;
    private static final MethodHandle STATIC_CONSTRUCTOR_HANDLE;

    static {
        try {
            MethodHandles.Lookup staticLookup = MethodHandles.lookup();
            STATIC_ADD_METHOD_HANDLE = staticLookup.findVirtual(TestClass.class, "add", MethodType.methodType(int.class, int.class, int.class));
            STATIC_ADD_METHOD_HANDLE_AS_TYPE = STATIC_ADD_METHOD_HANDLE.asType(MethodType.methodType(Object.class, Object.class, int.class, int.class));
            STATIC_GET_VALUE_METHOD_HANDLE = staticLookup.findVirtual(TestClass.class, "getValue", MethodType.methodType(int.class));
            STATIC_SET_VALUE_METHOD_HANDLE = staticLookup.findVirtual(TestClass.class, "setValue", MethodType.methodType(void.class, int.class));
            STATIC_VALUE_VAR_HANDLE = staticLookup.findVarHandle(TestClass.class, "value", int.class);
            STATIC_ADD_METHOD_HANDLE_BOUND = STATIC_ADD_METHOD_HANDLE.bindTo(new TestClass());
            STATIC_CONSTRUCTOR_HANDLE = staticLookup.findConstructor(TestClass.class, MethodType.methodType(void.class));
        } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Method getValueMethod;
    private Method setValueMethod;
    private Method addMethod;
    private Field valueField;
    private Constructor<TestClass> constructor;

    private MethodHandle addMethodHandle;
    private MethodHandle getValueMethodHandle;
    private MethodHandle setValueMethodHandle;
    private MethodHandle addMethodHandleAsType;
    private MethodHandle addMethodHandleBound;
    private MethodHandle constructorHandle;

    private VarHandle valueVarHandle;
    private VarHandle valueVarHandleVolatile;
    private VarHandle valueVarHandleOpaque;
    private VarHandle valueVarHandleAcquire;
    private VarHandle valueVarHandleRelease;

    private MethodHandles.Lookup lookup;

    private Supplier<Integer> lambdaGetValue;
    private Consumer<Integer> lambdaSetValue;
    private Function<TestClass, Integer> staticLambdaGetValue;
    private BiConsumer<TestClass, Integer> staticLambdaSetValue;

    @Setup
    public void setup() throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        getValueMethod = TestClass.class.getMethod("getValue");
        setValueMethod = TestClass.class.getMethod("setValue", int.class);
        addMethod = TestClass.class.getMethod("add", int.class, int.class);
        valueField = TestClass.class.getDeclaredField("value");
        valueField.setAccessible(true);
        constructor = TestClass.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        lookup = MethodHandles.lookup();
        addMethodHandle = lookup.findVirtual(TestClass.class, "add", MethodType.methodType(int.class, int.class, int.class));
        getValueMethodHandle = lookup.findVirtual(TestClass.class, "getValue", MethodType.methodType(int.class));
        setValueMethodHandle = lookup.findVirtual(TestClass.class, "setValue", MethodType.methodType(void.class, int.class));
        constructorHandle = lookup.findConstructor(TestClass.class, MethodType.methodType(void.class));

        valueVarHandle = lookup.findVarHandle(TestClass.class, "value", int.class);
        valueVarHandleVolatile = MethodHandles.privateLookupIn(TestClass.class, MethodHandles.lookup()).findVarHandle(TestClass.class, "value", int.class);
        valueVarHandleOpaque = valueVarHandleVolatile;
        valueVarHandleAcquire = valueVarHandleVolatile;
        valueVarHandleRelease = valueVarHandleVolatile;

        addMethodHandleAsType = addMethodHandle.asType(MethodType.methodType(Object.class, Object.class, int.class, int.class));
        addMethodHandleBound = addMethodHandle.bindTo(instance);

        lambdaGetValue = instance::getValue;
        lambdaSetValue = instance::setValue;
        staticLambdaGetValue = TestClass::getValue;
        staticLambdaSetValue = TestClass::setValue;
    }

    // ==================== Baseline ====================

    @Benchmark
    public int directCall() {
        return instance.add(10, 20);
    }

    // ==================== Constructor Benchmarks ====================

    @Benchmark
    public Object reflectionNewInstance() throws Exception {
        return TestClass.class.getDeclaredConstructor().newInstance();
    }

    @Benchmark
    public Object reflectionConstructorNewInstance() throws Exception {
        return constructor.newInstance();
    }

    @Benchmark
    public Object methodHandleConstructorNewInstance() throws Throwable {
        return constructorHandle.invoke();
    }

    @Benchmark
    public Object staticMethodHandleConstructorNewInstance() throws Throwable {
        return STATIC_CONSTRUCTOR_HANDLE.invoke();
    }

    // ==================== Reflection Benchmarks ====================

    @Benchmark
    public Method getMethod() throws NoSuchMethodException {
        return TestClass.class.getMethod("add", int.class, int.class);
    }

    @Benchmark
    public Object methodInvoke() throws Exception {
        return addMethod.invoke(instance, 10, 20);
    }

    @Benchmark
    public Field getField() throws NoSuchFieldException {
        return TestClass.class.getDeclaredField("value");
    }

    @Benchmark
    public Object reflectionFieldGet() throws Exception {
        return valueField.get(instance);
    }

    @Benchmark
    public void reflectionFieldSet() throws Exception {
        valueField.set(instance, 100);
    }

    @Benchmark
    public Object reflectionMethodGetValue() throws Exception {
        return getValueMethod.invoke(instance);
    }

    @Benchmark
    public void reflectionMethodSetValue() throws Exception {
        setValueMethod.invoke(instance, 100);
    }

    // ==================== MethodHandle Benchmarks ====================

    @Benchmark
    public int methodHandleInvokeExact() throws Throwable {
        return (int) addMethodHandle.invokeExact(instance, 10, 20);
    }

    @Benchmark
    public int methodHandleInvoke() throws Throwable {
        return (int) addMethodHandle.invoke(instance, 10, 20);
    }

    @Benchmark
    public int methodHandleInvokeAsType() throws Throwable {
        return (int) addMethodHandleAsType.invoke(instance, 10, 20);
    }

    @Benchmark
    public int methodHandleInvokeBound() throws Throwable {
        return (int) addMethodHandleBound.invokeExact(10, 20);
    }

    @Benchmark
    public Object methodHandleGetValue() throws Throwable {
        return getValueMethodHandle.invoke(instance);
    }

    @Benchmark
    public void methodHandleSetValue() throws Throwable {
        setValueMethodHandle.invoke(instance, 100);
    }

    // ==================== Static MethodHandle Benchmarks ====================

    @Benchmark
    public int staticMethodHandleInvoke() throws Throwable {
        return (int) STATIC_ADD_METHOD_HANDLE.invoke(instance, 10, 20);
    }

    @Benchmark
    public int staticMethodHandleInvokeExact() throws Throwable {
        return (int) STATIC_ADD_METHOD_HANDLE.invokeExact(instance, 10, 20);
    }

    @Benchmark
    public int staticMethodHandleInvokeAsType() throws Throwable {
        return (int) STATIC_ADD_METHOD_HANDLE_AS_TYPE.invoke(instance, 10, 20);
    }

    @Benchmark
    public int staticMethodHandleInvokeBound() throws Throwable {
        return (int) STATIC_ADD_METHOD_HANDLE_BOUND.invokeExact(10, 20);
    }

    @Benchmark
    public Object staticMethodHandleGetValue() throws Throwable {
        return STATIC_GET_VALUE_METHOD_HANDLE.invoke(instance);
    }

    @Benchmark
    public void staticMethodHandleSetValue() throws Throwable {
        STATIC_SET_VALUE_METHOD_HANDLE.invoke(instance, 100);
    }

    // ==================== VarHandle Benchmarks ====================

    @Benchmark
    public Object varHandleGet() {
        return valueVarHandle.get(instance);
    }

    @Benchmark
    public void varHandleSet() {
        valueVarHandle.set(instance, 100);
    }

    @Benchmark
    public Object varHandleGetVolatile() {
        return valueVarHandleVolatile.getVolatile(instance);
    }

    @Benchmark
    public void varHandleSetVolatile() {
        valueVarHandleVolatile.setVolatile(instance, 100);
    }

    @Benchmark
    public Object varHandleGetOpaque() {
        return valueVarHandleOpaque.getOpaque(instance);
    }

    @Benchmark
    public void varHandleSetOpaque() {
        valueVarHandleOpaque.setOpaque(instance, 100);
    }

    @Benchmark
    public Object varHandleGetAcquire() {
        return valueVarHandleAcquire.getAcquire(instance);
    }

    @Benchmark
    public void varHandleSetRelease() {
        valueVarHandleRelease.setRelease(instance, 100);
    }

    // ==================== Static VarHandle Benchmarks ====================

    @Benchmark
    public Object staticVarHandleGet() {
        return STATIC_VALUE_VAR_HANDLE.get(instance);
    }

    @Benchmark
    public void staticVarHandleSet() {
        STATIC_VALUE_VAR_HANDLE.set(instance, 100);
    }

    // ==================== Lambda Benchmarks ====================

    @Benchmark
    public int lambdaGetValue() {
        return lambdaGetValue.get();
    }

    @Benchmark
    public void lambdaSetValue() {
        lambdaSetValue.accept(100);
    }

    @Benchmark
    public int staticLambdaGetValue() {
        return staticLambdaGetValue.apply(instance);
    }

    @Benchmark
    public void staticLambdaSetValue() {
        staticLambdaSetValue.accept(instance, 100);
    }
}