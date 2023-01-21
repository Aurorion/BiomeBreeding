package me.aurorion.biomebreeding.commands;

import me.aurorion.biomebreeding.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BreedCommand implements CommandExecutor, TabCompleter {

    private final Main main;

    public BreedCommand(Main instance) {
        this.main = instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        /* Console is not allowed to perform this command
         Not really needed but I personally put this on most commands */
        if (!(sender instanceof Player)) {
            sender.sendMessage(main.parseMiniMessage("<console-error>"));
            return true;
        }

        Player player = (Player) sender;

        /* Simple permissions check */
        if (!player.hasPermission("biomebreed.admin")) {
            player.sendMessage(main.parseMiniMessage("<no-perm>"));
            return true;
        }

        /* If arguments are not 3 we send them how to use the command */
        if (args.length != 3) {
            this.sendUsage(player, label);
            return true;
        }

        String animal = args[1].toLowerCase();
        String biome = args[2].toLowerCase();

        /* Adding and removing the animal and biome to the cache */
        if (args[0].equalsIgnoreCase("add")) {
            main.getUtils().addAnimal(animal, biome);
            player.sendMessage(main.parseMiniMessage("<prefix> <yellow>" + animal + " <green>can now breed in <yellow>" + biome));
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            main.getUtils().removeAnimal(animal, biome);
            player.sendMessage(main.parseMiniMessage("<prefix> <yellow>" + animal + " <red>can no longer breed in <yellow>" + biome));
            return true;
        }

        return false;
    }

    /* Simple method to send usage */
    private void sendUsage(Player player, String label) {
        player.sendMessage(main.parseMiniMessage("<prefix> <gray>Usage: /" + label + " add|remove <entity> <biome>|all"));
        player.sendMessage(main.parseMiniMessage("<prefix> <gray>Example: /" + label + " add cow plains"));
        player.sendMessage(main.parseMiniMessage("<prefix> <gray>Example: /" + label + " remove cow all"));
    }

    /* Tab-complete for the command
     The part that is commented out gives error because I wrote it when in bed
     and had no way to check if it would work */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        Player player = (Player) sender;

        if (args.length == 1) {
            completions.add("add");
            completions.add("remove");
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (args.length == 2) {
                completions.add("<entity>");
                /*for (Entity entity : Entity.class.getEnumConstants()) {
                    if (entity instanceof Breedable && !main.getUtils().getCache().containsKey(entity.getName().toLowerCase())) {
                        completions.add(entity.getName().toLowerCase());
                    }
                }*/
            }
            if (args.length == 3) {
                completions.add("<biome>");
                completions.add(player.getWorld().getBiome(player.getLocation()).name().toLowerCase());
                /*for (Biome biome : player.getWorld().getBiomeProvider().getBiomes(player.getWorld())) {
                    completions.add(biome.name().toLowerCase());
                }*/
            }
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length == 2) {
                completions.addAll(main.getUtils().getCache().keySet());
            }
            if (args.length == 3) {
                if (main.getUtils().getCache().containsKey(args[1].toLowerCase())) {
                    completions.addAll(main.getUtils().getCache().get(args[1].toLowerCase()));
                }
            }
        }

        return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList());
    }
}
