package io.github.AKtomik.freeFireworks;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
public class CompleterFirework implements TabCompleter {

    @Override
    public @NotNull List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String alias,
            @NotNull String @NonNull [] args
    ) {
        if (!sender.hasPermission("freefireworks.launch.self")) return List.of();

        // init
        List<String> completions = new ArrayList<>();
        boolean isEffect = false;
        boolean isWrong = false;
        int countColor = 0;

        // arg compute
        for (int i = 0; i < Math.min(args.length, 3); i++) {
            String arg = args[i];
            if (CommandFirework.fireworkEffects.containsKey(arg)) {
                isEffect = true;
            } else if (CommandFirework.fireworkColors.containsKey(arg)) {
                countColor += 1;
            } else if (i != args.length - 1) {
                isWrong = true;
            }
        }

        // list
        if (!isEffect)
        {
            completions.addAll(CommandFirework.fireworkEffects.keySet().stream().toList());
        }
        if (countColor <= 1)
        {
            completions.addAll(CommandFirework.fireworkColors.keySet().stream().toList());
        }

        // filter
        if (!completions.isEmpty() && args.length > 0 && !args[args.length - 1].isEmpty()) {
            String input = args[args.length - 1];
            completions = completions.stream().filter(s -> s.contains(input)).toList();
        }

        // return
        return completions;
    }
}

