package dev.suresh

import counter.Counter
import counter.Counters
import counter.DecrementCounter
import counter.IncrementCounter
import events.DecrementEvent
import events.IncrementEvent
import java.util.concurrent.TimeUnit.NANOSECONDS
import java.util.concurrent.TimeUnit.SECONDS
import org.openjdk.jmh.annotations.*

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(NANOSECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = SECONDS)
@Fork(1)
@State(Scope.Benchmark)
open class KotlinBench {

  @Param("10", "100", "1000", "10000", "100000") var size = 0

  private val x = Math.PI

  val incrementCounters1 = (1..1000).map { IncrementCounter() }
  val decrementCounters1 = (1..1000).map { DecrementCounter() }
  val incrementCounters2 = (1..1000).map { IncrementCounter() }
  val decrementCounters2 = (1..1000).map { DecrementCounter() }
  val allCounters1 = arrayListOf<Counter>()
  val allCounters2 = arrayListOf<Counter>()
  val events = arrayListOf(IncrementEvent(1), DecrementEvent(1), IncrementEvent(2), DecrementEvent(2))

  init {
    for (i in 1..1000) {
      allCounters1.add(IncrementCounter())
      allCounters1.add(DecrementCounter())
      allCounters2.add(IncrementCounter())
      allCounters2.add(DecrementCounter())
    }
//    allCounters1.shuffle()
//    allCounters2.shuffle()
//    events.shuffle()
  }
  val countersStorage1 = CounterStorage(allCounters1)
  val countersStorage2 = CounterStorage(allCounters2)
  val counters1 = Counters(countersStorage1)
  val counters2 = Counters(countersStorage2)
//  val events = listOf<CountEvent>(IncrementEvent(1), IncrementEvent(2))

//  /** Baseline measurement: how much single Math.log costs. */
//  @Benchmark fun baseline() = ln(x)
//
//  /**
//   * Should your benchmark require returning multiple results, use explicit [Blackhole] objects, and
//   * sink the values there. (Background: [Blackhole] is just another @State object, bundled with
//   * JMH).
//   */
//  @Benchmark
//  fun test(bh: Blackhole) {
//    (1..10).forEach { bh.consume(it) }
//  }
//
//  /** Returned results are implicitly consumed by Blackholes */
//  @Benchmark
//  open fun measureRight(): Double {
//    // This is correct: the result is being used.
//    return ln(x)
//  }
//
//  @Benchmark
//  fun newByteArray() {
//    val bytes = ByteArray(size)
//    bytes[size - 1] = 10
//  }


  @Benchmark
  fun countAll() {
    for (event in events) {
      counters2.countAll(event)
    }
  }

  @Benchmark
  fun countConcrete() {
    for (event in events) {
      counters1.countConcrete(event)
    }
  }
}
