package siegeCore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ArmorStand.LockType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mule;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;


public class CrunchSiegeCore extends JavaPlugin {
	public static Plugin plugin;

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		plugin = this;

		this.getCommand("siegetest").setExecutor(new SiegeCommand());
		getServer().getPluginManager().registerEvents(new RotationHandler(), this);
		getServer().getPluginManager().registerEvents(new ClickHandler(), this);

	}
	
	public class SiegeCommand implements CommandExecutor {

		// This method is called, when somebody uses our command
		@Override
		@EventHandler
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.isOp()) {
					CreateTrebuchet(player);
				}
			}
			return true;
		}
	}

	public static HashMap<UUID, List<Entity>> TrackedStands = new HashMap<UUID, List<Entity>>();

	public static HashMap<UUID, SiegeEquipment> equipment = new HashMap<UUID, SiegeEquipment>();

	public static String convertTime(long time){

		long days = TimeUnit.MILLISECONDS.toDays(time);
		time -= TimeUnit.DAYS.toMillis(days);

		long hours = TimeUnit.MILLISECONDS.toHours(time);
		time -= TimeUnit.HOURS.toMillis(hours);

		long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
		time -= TimeUnit.MINUTES.toMillis(minutes);

		long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
		String timeLeftFormatted = String.format("�e" + minutes + " Minutes " +seconds +" Seconds�f");

		return timeLeftFormatted;
	}

	public static void CreateTrebuchet(Player player) {
		Location l = player.getLocation();
		l.setY(l.getY() - 1);
		Entity entity2 = player.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
		SiegeEquipment equip = new SiegeEquipment(entity2.getUniqueId());
		ItemStack item = new ItemStack(Material.CARVED_PUMPKIN);
		equip.ReadyModelNumber = 122;
		equip.ModelNumberToFireAt = 135;
		equip.MillisecondsBetweenFiringStages = 2;
		equip.MillisecondsBetweenReloadingStages = 30;
		equip.FiringModelNumbers = new ArrayList<>(Arrays.asList(
				123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139
				));
		equip.CycleThroughModelsBeforeFiring = false;
		equip.Entity = entity2;

		entity2.setCustomName("Trebuchet");
		ItemMeta meta = item.getItemMeta();
		
		meta.setCustomModelData(equip.ReadyModelNumber);
		item.setItemMeta(meta);

		LivingEntity ent = (LivingEntity) entity2;
		ArmorStand stand = (ArmorStand) ent;
		stand.addEquipmentLock(EquipmentSlot.HEAD, LockType.REMOVING_OR_CHANGING);
		stand.addEquipmentLock(EquipmentSlot.LEGS, LockType.ADDING_OR_CHANGING);
		stand.addEquipmentLock(EquipmentSlot.CHEST, LockType.ADDING_OR_CHANGING);
		stand.addEquipmentLock(EquipmentSlot.FEET, LockType.ADDING_OR_CHANGING);
		ent.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2000000, 1));
		//	stand.setSmall(true);
		stand.setVisible(false);
		//stand.setSmall(true);
		ent.getEquipment().setHelmet(item);
		if (TrackedStands.containsKey(player.getUniqueId())) {
			List<Entity> entities = TrackedStands.get(player.getUniqueId());
			entities.add(entity2);
			TrackedStands.put(player.getUniqueId(), entities);
		}
		else {
			List<Entity> newList = new ArrayList<Entity>();
			newList.add(entity2);
			TrackedStands.put(player.getUniqueId(), newList);
		}

		equipment.put(entity2.getUniqueId(), equip);
	}

	public static void UpdateEntityIdModel(Entity ent, int modelNumber, String WorldName) {
		if (ent instanceof LivingEntity)
		{
			LivingEntity liv = (LivingEntity) ent;
			ItemStack Helmet = liv.getEquipment().getHelmet();
			if (Helmet != null) {
				ItemMeta meta = Helmet.getItemMeta();
				meta.setCustomModelData(modelNumber);
				Helmet.setItemMeta(meta);
				liv.getEquipment().setHelmet(Helmet);
				//	plugin.getLogger().log(Level.INFO, "Updating stand?");
			}
		}
	}

}



