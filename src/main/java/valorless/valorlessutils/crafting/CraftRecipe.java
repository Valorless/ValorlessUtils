package valorless.valorlessutils.crafting;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import valorless.valorlessutils.ValorlessUtils;
import valorless.valorlessutils.ValorlessUtils.Log;

/**
 * Represents a custom crafting recipe in Minecraft.
 * <p>
 * Supports both shaped and shapeless recipes using the base Minecraft crafting system.
 * Recipes can optionally require a permission to craft.
 * </p>
 */
public class CraftRecipe implements Listener {

    /** Enum representing the type of crafting recipe. */
    public enum RecipeType { Shaped, Shapeless }

    /** The shapeless recipe object. */
    ShapelessRecipe shapelessRecipe;

    /** The shaped recipe object. */
    ShapedRecipe shapedRecipe;

    /** The unique namespaced key of the recipe. */
    NamespacedKey key;

    /** The permission required to craft the recipe. Null if no permission required. */
    Permission permission = null;

    /** The list of ingredients required for the recipe. */
    List<Ingredient> ingredients = new ArrayList<>();

    /** The shape of the recipe (for shaped recipes). */
    List<String> shape = new ArrayList<>();

    /** The type of the recipe (shaped or shapeless). */
    RecipeType type;

    /** The result of the crafting recipe. */
    ItemStack result;

    /**
     * Constructs a new CraftRecipe.
     *
     * @param plugin      The plugin used for the NamespacedKey.
     * @param recipe      The unique recipe ID.
     * @param type        The type of the recipe (shaped or shapeless).
     * @param ingredients The ingredients required for crafting.
     * @param result      The resulting item.
     * @param shape       Optional shape for shaped recipes.
     */
    @SafeVarargs
    public CraftRecipe(JavaPlugin plugin, String recipe, RecipeType type, List<Ingredient> ingredients, ItemStack result, List<String>... shape) {
        this.key = new NamespacedKey(plugin, recipe);
        this.type = type;
        this.ingredients = ingredients;
        this.result = result;
        if (shape.length != 0) {
            this.shape = shape[0];
        }
        SetUpRecipe();
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /** Initializes the recipe based on its type and ingredients. */
    void SetUpRecipe() {
        if (type == RecipeType.Shaped) {
            shapedRecipe = new ShapedRecipe(this.key, this.result);
            for (String s : shape) s = s.replace("X", " "); // Remove placeholders
            shapedRecipe.shape(shape.get(0), shape.get(1), shape.get(2));
            for (Ingredient ing : ingredients) {
                if (ing.item != null) {
                    shapedRecipe.setIngredient(ing.letter, new RecipeChoice.ExactChoice(ing.item));
                } else {
                    shapedRecipe.setIngredient(ing.letter, ing.material);
                }
            }
        } else if (type == RecipeType.Shapeless) {
            shapelessRecipe = new ShapelessRecipe(this.key, this.result);
            for (Ingredient ing : ingredients) {
                if (ing.item != null) {
                    shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(ing.item));
                } else {
                    if (ing.amount > 1) {
                        shapelessRecipe.addIngredient(ing.amount, ing.material);
                    } else {
                        shapelessRecipe.addIngredient(ing.material);
                    }
                }
            }
        }
    }

    /** Sets the shape of a shaped recipe. */
    public void SetShape(List<String> shape) {
        this.shape = shape;
        SetUpRecipe();
    }

    /** Gets the shape of the recipe. */
    public List<String> GetShape() {
        return shape;
    }

    /** Sets the ingredients of the recipe. */
    public void SetIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        SetUpRecipe();
    }

    /** Gets the ingredients of the recipe. */
    public List<Ingredient> GetIngredients() {
        return ingredients;
    }

    /** Sets the recipe type (shaped or shapeless). */
    public void SetType(RecipeType type) {
        this.type = type;
        SetUpRecipe();
    }

    /** Gets the recipe type. */
    public RecipeType GetType() {
        return type;
    }

    /**
     * Sets the permission required to craft this recipe by string.
     * <p>If null, no permission is required.</p>
     */
    public void SetPermission(@Nullable String permission) {
        this.permission = new Permission(permission);
    }

    /** Sets the permission required to craft this recipe directly. */
    public void SetPermission(@Nullable Permission permission) {
        this.permission = permission;
    }

    /** Gets the permission required to craft this recipe. */
    public Permission GetPermission() {
        return permission;
    }

    /** Sets the result of the recipe. */
    public void SetResult(ItemStack result) {
        this.result = result;
    }

    /** Gets the result of the recipe. */
    public ItemStack GetResult() {
        return result;
    }

    /** Removes this recipe from the server. */
    public void Remove() {
        Bukkit.removeRecipe(key);
    }

    /** Adds this recipe to the server. */
    public void Add() {
        if (key == null || result == null || ingredients.size() == 0 || shape.size() == 0)
            throw new NullPointerException();
        try {
            if (type == RecipeType.Shaped && shapedRecipe != null) {
                Bukkit.getServer().addRecipe(shapedRecipe);
            } else if (type == RecipeType.Shapeless && shapelessRecipe != null) {
                Bukkit.getServer().addRecipe(shapelessRecipe);
            } else {
                Log.Error(ValorlessUtils.thisPlugin, String.format("Recipe '%s' could not be added.", key.toString()));
            }
            if (permission != null && Bukkit.getPluginManager().getPermission(permission.getName()) != null) {
                Bukkit.getPluginManager().addPermission(permission);
            }
            Log.Info(ValorlessUtils.thisPlugin, String.format("Recipe '%s' added.", key.toString()));
        } catch (Exception e) {
            Log.Error(ValorlessUtils.thisPlugin, String.format("Recipe '%s' could not be added.", key.toString()));
            e.printStackTrace();
        }
    }

    /**
     * Event handler for when a player prepares an item for crafting.
     * <p>
     * Ensures that only players with the required permission can craft the recipe.
     * </p>
     *
     * @param event The PrepareItemCraftEvent.
     */
    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        try {
            if (event.getRecipe() == null) return;
            if (type == RecipeType.Shaped) {
                try {
                    ShapedRecipe recipe = (ShapedRecipe) event.getRecipe();
                    if (recipe.getKey().toString().equalsIgnoreCase(key.toString()) && permission != null) {
                        for (HumanEntity player : event.getViewers()) {
                            if (!player.hasPermission(permission)) event.getInventory().setResult(null);
                        }
                    }
                } catch (Exception ignored) {}
            } else if (type == RecipeType.Shapeless) {
                try {
                    ShapelessRecipe recipe = (ShapelessRecipe) event.getRecipe();
                    if (recipe.getKey().toString().equalsIgnoreCase(key.toString()) && permission != null) {
                        for (HumanEntity player : event.getViewers()) {
                            if (!player.hasPermission(permission)) event.getInventory().setResult(null);
                        }
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Returns a string representation of this CraftRecipe. */
    @Override
    public String toString() {
        List<String> ing = new ArrayList<>();
        for (Ingredient i : ingredients) ing.add(i.toString());
        return String.format(
                "CraftRecipe{key=%s, type=%s, shape=%s, ingredients=%s, result=%s, permission=%s}",
                key.toString(), type.toString(), shape.toString(), ing.toString(), result.toString(),
                permission != null ? permission.toString() : "none"
        );
    }
}
