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

import me.dkim19375.dkimbukkitcore.javaplugin.CoreJavaPlugin
import me.dkim19375.dkimcore.annotation.API
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.atomic.AtomicReference

private val plugin: CoreJavaPlugin by lazy { JavaPlugin.getPlugin(CoreJavaPlugin::class.java) }

@API
fun runSync(task: () -> Unit) {
    if (Bukkit.isPrimaryThread()) {
        task()
        return
    }
    Bukkit.getScheduler().runTask(plugin, task)
}

@API
fun <T> runSyncBlocking(task: () -> T): T {
    if (Bukkit.isPrimaryThread()) {
        return task()
    }
    val atomic = AtomicReference<T>()
    Bukkit.getScheduler().runTask(plugin) { ->
        atomic.set(task())
    }
    while (true) {
        return atomic.get() ?: continue
    }
}

@API
fun runAsync(task: () -> Unit) {
    if (!Bukkit.isPrimaryThread()) {
        task()
        return
    }
    Bukkit.getScheduler().runTaskAsynchronously(plugin, task)
}

@API
fun <T> runAsyncBlocking(task: () -> T): T {
    if (!Bukkit.isPrimaryThread()) {
        return task()
    }
    val atomic = AtomicReference<T>()
    Bukkit.getScheduler().runTaskAsynchronously(plugin) { ->
        atomic.set(task())
    }
    while (true) {
        return atomic.get() ?: continue
    }
}