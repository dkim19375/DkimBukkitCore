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

import me.dkim19375.dkimcore.annotation.API
import me.dkim19375.dkimcore.extension.setDecimalPlaces
import me.dkim19375.dkimcore.extension.setPlaceholders
import org.bukkit.Location

@API
fun Location.format(format: String = "world: %world%, %x%, %y%, %z%", decimalPlaces: Int? = 2): String {
    val world = world?.name ?: "none"
    val x = x.setDecimalPlaces(decimalPlaces).toString()
    val y = y.setDecimalPlaces(decimalPlaces).toString()
    val z = z.setDecimalPlaces(decimalPlaces).toString()
    val yaw = yaw.setDecimalPlaces(decimalPlaces).toString()
    val pitch = pitch.setDecimalPlaces(decimalPlaces).toString()
    if (format == "world: %world%, %x%, %y%, %z%") {
        return "world: $world, $x, $y, $z"
    }
    return format.setPlaceholders(mapOf(
        "world" to world,
        "x" to x,
        "y" to y,
        "z" to z,
        "yaw" to yaw,
        "pitch" to pitch
    ))
}