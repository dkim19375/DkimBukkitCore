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

import me.dkim19375.dkimbukkitcore.data.HelpMessage
import me.dkim19375.dkimbukkitcore.data.HelpMessageFormat
import me.dkim19375.dkimbukkitcore.javaplugin.CoreJavaPlugin
import me.dkim19375.dkimcore.annotation.API
import me.dkim19375.dkimcore.extension.toUUID
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.Permissible
import java.util.*
import kotlin.math.ceil

@API
fun String.toPlayer(): Player? {
    val usernamePlayer = Bukkit.getPlayer(this)
    if (usernamePlayer != null) {
        return usernamePlayer
    }
    val uuid = toUUID() ?: return null
    return Bukkit.getPlayer(uuid)
}

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
) {
    val replaceMap: Map<String, String> = mapOf(
        "name" to plugin.description.name,
        "version" to plugin.description.version,
        "page" to page.toString(),
        "maxpages" to getMaxHelpPages(commands).toString(),
        "label" to label,
        "error" to (error ?: "")
    )
    sendMessageReplaced(format.topBar, replaceMap)
    sendMessageReplaced(format.header, replaceMap)
    val newCommands = commands.filter { msg -> msg.permission?.let { hasPermission(it) } != false }
    for (i in ((page - 1) * 7) until page * 7) {
        val cmd = newCommands.getOrNull(i) ?: continue
        sendHelpMsgFormatted(cmd, format, replaceMap)
    }
    error?.let {
        sendMessageReplaced(format.error, replaceMap)
    }
    sendMessageReplaced(format.bottomBar, replaceMap)
}

fun CommandSender.sendMessageReplaced(str: String?, replaceMap: Map<String, String>) {
    var new = str ?: return
    sendMessage(str.let { _ ->
        replaceMap.forEach { new = new.replace("%${it.key}%", it.value) }
        new
    })
}

@API
fun Permissible.getMaxHelpPages(commands: List<HelpMessage>): Int {
    val newCommands = commands.filter { msg -> msg.permission?.let { hasPermission(it) } != false }
    return ceil(newCommands.size.toDouble() / 7.0).toInt()
}

fun CommandSender.sendHelpMsgFormatted(
    message: HelpMessage,
    format: HelpMessageFormat,
    currentMap: Map<String, String>,
) {
    if (message.permission?.let { hasPermission(it) } == false) {
        return
    }
    val replaceMap = currentMap.plus(
        mapOf(
            "arg" to message.arg,
            "description" to message.description
        )
    )
    sendMessageReplaced(format.command, replaceMap)
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
fun Player.playSound(sound: Sound, volume: Float = 0.7f, pitch: Float = 1.0f) {
    playSound(location, sound, volume, pitch)
}

@API
fun Iterable<UUID>.getPlayers(): Set<Player> = map(Bukkit::getPlayer).filterNonNull().toSet()