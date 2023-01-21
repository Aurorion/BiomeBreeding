package me.aurorion.biomebreeding.system;

import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    private final Map<String, List<String>> cache;

    public Utils() {
        this.cache = new HashMap<>();
    }

    public Map<String, List<String>> getCache() {
        return this.cache;
    }

    public boolean breedingAllowed(Entity animal) {
        String name = animal.getName().toLowerCase();
        if (this.getCache().containsKey(name)) {
            String biome = animal.getWorld().getBiome(animal.getLocation()).name().toLowerCase();
            return this.getCache().get(name).contains(biome) || this.getCache().get(name).contains("all");
        }
        return false;
    }

    public void addAnimal(String animal, String biome) {
        if (!this.getCache().containsKey(animal)) {
            this.getCache().put(animal, new ArrayList<>());
        }
        this.getCache().get(animal).add(biome);
    }

    public void removeAnimal(String animal, String biome) {
        if (biome.equalsIgnoreCase("all")) {
            this.getCache().remove(animal);
        }
        this.getCache().get(animal).remove(biome);
    }

}
