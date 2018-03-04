package szewek.fl.test

import java.util.*
import java.util.function.BiConsumer

object NamedCounters {
    private val names = HashMap<String, Counter>()
    private val consumers = HashMap<Counter, BiConsumer<String, Counter>>()

    fun getCounter(n: String): Counter {
        return names.computeIfAbsent(n, { Counter(it) })
    }

    fun addConsumer(c: Counter, bic: BiConsumer<String, Counter>) {
        val obic = consumers[c]
        if (obic == null)
            consumers.put(c, bic)
        else
            consumers.put(c, obic.andThen(bic))
    }

    fun checkAndResetAll() {
        for ((key, c) in names) {
            if (consumers.containsKey(c))
                consumers[c]!!.accept(key, c)
            c.count = 0
        }
    }

    class Counter internal constructor(x: String) {
        var count: Long = 0
            internal set

        fun add() {
            if (count < java.lang.Long.MAX_VALUE)
                count++
        }

        fun expect(min: Long, max: Long): Boolean {
            return count >= min && (min >= max || count <= max)
        }
    }
}
