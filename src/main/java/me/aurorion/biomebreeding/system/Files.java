package me.aurorion.biomebreeding.system;

import me.aurorion.biomebreeding.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Files {

    private final Main main;

    private final File sfile;
    private final FileConfiguration settings;

    public Files(Main instance) {
        this.main = instance;
        this.sfile = new File(this.main.getDataFolder(), "Settings.yml");
        this.settings = YamlConfiguration.loadConfiguration(this.sfile);
        if (!this.sfile.exists()) {
            this.main.getServer().getConsoleSender().sendMessage(this.main.parseMiniMessage("<console> <green>Settings.yml created!"));
        }
    }

    public FileConfiguration getSettings() {
        return this.settings;
    }

    public void saveSettings() {
        try {
            this.settings.save(this.sfile);
        } catch (IOException e) {
            this.main.getServer().getConsoleSender().sendMessage(this.main.parseMiniMessage("<console> <red>Could not save Settings.yml!"));
            e.printStackTrace();
        }
    }
}
