/*
 * MIT License
 *
 * Copyright (c) 2021 dkim19375
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.dkim19375.dkimbukkitcore.function

import me.dkim19375.dkimcore.annotation.API
import me.dkim19375.dkimcore.extension.setDecimalPlaces
import me.dkim19375.dkimcore.extension.setPlaceholders
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import java.lang.reflect.Method
import java.util.*

@API
fun Location.format(format: String = "world: %world%, %x%, %y%, %z%", decimalPlaces: Int? = 2): String {
    val world = world?.name ?: "none"
    val x = x.setDecimalPlaces(decimalPlaces).toString()
    val y = y.setDecimalPlaces(decimalPlaces).toString()
    val z = z.setDecimalPlaces(decimalPlaces).toString()
    val yaw = yaw.setDecimalPlaces(decimalPlaces).toString()
    val pitch = pitch.setDecimalPlaces(decimalPlaces).toString()
    if (format == "world: %world%, %x%, %y%, %z%") {
        return "world: $world, $x, $y, $z"
    }
    return format.setPlaceholders(mapOf(
        "world" to world,
        "x" to x,
        "y" to y,
        "z" to z,
        "yaw" to yaw,
        "pitch" to pitch
    ))
}

private var getHandle: Method? = null

private var getEntity: Method? = null

private var getBukkitEntity: Method? = null

// Pair<Pair<getHandle, getEntity>, getBukkitEntity>
private fun init(): Pair<Pair<Method, Method>, Method> {
    val world = Bukkit.getWorlds().first()
    val tempHandle = world::class.java.getMethod("getHandle")
    val tempEntity = tempHandle.invoke(world)::class.java.getMethod("getEntity", UUID::class.java)
    val entity = world.spawn(Location(world, 0.0, 150.0, 0.0), ArmorStand::class.java)
    entity.isVisible = false
    entity.setGravity(false) // just to be safe idk
    try {
        val tempBukkitEntity =
            tempEntity.invoke(tempHandle.invoke(world), entity.uniqueId)::class.java.getMethod("getBukkitEntity")
        getHandle = tempHandle
        getEntity = tempEntity
        getBukkitEntity = tempBukkitEntity
        return (tempHandle to tempEntity) to tempBukkitEntity
    } finally {
        entity.remove()
    }
}

private val hasNewGetEntity = runCatching { Bukkit.getEntity(UUID.randomUUID()) }.isSuccess

@API
fun UUID.getEntity(): Entity? {
    if (hasNewGetEntity) {
        return Bukkit.getEntity(this)
    }
    val getHandle = getHandle ?: init().first.first
    val getEntity = getEntity ?: init().first.second
    val getBukkitEntity = getBukkitEntity ?: init().second
    for (world in Bukkit.getWorlds()) {
        return getBukkitEntity.invoke(
            getEntity.invoke(getHandle.invoke(world), this) ?: continue
        ) as? Entity ?: continue
    }
    return null
}