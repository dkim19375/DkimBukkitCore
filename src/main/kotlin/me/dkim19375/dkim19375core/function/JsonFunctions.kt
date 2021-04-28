package me.dkim19375.dkim19375core.function

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.dkim19375.dkim19375core.annotation.API
import me.dkim19375.dkim19375core.function.getJson
import me.dkim19375.dkim19375core.function.toJsonObject
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL


fun URL.getJson(
    consumer: (JsonObject) -> Unit, exception: (IOException) -> Unit, plugin: JavaPlugin
) {
    val runnable = Runnable {
        @Suppress("UnstableApiUsage")
        val string: String = try {
            readText()
        } catch (e: IOException) {
            exception(e)
            return@Runnable
        }
        consumer(string.toJsonObject())
    }
    if (Bukkit.isPrimaryThread()) {
        runnable.run()
        return
    }
    Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable)
}

@API
fun String.getJson(
    consumer: (JsonObject) -> Unit, exception: (IOException) -> Unit, plugin: JavaPlugin
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