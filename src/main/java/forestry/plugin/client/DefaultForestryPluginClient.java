package forestry.plugin.client;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

import forestry.api.ForestryConstants;
import forestry.api.arboriculture.ForestryTreeSpecies;
import forestry.api.plugin.client.IClientArboricultureRegistration;
import forestry.api.plugin.client.IClientLepidopterologyRegistration;

public class DefaultForestryPluginClient {
	public static class Arboriculture implements Consumer<IClientArboricultureRegistration> {
		@Override
		public void accept(IClientArboricultureRegistration registration) {
			registerSapling(registration, "minecraft", ForestryTreeSpecies.OAK);
			registerSapling(registration, "minecraft", ForestryTreeSpecies.DARK_OAK);
			registerSapling(registration, "minecraft", ForestryTreeSpecies.BIRCH);
			registerSapling(registration, "minecraft", ForestryTreeSpecies.ACACIA_VANILLA);
			registerSapling(registration, "minecraft", ForestryTreeSpecies.SPRUCE);
			registerSapling(registration, "minecraft", ForestryTreeSpecies.JUNGLE);

			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.LIME);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.WALNUT);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.CHESTNUT);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.CHERRY);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.LEMON);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.PLUM);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.MAPLE);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.LARCH);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.PINE);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.SEQUOIA);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.GIANT_SEQUOIA);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.TEAK);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.IPE);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.KAPOK);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.EBONY);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.ZEBRAWOOD);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.MAHOGANY);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.DESERT_ACACIA);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.PADAUK);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.BALSA);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.COCOBOLO);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.WENGE);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.BAOBAB);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.MAHOE);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.WILLOW);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.SIPIRI);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.PAPAYA);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.DATE);
			registerSapling(registration, ForestryConstants.MOD_ID, ForestryTreeSpecies.POPLAR);
		}

		private static void registerSapling(IClientArboricultureRegistration registration, String modId, ResourceLocation speciesId) {
			// remove the "tree/" prefix and add "_sapling"
			String path = speciesId.getPath().substring(5) + "_sapling";
			ResourceLocation blockModel = new ResourceLocation(modId, "block/" + path);
			ResourceLocation itemModel = new ResourceLocation(modId, path);
			registration.registerSaplingModels(speciesId, blockModel, itemModel);
		}
	}

	public static class Lepidopterology implements Consumer<IClientLepidopterologyRegistration> {
		@Override
		public void accept(IClientLepidopterologyRegistration registration) {
			registration.registerButterflySprite();
		}
	}
}
