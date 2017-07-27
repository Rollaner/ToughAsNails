/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.core.ToughAsNails;

public class GameplayConfigurationHandler {
	public static final String SURVIVAL_SETTINGS = "Survival Settings";
	public static final String DRINKS = "Drink Configuration";
	public static final String TEMPERATURE_TWEAKS = "Temperature Tweaks";
	public static final String CROP_TWEAKS = "Crop Tweaks";

	public static Configuration config;

	public static final Map<String, CropGrowConfigEntry> EXTERNAL_DECAYING_CROPS = new HashMap<String, CropGrowConfigEntry>();
	public static final Map<String, CropGrowConfigEntry> EXTERNAL_HIBERNATING_CROPS = new HashMap<String, CropGrowConfigEntry>();

	private static void parseExternalDecayingCrops() {
		List<String> crops = SyncedConfig.getListValue(GameplayOption.CROPS);
		for (String cropEntry : crops) {
			String[] cropData = cropEntry.split(";");
			if (cropData.length == 6) {
				String cropName = cropData[0];
				System.out.println("Parsing crop: " + cropName);
				int minLiving = 0;
				int minOptimal = 0;
				int maxOptimal = 0;
				int maxLiving = 0;
				float nonOptimalChance = 0;
				try {
					minLiving = Integer.parseInt(cropData[1]);
					minOptimal = Integer.parseInt(cropData[2]);
					maxOptimal = Integer.parseInt(cropData[3]);
					maxLiving = Integer.parseInt(cropData[4]);
					nonOptimalChance = Float.parseFloat(cropData[5]);
					CropGrowConfigEntry cropGrowData = new CropGrowConfigEntry(minLiving, minOptimal, maxOptimal,
							maxLiving, nonOptimalChance);
					EXTERNAL_DECAYING_CROPS.put(cropName, cropGrowData);
				} catch (NumberFormatException e) {
					ToughAsNails.logger.error("Tried to process misconfigured crop! " + cropData.toString());
				}
			}
		}
	}

	private static void parseExternalHibernatingCrops() {
		List<String> hibernating = SyncedConfig.getListValue(GameplayOption.HIBERNATING);
		for (String cropEntry : hibernating) {
			String[] cropData = cropEntry.split(";");
			if (cropData.length == 6) {
				String cropName = cropData[0];
				System.out.println("Parsing hibernating crop: " + cropName);
				int minWaking = 0;
				int minOptimal = 0;
				int maxOptimal = 0;
				int maxWaking = 0;
				float nonOptimalChance = 0;
				try {
					minWaking = Integer.parseInt(cropData[1]);
					minOptimal = Integer.parseInt(cropData[2]);
					maxOptimal = Integer.parseInt(cropData[3]);
					maxWaking = Integer.parseInt(cropData[4]);
					nonOptimalChance = Float.parseFloat(cropData[5]);
					CropGrowConfigEntry cropGrowData = new CropGrowConfigEntry(minWaking, minOptimal, maxOptimal,
							maxWaking, nonOptimalChance);
					EXTERNAL_HIBERNATING_CROPS.put(cropName, cropGrowData);
				} catch (NumberFormatException e) {
					ToughAsNails.logger
							.error("Tried to process misconfigured hibernating crop! " + cropData.toString());
				}
			}
		}
	}

	public static void init(File configFile) {
		if (config == null) {
			config = new Configuration(configFile);
			loadConfiguration();
		}

		// Parse the external decaying crops list into memory for greater
		// efficiency.
		if (EXTERNAL_DECAYING_CROPS.isEmpty()) {
			parseExternalDecayingCrops();
		}

		// Parse the external hibernating crops list into memory for greater
		// efficiency.
		if (EXTERNAL_HIBERNATING_CROPS.isEmpty()) {
			parseExternalHibernatingCrops();
		}
	}

	private static void loadConfiguration() {
		try {
			// Major features
			addSyncedBool(GameplayOption.ENABLE_LOWERED_STARTING_HEALTH, true, SURVIVAL_SETTINGS,
					"Players begin with a lowered maximum health.");
			addSyncedBool(GameplayOption.ENABLE_SEASONS, true, SURVIVAL_SETTINGS, "Seasons progress as days increase");
			addSyncedBool(GameplayOption.ENABLE_TEMPERATURE, true, SURVIVAL_SETTINGS,
					"Players are affected by temperature");
			addSyncedBool(GameplayOption.ENABLE_THIRST, true, SURVIVAL_SETTINGS, "Players are affected by thirst");

			// Drink list
			String[] drinkDefault = { "minecraft:milk_bucket;*;6;0.4;0.0", "biomesoplenty:ambrosia;*;20;1.0;0.0",
					"harvestcraft:blackberryItem;*;1;0.2;0.0", "harvestcraft:blueberryItem;*;1;0.2;0.0",
					"harvestcraft:candleberryItem;*;1;0.2;0.0", "harvestcraft:raspberryItem;*;1;0.2;0.0",
					"harvestcraft:strawberryItem;*;1;0.2;0.0", "harvestcraft:cactusfruitItem;*;3;0.5;0.0",
					"harvestcraft:cantaloupeItem;*;1;0.3;0.0", "harvestcraft:cucumberItem;*;1;0.2;0.0",
					"harvestcraft:tomatoItem;*;1;0.2;0.0", "harvestcraft:pineappleItem;*;2;0.4;0.0",
					"harvestcraft:grapeItem;*;2;0.2;0.0", "harvestcraft:kiwiItem;*;2;0.2;0.0",
					"harvestcraft:cranberryItem;*;1;0.1;0.0", "harvestcraft:cherryItem;*;1;0.1;0.0",
					"harvestcraft:dragonfruitItem;*;1;0.2;0.0", "harvestcraft:lemonItem;*;1;0.2;0.0",
					"harvestcraft:pearItem;*;1;0.1;0.0", "harvestcraft:grapefruitItem;*;2;0.2;0.0",
					"harvestcraft:pomegranateItem;*;2;0.2;0.0", "harvestcraft:mangoItem;*;3;0.2;0.0",
					"harvestcraft:coconutItem;*;6;0.4;0.0", "harvestcraft:orangeItem;*;3;0.3;0.0",
					"harvestcraft:peachItem;*;2;0.2;0.0", "harvestcraft:raspberryjuiceItem;*;4;0.4;0.0",
					"harvestcraft:freshmilkItem;*;6;0.1;0.0", "harvestcraft:teaItem;*;3;0.4;0.0",
					"harvestcraft:coffeeItem;*;3;0.4;0.0", "harvestcraft:applejuiceItem;*;4;0.4;0.0",
					"harvestcraft:melonjuiceItem;*;4;0.4;0.0", "harvestcraft:carrotjuiceItem;*;4;0.4;0.0",
					"harvestcraft:strawberryjuiceItem;*;4;0.4;0.0", "harvestcraft:grapejuiceItem;*;4;0.4;0.0",
					"harvestcraft:blueberryjuiceItem;*;4;0.4;0.0", "harvestcraft:cherryjuiceItem;*;4;0.4;0.0",
					"harvestcraft:papayajuiceItem;*;4;0.4;0.0", "harvestcraft:starfruitjuiceItem;*;4;0.4;0.0",
					"harvestcraft:orangejuiceItem;*;4;0.4;0.0", "harvestcraft:peachjuiceItem;*;4;0.4;0.0",
					"harvestcraft:limejuiceItem;*;4;0.4;0.0", "harvestcraft:mangojuiceItem;*;4;0.4;0.0",
					"harvestcraft:pomegranatejuiceItem;*;4;0.4;0.0", "harvestcraft:blackberryjuiceItem;*;4;0.4;0.0",
					"harvestcraft:raspberryjuiceItem;*;4;0.4;0.0", "harvestcraft:kiwijuiceItem;*;4;0.4;0.0",
					"harvestcraft:cranberryjuiceItem;*;4;0.4;0.0", "harvestcraft:cactusfruitjuiceItem;*;4;0.4;0.0",
					"harvestcraft:plumjuiceItem;*;4;0.4;0.0", "harvestcraft:pearjuiceItem;*;4;0.4;0.0",
					"harvestcraft:apricotjuiceItem;*;4;0.4;0.0", "harvestcraft:figjuiceItem;*;4;0.4;0.0",
					"harvestcraft:grapefruitjuiceItem;*;4;0.4;0.0", "harvestcraft:persimmonjuiceItem;*;4;0.4;0.0",
					"harvestcraft:pumkinsoupItem;*;4;0.4;0.0", "harvestcraft:melonsmoothieItem;*;5;0.4;0.0",
					"harvestcraft:carrotsoupItem;*;4;0.4;0.0", "harvestcraft:strawberrysmoothieItem;*;4;0.4;0.0",
					"harvestcraft:lemonadeItem;*;5;0.4;0.0", "harvestcraft:lemonsmoothieItem;*;5;0.4;0.0",
					"harvestcraft:blueberrysmoothieItem;*;4;0.4;0.0", "harvestcraft:cherrysmoothieItem;*;4;0.4;0.0",
					"harvestcraft:raspberryicedteaItem;*;4;0.5;0.0", "harvestcraft:chaiteaItem;*;5;0.4;0.0",
					"harvestcraft:espressoItem;*;4;0.4;0.0", "harvestcraft:coffeeconlecheItem;*;4;0.4;0.0",
					"harvestcraft:bananasmoothieItem;*;4;0.4;0.0", "harvestcraft:coconutmilkItem;*;6;0.1;0.0",
					"harvestcraft:orangesmoothieItem;*;5;0.4;0.0", "harvestcraft:peachsmoothieItem;*;4;0.4;0.0",
					"harvestcraft:pomegranatesmoothieItem;*;4;0.4;0.0", "harvestcraft:papayasmoothieItem;*;5;0.4;0.0",
					"harvestcraft:mangosmoothieItem;*;5;0.4;0.0", "harvestcraft:blackberrysmoothieItem;*;4;0.4;0.0",
					"harvestcraft:raspberrysmoothieItem;*;4;0.4;0.0", "harvestcraft:chocolatemilk;*;4;0.2;0.0",
					"harvestcraft:pinacoladaItem;*;5;0.4;0.0", "harvestcraft:fruitpunchItem;*;5;0.4;0.0",
					"harvestcraft:bubblywaterItem;*;1;0.1;0.0", "harvestcraft:cherrysodaItem;*;5;0.4;0.0",
					"harvestcraft:colasodaItem;*;5;0.4;0.0", "harvestcraft:gingersodaItem;*;5;0.4;0.0",
					"harvestcraft:grapesodaItem;*;5;0.4;0.0", "harvestcraft:lemonlimesodaItem;*;5;0.4;0.0",
					"harvestcraft:orangesodaItem;*;5;0.4;0.0", "harvestcraft:rootbeersodaItem;*;5;0.4;0.0",
					"harvestcraft:strawberrysodaItem;*;5;0.4;0.0", "harvestcraft:energydrinkItem;*;4;0.4;0.0",
					"harvestcraft:appleciderItem;*;5;0.4;0.0", "harvestcraft:strawberrymilkshakeItem;*;4;0.4;0.0",
					"harvestcraft:chocolatemilkshakeItem;*;4;0.4;0.0", "harvestcraft:bananamilkshakeItem;*;4;0.4;0.0",
					"harvestcraft:applesmoothieItem;*;4;0.4;0.0", "harvestcraft:plumsmoothieItem;*;4;0.4;0.0",
					"harvestcraft:coconutsmoothieItem;*;4;0.4;0.0", "harvestcraft:pearsmoothieItem;*;4;0.4;0.0",
					"harvestcraft:grapesmoothieItem;*;4;0.4;0.0", "potioncore:custom_potion;*;2;0.0;0.1" };
			addSyncedList(GameplayOption.DRINKS, drinkDefault, DRINKS,
					"List of additional drinks with configurable damage (* = any), thirst, "
							+ "hydration, and poison chance values. ;-delimited");

			// Temperature tweaks
			// Thermometer override
			addSyncedBool(GameplayOption.OVERRIDE_THERMOMETER_LIMITS, false, TEMPERATURE_TWEAKS,
					"Override the default TAN thermometer to have upper and lower bounds at the specified limits.");
			addSyncedInt(GameplayOption.THERMOMETER_LOWER_BOUND, -25, TEMPERATURE_TWEAKS,
					"The lower bound of the thermometer.");
			addSyncedInt(GameplayOption.THERMOMETER_UPPER_BOUND, 25, TEMPERATURE_TWEAKS,
					"The upper bound of the thermometer.");

			// Rain-chill
			addSyncedBool(GameplayOption.RAIN_CHILL, true, TEMPERATURE_TWEAKS,
					"Should rain reduce the temperature of a block in the world, or only snow?");

			// Temperature modifiers
			addSyncedInt(GameplayOption.BIOME_TEMP_MODIFIER, 10, TEMPERATURE_TWEAKS,
					"Scale how significantly biome influences temperature.");
			addSyncedInt(GameplayOption.ALTITUDE_TEMP_MODIFIER, 3, TEMPERATURE_TWEAKS,
					"Scale how significantly altitude influences temperature.");
			addSyncedInt(GameplayOption.WET_TEMP_MODIFIER, -7, TEMPERATURE_TWEAKS,
					"Scale how significantly being wet influences temperature.");
			addSyncedInt(GameplayOption.SNOW_TEMP_MODIFIER, -10, TEMPERATURE_TWEAKS,
					"Scale how significantly snow influences temperature.");
			addSyncedInt(GameplayOption.TIME_TEMP_MODIFIER, 7, TEMPERATURE_TWEAKS,
					"Scale how significantly time of day influences temperature.");
			addSyncedFloat(GameplayOption.TIME_EXTREMITY_MODIFIER, 1.25f, TEMPERATURE_TWEAKS,
					"Scale how significantly the extreme times of day change the temperature.");
			addSyncedInt(GameplayOption.EARLY_AUTUMN_MODIFIER, 2, TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the EARLY_AUTUMN season.");
			addSyncedInt(GameplayOption.MID_AUTUMN_MODIFIER, 0, TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the MID_AUTUMN season.");
			addSyncedInt(GameplayOption.LATE_AUTUMN_MODIFIER, -2, TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the LATE_AUTUMN season.");
			addSyncedInt(GameplayOption.EARLY_WINTER_MODIFIER, -4, TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the EARLY_WINTER season.");
			addSyncedInt(GameplayOption.MID_WINTER_MODIFIER, -6, TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the MID_WINTER season.");
			addSyncedInt(GameplayOption.LATE_WINTER_MODIFIER, -6, TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the LATE_WINTER season.");
			addSyncedInt(GameplayOption.EARLY_SPRING_MODIFIER, -4, TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the EARLY_SPRING season.");
			addSyncedInt(GameplayOption.MID_SPRING_MODIFIER, -2, TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the MID_SPRING season.");
			addSyncedInt(GameplayOption.LATE_SPRING_MODIFIER, 0, TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the LATE_SPRING season.");
			addSyncedInt(GameplayOption.EARLY_SUMMER_MODIFIER, 0, TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the EARLY_SUMMER season.");
			addSyncedInt(GameplayOption.MID_SUMMER_MODIFIER, 2, TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the MID_SUMMER season.");
			addSyncedInt(GameplayOption.LATE_SUMMER_MODIFIER, 4, TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the LATE_SUMMER season.");

			// Crop tweaks
			addSyncedBool(GameplayOption.TEMPERATURE_WITHERING, false, CROP_TWEAKS,
					"Should crop withering be based on actual plant temperature or just the season?");

			String[] cropDefault = { "minecraft:wheat;5;10;15;25;0.5", "minecraft:carrots;5;10;15;25;0.5",
					"minecraft:potatoes;5;10;15;25;0.5", "minecraft:beetroots;5;10;15;25;0.5",
					"minecraft:pumpkin_stem;5;10;15;25;0.5", "minecraft:melon_stem;5;10;15;25;0.5",
					"harvestcraft:pamblackberryCrop;5;10;15;25;0.5", "harvestcraft:pamblueberryCrop;5;10;15;25;0.5",
					"harvestcraft:pamcandleberryCrop;5;10;15;25;0.5", "harvestcraft:pamraspberryCrop;5;10;15;25;0.5",
					"harvestcraft:pamstrawberryCrop;5;10;15;25;0.5", "harvestcraft:pamcactusfruitCrop;5;10;15;25;0.5",
					"harvestcraft:pamasparagusCrop;5;10;15;25;0.5", "harvestcraft:pambarleyCrop;5;10;15;25;0.5",
					"harvestcraft:pamoatsCrop;5;10;15;25;0.5", "harvestcraft:pamryeCrop;5;10;15;25;0.5",
					"harvestcraft:pamcornCrop;5;10;15;25;0.5", "harvestcraft:pambambooshootCrop;5;10;15;25;0.5",
					"harvestcraft:pamcantaloupeCrop;5;10;15;25;0.5", "harvestcraft:pamcucumberCrop;5;10;15;25;0.5",
					"harvestcraft:pamwintersquashCrop;5;10;15;25;0.5", "harvestcraft:pamzucchiniCrop;5;10;15;25;0.5",
					"harvestcraft:pambeetCrop;5;10;15;25;0.5", "harvestcraft:pamonionCrop;5;10;15;25;0.5",
					"harvestcraft:pamparsnipCrop;5;10;15;25;0.5", "harvestcraft:pampeanutCrop;5;10;15;25;0.5",
					"harvestcraft:pamradishCrop;5;10;15;25;0.5", "harvestcraft:pamrutabagaCrop;5;10;15;25;0.5",
					"harvestcraft:pamsweetpotatoCrop;5;10;15;25;0.5", "harvestcraft:pamturnipCrop;5;10;15;25;0.5",
					"harvestcraft:pamrhubarbCrop;5;10;15;25;0.5", "harvestcraft:pamceleryCrop;5;10;15;25;0.5",
					"harvestcraft:pamgarlicCrop;5;10;15;25;0.5", "harvestcraft:pamgingerCrop;5;10;15;25;0.5",
					"harvestcraft:pamspiceleafCrop;5;10;15;25;0.5", "harvestcraft:pamtealeafCrop;5;10;15;25;0.5",
					"harvestcraft:pamcoffeebeanCrop;5;10;15;25;0.5", "harvestcraft:pammustardseedsCrop;5;10;15;25;0.5",
					"harvestcraft:pambroccoliCrop;5;10;15;25;0.5", "harvestcraft:pamcauliflowerCrop;5;10;15;25;0.5",
					"harvestcraft:pamleekCrop;5;10;15;25;0.5", "harvestcraft:pamlettuceCrop;5;10;15;25;0.5",
					"harvestcraft:pamscallionCrop;5;10;15;25;0.5", "harvestcraft:pamartichokeCrop;5;10;15;25;0.5",
					"harvestcraft:pambrusselsproutCrop;5;10;15;25;0.5", "harvestcraft:pamcabbageCrop;5;10;15;25;0.5",
					"harvestcraft:pamspinachCrop;5;10;15;25;0.5", "harvestcraft:pamwhitemushroomCrop;5;10;15;25;0.5",
					"harvestcraft:pambeanCrop;5;10;15;25;0.5", "harvestcraft:pamsoybeanCrop;5;10;15;25;0.5",
					"harvestcraft:pambellpepperCrop;5;10;15;25;0.5", "harvestcraft:pamchilipepperCrop;5;10;15;25;0.5",
					"harvestcraft:pameggplantCrop;5;10;15;25;0.5", "harvestcraft:pamokraCrop;5;10;15;25;0.5",
					"harvestcraft:pampeasCrop;5;10;15;25;0.5", "harvestcraft:pamtomatoCrop;5;10;15;25;0.5",
					"harvestcraft:pamcottonCrop;5;10;15;25;0.5", "harvestcraft:pampineappleCrop;5;10;15;25;0.5",
					"harvestcraft:pamgrapeCrop;5;10;15;25;0.5", "harvestcraft:pamkiwiCrop;5;10;15;25;0.5",
					"harvestcraft:pamcranberryCrop;5;10;15;25;0.5", "harvestcraft:pamriceCrop;5;10;15;25;0.5",
					"harvestcraft:pamseaweedCrop;5;10;15;25;0.5", "harvestcraft:pamcurryleafCrop;5;10;15;25;0.5",
					"harvestcraft:pamsesameseedsCrop;5;10;15;25;0.5",
					"harvestcraft:pamwaterchestnutCrop;5;10;15;25;0.5" };
			addSyncedList(GameplayOption.CROPS, cropDefault, CROP_TWEAKS,
					"List of crops with configurable min living, " + "min optimal, max optimal, max living temps, "
							+ "and the chance of skipping a growth tick outside "
							+ "of the optimal temperature range. ;-delimited");

			String[] hibernateDefault = { "harvestcraft:pamAlmond;5;10;15;20;0.5",
					"harvestcraft:pamApple;5;10;15;20;0.5", "harvestcraft:pamApricot;5;10;15;20;0.5",
					"harvestcraft:pamAvocado;5;10;15;20;0.5", "harvestcraft:pamBanana;5;10;15;20;0.5",
					"harvestcraft:pamCashew;5;10;15;20;0.5", "harvestcraft:pamCherry;5;10;15;20;0.5",
					"harvestcraft:pamChestnut;5;10;15;20;0.5", "harvestcraft:pamCinnamon;5;10;15;20;0.5",
					"harvestcraft:pamCoconut;5;10;15;20;0.5", "harvestcraft:pamDate;5;10;15;20;0.5",
					"harvestcraft:pamDragonfruit;5;10;15;20;0.5", "harvestcraft:pamDurian;5;10;15;20;0.5",
					"harvestcraft:pamFig;5;10;15;20;0.5", "harvestcraft:pamGooseberry;5;10;15;20;0.5",
					"harvestcraft:pamGrapefruit;5;10;15;20;0.5", "harvestcraft:pamLemon;5;10;15;20;0.5",
					"harvestcraft:pamLime;5;10;15;20;0.5", "harvestcraft:pamMango;5;10;15;20;0.5",
					"harvestcraft:pamMaple;5;10;15;20;0.5", "harvestcraft:pamNutmeg;5;10;15;20;0.5",
					"harvestcraft:pamOlive;5;10;15;20;0.5", "harvestcraft:pamOrange;5;10;15;20;0.5",
					"harvestcraft:pamPapaya;5;10;15;20;0.5", "harvestcraft:pamPaperbark;5;10;15;20;0.5",
					"harvestcraft:pamPeach;5;10;15;20;0.5", "harvestcraft:pamPear;5;10;15;20;0.5",
					"harvestcraft:pamPecan;5;10;15;20;0.5", "harvestcraft:pamPeppercorn;5;10;15;20;0.5",
					"harvestcraft:pamPersimmon;5;10;15;20;0.5", "harvestcraft:pamPistachio;5;10;15;20;0.5",
					"harvestcraft:pamPlum;5;10;15;20;0.5", "harvestcraft:pamPomegranate;5;10;15;20;0.5",
					"harvestcraft:pamStarfruit;5;10;15;20;0.5", "harvestcraft:pamVanillabean;5;10;15;20;0.5",
					"harvestcraft:pamWalnut;5;10;15;20;0.5" };
			addSyncedList(GameplayOption.HIBERNATING, hibernateDefault, CROP_TWEAKS,
					"List of hibernating crops with configurable min waking, "
							+ "min optimal, max optimal, max waking temps, "
							+ "and the chance of skipping a growth tick outside "
							+ "of the optimal temperature range. ;-delimited");
		} catch (Exception e) {
			ToughAsNails.logger.error("Tough As Nails has encountered a problem loading gameplay.cfg", e);
		} finally {
			if (config.hasChanged()) {
				config.save();
			}
		}
	}

	private static void addSyncedBool(GameplayOption option, boolean defaultValue, String category, String comment) {
		boolean value = config.getBoolean(option.getOptionName(), category, defaultValue, comment);
		SyncedConfig.addOption(option, "" + value);
	}

	private static void addSyncedInt(GameplayOption option, int defaultValue, String category, String comment) {
		int value = config.getInt(option.getOptionName(), category, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE,
				comment);
		SyncedConfig.addOption(option, "" + value);
	}

	private static void addSyncedFloat(GameplayOption option, float defaultValue, String category, String comment) {
		float value = config.getFloat(option.getOptionName(), category, defaultValue, Float.MIN_VALUE, Float.MAX_VALUE,
				comment);
		SyncedConfig.addOption(option, "" + value);
	}

	private static void addSyncedList(GameplayOption option, String[] defaultValue, String category, String comment) {
		String[] drinkEntries = config.getStringList(option.getOptionName(), category, defaultValue, comment);
		String drinkString = "";
		for (String drinkEntry : drinkEntries) {
			drinkString += (drinkEntry + ",");
		}
		SyncedConfig.addOption(option, drinkString);
	}

	@SubscribeEvent
	public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equalsIgnoreCase(ToughAsNails.MOD_ID)) {
			loadConfiguration();
		}
	}
}
