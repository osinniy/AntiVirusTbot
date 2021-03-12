@file:Suppress("unused")

package app

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import java.time.LocalTime
import java.time.ZoneId
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.hours
import kotlin.time.measureTime

class CoroutineScheduler {
    companion object {
        fun init(dispatchOn: CoroutineDispatcher, block: suspend CoroutineScheduler.() -> Unit) =
            GlobalScope.launch(dispatchOn) {
                CoroutineScheduler().block()
            }
    }
}

fun scheduleOn(dispatchOn: CoroutineDispatcher, builder: suspend CoroutineScheduler.() -> Unit) =
    CoroutineScheduler.init(dispatchOn, builder)

@ExperimentalTime
suspend inline fun CoroutineScheduler.runAfter(time: Duration, action: () -> Unit) {
    delay(time)
    action()
}

@ExperimentalTime
suspend inline fun CoroutineScheduler.runEvery(time: Duration, startNow: Boolean = true, action: () -> Unit) {
    if (startNow) action()
    while (true) {
        delay(time)
        action()
    }
}

suspend inline fun CoroutineScheduler.runAt(time: Instant, action: () -> Unit) {
    val period = time.toEpochMilliseconds() - System.currentTimeMillis()
    if (period <= 0) {
        action()
    } else {
        delay(period)
        action()
    }
}

@ExperimentalTime
suspend inline fun CoroutineScheduler.runEveryDayAt(
    hours: Int,
    minutes: Int = 0,
    seconds: Int = 0,
    action: () -> Unit,
) {
    val secondsFromMidnight = hours * 60 * 60 + minutes * 60 + seconds
    val secondsFromMidnightNow = with(LocalTime.now(ZoneId.of("UTC"))) { hour * 60 * 60 + minute * 60 + second }

    var firstDelaySeconds = secondsFromMidnight - secondsFromMidnightNow
    if (firstDelaySeconds < 0) firstDelaySeconds = 24 * 60 * 60 - firstDelaySeconds

    delay(firstDelaySeconds * 1000L)
    while (true) {
        val correction = measureTime {
            action()
        }
        delay(24.hours - correction)
    }
}
