package me.dkim19375.dkim19375core.checker

import com.google.gson.JsonObject
import me.dkim19375.dkim19375core.annotation.API
import me.dkim19375.dkim19375core.function.getJson
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Consumer
import java.io.IOException
import java.net.URL
import java.util.*

@API
class UpdateChecker(
    private val resourceId: String? = null,
    // Example URL: https://api.github.com/repos/dkim19375/dkim19375Core/releases/latest
    private val url: URL? = null,
    private val plugin: JavaPlugin
) {

    @API
    fun getSpigotVersion(version: (String) -> Unit, exception: (IOException) -> Unit) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            try {
                URL("https://api.spigotmc.org/legacy/update.php?resource=$resourceId").openStream()
                    .use { inputStream ->
                        Scanner(inputStream).use { scanner ->
                            if (scanner.hasNext()) {
                                version(scanner.next())
                            }
                        }
                    }
            } catch (e: IOException) {
                exception(e)
            }
        })
    }

    @API
    fun getFromRaw(version: Consumer<String>, exception: (IOException) -> Unit) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            if (url != null) {
                try {
                    url.openStream().use { inputStream ->
                        Scanner(inputStream).use { scanner ->
                            if (scanner.hasNext()) {
                                version.accept(scanner.next())
                            }
                        }
                    }
                } catch (e: IOException) {
                    exception(e)
                }
            }
        })
    }

    @API
    fun getFromGithubJson(version: Consumer<String>, exception: (IOException) -> Unit) {
        val url = url ?: throw IllegalStateException()
        url.getJson({ j: JsonObject ->
            val element = j["tag_name"]
            val tag = element.asString
            version.accept(tag)
        }, exception, plugin)
    }
}