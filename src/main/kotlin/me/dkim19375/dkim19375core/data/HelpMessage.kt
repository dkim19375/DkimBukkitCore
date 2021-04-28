package me.dkim19375.dkim19375core.data

import me.dkim19375.dkim19375core.annotation.API

@API
data class HelpMessage(val arg: String, val description: String, val permission: String)