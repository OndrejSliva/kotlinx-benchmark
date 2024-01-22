package dev.suresh

import counter.Counter
import counter.DecrementCounter
import counter.IncrementCounter
import events.CountEvent
import events.DecrementEvent
import events.IncrementEvent

class CounterStorage(private val counters: List<Counter>) {

    private val incrementCounters = counters.filterIsInstance<IncrementCounter>()
    private val decrementCounters = counters.filterIsInstance<DecrementCounter>()

    fun getAll(countEvent: CountEvent): List<Counter> {
//        return incrementCounters
        return when (countEvent) {
            is IncrementEvent -> incrementCounters
            is DecrementEvent -> decrementCounters
        }
    }
//
//    fun getAll(): List<Counter> {
//        return counters
//    }

    fun getIncrement(): List<IncrementCounter> = incrementCounters

    fun getDecrement(): List<DecrementCounter> = decrementCounters

}