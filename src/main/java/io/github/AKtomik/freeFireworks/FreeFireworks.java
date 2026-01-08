package io.github.AKtomik.freeFireworks;

import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public final class FreeFireworks extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Enabled!");
        getCommand("firework").setExecutor(new CommandFirework());
        getCommand("firework").setTabCompleter(new CompleterFirework());
    }

    public void onReload() {
        getLogger().info("Reload!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Disabled!");
    }
}
