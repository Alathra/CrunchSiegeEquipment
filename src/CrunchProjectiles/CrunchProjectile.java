package CrunchProjectiles;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface CrunchProjectile {
 
	public void Shoot(Player player, Entity entity, double XOffset, double YOffset, Float velocity);

}