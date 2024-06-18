package me.idroom1.betterbows;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterBows extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("reloadconfig").setExecutor((sender, command, label, args) -> {
            reloadConfig();
            sender.sendMessage("Config reloaded!");
            return true;
        });
        getLogger().info("BetterBows has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("BetterBows has been disabled.");
    }

    public String getProjectileType() {
        return getConfig().getString("projectile");
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            EntityType projectileType;
            try {
                projectileType = EntityType.valueOf(getProjectileType());
            } catch (IllegalArgumentException e) {
                player.sendMessage("Invalid projectile type in config!");
                return;
            }

            // Cancel the default arrow
            event.setCancelled(true);

            // Launch the configured projectile
            Entity projectileEntity = player.getWorld().spawnEntity(player.getEyeLocation(), projectileType);
            if (projectileEntity instanceof Projectile) {
                Projectile projectile = (Projectile) projectileEntity;
                projectile.setVelocity(player.getEyeLocation().getDirection().multiply(2));
                projectile.setShooter(player);
            } else {
                player.sendMessage("Configured projectile type is not a valid projectile!");
                projectileEntity.remove();
            }
        }
    }
}