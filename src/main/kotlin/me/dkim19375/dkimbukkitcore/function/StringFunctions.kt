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
import me.dkim19375.dkimcore.annotation.API
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