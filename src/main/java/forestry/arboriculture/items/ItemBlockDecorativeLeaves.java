package forestry.arboriculture.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.core.ItemGroups;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.arboriculture.blocks.BlockAbstractLeaves;
import forestry.arboriculture.blocks.BlockDecorativeLeaves;
import forestry.arboriculture.blocks.ForestryLeafType;
import forestry.core.items.ItemBlockForestry;
import forestry.core.items.definitions.IColoredItem;

import forestry.api.genetics.IGenome;

public class ItemBlockDecorativeLeaves extends ItemBlockForestry<BlockDecorativeLeaves> implements IColoredItem {
	public ItemBlockDecorativeLeaves(BlockDecorativeLeaves block) {
		super(block, new Item.Properties().tab(ItemGroups.tabArboriculture));
	}

	@Override
	public Component getName(ItemStack itemStack) {
		BlockDecorativeLeaves block = getBlock();
		ForestryLeafType treeDefinition = block.getType();
		return ItemBlockLeaves.getDisplayName(treeDefinition.getIndividual().getSpecies());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getColorFromItemStack(ItemStack itemStack, int renderPass) {
		BlockDecorativeLeaves block = getBlock();
		ForestryLeafType leafType = block.getType();

		IGenome genome = leafType.getIndividual().getGenome();

		if (renderPass == BlockAbstractLeaves.FRUIT_COLOR_INDEX) {
			IFruit fruitProvider = genome.getActiveValue(TreeChromosomes.FRUITS);
			return fruitProvider.getDecorativeColor();
		}
		return genome.getActiveValue(TreeChromosomes.SPECIES).getLeafSpriteProvider().getColor(false);
	}
}
