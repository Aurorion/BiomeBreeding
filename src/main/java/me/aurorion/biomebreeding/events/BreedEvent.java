package me.aurorion.biomebreeding.events;

import me.aurorion.biomebreeding.Main;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BreedEvent implements Listener {

    private final Main main;

    public BreedEvent(Main instance) {
        this.main = instance;
    }

    @EventHandler
    public void onBreed(EntityBreedEvent event) {
        Entity animal = event.getEntity();

        /* Check if the animal is allowed to breed in this biome */
        if (!main.getUtils().breedingAllowed(animal)) {

            event.setCancelled(true);

            Breedable father = (Breedable) event.getFather();
            Breedable mother = (Breedable) event.getMother();

            /* Set breed to false to stop them from trying to breed over and over */
            father.setBreed(false);
            mother.setBreed(false);

            if (event.getBreeder() != null && event.getBreeder() instanceof Player) {
                event.getBreeder().sendMessage(main.parseMiniMessage("<prefix> <red>That animal cannot be bred in this biome!"));
            }

            /* Set breed to true to make them being able to breed again
             Else they are never able to breed anywhere again */
            new BukkitRunnable() {

                @Override
                public void run() {
                    father.setBreed(true);
                    mother.setBreed(true);
                }

            }.runTaskLater(main, 20L);
        }
    }

}
