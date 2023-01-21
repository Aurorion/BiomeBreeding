package me.aurorion.biomebreeding;

import me.aurorion.biomebreeding.commands.BreedCommand;
import me.aurorion.biomebreeding.listeners.BreedEvent;
import me.aurorion.biomebreeding.system.Files;
import me.aurorion.biomebreeding.system.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class Main extends JavaPlugin {

    private MiniMessage mm;
    private Files files;
    private Utils utils;

    @Override
    public void onEnable() {
        this.buildMiniMessage();
        this.registerClasses();
        this.registerCommands();
        this.registerListeners();
        this.cacheSettings();
    }

    @Override
    public void onDisable() {
        if (!this.getUtils().getCache().isEmpty()) {
            for (String animals : this.getUtils().getCache().keySet()) {
                this.getFiles().getSettings().set(animals, getUtils().getCache().get(animals));
            }
        }
        this.getFiles().saveSettings();
    }

    private void buildMiniMessage() {
        this.mm = MiniMessage.builder().tags(TagResolver.builder()
                .resolver(TagResolver.standard())
                .resolver(Placeholder.parsed("prefix", "<dark_aqua><b>[</b><aqua>BiomeBreed<dark_aqua><b>]</b>"))
                .resolver(Placeholder.parsed("console", "<dark_aqua>[<aqua>BiomeBreed<dark_aqua>]"))
                .resolver(Placeholder.parsed("console-error", "<console> <red>Console can't perform this command"))
                .resolver(Placeholder.parsed("no-perm", "<prefix> <red>I'm sorry, you're not allowed to do that!"))
                .build()).build();
    }

    private void registerClasses() {
        this.files = new Files(this);
        this.utils = new Utils();
    }

    private void registerCommands() {
        BreedCommand breedCommand = new BreedCommand(this);
        this.getCommand("biomebreed").setExecutor(breedCommand);
        this.getCommand("biomebreed").setTabCompleter(breedCommand);
    }

    private void registerListeners() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new BreedEvent(this), this);
    }

    private void cacheSettings() {
        for (String animals : this.getFiles().getSettings().getKeys(false)) {
            this.getUtils().getCache().put(animals, new ArrayList<>());
            for (String biomes : this.getFiles().getSettings().getStringList(animals)) {
                this.getUtils().getCache().get(animals).add(biomes);
            }
        }
    }

    /* --- Getters ---*/
    public Component parseMiniMessage(String text) {
        return this.mm.deserialize(text);
    }

    public Component parseMiniMessage(String text, TagResolver... resolvers) {
        return this.mm.deserialize(text, resolvers);
    }

    public Files getFiles() {
        return this.files;
    }

    public Utils getUtils() {
        return this.utils;
    }

}
