package me.dkim19375.dkim19375core.external;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;

public class WorldGuardUtils {

    public static LocalPlayer getLocalPlayerFromBukkitPlayer(final Player player) {
        return WorldGuardPlugin.inst().wrapPlayer(player);
    }

    public static BlockVector3 getBlockVector3FromLocation(final Location location) {
        return BlockVector3.at(location.getX(), location.getY(), location.getZ());
    }

    public static BlockVector3 getBlockVector3FromLocation(final com.sk89q.worldedit.util.Location location) {
        return BlockVector3.at(location.getX(), location.getY(), location.getZ());
    }


    public static World getWorldGuardWorldFromBukkitWorld(final org.bukkit.World world) {
        return BukkitAdapter.adapt(world);
    }
    
    public static boolean testStateOfFlag(final Player player, final StateFlag flag, Consumer<RegionManager> nullRegion) {
        RegionContainer c = WorldGuard.getInstance().getPlatform().getRegionContainer();
        World world = getWorldGuardWorldFromBukkitWorld(player.getWorld());
        RegionManager regions = c.get(world);
        BlockVector3 position = getBlockVector3FromLocation(player.getLocation());
        if (regions == null) {
            nullRegion.accept(null);
            return false;
        }
        ApplicableRegionSet set = regions.getApplicableRegions(position);
        final LocalPlayer lp = getLocalPlayerFromBukkitPlayer(player);
        return set.testState(lp, flag);
    }

    public static boolean testStateOfFlag(Player player, Location location, StateFlag flag, Consumer<RegionManager> nullRegion) {
        RegionContainer c = WorldGuard.getInstance().getPlatform().getRegionContainer();
        World world = getWorldGuardWorldFromBukkitWorld(location.getWorld());
        RegionManager regions = c.get(world);
        BlockVector3 position = getBlockVector3FromLocation(location);
        if (regions == null) {
            nullRegion.accept(null);
            return false;
        }
        ApplicableRegionSet set = regions.getApplicableRegions(position);
        final LocalPlayer lp = getLocalPlayerFromBukkitPlayer(player);
        return set.testState(lp, flag);
    }
}
