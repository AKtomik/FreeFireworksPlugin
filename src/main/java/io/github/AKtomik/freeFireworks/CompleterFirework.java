package io.github.AKtomik.freeFireworks;

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

        // init
        List<String> completions = new ArrayList<>();

        // list
        if (args.length == 1)
        {
            completions = CommandFirework.fireworkEffects.keySet().stream().toList();
        }
        else if (args.length <= 3)
        {
            completions = CommandFirework.fireworkColors.keySet().stream().toList();
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

