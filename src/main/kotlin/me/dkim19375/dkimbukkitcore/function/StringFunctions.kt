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

import me.clip.placeholderapi.PlaceholderAPI
import me.dkim19375.dkimbukkitcore.javaplugin.CoreJavaPlugin
import me.dkim19375.dkimcore.annotation.API
import me.dkim19375.dkimcore.extension.runCatchingOrNull
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import java.util.logging.Level

private val hasPAPI by lazy { Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI") }

@API
fun String.color(altColorChar: Char = '&'): String = ChatColor.translateAlternateColorCodes(altColorChar, this)

@API
fun String.applyPAPI(player: OfflinePlayer?): String {
    if (!hasPAPI) {
        return this
    }
    return PlaceholderAPI.setPlaceholders(player, this)
}

@API
fun String.formatAll(player: OfflinePlayer? = null, altColorChar: Char = '&') =
    applyPAPI(player)
        .color(altColorChar)
        .formatLegacyHex()
        .replace("%newline%", "\n")

private lateinit var storedPlugin: CoreJavaPlugin
fun setLoggingPlugin(plugin: CoreJavaPlugin) {
    storedPlugin = plugin
}

@API
fun logInfo(text: String, level: Level = Level.INFO) = storedPlugin.logger.log(level, text)

@API
fun String.log(level: Level = Level.INFO) = storedPlugin.logger.log(level, this)

@API
fun Set<String>.formatAndRemoveColor(player: OfflinePlayer? = null): Set<String> = map {
    it.formatAndRemoveColor(player)
}.toSet()

@API
fun String.formatAndRemoveColor(player: OfflinePlayer? = null): String = ChatColor.stripColor(formatAll(player)) ?: ""

private val hexRegex = "(#[A-f\\d]{6})".toRegex()
private val chatColorOfMethod by lazy {
    runCatchingOrNull {
        net.md_5.bungee.api.ChatColor::class.java.getMethod("of", String::class.java)
    }
}

@API
fun String.formatLegacyHex(): String = chatColorOfMethod?.let { method ->
    hexRegex.replace(this) { result ->
        method.invoke(null, result.value).toString()
    }
} ?: this