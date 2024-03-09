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
 * Represents a custom crafting recipe in Minecraft.<br>
 * These recipes are not advanced, and only use the base crafting system in Minecraft.
 */
public class CraftRecipe implements Listener {

    /** The type of the crafting recipe. */
    public enum RecipeType { Shaped, Shapeless }

    /** The shapeless recipe object. */
    ShapelessRecipe shapelessRecipe;

    /** The shaped recipe object. */
    ShapedRecipe shapedRecipe;

    /** The namespaced key of the recipe. */
    NamespacedKey key;

    /** The permission required to craft the recipe. */
    Permission permission = null;

    /** The list of ingredients required for the recipe. */
    List<Ingredient> ingredients = new ArrayList<Ingredient>();

    /** The shape of the recipe (for shaped recipes). */
    List<String> shape = new ArrayList<String>();

    /** The type of the recipe (shaped or shapeless). */
    RecipeType type;

    /** The result of the crafting recipe. */
    ItemStack result;
    
    /**
     * Constructs a CraftRecipe object with the provided parameters.
     * @param plugin     The plugin to be used in the NamespacedKey.
     * @param recipe     The ID of the recipe.
     * @param type       The type of the recipe (shaped or shapeless).
     * @param ingredients  The list of ingredients required for the recipe.
     * @param result     The result of the crafting recipe.
     * @param shape      (Optional) The shape of the recipe (required for shaped recipes).
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

    /**
     * Sets up the crafting recipe based on its type.
     */
    void SetUpRecipe() {
        if (type == RecipeType.Shaped) {
            shapedRecipe = new ShapedRecipe(this.key, this.result);
			for(String s : shape) { s = s.replace("X", " "); }
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
                    //shapedRecipe.setIngredient(ing.letter, new RecipeChoice.ExactChoice(ing.item));
                } else {
                	if(ing.amount > 1) {
                		shapelessRecipe.addIngredient(ing.amount, ing.material);

                	}else {
                		shapelessRecipe.addIngredient(ing.material);
                	}
                    //shapedRecipe.setIngredient(ing.letter, ing.material);
                }
            }
        }
    }
	
    /**
     * Sets the shape of the recipe (for shaped recipes).
     * @param shape The shape of the recipe.
     */
    public void SetShape(List<String> shape) {
        this.shape = shape;
        SetUpRecipe();
    }

    /**
     * Gets the shape of the recipe.
     * @return The shape of the recipe.
     */
    public List<String> GetShape(){
        return shape;
    }

    /**
     * Sets the ingredients required for the recipe.
     * @param ingredients The list of ingredients.
     */
    public void SetIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        SetUpRecipe();
    }

    /**
     * Gets the list of ingredients required for the recipe.
     * @return The list of ingredients.
     */
    public List<Ingredient> GetIngredients(){
        return ingredients;
    }

    /**
     * Sets the type of the crafting recipe.
     * @param type The type of the crafting recipe (shaped or shapeless).
     */
    public void SetType(RecipeType type) {
        this.type = type;
        SetUpRecipe();
    }

    /**
     * Gets the type of the crafting recipe.
     * @return The type of the crafting recipe.
     */
    public RecipeType GetType() {
        return type;
    }

    /**
     * Sets the permission required to craft the recipe.<br>
     * If the permission is set to null, anyone can craft the item.
     * @param permission The permission required to craft the recipe.
     */
    public void SetPermission(@Nullable String permission) {
        this.permission = new Permission(permission);
    }

    /**
     * Sets the permission required to craft the recipe.<br>
     * If the permission is set to null, anyone can craft the item.
     * @param permission The permission required to craft the recipe.
     */
    public void SetPermission(@Nullable Permission permission) {
        this.permission = permission;
    }

    /**
     * Gets the permission required to craft the recipe.
     * @return The permission required to craft the recipe.
     */
    public Permission GetPermission() {
        return permission;
    }

    /**
     * Sets the result of the crafting recipe.
     * @param result The result of the crafting recipe.
     */
    public void SetResult(ItemStack result) {
        this.result = result;
    }

    /**
     * Gets the result of the crafting recipe.
     * @return The result of the crafting recipe.
     */
    public ItemStack GetResult() {
        return result;
    }

    /**
     * Removes the crafting recipe from the server.
     */
    public void Remove() {
        Bukkit.removeRecipe(key);
    }

    /**
     * Adds the crafting recipe to the server.
     */
    public void Add() {
    	if(key == null || result == null || ingredients.size() == 0 || shape.size() == 0) throw new NullPointerException();
    	
    	try {
    		if (type == RecipeType.Shaped && shapedRecipe != null) {
        		Bukkit.getServer().addRecipe(shapedRecipe);
        	} else if (type == RecipeType.Shapeless && shapelessRecipe != null) {
        		Bukkit.getServer().addRecipe(shapelessRecipe);
        	}else {
    			Log.Error(ValorlessUtils.thisPlugin, String.format("Recipe '%s' could not be added.", key.toString()));
        	}
    		if(permission != null) {
    			if(Bukkit.getPluginManager().getPermission(permission.getName()) != null) {
					Bukkit.getPluginManager().addPermission(permission);
				}
    		}
        	Log.Info(ValorlessUtils.thisPlugin, String.format("Recipe '%s' added.", key.toString()));
    	} catch (Exception e) {
			Log.Error(ValorlessUtils.thisPlugin, String.format("Recipe '%s' could not be added.", key.toString()));
			e.printStackTrace();
		}
    }

    /**
     * Handles the event when an item is prepared for crafting.
     * @param event The event object.
     */
    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
    	try {
    		if(event.getRecipe() == null) return;
    		if (type == RecipeType.Shaped) {
    			try {
        	    	ShapedRecipe recipe = (ShapedRecipe)event.getRecipe();
        	    	if(recipe.getKey().toString().equalsIgnoreCase(key.toString())) {
            	    	if(permission != null) {
                        	for (HumanEntity player : event.getViewers()) {
                        		if (!player.hasPermission(permission)) {
                        			event.getInventory().setResult(null);
                            	}
                        	}
                        }
            	    }
        	    }catch(Exception er) {}
        	} else if (type == RecipeType.Shapeless) {
        		try {
        	    	ShapelessRecipe recipe = (ShapelessRecipe)event.getRecipe();
        	    	if(recipe.getKey().toString().equalsIgnoreCase(key.toString())) {
            	    	if(permission != null) {
                        	for (HumanEntity player : event.getViewers()) {
                        		if (!player.hasPermission(permission)) {
                        			event.getInventory().setResult(null);
                            	}
                        	}
                        }
            	    }
        	    }catch(Exception er) {}
        	}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Returns a string representation of this CraftRecipe object.
     * @return A string representation of this CraftRecipe object.
     */
    @Override
    public String toString() {
    	List<String> ing = new ArrayList<String>();
    	for(Ingredient i : ingredients) ing.add(i.toString());
    	return String.format("CraftRecipe{key=%s, type=%s, shape=%s, ingredients=%s, result=%s, permission=%s}", key.toString(), type.toString(), shape.toString(), ing.toString(), result.toString(), permission.toString());
    	
    }
}
