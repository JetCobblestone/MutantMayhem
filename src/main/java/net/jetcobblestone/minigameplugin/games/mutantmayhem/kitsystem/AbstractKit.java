package net.jetcobblestone.minigameplugin.games.mutantmayhem.kitsystem;

import lombok.Getter;
import net.jetcobblestone.minigameplugin.assets.customdamage.DamageablePlayer;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.MutantMayhemPlayer;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kitsystem.classitem.ClassItem;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractKit {
	protected final Map<Integer, ClassItem> itemList = new HashMap<>();
	protected String name;
	protected ItemStack icon;
	protected double damage = 0;
	@Getter protected double health = 20;
	@Getter protected double defence = 1;

	
	public void equip(MutantMayhemPlayer mutantMayhemPlayer) {
		final DamageablePlayer damageablePlayer = mutantMayhemPlayer.getDamageablePlayer();
		final Player player = damageablePlayer.getLivingEntity();
		damageablePlayer.setDamage(damage);

		player.getInventory().clear();
		for (Integer key : itemList.keySet()) {
			final ClassItem classItem = itemList.get(key);
			if (classItem == null) return;
			player.getInventory().setItem(key, classItem.getItem());
		}
		
		Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(defence);
	}

	public ItemStack getIcon() {
		return icon.clone();
	}
	
	public String getName() {
		return name;
	}
}