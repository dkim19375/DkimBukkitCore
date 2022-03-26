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

import kotlinx.coroutines.CoroutineScope
import me.dkim19375.dkimbukkitcore.coroutine.BukkitConsumer
import me.dkim19375.dkimbukkitcore.javaplugin.CoreJavaPlugin
import me.dkim19375.dkimcore.annotation.API
import me.dkim19375.dkimcore.async.ActionConsumer
import me.dkim19375.dkimcore.async.CoroutineConsumer
import me.dkim19375.dkimcore.async.ExecutorsConsumer
import me.dkim19375.dkimcore.async.SyncConsumer
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.Contract
import java.util.concurrent.TimeUnit

private val plugin: CoreJavaPlugin by lazy { JavaPlugin.getPlugin(CoreJavaPlugin::class.java) }

@API
fun runSync(
    timeout: Long = 0,
    unit: TimeUnit = TimeUnit.MILLISECONDS,
    task: () -> Unit
) = getSyncAction(task).let {
    if (timeout <= 0) {
        it.queue()
    } else {
        it.queueWithSafeTimeout(timeout, unit)
    }
}

@API
suspend fun <T> runSyncAwait(
    timeout: Long = 0,
    unit: TimeUnit = TimeUnit.MILLISECONDS,
    task: () -> T
): T? = getSyncAction(task).let {
    if (timeout <= 0) {
        it.await()
    } else {
        it.awaitWithSafeTimeout(timeout, unit)
    }
}

@API
fun <T> runSyncBlocking(task: () -> T): T = getSyncAction(task).complete()

@API
@Contract(pure = true)
fun <T> getSyncAction(task: () -> T): ActionConsumer<T> = BukkitConsumer(plugin, false, task)

@API
fun runAsync(
    bukkit: Boolean = false,
    scope: CoroutineScope? = null,
    task: () -> Unit,
) = getAsyncAction(bukkit, scope, task).queue()

@API
fun runAsyncBukkit(task: () -> Unit) {
    getAsyncActionBukkit(task).queue()
}

@API
@Contract(pure = true)
fun <T> getAsyncActionBukkit(task: () -> T): ActionConsumer<T> = BukkitConsumer(plugin, true, task)

@API
@Contract(pure = true)
fun <T> getAsyncAction(
    bukkit: Boolean = false,
    scope: CoroutineScope? = null,
    task: () -> T,
): ActionConsumer<T> {
    if (!Bukkit.isPrimaryThread() && bukkit) {
        return SyncConsumer(task)
    }
    return if (scope != null) {
        CoroutineConsumer(scope, task)
    } else {
        ExecutorsConsumer(task = task)
    }
}