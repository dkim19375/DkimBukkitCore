package me.dkim19375.dkim19375core.data

import me.dkim19375.dkim19375core.annotation.API
import org.bukkit.Location
import org.bukkit.World

@API
class LocationWrapper(loc: Location) {
    val x: Int = loc.blockX
    val y: Int = loc.blockY
    val z: Int = loc.blockZ
    val world: World = loc.world!!

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