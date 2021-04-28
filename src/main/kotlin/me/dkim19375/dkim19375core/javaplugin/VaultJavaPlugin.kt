package me.dkim19375.dkim19375core.javaplugin

import me.dkim19375.dkim19375core.annotation.API
import net.milkbowl.vault.chat.Chat
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.permission.Permission

@API
abstract class VaultJavaPlugin : CoreJavaPlugin() {
    @API
    lateinit var econ: Economy
    @API
    lateinit var permissions: Permission
    @API
    lateinit var chat: Chat

    override fun onEnable() {
        setupEconomy()
        setupPermissions()
        setupChat()
    }

    abstract override fun onDisable()

    @API
    protected fun setupEconomy(): Boolean {
        if (server.pluginManager.getPlugin("Vault") == null) {
            return false
        }
        val rsp = server.servicesManager.getRegistration(
            Economy::class.java
        ) ?: return false
        econ = rsp.provider
        return this::econ.isInitialized
    }

    @API
    protected fun setupChat(): Boolean {
        if (server.pluginManager.getPlugin("Vault") == null) {
            return false
        }
        val rsp = server.servicesManager.getRegistration(
            Chat::class.java
        ) ?: return false
        chat = rsp.provider
        return this::chat.isInitialized
    }

    @API
    protected fun setupPermissions(): Boolean {
        if (server.pluginManager.getPlugin("Vault") == null) {
            return false
        }
        val rsp = server.servicesManager.getRegistration(
            Permission::class.java
        ) ?: return false
        permissions = rsp.provider
        return this::permissions.isInitialized
    }
}