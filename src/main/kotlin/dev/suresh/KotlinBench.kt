package dev.suresh

import counter.Counter
import counter.Counters
import counter.DecrementCounter
import counter.IncrementCounter
import dev.suresh.ads.Ad
import dev.suresh.ads.Link
import dev.suresh.ads.PpvSlot
import dev.suresh.ads.PpvSuggestedAd
import dev.suresh.ads.adsFor
import dev.suresh.validation.MutableValidationResult
import dev.suresh.validation.RequestValidationResult
import events.DecrementEvent
import events.IncrementEvent
import java.util.concurrent.TimeUnit.NANOSECONDS
import java.util.concurrent.TimeUnit.SECONDS
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import kotlin.math.ln

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
  val events = arrayListOf(IncrementEvent(1), DecrementEvent(1))

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



  fun countAll() {
    for (event in events) {
      counters2.countAll(event)
    }
  }

  fun countConcrete() {
    for (event in events) {
      counters1.countConcrete(event)
    }
  }

  fun createImutableResult(){
    val result = RequestValidationResult()
      .addErrorIf(true, "error1")
      .addErrorIf(true, "error2")
      .addErrorIf(true, "error3")

    result.isValid

  }

  fun createMutableResult(){
    val result = MutableValidationResult()
      .addErrorIf(true, "error1")
      .addErrorIf(true, "error2")
      .addErrorIf(true, "error3")

    result.isValid()
  }

  fun exceptionDrivenValidation(){
    try{
        if(true){
            throw Exception("error1")
        }
        if(true){
            throw Exception("error2")
        }
        if(true){
            throw Exception("error3")
        }
    }catch(e: Exception){
        e.message
    }
  }

  @Benchmark
  fun originalCode() {
    originalChooseFrom(ads)
  }

  @Benchmark
  fun refactoredCode(){
      refactoredChooseFrom(ads)
  }

  val ppvSlots = listOf(
    PpvSlot("slot1", listOf("1"), 1,2),
    PpvSlot("slot2", listOf("1"), 2,1)
  )

  val ads = listOf(Ad(
    id = "2",
    landingPageUrl = "url",
    duration = 1,
    width = 1,
    height = 2,
    links = listOf(Link("1", "1", "landscape"))
  ),
                   Ad(
                     id = "1",
                     landingPageUrl = "url",
                     duration = 1,
                     width = 2,
                     height = 1,
                     links = listOf(Link("2", "1", "landscape"))
                   ),
                   Ad(
                     id = "3",
                     landingPageUrl = "url",
                     duration = 1,
                     width = 1,
                     height = 2,
                     links = emptyList()
                   )

  )

  fun originalChooseFrom(ads: List<Ad>): List<PpvSuggestedAd> {

    val ppvSuggestedAds = mutableListOf<PpvSuggestedAd>()
    val (firstSlot, secondSlot) = ppvSlots

    val adsDisplayableInFirstSlot = ads.filter{ it.fitsIn(firstSlot) }
    val adsDisplayableInSecondSlot = ads.filter{ it.fitsIn(secondSlot) }

    val adToSuggest = adsDisplayableInFirstSlot.firstOrNull { ad ->
      ads.adsFor(ad.links).any { it in adsDisplayableInSecondSlot }
    }

    adToSuggest?.let { ad ->
      val adOfLink = ads.adsFor(ad.links).first { it in adsDisplayableInSecondSlot }

      ppvSuggestedAds.add(PpvSuggestedAd.forAd(ad, firstSlot.id))
      ppvSuggestedAds.add(PpvSuggestedAd.forAd(adOfLink, secondSlot.id))
    }

    return ppvSuggestedAds
  }

  fun refactoredChooseFrom(ads: List<Ad>): List<PpvSuggestedAd> {
    val slots = ppvSlots.take(2)

    val filters = slots.map { slot ->
      ads.filter { it.fitsIn(slot) }
    }

    val pairedAds = filters[0].asSequence().mapNotNull { ad ->
      val linkedAd = ads.adsFor(ad.links).firstOrNull { it in filters[1] }
      linkedAd?.let { listOf(ad to slots[0].id, it to slots[1].id) }
    }.firstOrNull().orEmpty()

    val ppvSuggestedAds = pairedAds.map { (ad, slotId) -> PpvSuggestedAd.forAd(ad, slotId) }

    return ppvSuggestedAds
  }

}
