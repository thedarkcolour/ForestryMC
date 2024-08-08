package forestry.plugin.client;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

import forestry.api.arboriculture.ForestryTreeSpecies;
import forestry.api.client.plugin.IClientRegistration;
import forestry.arboriculture.client.BiomeLeafTint;
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
		ResourceLocation itemModel = new ResourceLocation(modId, "item/" + path);
		registration.setSaplingModel(speciesId, blockModel, itemModel);
	}

	private static void registerLepidopterology(IClientRegistration client) {
		// todo
	}
}
