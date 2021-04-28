package me.dkim19375.dkim19375core.function

import me.clip.placeholderapi.PlaceholderAPI
import me.dkim19375.dkim19375core.annotation.API
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import java.util.*

@API
fun String.toUUID(): UUID? {
    try {
        return UUID.fromString(this)
    } catch (ignored: IllegalArgumentException) {
    }
    try {
        val uuid1: String = substring(0, 8)
        val uuid2: String = substring(8, 12)
        val uuid3: String = substring(12, 16)
        val uuid4: String = substring(16, 20)
        val uuid5: String = substring(20)
        return UUID.fromString("$uuid1-$uuid2-$uuid3-$uuid4-$uuid5")
    } catch (_: IndexOutOfBoundsException) {
    } catch (_: IllegalArgumentException) {
    }
    return null
}

@API
fun String.color(altColorChar: Char = '&') = ChatColor.translateAlternateColorCodes(altColorChar, this)

@API
fun String.applyPAPI(player: OfflinePlayer?) = PlaceholderAPI.setPlaceholders(player, this)

@API
fun String.formatAll(player: OfflinePlayer? = null, altColorChar: Char = '&') =
    color(altColorChar)
        .applyPAPI(player)
        .replace("%newline%", "\n")