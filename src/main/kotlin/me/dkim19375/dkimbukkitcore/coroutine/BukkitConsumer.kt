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

package me.dkim19375.dkimbukkitcore.coroutine

import me.dkim19375.dkimcore.async.*
import me.dkim19375.dkimcore.extension.getResult
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.util.concurrent.*
import kotlin.coroutines.Continuation

class BukkitConsumer<T>(
    private val plugin: Plugin,
    private val async: Boolean = true,
    task: () -> T,
) : ActionConsumer<T>(task) {
    override fun queue(
        success: ((T) -> Unit),
        failure: ((Throwable) -> Unit),
    ) {
        val runnable = {
            try {
                success(task())
            } catch (error: Throwable) {
                failure(error)
            }
        }
        if (async) {
            if (Bukkit.isPrimaryThread()) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable)
                return
            }
            runnable()
            return
        }
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(plugin, runnable)
            return
        }
        runnable()
    }

    @Deprecated(
        message = "Use a different type of ActionConsumer as the task in BukkitConsumer!",
        replaceWith = ReplaceWith("queue(success, failure)")
    )
    override fun queueWithTimeout(
        timeout: Long,
        unit: TimeUnit,
        success: (T) -> Unit,
        failure: (Throwable) -> Unit,
    ) = throw UnsupportedOperationException("Use a different type of ActionConsumer as the task in BukkitConsumer!")

    @Deprecated(
        message = "Use a different type of ActionConsumer as the task in BukkitConsumer!",
        replaceWith = ReplaceWith("queue(success, failure)")
    )
    override fun queueWithSafeTimeout(
        timeout: Long,
        unit: TimeUnit,
        success: (T?) -> Unit,
        failure: (Throwable) -> Unit,
    ) = throw UnsupportedOperationException("Use a different type of ActionConsumer as the task in BukkitConsumer!")

    @Deprecated(
        message = "Use a different type of ActionConsumer as the task in BukkitConsumer!",
        replaceWith = ReplaceWith("await(failure)")
    )
    override suspend fun awaitWithTimeout(
        timeout: Long,
        unit: TimeUnit,
        failure: (Continuation<T>, Throwable) -> Unit,
    ): T = throw UnsupportedOperationException("Use a different type of ActionConsumer as the task in BukkitConsumer!")

    @Deprecated(
        message = "Use a different type of ActionConsumer as the task in BukkitConsumer!",
        replaceWith = ReplaceWith("await(failure)")
    )
    override suspend fun awaitWithSafeTimeout(
        timeout: Long,
        unit: TimeUnit,
        failure: (Continuation<T>, Throwable) -> Unit,
    ): T = throw UnsupportedOperationException("Use a different type of ActionConsumer as the task in BukkitConsumer!")

    override fun complete(): T = submit().getResult().getOrThrow()

    @Deprecated(
        message = "Use a different type of ActionConsumer as the task in BukkitConsumer!",
        replaceWith = ReplaceWith("complete()")
    )
    override fun completeWithTimeout(timeout: Long, unit: TimeUnit): T =
        throw UnsupportedOperationException("Use a different type of ActionConsumer as the task in BukkitConsumer!")

    @Deprecated(
        message = "Use a different type of ActionConsumer as the task in BukkitConsumer!",
        replaceWith = ReplaceWith("complete()")
    )
    override fun completeWithSafeTimeout(timeout: Long, unit: TimeUnit): T =
        throw UnsupportedOperationException("Use a different type of ActionConsumer as the task in BukkitConsumer!")

    override fun submit(): CompletableFuture<T> {
        val future = CompletableFuture<T>()
        queue(
            success = {
                future.complete(it)
            },
            failure = {
                future.completeExceptionally(it)
            }
        )
        return future
    }
}