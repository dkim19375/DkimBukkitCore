package me.dkim19375.dkim19375core.function

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.util.Location
import com.sk89q.worldguard.LocalPlayer
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import com.sk89q.worldguard.protection.flags.StateFlag
import com.sk89q.worldguard.protection.managers.RegionManager
import me.dkim19375.dkim19375core.annotation.API
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player

fun Player.toLocalPlayer(): LocalPlayer {
    return WorldGuardPlugin.inst().wrapPlayer(player)
}

@API
fun com.sk89q.worldedit.entity.Player.toBukkitPlayer(): Player = Bukkit.getPlayer(uniqueId)!!

fun Location.toBlockVector3(): BlockVector3 {
    return BlockVector3.at(x, y, z)
}

@API
fun org.bukkit.Location.toBlockVector3(): BlockVector3 {
    return BlockVector3.at(x, y, z)
}

fun World.toWorldeditWorld(): com.sk89q.worldedit.world.World {
    return BukkitAdapter.adapt(this)
}

@API
fun StateFlag.testStateOfFlag(
    player: Player,
    nullRegion: (RegionManager?) -> Unit
): Boolean {
    return testStateOfFlag(
        player,
        player.toLocalPlayer().location,
        nullRegion
    )
}

@API
fun StateFlag.testStateOfFlag(
    player: com.sk89q.worldedit.entity.Player,
    nullRegion: (RegionManager?) -> Unit
): Boolean {
    return testStateOfFlag(
        Bukkit.getPlayer(player.uniqueId)!!,
        player.location, nullRegion
    )
}

fun StateFlag.testStateOfFlag(
    player: Player,
    location: Location,
    nullRegion: (RegionManager?) -> Unit
): Boolean {
    val c = WorldGuard.getInstance().platform.regionContainer
    val regions = c[player.world.toWorldeditWorld()]
    val position: BlockVector3 = location.toBlockVector3()
    if (regions == null) {
        nullRegion(null)
        return false
    }
    val set = regions.getApplicableRegions(position)
    val lp = player.toLocalPlayer()
    return set.testState(lp, this)
}