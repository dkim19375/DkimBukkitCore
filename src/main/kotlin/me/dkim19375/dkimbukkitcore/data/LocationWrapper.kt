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
import org.bukkit.Location
import org.bukkit.World

@API
class LocationWrapper(loc: Location) {
    val x: Int = loc.blockX
    val y: Int = loc.blockY
    val z: Int = loc.blockZ
    val world: World = loc.world ?: throw IllegalStateException("World must be set!")

    @API
    fun getLocation() = Location(world, x.toDouble() + 0.5, y.toDouble(), z.toDouble() + 0.5)

    @API
    fun getDistance(other: LocationWrapper): Double = getDistance(other.getLocation())

    @API
    fun getDistance(other: Location): Double = getLocation().distance(other)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocationWrapper

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false
        if (world.name != other.world.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        result = 31 * result + world.hashCode()
        return result
    }
}