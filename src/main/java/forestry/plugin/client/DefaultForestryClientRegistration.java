package forestry.plugin.client;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

import forestry.api.ForestryConstants;
import forestry.api.arboriculture.ForestryTreeSpecies;
import forestry.api.client.plugin.IClientRegistration;
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
		client.registerLeafSprite(ForestryTreeSpecies.OAK, LeafSprite.OAK);
		client.registerLeafSprite(ForestryTreeSpecies.DARK_OAK, LeafSprite.OAK);
		client.registerLeafSprite(ForestryTreeSpecies.BIRCH, LeafSprite.BIRCH);
		client.registerLeafSprite(ForestryTreeSpecies.ACACIA_VANILLA, LeafSprite.ACACIA);
		client.registerLeafSprite(ForestryTreeSpecies.SPRUCE, LeafSprite.SPRUCE);
		client.registerLeafSprite(ForestryTreeSpecies.JUNGLE, LeafSprite.JUNGLE);

		// Forestry leaf sprites
		client.registerLeafSprite(ForestryTreeSpecies.LIME, LeafSprite.BIRCH);
		client.registerLeafSprite(ForestryTreeSpecies.WALNUT, LeafSprite.ACACIA);
		client.registerLeafSprite(ForestryTreeSpecies.CHESTNUT, LeafSprite.BIRCH);
		client.registerLeafSprite(ForestryTreeSpecies.CHERRY, LeafSprite.BIRCH);
		client.registerLeafSprite(ForestryTreeSpecies.LEMON, LeafSprite.OAK);
		client.registerLeafSprite(ForestryTreeSpecies.PLUM, LeafSprite.OAK);
		client.registerLeafSprite(ForestryTreeSpecies.MAPLE, LeafSprite.MAPLE);
		client.registerLeafSprite(ForestryTreeSpecies.LARCH, LeafSprite.SPRUCE);
		client.registerLeafSprite(ForestryTreeSpecies.PINE, LeafSprite.SPRUCE);
		client.registerLeafSprite(ForestryTreeSpecies.SEQUOIA, LeafSprite.SPRUCE);
		client.registerLeafSprite(ForestryTreeSpecies.GIANT_SEQUOIA, LeafSprite.SPRUCE);
		client.registerLeafSprite(ForestryTreeSpecies.TEAK, LeafSprite.JUNGLE);
		client.registerLeafSprite(ForestryTreeSpecies.IPE, LeafSprite.JUNGLE);
		client.registerLeafSprite(ForestryTreeSpecies.KAPOK, LeafSprite.JUNGLE);
		client.registerLeafSprite(ForestryTreeSpecies.EBONY, LeafSprite.JUNGLE);
		client.registerLeafSprite(ForestryTreeSpecies.ZEBRAWOOD, LeafSprite.JUNGLE);
		client.registerLeafSprite(ForestryTreeSpecies.MAHOGANY, LeafSprite.JUNGLE);
		client.registerLeafSprite(ForestryTreeSpecies.DESERT_ACACIA, LeafSprite.ACACIA);
		client.registerLeafSprite(ForestryTreeSpecies.PADAUK, LeafSprite.ACACIA);
		client.registerLeafSprite(ForestryTreeSpecies.BALSA, LeafSprite.ACACIA);
		client.registerLeafSprite(ForestryTreeSpecies.COCOBOLO, LeafSprite.MANGROVE);
		client.registerLeafSprite(ForestryTreeSpecies.WENGE, LeafSprite.OAK);
		client.registerLeafSprite(ForestryTreeSpecies.BAOBAB, LeafSprite.ACACIA);
		client.registerLeafSprite(ForestryTreeSpecies.MAHOE, LeafSprite.OAK);
		client.registerLeafSprite(ForestryTreeSpecies.WILLOW, LeafSprite.WILLOW);
		client.registerLeafSprite(ForestryTreeSpecies.SIPIRI, LeafSprite.MANGROVE);
		client.registerLeafSprite(ForestryTreeSpecies.PAPAYA, LeafSprite.PALM);
		client.registerLeafSprite(ForestryTreeSpecies.DATE, LeafSprite.PALM);
		client.registerLeafSprite(ForestryTreeSpecies.POPLAR, LeafSprite.BIRCH);
	}

	private static void registerSapling(IClientRegistration registration, String modId, ResourceLocation speciesId) {
		// remove the "tree/" prefix and add "_sapling"
		String path = speciesId.getPath().substring(5) + "_sapling";
		ResourceLocation blockModel = new ResourceLocation(modId, "block/" + path);
		ResourceLocation itemModel = new ResourceLocation(modId, path);
		registration.registerSaplingModel(speciesId, blockModel, itemModel);
	}

	private void registerLepidopterology(IClientRegistration client) {

	}
}
