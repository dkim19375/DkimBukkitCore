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

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import me.dkim19375.dkimcore.annotation.API
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

fun URL.getJson(
    consumer: (JsonObject) -> Unit,
    exception: (IOException) -> Unit,
    plugin: JavaPlugin,
) {
    if (!Bukkit.isPrimaryThread()) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin) { getJson(consumer, exception, plugin) }
        return
    }
    val string: String =
        try {
            readText()
        } catch (e: IOException) {
            exception(e)
            return
        }
    consumer(string.toJsonObject())
}

@API
fun String.getJson(
    consumer: (JsonObject) -> Unit,
    exception: (IOException) -> Unit,
    plugin: JavaPlugin,
) {
    try {
        URL(this).getJson(consumer, exception, plugin)
    } catch (e: MalformedURLException) {
        e.printStackTrace()
    }
}

@API
fun String.toJsonObject(): JsonObject {
    val parse = JsonParser()
    val json = parse.parse(this)
    return json.asJsonObject
}
