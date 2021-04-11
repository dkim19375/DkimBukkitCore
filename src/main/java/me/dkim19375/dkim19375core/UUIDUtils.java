package me.dkim19375.dkim19375core;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class UUIDUtils {
    @Nullable
    public static UUID getFromString(String name) {
        try {
            return UUID.fromString(name);
        } catch (IllegalArgumentException ignored) {}
        try {
            String uuid1 = name.substring(0, 8);
            String uuid2 = name.substring(8, 12);
            String uuid3 = name.substring(12, 16);
            String uuid4 = name.substring(16, 20);
            String uuid5 = name.substring(20);
            return UUID.fromString(uuid1 + "-" + uuid2 + "-" + uuid3 + "-" + uuid4 + "-" + uuid5);
        } catch (IndexOutOfBoundsException ignored) {}
        return null;
    }
}
