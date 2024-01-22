package events

import counter.CounterType

class IncrementEvent(val amountToIncrement: Int) : CountEvent {

    override fun getCounterType(): CounterType {
        return CounterType.INCREMENT
    }
}