package events

import counter.CounterType

class DecrementEvent(val amountToDecrement: Int) : CountEvent {

    override fun getCounterType(): CounterType {
        return CounterType.DECREMENT
    }
}