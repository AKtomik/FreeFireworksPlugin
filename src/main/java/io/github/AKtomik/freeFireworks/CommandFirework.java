package io.github.AKtomik.freeFireworks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandFirework implements CommandExecutor {

    public static Map<String, Color> fireworkColors = Map.ofEntries(
            Map.entry("white", DyeColor.WHITE.getFireworkColor()),
            Map.entry("light_gray", DyeColor.LIGHT_GRAY.getFireworkColor()),
            Map.entry("gray", DyeColor.GRAY.getFireworkColor()),
            Map.entry("black", DyeColor.BLACK.getFireworkColor()),
            Map.entry("brown", DyeColor.BROWN.getFireworkColor()),
            Map.entry("red", DyeColor.RED.getFireworkColor()),
            Map.entry("orange", DyeColor.ORANGE.getFireworkColor()),
            Map.entry("yellow", DyeColor.YELLOW.getFireworkColor()),
            Map.entry("lime", DyeColor.LIME.getFireworkColor()),
            Map.entry("green", DyeColor.GREEN.getFireworkColor()),
            Map.entry("cyan", DyeColor.CYAN.getFireworkColor()),
            Map.entry("light_blue", DyeColor.LIGHT_BLUE.getFireworkColor()),
            Map.entry("blue", DyeColor.BLUE.getFireworkColor()),
            Map.entry("purple", DyeColor.PURPLE.getFireworkColor()),
            Map.entry("magenta", DyeColor.MAGENTA.getFireworkColor()),
            Map.entry("pink", DyeColor.PINK.getFireworkColor())
    );

    public static Map<String, FireworkEffect.Type> fireworkEffects = Map.ofEntries(
            Map.entry("ball", FireworkEffect.Type.BALL),
            Map.entry("ball_large", FireworkEffect.Type.BALL_LARGE),
            Map.entry("star", FireworkEffect.Type.STAR),
            Map.entry("burst", FireworkEffect.Type.BURST),
            Map.entry("creeper", FireworkEffect.Type.CREEPER)
    );

    public static Map<FireworkEffect.Type, String> effectsTexts = Map.ofEntries(
            Map.entry(FireworkEffect.Type.BALL, "PFIOUU"),
            Map.entry(FireworkEffect.Type.BALL_LARGE, "PFIOUUUUUU"),
            Map.entry(FireworkEffect.Type.STAR, "PFWAA"),
            Map.entry(FireworkEffect.Type.BURST, "PFBOOM"),
            Map.entry(FireworkEffect.Type.CREEPER, "PFOWMAN")
    );


    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("only players can run this command!").color(NamedTextColor.RED));

            return true;
        }
        if (!player.hasPermission("freefireworks.launch")) {
            player.sendMessage(Component.text("you dont have permission to use this command!").color(NamedTextColor.RED));
            return true;
        }

        // pick
        Random rand = new Random();
        FireworkEffect.Type[] effectValues = fireworkEffects.values().toArray(new FireworkEffect.Type[0]);
        FireworkEffect.Type fireEffect = null;
        Color[] colorValues = fireworkColors.values().toArray(new Color[0]);
        Color colorFirst = null;
        Color colorFade = null;

        // arg compute
        for (int i = 0; i < Math.min(args.length, 3); i++) {
            String arg = args[i];
            if (fireworkEffects.containsKey(arg)) {
                fireEffect = fireworkEffects.get(arg);
            } else if (fireworkColors.containsKey(arg)) {
                if (colorFirst == null)
                    colorFirst = fireworkColors.get(arg);
                else
                    colorFade = fireworkColors.get(arg);
            } else {
                player.sendMessage(Component.text("§cthe effect/color §l%s§c does not exist.".formatted(arg)));
                return true;
            }
        }

        // default
        if (fireEffect == null)
        {
            //fireEffect = effectValues[rand.nextInt(effectValues.length)];
            fireEffect = FireworkEffect.Type.BALL;
        }

        if (colorFirst == null)
        {
            colorFade = colorValues[rand.nextInt(colorValues.length)];
            colorFirst = colorValues[rand.nextInt(colorValues.length)];
        }

        if (colorFade == null)
        {
            colorFade = colorFirst;
        }

        // build
        Firework firework = player.getWorld().spawn(
                player.getLocation(),
                Firework.class
        );
        FireworkMeta meta = firework.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder()
                .with(fireEffect)
                .withColor(colorFirst)
                .withFade(colorFade)
                .flicker(true)
                .trail(true)
                .build();
        meta.addEffect(effect);
        firework.setFireworkMeta(meta);

        TextColor firstTextColor = TextColor.color(colorFirst.getRed(), colorFirst.getGreen(), colorFirst.getBlue());
        TextColor fadeTextColor = TextColor.color(colorFade.getRed(), colorFade.getGreen(), colorFade.getBlue());
        player.sendMessage(
                Component.text(effectsTexts.get(fireEffect)).color(firstTextColor)
                .append(Component.text("!").color(fadeTextColor)
                )
        );
        return true;
    }
}