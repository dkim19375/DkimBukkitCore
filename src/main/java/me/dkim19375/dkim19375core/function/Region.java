package me.dkim19375.dkim19375core.function;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Region implements ConfigurationSerializable, Cloneable {
    @NotNull
    private final World world;
    @NotNull
    private final Location pos1;
    @NotNull
    private final Location pos2;

    public Region(@NotNull final World world, @NotNull final Location pos1, @NotNull final Location pos2) {
        this.world = world;
        this.pos1 = pos1;
        this.pos2 = pos2;
        if (pos1.getWorld() != pos2.getWorld()) throw new IllegalArgumentException();
    }

    @NotNull
    public Location getPos1() {
        return pos1;
    }

    @NotNull
    public Location getPos2() {
        return pos2;
    }

    public boolean contains(@NotNull Location position) {
        if (position.getWorld() == null) {
            return false;
        }
        if (!world.getUID().equals(position.getWorld().getUID())) {
            return false;
        }
        final double x = position.getX();
        final double y = position.getY();
        final double z = position.getZ();
        final double x1 = pos1.getX();
        final double y1 = pos1.getY();
        final double z1 = pos1.getZ();
        final double x2 = pos2.getX();
        final double y2 = pos2.getY();
        final double z2 = pos2.getZ();
        return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
    }

    @Override
    public String toString() {
        return "Region{" +
                "world=" + world +
                ", pos1=" + pos1 +
                ", pos2=" + pos2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return world.equals(region.world) && pos1.equals(region.pos1) && pos2.equals(region.pos2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, pos1, pos2);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<>();
        map.put("world", world.getName());
        map.put("pos1", pos1);
        map.put("pos2", pos2);
        return map;
    }

    @Nullable
    public static Region deserialize(@NotNull final Map<String, Object> map) {
        try {
            final World world = Bukkit.getWorld((String) map.get("world"));
            Validate.notNull(world);
            final Location pos1 = (Location) map.get("pos1");
            Validate.notNull(pos1);
            final Location pos2 = (Location) map.get("pos2");
            Validate.notNull(pos2);
            return new Region(world, pos1, pos2);
        } catch (Exception ignored) {
            return null;
        }
    }
}