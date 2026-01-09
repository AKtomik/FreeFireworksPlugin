package io.github.AKtomik.freeFireworks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Map;
import java.util.Random;

public class CommandFireplayer implements CommandExecutor {

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
        if (!sender.hasPermission("freefireworks.launch.others")) {
            sender.sendMessage(Component.text("you dont have permission to use this command!").color(NamedTextColor.RED));
            return true;
        }

        // player
        if (args.length == 0) {
            sender.sendMessage(Component.text("give a player!").color(NamedTextColor.RED));
            return true;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null)
        {
            sender.sendMessage(Component.text("§ccan't find the player §l%s§c.".formatted(args[0])));
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
        for (int i = 1; i < Math.min(args.length, 4); i++) {
            String arg = args[i];
            if (fireworkEffects.containsKey(arg) && fireEffect == null) {
                fireEffect = fireworkEffects.get(arg);
            } else if (fireworkColors.containsKey(arg) && colorFade == null) {
                if (colorFirst == null)
                    colorFirst = fireworkColors.get(arg);
                else
                    colorFade = fireworkColors.get(arg);
            } else {

                String whatText = "";
                if (fireEffect == null)
                {
                    whatText += "effect";
                }
                if (colorFade == null)
                {
                    if (!whatText.isEmpty())
                    {
                        whatText += "/";
                    }
                    whatText += "color";
                }

                sender.sendMessage(Component.text("§cthe %s §l%s§c does not exist.".formatted(whatText, arg)));
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
        sender.sendMessage(
                Component.text(effectsTexts.get(fireEffect)).color(firstTextColor)
                .append(Component.text("!").color(fadeTextColor)
                )
        );
        return true;
    }
}