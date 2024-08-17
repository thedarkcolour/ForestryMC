package forestry.apiimpl.client;

import com.google.common.base.Preconditions;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.loading.DatagenModLoader;

import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.api.client.IForestryClientApi;
import forestry.api.client.ITextureManager;
import forestry.api.client.arboriculture.ITreeClientManager;
import forestry.api.client.lepidopterology.IButterflyClientManager;
import forestry.api.client.plugin.IClientHelper;
import forestry.apiimpl.client.plugin.ClientHelper;
import forestry.core.render.ForestryTextureManager;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public class ForestryClientApiImpl implements IForestryClientApi {
	private final ITextureManager textureManager;
	private final IClientHelper helper = new ClientHelper();
	@Nullable
	private ITreeClientManager treeManager;
	@Nullable
	private IButterflyClientManager butterflyManager;

	public ForestryClientApiImpl() {
		Preconditions.checkState(FMLEnvironment.dist == Dist.CLIENT, "Tried to load IForestryClientApi on invalid side " + FMLEnvironment.dist);

		if (DatagenModLoader.isRunningDataGen()) {
			this.textureManager = new DummyTextureManager();
		} else {
			this.textureManager = new ForestryTextureManager();
		}
	}

	@Override
	public ITextureManager getTextureManager() {
		return this.textureManager;
	}

	@Override
	public ITreeClientManager getTreeManager() {
		ITreeClientManager manager = this.treeManager;
		if (manager == null) {
			throw new IllegalStateException("ITreeClientManager not initialized yet");
		}
		return manager;
	}

	@Override
	public IButterflyClientManager getButterflyManager() {
		IButterflyClientManager manager = this.butterflyManager;
		if (manager == null) {
			throw new IllegalStateException("IButterflyClientManager not initialized yet");
		}
		return manager;
	}

	@Override
	public IClientHelper getHelper() {
		return this.helper;
	}

	@ApiStatus.Internal
	public void setTreeManager(ITreeClientManager treeManager) {
		this.treeManager = treeManager;
	}

	public void setButterflyManager(IButterflyClientManager butterflyManager) {
		this.butterflyManager = butterflyManager;
	}
}
