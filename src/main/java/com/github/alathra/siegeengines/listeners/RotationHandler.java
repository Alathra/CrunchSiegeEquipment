package com.github.alathra.siegeengines.listeners;

import java.util.UUID;

import com.github.alathra.siegeengines.SiegeEngines;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.github.alathra.siegeengines.SiegeEquipment;
import com.github.alathra.siegeengines.config.Config;

public class RotationHandler implements Listener {

    @EventHandler
    public void playerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        NamespacedKey key = new NamespacedKey(SiegeEngines.plugin, "cannons");
        if (SiegeEngines.TrackedStands.containsKey(player.getUniqueId())) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand != null) {
                if (itemInHand.getType() != Config.controlItem) {
                    //	TrackedStands.remove(player.getUniqueId());

                    return;
                }
                for (Entity ent : SiegeEngines.TrackedStands.get(player.getUniqueId())) {
                    if (ent != null) {
                        SiegeEquipment equipment = SiegeEngines.equipment.get(ent.getUniqueId());
                        if (ent.isDead()) {
                            continue;
                        }
                        double distance = player.getLocation().distance(ent.getLocation());
                        if (distance <= 32) {
                            //	player.sendMessage("§egot id");
                            LivingEntity living = (LivingEntity) ent;
                            Location loc = ent.getLocation();

                            if (equipment.RotateSideways) {
                                Location direction = player.getLocation().add(player.getLocation().getDirection().multiply(50));

                                Vector dirBetweenLocations = direction.toVector().subtract(loc.toVector());
                                if (ent.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                                    Entity base = Bukkit.getEntity(UUID.fromString(ent.getPersistentDataContainer().get(key, PersistentDataType.STRING)));
                                    Location baseloc = base.getLocation();
                                    baseloc.setDirection(dirBetweenLocations);
                                    base.teleport(baseloc);
                                }
                                loc.setDirection(dirBetweenLocations);
                            }
                            //loc.setYaw(player.getLocation().getYaw());
                            //loc.setPitch(player.getLocation().getPitch());


                            ArmorStand stand = (ArmorStand) living;

                            if (equipment.RotateUpDown) {
                                loc.setPitch(player.getLocation().getPitch());
                                if (loc.getPitch() < -85) {
                                    loc.setPitch(-85);
                                }
                                if (loc.getPitch() > 85) {
                                    loc.setPitch(85);
                                }
                                if (equipment.RotateStandHead) {
                                    stand.setHeadPose(new EulerAngle(loc.getDirection().getY() * (-1), 0, 0));
                                }
                            }


                            living.teleport(loc);
                            equipment.ShowFireLocation(player);
                        } else {
                            SiegeEngines.TrackedStands.get(player.getUniqueId()).remove(ent);
                            continue;
                        }
                    }
                }
            }
        }
    }

    public Vector getDirectionBetweenLocations(Location Start, Location End) {
        Vector from = Start.toVector();
        Vector to = End.toVector();
        return to.subtract(from);
    }
}
