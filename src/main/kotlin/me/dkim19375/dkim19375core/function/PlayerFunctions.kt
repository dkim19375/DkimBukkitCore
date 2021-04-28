package me.dkim19375.dkim19375core.function

import me.dkim19375.dkim19375core.annotation.API
import me.dkim19375.dkim19375core.data.HelpMessage
import me.dkim19375.dkim19375core.javaplugin.CoreJavaPlugin
import org.bukkit.Bukkit
import org.bukkit.ChatColor
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

@API
/**
 * @param label The label of the command ran
 * @param error An error, if any
 * @param page The page of the help page
 * @param commands The commands to show
 */
fun Player.showHelpMessage(label: String, error: String? = null, page: Int = 1, commands: List<HelpMessage>, plugin: CoreJavaPlugin) {
    sendMessage("${ChatColor.DARK_BLUE}------------------------------------------------")
    sendMessage(
        "${ChatColor.GREEN}Bedwars v${plugin.description.version} " +
                "Help Page: $page/${getMaxHelpPages(commands)}  <> = required  [] = optional"
    )
    val newCommands = commands.filter { msg -> hasPermission(msg.permission) }
    for (i in ((page - 1) * 7) until page * 7) {
        val cmd = newCommands.getOrNull(i) ?: continue
        sendHelpMsgFormatted(label, cmd)
    }
    error?.let {
        sendMessage("${ChatColor.RED}$it")
    }
    sendMessage("${ChatColor.DARK_BLUE}------------------------------------------------")
}

@API
fun Permissible.getMaxHelpPages(commands: List<HelpMessage>): Int {
    val newCommands = commands.filter { msg -> hasPermission(msg.permission) }
    return ceil(newCommands.size.toDouble() / 7.0).toInt()
}

private fun CommandSender.sendHelpMsgFormatted(label: String, message: HelpMessage) {
    if (!hasPermission(message.permission)) {
        return
    }
    sendMessage("${ChatColor.AQUA}/$label ${message.arg} - ${ChatColor.GOLD}${message.description}")
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
fun Set<UUID>.getPlayers(): Set<Player> = map(Bukkit::getPlayer).filterNonNull().toSet()