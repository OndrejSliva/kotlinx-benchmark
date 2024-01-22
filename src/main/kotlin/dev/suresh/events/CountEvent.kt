package events

import counter.CounterType

sealed interface CountEvent {
    fun getCounterType(): CounterType


}