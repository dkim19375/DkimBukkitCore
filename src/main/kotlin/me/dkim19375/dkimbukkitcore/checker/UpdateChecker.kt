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

package me.dkim19375.dkimbukkitcore.checker

import com.google.gson.JsonObject
import java.io.IOException
import java.net.URL
import me.dkim19375.dkimbukkitcore.function.getJson
import me.dkim19375.dkimcore.annotation.API
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

@API
class UpdateChecker(
    private val resourceId: String? = null,
    // Example URL: https://api.github.com/repos/dkim19375/DkimBukkitCore/releases/latest
    private val url: URL? = null,
    private val plugin: JavaPlugin,
) {

    @API
    fun getSpigotVersion(version: (String) -> Unit, exception: (IOException) -> Unit) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin) {
            try {
                version(
                    URL("https://api.spigotmc.org/legacy/update.php?resource=$resourceId")
                        .readText()
                )
            } catch (e: IOException) {
                exception(e)
            }
        }
    }

    @API
    fun getFromRaw(version: (String) -> Unit, exception: (IOException) -> Unit) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin) {
            try {
                url?.readText()?.let(version)
            } catch (e: IOException) {
                exception(e)
            }
        }
    }

    @API
    fun getFromGithubJson(version: (String) -> Unit, exception: (IOException) -> Unit) {
        val url = url ?: throw IllegalStateException()
        url.getJson(
            { jsonObject: JsonObject ->
                val element = jsonObject["tag_name"]
                val tag = element.asString
                version(tag)
            },
            exception,
            plugin,
        )
    }
}
