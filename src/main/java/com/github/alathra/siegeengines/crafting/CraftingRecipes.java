package com.github.alathra.siegeengines.crafting;

import com.github.alathra.siegeengines.SiegeEngines;
import com.github.alathra.siegeengines.api.SiegeEnginesAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;


public class CraftingRecipes {

    private static boolean areLoaded = false;

    public static Recipe trebuchetRecipe() {
        ItemStack trebuchet = SiegeEnginesAPI.getTrebuchetItem();
        NamespacedKey key = new NamespacedKey(SiegeEngines.getInstance(),
            SiegeEngines.getInstance().getName() + "trebuchetRecipe");
        ShapedRecipe trebuchetRecipe = new ShapedRecipe(key, trebuchet);
        trebuchetRecipe.shape("$@$", "%#$", "@^@");
        trebuchetRecipe.setIngredient('@', Material.OAK_FENCE);
        trebuchetRecipe.setIngredient('%', Material.OAK_LOG);
        trebuchetRecipe.setIngredient('#', Material.OAK_TRAPDOOR);
        trebuchetRecipe.setIngredient('$', Material.STRING);
        trebuchetRecipe.setIngredient('^', Material.BOWL);
        return trebuchetRecipe;
    }

    public static Recipe ballistaRecipe() {
        ItemStack ballista = SiegeEnginesAPI.getBallistaItem();
        NamespacedKey key = new NamespacedKey(SiegeEngines.getInstance(),
            SiegeEngines.getInstance().getName() + "ballistaRecipe");
        ShapedRecipe ballistaRecipe = new ShapedRecipe(key, ballista);
        ballistaRecipe.shape("@%%", "%# ", "% $");
        ballistaRecipe.setIngredient('@', Material.CROSSBOW);
        ballistaRecipe.setIngredient('%', Material.STICK);
        ballistaRecipe.setIngredient('#', Material.OAK_PLANKS);
        ballistaRecipe.setIngredient('$', Material.OAK_LOG);
        return ballistaRecipe;
    }

    public static Recipe swivelCannonRecipe() {
        ItemStack swivelCannon = SiegeEnginesAPI.getSwivelCannonItem();
        NamespacedKey key = new NamespacedKey(SiegeEngines.getInstance(),
            SiegeEngines.getInstance().getName() + "swivelCannonRecipe");
        ShapedRecipe swivelCannonRecipe = new ShapedRecipe(key, swivelCannon);
        swivelCannonRecipe.shape("@@@", "%%#", "  $");
        swivelCannonRecipe.setIngredient('@', Material.IRON_INGOT);
        swivelCannonRecipe.setIngredient('%', Material.IRON_BLOCK);
        swivelCannonRecipe.setIngredient('#', Material.CAULDRON);
        swivelCannonRecipe.setIngredient('$', Material.ANVIL);
        return swivelCannonRecipe;
    }

    public static Recipe breachCannonRecipe() {
        ItemStack breachCannon = SiegeEnginesAPI.getBreachCannonItem();
        NamespacedKey key = new NamespacedKey(SiegeEngines.getInstance(),
            SiegeEngines.getInstance().getName() + "breachCannonRecipe");
        ShapedRecipe breachCannonRecipe = new ShapedRecipe(key, breachCannon);
        breachCannonRecipe.shape(" @#", "@%@", "$@ ");
        breachCannonRecipe.setIngredient('@', Material.IRON_INGOT);
        breachCannonRecipe.setIngredient('%', SiegeEnginesAPI.getSwivelCannonItem());
        breachCannonRecipe.setIngredient('#', Material.CAULDRON);
        breachCannonRecipe.setIngredient('$', Material.ANVIL);
        return breachCannonRecipe;
    }

    public static void loadCraftingRecipes() {
        SiegeEngines.getInstance().getServer().addRecipe(trebuchetRecipe());
        SiegeEngines.getInstance().getServer().addRecipe(ballistaRecipe());
        SiegeEngines.getInstance().getServer().addRecipe(swivelCannonRecipe());
        SiegeEngines.getInstance().getServer().addRecipe(breachCannonRecipe());
        areLoaded = true;
    }

    public static void unloadCraftingRecipes() {
        SiegeEngines.getInstance().getServer().removeRecipe(new NamespacedKey(SiegeEngines.getInstance(),
            SiegeEngines.getInstance().getName() + "trebuchetRecipe"));
        SiegeEngines.getInstance().getServer().removeRecipe(new NamespacedKey(SiegeEngines.getInstance(),
            SiegeEngines.getInstance().getName() + "ballistaRecipe"));
        SiegeEngines.getInstance().getServer().removeRecipe(new NamespacedKey(SiegeEngines.getInstance(),
            SiegeEngines.getInstance().getName() + "swivelCannonRecipe"));
        SiegeEngines.getInstance().getServer().removeRecipe(new NamespacedKey(SiegeEngines.getInstance(),
            SiegeEngines.getInstance().getName() + "breachCannonRecipe"));
        areLoaded = false;
    }

    public static boolean areLoaded() {
        return areLoaded;
    }

}
