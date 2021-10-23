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
import me.dkim19375.dkimbukkitcore.coroutine.BukkitActionConsumer
import me.dkim19375.dkimbukkitcore.javaplugin.CoreJavaPlugin
import me.dkim19375.dkimcore.annotation.API
import me.dkim19375.dkimcore.coroutine.ActionConsumer
import me.dkim19375.dkimcore.extension.IO_SCOPE
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.Contract

private val plugin: CoreJavaPlugin by lazy { JavaPlugin.getPlugin(CoreJavaPlugin::class.java) }

@API
fun runSync(task: () -> Unit) = runSyncAction(task).queue()

@API
fun <T> runSyncBlocking(task: () -> T): T = runSyncAction(task).complete()

@API
@Contract(pure = true)
fun <T> runSyncAction(task: () -> T): ActionConsumer<T> = BukkitActionConsumer(plugin, false, task)

@API
fun runAsync(
    bukkit: Boolean = false,
    scope: CoroutineScope = IO_SCOPE,
    task: () -> Unit,
) = runAsyncAction(bukkit, scope, task).queue()

@API
fun runAsyncBukkit(task: () -> Unit) {
    runAsyncActionBukkit(task).queue()
}

@API
@Contract(pure = true)
fun <T> runAsyncActionBukkit(task: () -> T): ActionConsumer<T> = BukkitActionConsumer(plugin, true, task)

@API
fun <T> runAsyncAction(
    bukkit: Boolean = false,
    scope: CoroutineScope = IO_SCOPE,
    task: () -> T,
): ActionConsumer<T> {
    if (!Bukkit.isPrimaryThread() && bukkit) {
        return ActionConsumer(null) {
            task()
        }
    }
    return ActionConsumer(scope, task)
}