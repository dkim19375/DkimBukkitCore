package me.dkim19375.dkim19375core.external

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.dkim19375.dkim19375core.annotation.API
import me.dkim19375.dkim19375core.javaplugin.CoreJavaPlugin

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