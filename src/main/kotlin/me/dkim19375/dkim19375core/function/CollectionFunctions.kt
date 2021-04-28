package me.dkim19375.dkim19375core.function

import me.dkim19375.dkim19375core.annotation.API
import java.util.*

operator fun <V> Map<String, V>.get(other: String, ignoreCase: Boolean = false): V? {
    if (!ignoreCase) {
        return get(other)
    }
    for (entry in entries) {
        if (entry.key.equals(other, ignoreCase = ignoreCase)) {
            return entry.value
        }
    }
    return null
}

@API
fun <T> List<T?>.filterNonNull(): List<T> = filter(Objects::nonNull).map { t -> t!! }