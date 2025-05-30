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

import java.lang.reflect.Method
import java.util.UUID
import kotlin.math.ceil
import me.dkim19375.dkimbukkitcore.data.HelpMessage
import me.dkim19375.dkimbukkitcore.data.HelpMessageFormat
import me.dkim19375.dkimbukkitcore.javaplugin.CoreJavaPlugin
import me.dkim19375.dkimcore.annotation.API
import me.dkim19375.dkimcore.extension.runCatchingOrNull
import me.dkim19375.dkimcore.extension.toUUID
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.Permissible

@API
fun String.toPlayer(): Player? {
    val usernamePlayer = Bukkit.getPlayerExact(this)
    if (usernamePlayer != null) {
        return usernamePlayer
    }
    val uuid = toUUID() ?: return null
    return Bukkit.getPlayer(uuid)
}

private val getOfflinePlayerIfCached: Method? by lazy {
    runCatchingOrNull {
        Bukkit::class.java.getMethod("getOfflinePlayerIfCached", String::class.java) // Paper only
    }
}

@API
fun String.toOfflinePlayer(): OfflinePlayer? =
    Bukkit.getPlayerExact(this)
        ?: toUUID()?.let(Bukkit::getOfflinePlayer)
        ?: (getOfflinePlayerIfCached?.invoke(null, this) as? OfflinePlayer)

/**
 * Show help message
 *
 * @param label The label of the command ran
 * @param error An error, if any
 * @param page The page of the help page
 * @param commands The commands to show
 * @param plugin The [CoreJavaPlugin] of this plugin
 * @param format The [HelpMessageFormat] that should be sent
 */
@API
fun CommandSender.showHelpMessage(
    label: String,
    error: String? = null,
    page: Int = 1,
    commands: List<HelpMessage>,
    plugin: CoreJavaPlugin,
    format: HelpMessageFormat = HelpMessageFormat(),
    maxPerPage: Int = 7,
    sendMessage: (String) -> Unit = this@showHelpMessage::sendMessage,
) {
    val maxPages = getMaxHelpPages(commands, maxPerPage)
    val replaceMap: Map<String, String> =
        mapOf(
            "name" to plugin.description.name,
            "version" to plugin.description.version,
            "page" to page.toString(),
            "maxpages" to maxPages.toString(),
            "label" to label,
            "error" to (error ?: ""),
        )
    sendMessageReplaced(format.topBar, replaceMap, sendMessage)
    val header = format.header
    if (header != null) {
        val helpPageHeader = if (maxPages <= 1) "" else format.helpPageHeader ?: ""
        sendMessageReplaced(header + helpPageHeader, replaceMap, sendMessage)
    }
    val newCommands = commands.filter { msg -> msg.permission?.let { hasPermission(it) } != false }
    for (i in ((page - 1) * maxPerPage) until page * maxPerPage) {
        val cmd = newCommands.getOrNull(i) ?: continue
        sendHelpMsgFormatted(cmd, format, replaceMap, sendMessage)
    }
    error?.let { sendMessageReplaced(format.error, replaceMap, sendMessage) }
    sendMessageReplaced(format.bottomBar, replaceMap, sendMessage)
}

fun CommandSender.sendMessageReplaced(
    str: String?,
    replaceMap: Map<String, String>,
    sendMessage: (String) -> Unit = this::sendMessage,
) {
    var new = str ?: return
    sendMessage(
        str.let { _ ->
            replaceMap.forEach { new = new.replace("%${it.key}%", it.value) }
            new
        }
    )
}

@API
fun Permissible.getMaxHelpPages(commands: List<HelpMessage>, maxPerPage: Int = 7): Int {
    val newCommands = commands.filter { msg -> msg.permission?.let { hasPermission(it) } != false }
    return ceil(newCommands.size.toDouble() / maxPerPage).toInt()
}

fun CommandSender.sendHelpMsgFormatted(
    message: HelpMessage,
    format: HelpMessageFormat,
    currentMap: Map<String, String>,
    sendMessage: (String) -> Unit = this::sendMessage,
) {
    if (message.permission?.let { hasPermission(it) } == false) {
        return
    }
    val replaceMap =
        currentMap.plus(mapOf("arg" to message.arg, "description" to message.description))
    sendMessageReplaced(format.command, replaceMap, sendMessage)
}

@API
fun Player.getItemAmount(type: Material): Int {
    var amount = 0
    val inv = inventory.contents.toList()
    for (item in inv) {
        item ?: continue
        if (item.type == type) {
            amount += item.amount
        }
    }
    return amount
}

@API
fun Player.playSound(sound: Sound, volume: Float = 0.7f, pitch: Float = 1.0f) =
    playSound(location, sound, volume, pitch)

@API fun Iterable<UUID>.getPlayers(): Set<Player> = mapNotNull(Bukkit::getPlayer).toSet()
