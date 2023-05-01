package valorless.valorlessutils.gui;

import java.util.List;

import org.bukkit.Material;

public class Slot {
	
	private final String name;
	private final Material item;
	private final List<String> lore;
	private final Boolean interactable;
	private final String tag;
	
	public Slot(String name, Material item, List<String> lore, Boolean interactable, String tag) {
		this.name = name;
		this.item = item;
		this.lore = lore;
		this.interactable = interactable;
		this.tag = tag;
	}
	
	public Slot(String name, String item, List<String> lore, Boolean interactable, String tag) {
		this.name = name;
		this.item = Material.getMaterial(item);
		this.lore = lore;
		this.interactable = interactable;
		this.tag = tag;
	}
	
	//public void SetName(String name) {
	//	this.name = name;
	//}
	
	public String GetName() {
		return this.name;
	}
	
	//public void SetMaterial(Material material) {
	//	this.item = material;
	//}
	
	public Material GetMaterial() {
		return this.item;
	}
	
	//public void SetLore(List<String> lore) {
	//	this.lore = lore;
	//}
	
	public List<String> GetLore() {
		return this.lore;
	}
	
	//public void Interactable(Boolean value) {
	//	this.interactable = value;
	//}
	
	public Boolean IsInteractable() {
		return this.interactable;
	}
	
	//public void SetTag(String tag) {
	//	this.tag = tag;
	//}
	
	public String GetTag() {
		return this.tag;
	}

}
