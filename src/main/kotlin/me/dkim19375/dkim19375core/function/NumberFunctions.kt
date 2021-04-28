package me.dkim19375.dkim19375core.function

import me.dkim19375.dkim19375core.annotation.API
import kotlin.random.Random

@API
fun Long.getPercentage(max: Long): Long {
    return this * 100 / max
}

@API
fun Double.getPercentageDouble(max: Double): Double {
    return this * 100 / max
}

@API
fun Float.percentChance(): Boolean {
    return Math.random() * 100 <= this
}

@API
fun Int.percentChance(): Boolean {
    return Math.random() * 100 <= this
}

@API
fun Int.getRandomNumber(maximum: Int): Int {
    return Random.nextInt(maximum - this + 1) + this
}