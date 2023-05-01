package valorless.valorlessutils.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import valorless.valorlessutils.ValorlessUtils.Tags;
import valorless.valorlessutils.config.Config;

public class BasicGUI {
	
	private JavaPlugin plugin;
	
	private Inventory inventory;
	private String title;
	private List<Slot> slots;
	private InventoryHolder owner;
	private Material filler = Material.BLACK_STAINED_GLASS_PANE;
	
	public BasicGUI(final JavaPlugin plugin, final String title, final int rows, final Player owner) {
		this.title = title;
		this.slots = new ArrayList<Slot>(rows*9);
		this.owner = owner;
		this.plugin = plugin;
		RefreshInventory();
	}
	
	public BasicGUI(final JavaPlugin plugin, final String title, final List<Slot> slots, final Player owner) {
		this.title = title;
		this.slots = slots;
		this.owner = owner;
		this.plugin = plugin;
		RefreshInventory();
	}
	
	public void SetTitle(final String title) {
		this.title = title;
		RefreshInventory();
	}
	
	public void SetRows(final int rows) {
		this.slots = new ArrayList<Slot>(rows*9);
		RefreshInventory();
	}
	
	public void SetSlots(final List<Slot> slots) {
		this.slots = slots;
		RefreshInventory();
	}
	
	/**
	 * Used to add slots manually.
	 */
	public void AddSlot(final Slot item) {
		this.slots.add(item);
		RefreshInventory();
	}
	
	/**
	 * Used to remove slots manually.
	 */
	public void RemoveSlot(final Slot item) {
		this.slots.remove(item);
		RefreshInventory();
	}
	
	/**
	 * Used to remove slots manually.
	 */
	public void RemoveSlot(final int index) {
		this.slots.remove(index);
		RefreshInventory();
	}
	
	public Inventory GetInventory() {
		return this.inventory;
	}
	
	public void OpenInventory(final HumanEntity ent) {
        ent.openInventory(this.inventory);
    }
	
	public void LoadSlotsFromConfig(Config config, String key, int rows) {
		List<Slot>slots = new ArrayList<Slot>();
    	for(int i = 0; i < rows*9; i++) {
    		Slot item = new Slot(
    				config.GetString(key + "." + i + ".name"),
    				config.GetMaterial(key + "." + i + ".item"),
    				config.GetStringList(key + "." + i + ".lore"),
    				config.GetBool(key + "." + i + ".interact"),
    				config.GetString(key + "." + i + ".tag")
    				);
        	slots.add(item);
    	}
    	this.slots = slots;
		RefreshInventory();
	}
	
	public void SetFillerItem(Material material) {
		this.filler = material;
		RefreshInventory();
	}
	
	public void SetFillerFromConfig(Config config, String key) {
		this.filler = config.GetMaterial(key);
		RefreshInventory();
	}
	
	protected void RefreshInventory() {
		this.inventory = Bukkit.createInventory(this.owner, this.slots.size(), this.title);
		for(int i = 0; i < this.slots.size(); i++) {
			if(this.slots.get(i).GetMaterial() != null) {
				this.inventory.setItem(i, CreateGUIItem(
						this.slots.get(i).GetMaterial(), 
						this.slots.get(i).GetName(), 
						this.slots.get(i).IsInteractable(), 
						this.slots.get(i).GetTag(), 
						this.slots.get(i).GetLore()
						));
    		} else {
    			this.inventory.setItem(i, CreateGUIItem(filler, "§f", false, null, null));
    		}
		}
	}
	
	protected ItemStack CreateGUIItem(final Material material, final String name, final boolean interact, final String tag, final List<String> lore) {
		ItemStack item = new ItemStack(material, 1);
		ItemMeta meta = item.getItemMeta();
		if(meta != null) {
			meta.setDisplayName(name);

			if(lore != null && lore.size() != 0) {
        		meta.setLore(lore);
        	}
        	
			Tags.Set(plugin, meta.getPersistentDataContainer(), "interact", interact ? 1 : 0, PersistentDataType.INTEGER);
			Tags.Set(plugin, meta.getPersistentDataContainer(), "tag", tag, PersistentDataType.STRING);
        	
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        	
			item.setItemMeta(meta);
		}

		return item;
	}
	
}
