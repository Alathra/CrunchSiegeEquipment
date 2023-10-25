package com.github.alathra.siegeengines.command;

import com.github.alathra.siegeengines.GunnerEquipment;
import com.github.alathra.siegeengines.projectile.GunnersProjectile;
import com.github.milkdrinkers.colorparser.ColorParser;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.github.alathra.siegeengines.SiegeEngines.*;

public class SiegeEnginesCommand {
    public SiegeEnginesCommand() {
        new CommandAPICommand("siegeengines")
            .withFullDescription("Example command.")
            .withShortDescription("Example command.")
            .withPermission("siegeengines.command")
            .withSubcommands(
                commandGetAll(),
                commandReload()
            )
            .executesPlayer(this::onExecute)
            .register();
    }

    private void onExecute(CommandSender sender, CommandArguments args) {
        sender.sendMessage(ColorParser.of("<yellow>Incorrect usage, /SiegeEngines get, /SiegeEngines reload").build());
    }

    private CommandAPICommand commandGetAll() {
        return new CommandAPICommand("getAll")
            .withPermission("siegeengines.command.getAll")
            .executesPlayer(this::onGetAll);
    }

    private CommandAPICommand commandReload() {
        return new CommandAPICommand("reload")
            .withPermission("siegeengines.command.reload")
            .executesPlayer(this::onReload);
    }

    private void onGetAll(CommandSender sender, CommandArguments args) throws WrapperCommandSyntaxException {
        if (!(sender instanceof Player player))
            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>Only players can use this command.").build());

        for (GunnerEquipment i : DefinedEquipment.values()) {
            giveEquipment(player, i);
        }
    }

    private void onReload(CommandSender sender, CommandArguments args) throws WrapperCommandSyntaxException {
        if (!(sender instanceof Player))
            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>Only players can use this command.").build());

        equipment.clear();
        TrackedStands.clear();
        DefinedEquipment.clear();
        AddDefaults();
        HashMap<ItemStack, GunnersProjectile> projObj = new HashMap<>();
        GunnerEquipment equip = CreateNewGun(null, null, null, null, null, null, projObj);
        for (GunnerEquipment i : DefinedEquipment.values()) {
            sender.sendMessage(ColorParser.of("<yellow>Enabled Weapon : %s".formatted(i.EquipmentName)).build());
            sender.sendMessage(ColorParser.of("<yellow>Weapon Propellant/\"Fuel\" ItemStacks : %s".formatted(i.FuelMaterial)).build());
            for (ItemStack proj : i.Projectiles.keySet()) {
                sender.sendMessage(ColorParser.of("<yellow>Weapon Projectile ItemStacks : %s".formatted(proj)).build());
            }
        }
        sender.sendMessage("<yellow>Gunners Core configs reloaded");
    }

    private void giveEquipment(Player player, GunnerEquipment gunnerEquipment) {
        ItemStack item = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(gunnerEquipment.ReadyModelNumber);
        meta.displayName(ColorParser.of("<yellow>%s Item".formatted(gunnerEquipment.EquipmentName)).build());
        List<Component> lore = new ArrayList<>();
        lore.add(ColorParser.of("<yellow>Place as a block to spawn a '%s'".formatted(gunnerEquipment.EquipmentName)).build());
        lore.add(ColorParser.of("<yellow>or put on an Armor Stand.").build());
        lore.add(ColorParser.of("<yellow>Right click to toggle visibility of stand.").build());
        meta.lore(lore);
        item.setItemMeta(meta);
        player.getInventory().addItem(item);
    }
}