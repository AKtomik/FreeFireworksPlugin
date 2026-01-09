package io.github.AKtomik.freeFireworks;

import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public final class FreeFireworks extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Enabled!");

        PluginCommand commandSelf = getCommand("firework");
        if (commandSelf == null) throw new IllegalStateException("§ccommand 'firework' not registered in yml");
        commandSelf.setExecutor(new CommandFirework());
        commandSelf.setTabCompleter(new CompleterFirework());

        PluginCommand commandOther = getCommand("fireplayer");
        if (commandOther == null) throw new IllegalStateException("command 'fireplayer' not registered in yml");
        commandOther.setExecutor(new CommandFireplayer());
        commandOther.setTabCompleter(new CompleterFireplayer());
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
