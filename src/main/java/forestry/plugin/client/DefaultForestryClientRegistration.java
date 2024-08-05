package forestry.plugin.client;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

import forestry.api.ForestryConstants;
import forestry.api.arboriculture.ForestryTreeSpecies;
import forestry.api.client.plugin.IClientRegistration;
import forestry.arboriculture.client.BiomeLeafTint;
import forestry.arboriculture.client.FixedLeafTint;
import forestry.arboriculture.models.LeafSprite;

public class DefaultForestryClientRegistration implements Consumer<IClientRegistration> {
	@Override
	public void accept(IClientRegistration client) {
		registerArboriculture(client);
		registerLepidopterology(client);
	}

	private static void registerArboriculture(IClientRegistration client) {
		// Vanilla sapling models
		registerSapling(client, "minecraft", ForestryTreeSpecies.OAK);
		registerSapling(client, "minecraft", ForestryTreeSpecies.DARK_OAK);
		registerSapling(client, "minecraft", ForestryTreeSpecies.BIRCH);
		registerSapling(client, "minecraft", ForestryTreeSpecies.ACACIA_VANILLA);
		registerSapling(client, "minecraft", ForestryTreeSpecies.SPRUCE);
		registerSapling(client, "minecraft", ForestryTreeSpecies.JUNGLE);

		// Forestry sapling models
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.LIME);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.WALNUT);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.CHESTNUT);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.CHERRY);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.LEMON);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.PLUM);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.MAPLE);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.LARCH);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.PINE);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.SEQUOIA);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.GIANT_SEQUOIA);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.TEAK);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.IPE);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.KAPOK);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.EBONY);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.ZEBRAWOOD);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.MAHOGANY);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.DESERT_ACACIA);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.PADAUK);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.BALSA);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.COCOBOLO);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.WENGE);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.BAOBAB);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.MAHOE);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.WILLOW);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.SIPIRI);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.PAPAYA);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.DATE);
		registerSapling(client, ForestryConstants.MOD_ID, ForestryTreeSpecies.POPLAR);

		// Vanilla leaf sprites
		client.setLeafSprite(ForestryTreeSpecies.OAK, LeafSprite.OAK);
		client.setLeafSprite(ForestryTreeSpecies.DARK_OAK, LeafSprite.OAK);
		client.setLeafSprite(ForestryTreeSpecies.BIRCH, LeafSprite.BIRCH);
		client.setLeafSprite(ForestryTreeSpecies.ACACIA_VANILLA, LeafSprite.ACACIA);
		client.setLeafSprite(ForestryTreeSpecies.SPRUCE, LeafSprite.SPRUCE);
		client.setLeafSprite(ForestryTreeSpecies.JUNGLE, LeafSprite.JUNGLE);

		// Forestry leaf sprites
		client.setLeafSprite(ForestryTreeSpecies.LIME, LeafSprite.BIRCH);
		client.setLeafSprite(ForestryTreeSpecies.WALNUT, LeafSprite.ACACIA);
		client.setLeafSprite(ForestryTreeSpecies.CHESTNUT, LeafSprite.BIRCH);
		client.setLeafSprite(ForestryTreeSpecies.CHERRY, LeafSprite.BIRCH);
		client.setLeafSprite(ForestryTreeSpecies.LEMON, LeafSprite.OAK);
		client.setLeafSprite(ForestryTreeSpecies.PLUM, LeafSprite.OAK);
		client.setLeafSprite(ForestryTreeSpecies.MAPLE, LeafSprite.MAPLE);
		client.setLeafSprite(ForestryTreeSpecies.LARCH, LeafSprite.SPRUCE);
		client.setLeafSprite(ForestryTreeSpecies.PINE, LeafSprite.SPRUCE);
		client.setLeafSprite(ForestryTreeSpecies.SEQUOIA, LeafSprite.SPRUCE);
		client.setLeafSprite(ForestryTreeSpecies.GIANT_SEQUOIA, LeafSprite.SPRUCE);
		client.setLeafSprite(ForestryTreeSpecies.TEAK, LeafSprite.JUNGLE);
		client.setLeafSprite(ForestryTreeSpecies.IPE, LeafSprite.JUNGLE);
		client.setLeafSprite(ForestryTreeSpecies.KAPOK, LeafSprite.JUNGLE);
		client.setLeafSprite(ForestryTreeSpecies.EBONY, LeafSprite.JUNGLE);
		client.setLeafSprite(ForestryTreeSpecies.ZEBRAWOOD, LeafSprite.JUNGLE);
		client.setLeafSprite(ForestryTreeSpecies.MAHOGANY, LeafSprite.JUNGLE);
		client.setLeafSprite(ForestryTreeSpecies.DESERT_ACACIA, LeafSprite.ACACIA);
		client.setLeafSprite(ForestryTreeSpecies.PADAUK, LeafSprite.ACACIA);
		client.setLeafSprite(ForestryTreeSpecies.BALSA, LeafSprite.ACACIA);
		client.setLeafSprite(ForestryTreeSpecies.COCOBOLO, LeafSprite.MANGROVE);
		client.setLeafSprite(ForestryTreeSpecies.WENGE, LeafSprite.OAK);
		client.setLeafSprite(ForestryTreeSpecies.BAOBAB, LeafSprite.ACACIA);
		client.setLeafSprite(ForestryTreeSpecies.MAHOE, LeafSprite.OAK);
		client.setLeafSprite(ForestryTreeSpecies.WILLOW, LeafSprite.WILLOW);
		client.setLeafSprite(ForestryTreeSpecies.SIPIRI, LeafSprite.MANGROVE);
		client.setLeafSprite(ForestryTreeSpecies.PAPAYA, LeafSprite.PALM);
		client.setLeafSprite(ForestryTreeSpecies.DATE, LeafSprite.PALM);
		client.setLeafSprite(ForestryTreeSpecies.POPLAR, LeafSprite.BIRCH);

		// Vanilla leaf tints
		client.setLeafTint(ForestryTreeSpecies.OAK, BiomeLeafTint.INSTANCE);
		client.setLeafTint(ForestryTreeSpecies.DARK_OAK, BiomeLeafTint.INSTANCE);
		client.setLeafTint(ForestryTreeSpecies.JUNGLE, BiomeLeafTint.INSTANCE);
		client.setLeafTint(ForestryTreeSpecies.ACACIA_VANILLA, BiomeLeafTint.INSTANCE);
	}

	private static void registerSapling(IClientRegistration registration, String modId, ResourceLocation speciesId) {
		// remove the "tree/" prefix and add "_sapling"
		String path = speciesId.getPath().substring(5) + "_sapling";
		ResourceLocation blockModel = new ResourceLocation(modId, "block/" + path);
		ResourceLocation itemModel = new ResourceLocation(modId, path);
		registration.setSaplingModel(speciesId, blockModel, itemModel);
	}

	private static void registerLepidopterology(IClientRegistration client) {
		// todo
	}
}
