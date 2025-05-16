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

package me.dkim19375.dkimbukkitcore.data

import me.dkim19375.dkimcore.annotation.API
import org.bukkit.ChatColor

data class HelpMessageFormat(
    val topBar: String? = null,
    val header: String? = "${ChatColor.YELLOW}%name% ${ChatColor.GRAY}v%version%",
    val helpPageHeader: String? = "${ChatColor.GRAY} Help Page: %page%/%maxpages%",
    val command: String? =
        "${ChatColor.YELLOW}/%label% ${ChatColor.GOLD}%arg% ${ChatColor.DARK_GRAY}- ${ChatColor.GRAY}%description%",
    val error: String? = "${ChatColor.RED}%error%",
    val bottomBar: String? = null,
) {
    companion object {
        @API
        val MiniMessageDefault =
            HelpMessageFormat(
                topBar = null,
                header = "<yellow>%name% <gray>v%version%",
                helpPageHeader = "<gray> Help Page: %page%/%maxpages%",
                command = "<yellow>/%label% <gold>%arg% <dark_gray>- <gray>%description%",
                error = "<red>%error%",
                bottomBar = null,
            )
    }
}
