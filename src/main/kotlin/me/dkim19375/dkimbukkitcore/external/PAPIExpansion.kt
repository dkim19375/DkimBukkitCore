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

package me.dkim19375.dkimbukkitcore.external

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.dkim19375.dkimbukkitcore.javaplugin.CoreJavaPlugin
import me.dkim19375.dkimcore.annotation.API

@API
abstract class PAPIExpansion(
    private val plugin: CoreJavaPlugin,
    private val identifier: String = plugin.name,
    private val authors: String = plugin.description.authors.joinToString(", "),
    private val version: String = plugin.description.version
) : PlaceholderExpansion() {

    override fun persist(): Boolean = true

    override fun canRegister(): Boolean = true

    override fun getAuthor(): String = authors

    override fun getIdentifier(): String = identifier

    override fun getVersion(): String = version
}