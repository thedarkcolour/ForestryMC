package forestry.apiimpl.client;

import com.google.common.base.Preconditions;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.loading.DatagenModLoader;

import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.api.client.IForestryClientApi;
import forestry.api.client.ITextureManager;
import forestry.api.client.arboriculture.ITreeClientManager;
import forestry.core.render.ForestryTextureManager;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public class ForestryClientApiImpl implements IForestryClientApi {
	private final ITextureManager textureManager;
	@Nullable
	private ITreeClientManager treeManager;

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

	@ApiStatus.Internal
	public void setTreeManager(ITreeClientManager treeManager) {
		this.treeManager = treeManager;
	}
}
