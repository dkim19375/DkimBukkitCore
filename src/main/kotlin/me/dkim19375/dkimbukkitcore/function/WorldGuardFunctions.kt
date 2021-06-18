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

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.util.Location
import com.sk89q.worldguard.LocalPlayer
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import com.sk89q.worldguard.protection.flags.StateFlag
import com.sk89q.worldguard.protection.managers.RegionManager
import me.dkim19375.dkimcore.annotation.API
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