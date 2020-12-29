package me.dkim19375.dkim19375core;

import org.bukkit.command.CommandSender;

public class TestClass {
    public static void testPrint() {
        System.out.println("TESTING");
    }
    public static void testPrint(CommandSender sender) {
        sender.sendMessage("TESTING");
    }
}
