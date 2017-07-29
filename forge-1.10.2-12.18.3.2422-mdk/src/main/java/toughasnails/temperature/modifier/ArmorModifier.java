package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.item.TANItems;
import toughasnails.api.temperature.Temperature;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.temperature.TemperatureTrend;

public class ArmorModifier extends TemperatureModifier {
	public final int ARMOR_RATE_MODIFIER;

	public final int JELLED_SLIME_HELMET_MODIFIER;
	public final int JELLED_SLIME_CHESTPLATE_MODIFIER;
	public final int JELLED_SLIME_LEGGINGS_MODIFIER;
	public final int JELLED_SLIME_BOOTS_MODIFIER;

	public final int WOOL_HELMET_MODIFIER;
	public final int WOOL_CHESTPLATE_MODIFIER;
	public final int WOOL_LEGGINGS_MODIFIER;
	public final int WOOL_BOOTS_MODIFIER;

	public ArmorModifier(TemperatureDebugger debugger) {
		super(debugger);
		this.ARMOR_RATE_MODIFIER = SyncedConfig.getIntegerValue(GameplayOption.ARMOR_RATE_MODIFIER);
		this.JELLED_SLIME_HELMET_MODIFIER = SyncedConfig.getIntegerValue(GameplayOption.JELLED_SLIME_HELMET_MODIFIER);
		this.JELLED_SLIME_CHESTPLATE_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.JELLED_SLIME_CHESTPLATE_MODIFIER);
		this.JELLED_SLIME_LEGGINGS_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.JELLED_SLIME_LEGGINGS_MODIFIER);
		this.JELLED_SLIME_BOOTS_MODIFIER = SyncedConfig.getIntegerValue(GameplayOption.JELLED_SLIME_BOOTS_MODIFIER);
		this.WOOL_HELMET_MODIFIER = SyncedConfig.getIntegerValue(GameplayOption.WOOL_HELMET_MODIFIER);
		this.WOOL_CHESTPLATE_MODIFIER = SyncedConfig.getIntegerValue(GameplayOption.WOOL_CHESTPLATE_MODIFIER);
		this.WOOL_LEGGINGS_MODIFIER = SyncedConfig.getIntegerValue(GameplayOption.WOOL_LEGGINGS_MODIFIER);
		this.WOOL_BOOTS_MODIFIER = SyncedConfig.getIntegerValue(GameplayOption.WOOL_BOOTS_MODIFIER);
	}

	@Override
	public int modifyChangeRate(World world, EntityPlayer player, int changeRate, TemperatureTrend trend) {
		int newChangeRate = changeRate;
		int armorRateModifier = ARMOR_RATE_MODIFIER;

		switch (trend) {
		case INCREASING:
			armorRateModifier *= -1;
			break;
		case STILL:
			armorRateModifier = 0;
			break;
		default:
			armorRateModifier = 0;
			break;
		}

		debugger.start(Modifier.ARMOR_RATE, newChangeRate);

		InventoryPlayer inventory = ((EntityPlayer) player).inventory;

		// Helmet
		if (inventory.armorInventory[3] != null) {
			newChangeRate += armorRateModifier;
		}

		// Chestplate
		if (inventory.armorInventory[2] != null) {
			newChangeRate += armorRateModifier;
		}

		// Legging
		if (inventory.armorInventory[1] != null) {
			newChangeRate += armorRateModifier;
		}

		// Boots
		if (inventory.armorInventory[0] != null) {
			newChangeRate += armorRateModifier;
		}

		debugger.end(newChangeRate);

		return newChangeRate;
	}

	@Override
	public Temperature modifyTarget(World world, EntityPlayer player, Temperature temperature) {
		int temperatureLevel = temperature.getRawValue();
		int newTemperatureLevel = temperatureLevel;

		debugger.start(Modifier.ARMOR_TARGET, newTemperatureLevel);

		InventoryPlayer inventory = ((EntityPlayer) player).inventory;

		// Helmet
		if (inventory.armorInventory[3] != null) {
			if (inventory.armorInventory[3].getItem() == TANItems.wool_helmet) {
				newTemperatureLevel += WOOL_HELMET_MODIFIER;
			}
			if (inventory.armorInventory[3].getItem() == TANItems.jelled_slime_helmet) {
				newTemperatureLevel += JELLED_SLIME_HELMET_MODIFIER;
			}
		}

		// Chestplate
		if (inventory.armorInventory[2] != null) {
			if (inventory.armorInventory[2].getItem() == TANItems.wool_chestplate) {
				newTemperatureLevel += WOOL_CHESTPLATE_MODIFIER;
			}
			if (inventory.armorInventory[2].getItem() == TANItems.jelled_slime_chestplate) {
				newTemperatureLevel += JELLED_SLIME_CHESTPLATE_MODIFIER;
			}
		}

		// Leggings
		if (inventory.armorInventory[1] != null) {
			if (inventory.armorInventory[1].getItem() == TANItems.wool_leggings) {
				newTemperatureLevel += WOOL_LEGGINGS_MODIFIER;
			}
			if (inventory.armorInventory[1].getItem() == TANItems.jelled_slime_leggings) {
				newTemperatureLevel += JELLED_SLIME_LEGGINGS_MODIFIER;
			}
		}

		// Boots
		if (inventory.armorInventory[0] != null) {
			if (inventory.armorInventory[0].getItem() == TANItems.wool_boots) {
				newTemperatureLevel += WOOL_BOOTS_MODIFIER;
			}
			if (inventory.armorInventory[0].getItem() == TANItems.jelled_slime_boots) {
				newTemperatureLevel += JELLED_SLIME_BOOTS_MODIFIER;
			}
		}

		debugger.end(newTemperatureLevel);

		return new Temperature(newTemperatureLevel);
	}
}
