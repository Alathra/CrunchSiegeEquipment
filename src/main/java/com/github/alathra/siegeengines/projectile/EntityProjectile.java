package com.github.alathra.siegeengines.projectile;

import com.github.alathra.siegeengines.SiegeEngines;
import com.github.alathra.siegeengines.SiegeEnginesUtil;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class EntityProjectile extends SiegeEngineProjectile {
	
	// Defaults 
	public int entityCount = 20;
    public int arrowOnlyDamage = 6;
    public Boolean delayedFire = false;
    public int delayTime = 6;
    public EntityType entityType = EntityType.ARROW;
    public float inaccuracy = 0.2f;
    public Particle particleType = Particle.EXPLOSION_LARGE;
    public Sound soundType = Sound.ENTITY_GENERIC_EXPLODE;
    
    private boolean playSound = true;
    
    public EntityProjectile(ItemStack ammunitionItem) {
		super(ProjectileType.ENTITY, ammunitionItem);
		
		// Defaults
	}
    
    public static EntityProjectile getDefaultFireballShot() {
    	EntityProjectile fireProj = new EntityProjectile(new ItemStack(Material.FIRE_CHARGE));
        fireProj.entityCount = 4;
        fireProj.entityType = EntityType.SMALL_FIREBALL;
        fireProj.particleType = Particle.WHITE_ASH;
        fireProj.soundType = Sound.ENTITY_BLAZE_SHOOT;
        fireProj.inaccuracy = 0.75f;
        return fireProj;
    }
    
    public static EntityProjectile getDefaultScatterShot() {
    	EntityProjectile scatterProj = new EntityProjectile(new ItemStack(Material.TNT));
    	scatterProj.inaccuracy = 0.5f;
    	scatterProj.entityCount = 24;
    	scatterProj.particleType = Particle.ELECTRIC_SPARK;
    	scatterProj.soundType = Sound.ITEM_CROSSBOW_SHOOT;
        return scatterProj;
    }

    @Override
    public void Shoot(Entity player, Entity entity, Location FireLocation, Float velocity) {
        playSound = true;
        int baseDelay = 0;
        for (int i = 0; i < entityCount; i++) {
            if (delayedFire) {
                baseDelay += delayTime;
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SiegeEngines.getInstance(), () -> {

                    CreateEntity(entity, FireLocation, velocity, player);
                }, (long) baseDelay);
            } else {
                CreateEntity(entity, FireLocation, velocity, player);
            }
        }
    }

    private void CreateEntity(Entity entity, Location loc, Float velocity, Entity player) {
        World world = entity.getLocation().getWorld();
        
        Entity arrow = world.spawnEntity(loc, entityType);
        if (inaccuracy != 0f) {
            arrow.setVelocity(loc.getDirection().multiply(velocity).add(Randomise())
                .subtract(Randomise()));
        } else {
            arrow.setVelocity(loc.getDirection().multiply(velocity));
        }
        arrow.setMetadata("isEntityProj", SiegeEnginesUtil.addMetaDataValue("true"));
        Bukkit.getServer().getPluginManager().callEvent(new org.bukkit.event.entity.ProjectileLaunchEvent(arrow));

        if (arrow instanceof org.bukkit.entity.Projectile) {
            if (player instanceof org.bukkit.projectiles.ProjectileSource)
                ((org.bukkit.entity.Projectile) arrow).setShooter((org.bukkit.projectiles.ProjectileSource) player);
        }
        if (arrow instanceof Arrow) {
            Arrow arr = (Arrow) arrow;
            arr.setDamage(8);
            if (player instanceof org.bukkit.projectiles.ProjectileSource) {
                arr.setShooter((org.bukkit.projectiles.ProjectileSource) player);
                arr.setPickupStatus(org.bukkit.entity.AbstractArrow.PickupStatus.CREATIVE_ONLY);
            }
        }
        if (playSound) {
            world.playSound(loc, this.soundType, 20, 2);
            world.spawnParticle(this.particleType, loc.getX(), loc.getY(), loc.getZ(), 0);
            if (!delayedFire) {
                playSound = false;
            }
        }

    }

    private Vector Randomise() {
        return new Vector(SiegeEngines.random.nextFloat() * (inaccuracy - (inaccuracy * -1)) + (inaccuracy * -1), SiegeEngines.random.nextFloat() * (inaccuracy - (inaccuracy * -1)) + (inaccuracy * -1), SiegeEngines.random.nextFloat() * (inaccuracy - (inaccuracy * -1)) + (inaccuracy * -1));
    }

	public ProjectileType getProjectileType() {
		return projectileType;
	}
}

