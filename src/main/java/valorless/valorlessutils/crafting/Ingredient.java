package valorless.valorlessutils.crafting;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an ingredient used in crafting recipes.
 */
public class Ingredient {

    /**
     * The identification letter of the ingredient.
     */
    public char letter;

    /**
     * The ItemStack representing the ingredient.
     */
    public ItemStack item;

    /**
     * The Material of the ingredient.
     */
    public Material material;

    /**
     * Constructs an Ingredient object with an identification letter and an ItemStack.<br>
     * The ingredient's material will also be set to the material of 'item'.
     *
     * @param letter The identification letter.
     * @param item   The ItemStack representing the ingredient.
     */
    public Ingredient(String letter, ItemStack item) {
        this.letter = letter.charAt(0);
        this.item = item;
        this.material = item.getType();
    }

    /**
     * Constructs an Ingredient object with an identification letter and a Material.
     *
     * @param letter   The identification letter.
     * @param material The Material of the ingredient.
     */
    public Ingredient(String letter, Material material) {
        this.letter = letter.charAt(0);
        this.item = null;
        this.material = material;
    }
    
    /**
     * Returns a string representation of the Ingredient object.
     * @return A string representation of the Ingredient object.
     */
    @Override
    public String toString() {
        return String.format("Ingredient{letter=%s, item=%s, material=%s}", letter, item, material);
    }
}