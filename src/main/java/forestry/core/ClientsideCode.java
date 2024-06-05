package forestry.core;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeManager;

import forestry.apiculture.proxy.ProxyApiculture;
import forestry.apiculture.proxy.ProxyApicultureClient;
import forestry.arboriculture.proxy.ProxyArboriculture;
import forestry.arboriculture.proxy.ProxyArboricultureClient;
import forestry.climatology.proxy.ProxyClimatology;
import forestry.climatology.proxy.ProxyClimatologyClient;
import forestry.core.genetics.root.ClientBreedingHandler;
import forestry.core.genetics.root.ServerBreedingHandler;
import forestry.core.proxy.ProxyClient;
import forestry.core.proxy.ProxyCommon;
import forestry.core.proxy.ProxyRender;
import forestry.core.proxy.ProxyRenderClient;
import forestry.cultivation.proxy.ProxyCultivation;
import forestry.cultivation.proxy.ProxyCultivationClient;
import forestry.factory.recipes.ClientCraftingHelper;
import forestry.farming.proxy.ProxyFarming;
import forestry.farming.proxy.ProxyFarmingClient;
import forestry.lepidopterology.proxy.ProxyLepidopterology;
import forestry.lepidopterology.proxy.ProxyLepidopterologyClient;
import forestry.modules.ClientModuleHandler;
import forestry.modules.CommonModuleHandler;
import forestry.storage.proxy.ProxyStorage;
import forestry.storage.proxy.ProxyStorageClient;

// Rule of thumb for safely calling client code: client classes must be in the body of a static method
// in another class (like this one), guarded by an if statement checking FMLEnvironment.dist == Dist.CLIENT
// Calls to this class must be guarded by an if statement.
// DistExecutor will be deprecated for removal in 1.20, and it doesn't work anyway.
public class ClientsideCode {
	public static ProxyArboriculture newProxyArboriculture() {
		return new ProxyArboricultureClient();
	}

	public static ProxyClimatology newProxyClimatology() {
		return new ProxyClimatologyClient();
	}

	public static ProxyLepidopterology newProxyLepidopterology() {
		return new ProxyLepidopterologyClient();
	}

	public static ProxyApiculture newProxyApiculture() {
		return new ProxyApicultureClient();
	}

	public static ProxyCultivation newProxyCultivation() {
		return new ProxyCultivationClient();
	}

	public static ProxyFarming newProxyProxyFarming() {
		return new ProxyFarmingClient();
	}

	public static ProxyStorage newProxyStorage() {
		return new ProxyStorageClient();
	}

	public static ProxyRender newProxyRender() {
		return new ProxyRenderClient();
	}

	public static ProxyCommon newProxyCommon() {
		return new ProxyClient();
	}

	public static ServerBreedingHandler newBreedingHandler() {
		return new ClientBreedingHandler();
	}

	public static CommonModuleHandler newModuleHandler() {
		return new ClientModuleHandler();
	}

	public static RecipeManager craftingHelperAdjust() {
		return ClientCraftingHelper.adjustClient();
	}

	@Nullable
	public static RecipeManager getRecipeManager() {
		ClientPacketListener connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			return connection.getRecipeManager();
		}
		return null;
	}

	public static void markForUpdate(BlockPos pos) {
		Minecraft.getInstance().levelRenderer.setBlocksDirty(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
	}
}
