package me.dkim19375.dkim19375core;

import org.bukkit.Location;

public class Region {
    private final Location pos1;
    private final Location pos2;

    public Region(Location pos1, Location pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        if (pos1.getWorld() != pos2.getWorld()) throw new IllegalArgumentException();
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public boolean contains(Location position) {
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
        return "{\"pos1\":" + pos1 + ", \"pos2\":" + pos2 + "}";
    }
}