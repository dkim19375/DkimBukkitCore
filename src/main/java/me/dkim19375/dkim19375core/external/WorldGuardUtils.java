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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldGuardUtils {

    @NotNull
    public static LocalPlayer getLocalPlayerFromBukkitPlayer(final @NotNull Player player) {
        return WorldGuardPlugin.inst().wrapPlayer(player);
    }

    @NotNull
    public static BlockVector3 getBlockVector3FromLocation(final @NotNull Location location) {
        return BlockVector3.at(location.getX(), location.getY(), location.getZ());
    }

    @NotNull
    public static BlockVector3 getBlockVector3FromLocation(final @NotNull com.sk89q.worldedit.util.Location location) {
        return BlockVector3.at(location.getX(), location.getY(), location.getZ());
    }

    @NotNull
    public static World getWorldGuardWorldFromBukkitWorld(final @NotNull org.bukkit.World world) {
        return BukkitAdapter.adapt(world);
    }

    /**
     * @deprecated Use {@link WorldGuardUtils#testStateOfFlag(Player, StateFlag, Runnable)}
     */
    public static boolean testStateOfFlag(final @NotNull Player player, final @NotNull StateFlag flag, final @NotNull Consumer<RegionManager> nullRegion) {
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

    public static boolean testStateOfFlag(final @NotNull Player player, final @NotNull StateFlag flag, final @NotNull Runnable nullRegion) {
        RegionContainer c = WorldGuard.getInstance().getPlatform().getRegionContainer();
        World world = getWorldGuardWorldFromBukkitWorld(player.getWorld());
        RegionManager regions = c.get(world);
        BlockVector3 position = getBlockVector3FromLocation(player.getLocation());
        if (regions == null) {
            nullRegion.run();
            return false;
        }
        ApplicableRegionSet set = regions.getApplicableRegions(position);
        final LocalPlayer lp = getLocalPlayerFromBukkitPlayer(player);
        return set.testState(lp, flag);
    }

    /**
     * @deprecated Use {@link WorldGuardUtils#testStateOfFlag(Player, Location, StateFlag, Runnable)}
     */
    public static boolean testStateOfFlag(final @NotNull Player player, final @NotNull Location location, final @NotNull StateFlag flag, final @NotNull Consumer<RegionManager> nullRegion) {
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

    public static boolean testStateOfFlag(final @NotNull Player player, final @NotNull Location location, final @NotNull StateFlag flag, final @NotNull Runnable nullRegion) {
        RegionContainer c = WorldGuard.getInstance().getPlatform().getRegionContainer();
        World world = getWorldGuardWorldFromBukkitWorld(location.getWorld());
        RegionManager regions = c.get(world);
        BlockVector3 position = getBlockVector3FromLocation(location);
        if (regions == null) {
            nullRegion.run();
            return false;
        }
        ApplicableRegionSet set = regions.getApplicableRegions(position);
        final LocalPlayer lp = getLocalPlayerFromBukkitPlayer(player);
        return set.testState(lp, flag);
    }
}
