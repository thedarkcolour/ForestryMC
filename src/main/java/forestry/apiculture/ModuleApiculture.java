/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.apiculture;

import java.util.ArrayList;
import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import forestry.api.IForestryApi;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.FlowerManager;
import forestry.api.apiculture.IArmorApiarist;
import forestry.api.apiculture.hives.HiveType;
import forestry.api.modules.ForestryModule;
import forestry.api.modules.ForestryModuleIds;
import forestry.api.modules.IPacketRegistry;
import forestry.apiculture.commands.CommandBee;
import forestry.apiculture.features.ApicultureItems;
import forestry.apiculture.flowers.EndFlowerAcceptableRule;
import forestry.apiculture.flowers.FlowerRegistry;
import forestry.apiculture.genetics.BeeDefinition;
import forestry.apiculture.genetics.BeeFactory;
import forestry.apiculture.genetics.BeeMutationFactory;
import forestry.apiculture.genetics.HiveDrop;
import forestry.apiculture.genetics.JubilanceFactory;
import forestry.apiculture.hives.HiveDefinition;
import forestry.apiculture.items.EnumHoneyComb;
import forestry.apiculture.items.EnumPollenCluster;
import forestry.apiculture.network.packets.PacketAlvearyChange;
import forestry.apiculture.network.packets.PacketBeeLogicActive;
import forestry.apiculture.network.packets.PacketHabitatBiomePointer;
import forestry.apiculture.network.packets.PacketImprintSelectionResponse;
import forestry.apiculture.proxy.ApicultureClientHandler;
import forestry.core.ModuleCore;
import forestry.core.network.PacketIdClient;
import forestry.modules.BlankForestryModule;
import forestry.api.client.IClientModuleHandler;

@ForestryModule
public class ModuleApiculture extends BlankForestryModule {
	public static String beekeepingMode = "NORMAL";
	public static int ticksPerBeeWorkCycle = 550;
	public static boolean hivesDamageOnPeaceful = false;
	public static boolean hivesDamageUnderwater = true;
	public static boolean hivesDamageOnlyPlayers = false;
	public static boolean hiveDamageOnAttack = true;
	public static boolean doSelfPollination = true;
	public static int maxFlowersSpawnedPerHive = 20;

	@Override
	public ResourceLocation getId() {
		return ForestryModuleIds.APICULTURE;
	}

	@Override
	public void registerEvents(IEventBus modBus) {
		MinecraftForge.EVENT_BUS.addListener(ModuleApiculture::onWorldLoad);
		modBus.addListener(ModuleApiculture::registerCapabilities);

		// Commands
		ModuleCore.rootCommand.then(CommandBee.register());
	}

	@Override
	public void setupApi() {
		BeeManager.armorApiaristHelper = new ArmorApiaristHelper();
	}

	@Override
	public void preInit() {

		ApicultureFilterRuleType.init();
		ApicultureFilterRule.init();
	}

	private static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(IArmorApiarist.class);
	}

	@Override
	public void doInit() {
		initFlowerRegistry();

		// Genetics
		BeeDefinition.initBees();

		// Hives
		//createHives();
		registerBeehiveDrops();

		// Inducers for swarmer
		BeeManager.inducers.put(ApicultureItems.ROYAL_JELLY.stack(), 10);

		BeeManager.commonVillageBees.add(BeeDefinition.FOREST.getGenome());
		BeeManager.commonVillageBees.add(BeeDefinition.MEADOWS.getGenome());
		BeeManager.commonVillageBees.add(BeeDefinition.MODEST.getGenome());
		BeeManager.commonVillageBees.add(BeeDefinition.MARSHY.getGenome());
		BeeManager.commonVillageBees.add(BeeDefinition.WINTRY.getGenome());
		BeeManager.commonVillageBees.add(BeeDefinition.TROPICAL.getGenome());

		BeeManager.uncommonVillageBees.add(BeeDefinition.FOREST.getRainResist().getGenome());
		BeeManager.uncommonVillageBees.add(BeeDefinition.COMMON.getGenome());
		BeeManager.uncommonVillageBees.add(BeeDefinition.VALIANT.getGenome());
	}

	// todo replace with tags "acceptable flowers," "plantable flowers," where plantable is subset of acceptable
	private void initFlowerRegistry() {
		FlowerRegistry flowerRegistry = (FlowerRegistry) FlowerManager.flowerRegistry;

		flowerRegistry.registerAcceptableFlowerRule(new EndFlowerAcceptableRule(), FlowerManager.FlowerTypeEnd);

		// Register acceptable plants
		flowerRegistry.registerAcceptableFlower(Blocks.DRAGON_EGG, FlowerManager.FlowerTypeEnd);
		flowerRegistry.registerAcceptableFlower(Blocks.CHORUS_PLANT, FlowerManager.FlowerTypeEnd);
		flowerRegistry.registerAcceptableFlower(Blocks.CHORUS_FLOWER, FlowerManager.FlowerTypeEnd);
		flowerRegistry.registerAcceptableFlower(Blocks.VINE, FlowerManager.FlowerTypeJungle);
		flowerRegistry.registerAcceptableFlower(Blocks.FERN, FlowerManager.FlowerTypeJungle);
		flowerRegistry.registerAcceptableFlower(Blocks.WHEAT, FlowerManager.FlowerTypeWheat);
		flowerRegistry.registerAcceptableFlower(Blocks.PUMPKIN_STEM, FlowerManager.FlowerTypeGourd);
		flowerRegistry.registerAcceptableFlower(Blocks.MELON_STEM, FlowerManager.FlowerTypeGourd);
		flowerRegistry.registerAcceptableFlower(Blocks.NETHER_WART, FlowerManager.FlowerTypeNether);
		flowerRegistry.registerAcceptableFlower(Blocks.CACTUS, FlowerManager.FlowerTypeCacti);

		Block[] standardFlowers = new Block[]{
				Blocks.DANDELION,
				Blocks.POPPY,
				Blocks.BLUE_ORCHID,
				Blocks.ALLIUM,
				Blocks.AZURE_BLUET,
				Blocks.RED_TULIP,
				Blocks.ORANGE_TULIP,
				Blocks.WHITE_TULIP,
				Blocks.PINK_TULIP,
				Blocks.OXEYE_DAISY,
				Blocks.CORNFLOWER,
				Blocks.WITHER_ROSE,
				Blocks.LILY_OF_THE_VALLEY,
		};
		Block[] pottedStandardFlowers = new Block[]{
				Blocks.POTTED_POPPY,
				Blocks.POTTED_BLUE_ORCHID,
				Blocks.POTTED_ALLIUM,
				Blocks.POTTED_AZURE_BLUET,
				Blocks.POTTED_RED_TULIP,
				Blocks.POTTED_ORANGE_TULIP,
				Blocks.POTTED_WHITE_TULIP,
				Blocks.POTTED_PINK_TULIP,
				Blocks.POTTED_OXEYE_DAISY,
				Blocks.POTTED_CORNFLOWER,
				Blocks.POTTED_LILY_OF_THE_VALLEY,
				Blocks.POTTED_WITHER_ROSE,
		};

		// Register plantable plants
		String[] standardTypes = new String[]{FlowerManager.FlowerTypeVanilla, FlowerManager.FlowerTypeSnow};
		for (Block standardFlower : standardFlowers) {
			flowerRegistry.registerPlantableFlower(standardFlower.defaultBlockState(), 1.0, standardTypes);
		}
		flowerRegistry.registerPlantableFlower(Blocks.BROWN_MUSHROOM.defaultBlockState(), 1.0, FlowerManager.FlowerTypeMushrooms);
		flowerRegistry.registerPlantableFlower(Blocks.RED_MUSHROOM.defaultBlockState(), 1.0, FlowerManager.FlowerTypeMushrooms);
		flowerRegistry.registerPlantableFlower(Blocks.CACTUS.defaultBlockState(), 1.0, FlowerManager.FlowerTypeCacti);

		//Flower Pots
		for (Block standardFlower : pottedStandardFlowers) {
			flowerRegistry.registerAcceptableFlower(standardFlower, standardTypes);
		}

		flowerRegistry.registerAcceptableFlower(Blocks.POTTED_RED_MUSHROOM, FlowerManager.FlowerTypeMushrooms);
		flowerRegistry.registerAcceptableFlower(Blocks.POTTED_BROWN_MUSHROOM, FlowerManager.FlowerTypeMushrooms);

		flowerRegistry.registerAcceptableFlower(Blocks.POTTED_CACTUS, FlowerManager.FlowerTypeCacti);
	}

	@Override
	public void registerPackets(IPacketRegistry registry) {
		registry.clientbound(PacketIdClient.IMPRINT_SELECTION_RESPONSE, PacketImprintSelectionResponse.class, PacketImprintSelectionResponse::decode, PacketImprintSelectionResponse::handle);
		registry.clientbound(PacketIdClient.BEE_LOGIC_ACTIVE, PacketBeeLogicActive.class, PacketBeeLogicActive::decode, PacketBeeLogicActive::handle);
		registry.clientbound(PacketIdClient.HABITAT_BIOME_POINTER, PacketHabitatBiomePointer.class, PacketHabitatBiomePointer::decode, PacketHabitatBiomePointer::handle);
		registry.clientbound(PacketIdClient.ALVERAY_CONTROLLER_CHANGE, PacketAlvearyChange.class, PacketAlvearyChange::decode, PacketAlvearyChange::handle);
	}

	@Override
	public void registerRecipes() {
		// BREWING RECIPES
		BrewingRecipeRegistry.addRecipe(
				Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)),
				Ingredient.of(ApicultureItems.POLLEN_CLUSTER.stack(EnumPollenCluster.NORMAL, 1)),
				PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.HEALING));
		BrewingRecipeRegistry.addRecipe(
				Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)),
				Ingredient.of(ApicultureItems.POLLEN_CLUSTER.stack(EnumPollenCluster.CRYSTALLINE, 1)),
				PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.REGENERATION));
	}

	private static void registerBeehiveDrops() {
		ItemStack honeyComb = ApicultureItems.BEE_COMBS.stack(EnumHoneyComb.HONEY, 1);
		IHiveRegistry hiveRegistry = IForestryApi.INSTANCE.getHiveManager().getRegistry();

		hiveRegistry.addDrops(HiveType.FOREST.getId(),
				new HiveDrop(0.80, BeeDefinition.FOREST, honeyComb).setIgnobleShare(0.7),
				new HiveDrop(0.08, BeeDefinition.FOREST.getRainResist(), honeyComb),
				new HiveDrop(0.03, BeeDefinition.VALIANT, honeyComb)
		);

		hiveRegistry.addDrops(HiveType.MEADOWS.getId(),
				new HiveDrop(0.80, BeeDefinition.MEADOWS, honeyComb).setIgnobleShare(0.7),
				new HiveDrop(0.03, BeeDefinition.VALIANT, honeyComb)
		);

		ItemStack parchedComb = ApicultureItems.BEE_COMBS.stack(EnumHoneyComb.PARCHED, 1);
		hiveRegistry.addDrops(HiveType.DESERT.getId(),
				new HiveDrop(0.80, BeeDefinition.MODEST, parchedComb).setIgnobleShare(0.7),
				new HiveDrop(0.03, BeeDefinition.VALIANT, parchedComb)
		);

		ItemStack silkyComb = ApicultureItems.BEE_COMBS.stack(EnumHoneyComb.SILKY, 1);
		hiveRegistry.addDrops(HiveType.JUNGLE.getId(),
				new HiveDrop(0.80, BeeDefinition.TROPICAL, silkyComb).setIgnobleShare(0.7),
				new HiveDrop(0.03, BeeDefinition.VALIANT, silkyComb)
		);

		ItemStack mysteriousComb = ApicultureItems.BEE_COMBS.stack(EnumHoneyComb.MYSTERIOUS, 1);
		hiveRegistry.addDrops(HiveType.END.getId(),
				new HiveDrop(0.90, BeeDefinition.ENDED, mysteriousComb)
		);

		ItemStack frozenComb = ApicultureItems.BEE_COMBS.stack(EnumHoneyComb.FROZEN, 1);
		hiveRegistry.addDrops(HiveType.SNOW.getId(),
				new HiveDrop(0.80, BeeDefinition.WINTRY, frozenComb).setIgnobleShare(0.5),
				new HiveDrop(0.03, BeeDefinition.VALIANT, frozenComb)
		);

		ItemStack mossyComb = ApicultureItems.BEE_COMBS.stack(EnumHoneyComb.MOSSY, 1);
		hiveRegistry.addDrops(HiveType.SWAMP.getId(),
				new HiveDrop(0.80, BeeDefinition.MARSHY, mossyComb).setIgnobleShare(0.4),
				new HiveDrop(0.03, BeeDefinition.VALIANT, mossyComb)
		);
	}

	// todo config
	public static double getSecondPrincessChance() {
		float secondPrincessChance = 0;
		return secondPrincessChance;
	}

	private static void onWorldLoad(LevelEvent.Load event) {
		BeeManager.beeRoot.resetBeekeepingMode();
	}

	@Override
	public void registerClientHandler(Consumer<IClientModuleHandler> registrar) {
		registrar.accept(new ApicultureClientHandler());
	}
}
