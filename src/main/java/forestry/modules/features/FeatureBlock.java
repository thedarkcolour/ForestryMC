package forestry.modules.features;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.properties.Property;

import net.minecraftforge.registries.RegistryObject;

import forestry.core.config.Constants;

public class FeatureBlock<B extends Block, I extends BlockItem> extends ModFeature implements IBlockFeature<B, I> {
	private final RegistryObject<B> blockObject;
	@Nullable
	private final RegistryObject<I> itemObject;

	public FeatureBlock(IFeatureRegistry features, String moduleID, String identifier, Supplier<B> constructorBlock, @Nullable Function<B, I> constructorItem) {
		super(moduleID, features.getModId(), identifier);
		this.blockObject = features.getRegistry(Registry.BLOCK_REGISTRY).register(identifier, constructorBlock);
		this.itemObject = constructorItem == null ? null : features.getRegistry(Registry.ITEM_REGISTRY).register(identifier, () -> constructorItem.apply(blockObject.get()));
	}

	public String getTranslationKey() {
		return blockObject.map(Block::getDescriptionId).orElse("block." + Constants.MOD_ID + "." + identifier.replace('/', '.'));
	}

	@Override
	public BlockState defaultState() {
		return block().defaultBlockState();
	}

	@Override
	public <V extends Comparable<V>> BlockState setValue(Property<V> property, V value) {
		return defaultState().setValue(property, value);
	}

	@Override
	public B block() {
		return blockObject.get();
	}

	@Override
	public I item() {
		return Objects.requireNonNull(itemObject, () -> "Missing item for block: " + identifier).get();
	}

	@Override
	public ResourceKey<? extends Registry<?>> getRegistry() {
		return Registry.BLOCK_REGISTRY;
	}
}
