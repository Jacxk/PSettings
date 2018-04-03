package psettings.minestom.Utilities;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {

    private Material material;
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private String displayName;
    private List<String> lore;
    private List<Enchantment> enchantments;
    private List<ItemFlag> itemFlags;
    private int amount;
    private short dataValue;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemBuilder setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public ItemMeta getItemMeta() {
        return itemStack.getItemMeta();
    }

    public ItemBuilder setItemMeta(ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public List<String> getLore() {
        return lore;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public List<Enchantment> getEnchantments() {
        return enchantments;
    }

    public ItemBuilder setEnchantments(List<Enchantment> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public ItemBuilder addEnchantments(Enchantment enchantments) {
        this.enchantments.add(enchantments);
        return this;
    }

    public List<ItemFlag> getItemFlags() {
        return itemFlags;
    }

    public ItemBuilder setItemFlags(List<ItemFlag> itemFlags) {
        this.itemFlags = itemFlags;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public short getDataValue() {
        return dataValue;
    }

    public ItemBuilder setDataValue(short dataValue) {
        this.dataValue = dataValue;
        return this;
    }

    public ItemStack build() {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (displayName != null) itemMeta.setDisplayName(displayName);
        if (lore != null) itemMeta.setLore(lore);

        if (itemFlags != null) itemFlags.forEach(itemMeta::addItemFlags);
        if (enchantments != null) enchantments.forEach(enchantment -> itemMeta.addEnchant(enchantment, 1, true));

        itemStack.setAmount(amount);
        itemStack.setDurability(dataValue);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
