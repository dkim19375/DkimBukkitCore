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

package me.dkim19375.dkimbukkitcore.data

import me.dkim19375.dkimcore.annotation.API
import me.dkim19375.dkimcore.extension.setPlaceholders
import org.bukkit.*
import org.bukkit.configuration.serialization.ConfigurationSerializable

@API
data class LocationWrapper(
    val world: World,
    val x: Int,
    val y: Int,
    val z: Int,
    val yaw: Int = 0,
    val pitch: Int = 0,
) : ConfigurationSerializable {

    @API
    constructor(loc: Location) : this(
        world = loc.world ?: throw IllegalArgumentException("World must be set!"),
        x = loc.blockX,
        y = loc.blockY,
        z = loc.blockZ,
        yaw = loc.yaw.toInt(),
        pitch = loc.pitch.toInt(),
    )

    @API
    fun getLocation() = Location(
        world,
        x.toDouble() + 0.5,
        y.toDouble(),
        z.toDouble() + 0.5,
        yaw.toFloat(),
        pitch.toFloat()
    )

    @API
    fun getDistance(other: LocationWrapper): Double = getDistance(other.getLocation())

    @API
    fun getDistance(other: Location): Double = getLocation().distance(other)

    @API
    fun coordsEquals(other: LocationWrapper?): Boolean {
        other ?: return false

        if (this === other) return true

        if (world.name != other.world.name) return false
        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocationWrapper

        if (world.name != other.world.name) return false
        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false
        if (yaw != other.yaw) return false
        if (pitch != other.pitch) return false

        return true
    }

    override fun serialize(): Map<String, Any> = mapOf(
        "world" to world.name,
        "x" to x,
        "y" to y,
        "z" to z,
        "yaw" to yaw,
        "pitch" to pitch,
    )

    override fun hashCode(): Int {
        var result = world.name.hashCode()
        result = 31 * result + x
        result = 31 * result + y
        result = 31 * result + z
        result = 31 * result + yaw
        result = 31 * result + pitch
        return result
    }

    override fun toString(): String {
        return "LocationWrapper(world=${world.name}, x=$x, y=$y, z=$z, yaw=$yaw, pitch=$pitch)"
    }

    @API
    fun format(format: String = "world: %world%, %x%, %y%, %z%"): String {
        if (format == "world: %world%, %x%, %y%, %z%") {
            return "world: ${world.name}, $x, $y, $z"
        }
        return format.setPlaceholders(mapOf(
            "world" to world.name,
            "x" to x.toString(),
            "y" to y.toString(),
            "z" to z.toString(),
            "yaw" to yaw.toString(),
            "pitch" to pitch.toString()
        ))
    }

    companion object {
        @API
        @JvmStatic
        fun deserialize(map: Map<String, Any>): LocationWrapper? {
            val world = (map["world"] as? String)?.let(Bukkit::getWorld) ?: return null
            val x = map["x"] as? Int ?: return null
            val y = map["y"] as? Int ?: return null
            val z = map["z"] as? Int ?: return null
            val yaw = map["yaw"] as? Int ?: return null
            val pitch = map["pitch"] as? Int ?: return null
            return LocationWrapper(world, x, y, z, yaw, pitch)
        }
    }
}

@API
fun Location.toWrapper(): LocationWrapper = LocationWrapper(this)