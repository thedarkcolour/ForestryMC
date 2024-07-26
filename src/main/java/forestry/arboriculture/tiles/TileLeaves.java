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
package forestry.arboriculture.tiles;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.PlantType;

import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.arboriculture.ILeafSpriteProvider;
import forestry.api.arboriculture.ILeafTickHandler;
import forestry.api.arboriculture.ITreekeepingMode;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.IEffectData;
import forestry.api.genetics.IFruitBearer;
import forestry.api.genetics.IFruitFamily;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IPollinatable;
import forestry.api.genetics.alleles.ButterflyChromosomes;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.api.lepidopterology.IButterflyNursery;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.apiculture.ModuleApiculture;
import forestry.arboriculture.features.ArboricultureTiles;
import forestry.arboriculture.network.IRipeningPacketReceiver;
import forestry.arboriculture.network.PacketRipeningUpdate;
import forestry.core.network.packets.PacketTileStream;
import forestry.core.utils.ColourUtil;
import forestry.core.utils.GeneticsUtil;
import forestry.core.utils.NetworkUtil;
import forestry.core.utils.RenderUtil;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.IGenome;
import forestry.core.utils.SpeciesUtil;

public class TileLeaves extends TileTreeContainer implements IPollinatable, IFruitBearer, IButterflyNursery, IRipeningPacketReceiver {
	private static final String NBT_RIPENING = "RT";
	private static final String NBT_DAMAGE = "ENC";
	private static final String NBT_FRUIT_LEAF = "FL";
	private static final String NBT_MATURATION = "CATMAT";
	private static final String NBT_CATERPILLAR = "CATER";

	public static final ModelProperty<ILeafSpriteProvider> SPRITE_PROVIDER = new ModelProperty<>();
	public static final ModelProperty<Boolean> POLLINATED = new ModelProperty<>();
	public static final ModelProperty<ResourceLocation> FRUIT_TEXTURE = new ModelProperty<>();

	private int colourFruits;

	@Nullable
	private ResourceLocation fruitSprite;
	@Nullable
	private ITreeSpecies species;
	@Nullable
	private IButterfly caterpillar;

	private boolean isFruitLeaf;
	private boolean checkFruit = true;
	private boolean isPollinatedState;
	private int ripeningTime;
	private short ripeningPeriod = Short.MAX_VALUE - 1;

	private int maturationTime;
	private int damage;

	private IEffectData[] effectData = new IEffectData[2];

	public TileLeaves(BlockPos pos, BlockState state) {
		super(ArboricultureTiles.LEAVES.tileType(), pos, state);
	}

	/* SAVING & LOADING */
	@Override
	public void load(CompoundTag compoundNBT) {
		super.load(compoundNBT);

		this.ripeningTime = compoundNBT.getShort(NBT_RIPENING);
		this.damage = compoundNBT.getInt(NBT_DAMAGE);
		this.isFruitLeaf = compoundNBT.getBoolean(NBT_FRUIT_LEAF);
		this.checkFruit = !compoundNBT.contains(NBT_FRUIT_LEAF, Tag.TAG_ANY_NUMERIC);

		if (compoundNBT.contains(NBT_CATERPILLAR)) {
			this.maturationTime = compoundNBT.getInt(NBT_MATURATION);
			this.caterpillar = SpeciesUtil.BUTTERFLY_TYPE.get().create(compoundNBT.getCompound(NBT_CATERPILLAR));
		}

		ITree tree = getTree();
		if (tree != null) {
			setTree(tree);
		}
	}

	@Override
	public void saveAdditional(CompoundTag compoundNBT) {
		super.saveAdditional(compoundNBT);

		compoundNBT.putInt(NBT_RIPENING, getRipeningTime());
		compoundNBT.putInt(NBT_DAMAGE, damage);
		compoundNBT.putBoolean(NBT_FRUIT_LEAF, isFruitLeaf);

		if (caterpillar != null) {
			compoundNBT.putInt(NBT_MATURATION, maturationTime);

			CompoundTag caterpillarNbt = new CompoundTag();
			caterpillar.write(caterpillarNbt);
			compoundNBT.put(NBT_CATERPILLAR, caterpillarNbt);
		}
	}

	@Override
	public void onBlockTick(Level worldIn, BlockPos pos, BlockState state, RandomSource rand) {
		ITree tree = getTree();
		if (tree == null) {
			return;
		}

		IGenome genome = tree.getGenome();
		IAlleleTreeSpecies primary = genome.getActiveAllele(TreeChromosomes.SPECIES);

		boolean isDestroyed = isDestroyed(tree, damage);
		for (ILeafTickHandler tickHandler : primary.getSpecies().getLeafTickHandlers()) {
			if (tickHandler.onRandomLeafTick(tree, level, rand, getBlockPos(), isDestroyed)) {
				return;
			}
		}

		if (isDestroyed) {
			return;
		}

		if (damage > 0) {
			damage--;
		}

		if (hasFruit() && getRipeningTime() < ripeningPeriod) {
			ITreekeepingMode treekeepingMode = SpeciesUtil.TREE_TYPE.get().getTreekeepingMode(level);
			float sappinessModifier = treekeepingMode.getSappinessModifier(genome, 1f);
			float sappiness = genome.getActiveValue(TreeChromosomes.SAPPINESS) * sappinessModifier;

			if (rand.nextFloat() < sappiness) {
				ripeningTime++;
				sendNetworkUpdateRipening();
			}
		}

		if (caterpillar != null) {
			matureCaterpillar();
		}

		effectData = tree.doEffect(effectData, level, getBlockPos());
	}

	@Override
	public void setTree(ITree tree) {
		ITree oldTree = getTree();
		super.setTree(tree);

		IGenome genome = tree.getGenome();
		this.species = genome.getActiveValue(TreeChromosomes.SPECIES);

		if (oldTree != null && !tree.equals(oldTree)) {
			checkFruit = true;
		}

		if (tree.canBearFruit() && checkFruit && level != null && !level.isClientSide) {
			IFruit fruitProvider = genome.getActiveValue(TreeChromosomes.FRUITS);
			if (fruitProvider.isFruitLeaf(genome, level, getBlockPos())) {
				isFruitLeaf = fruitProvider.getFruitChance(genome, level, getBlockPos()) >= level.random.nextFloat();
			}
		}

		if (isFruitLeaf) {
			IFruit fruitProvider = genome.getActiveValue(TreeChromosomes.FRUITS);
			if (this.level != null && this.level.isClientSide) {
				this.fruitSprite = fruitProvider.getSprite(genome, this.level, getBlockPos(), getRipeningTime());
			}

			this.ripeningPeriod = (short) fruitProvider.getRipeningPeriod();
		} else if (this.level != null && this.level.isClientSide) {
			this.fruitSprite = null;
		}
		requestModelDataUpdate();

		setChanged();
	}

	/* INFORMATION */
	private static boolean isDestroyed(@Nullable ITree tree, int damage) {
		return tree != null && damage > tree.getResilience();
	}

	@Override
	public boolean isPollinated() {
		ITree tree = getTree();
		return tree != null && !isDestroyed(tree, damage) && tree.getMate() != null;
	}

	@OnlyIn(Dist.CLIENT)
	public int getFoliageColour(Player player) {
		final boolean showPollinated = isPollinatedState && GeneticsUtil.hasNaturalistEye(player);
		final int baseColor = getLeafSpriteProvider().getColor(showPollinated);

		ITree tree = getTree();
		if (isDestroyed(tree, damage)) {
			return ColourUtil.addRGBComponents(baseColor, 92, 61, 0);
		} else if (caterpillar != null) {
			return ColourUtil.multiplyRGBComponents(baseColor, 1.5f);
		} else {
			return baseColor;
		}
	}

	public int getFruitColour() {
		if (colourFruits == 0 && hasFruit()) {
			colourFruits = determineFruitColour();
		}
		return colourFruits;
	}

	private int determineFruitColour() {
		ITree tree = getTree();
		if (tree == null) {
			tree = TreeDefinition.Cherry.createIndividual();
		}
		IGenome genome = tree.getGenome();
		IFruit fruit = genome.getActiveAllele(TreeChromosomes.FRUITS).getProvider();
		return fruit.getColour(genome, level, getBlockPos(), getRipeningTime());
	}

	@OnlyIn(Dist.CLIENT)
	public ResourceLocation getLeaveSprite(boolean fancy) {
		final ILeafSpriteProvider leafSpriteProvider = getLeafSpriteProvider();
		return leafSpriteProvider.getSprite(isPollinatedState, fancy);
	}

	@OnlyIn(Dist.CLIENT)
	private ILeafSpriteProvider getLeafSpriteProvider() {
		if (species != null) {
			return species.getLeafSpriteProvider();
		} else {
			IAlleleTreeSpecies oakSpecies = TreeDefinition.Oak.createIndividual().getGenome().getActiveAllele(TreeChromosomes.SPECIES);
			return oakSpecies.getLeafSpriteProvider();
		}
	}

	@Nullable
	public ResourceLocation getFruitSprite() {
		return fruitSprite;
	}

	@OnlyIn(Dist.CLIENT)
	public static ResourceLocation getLeaveSprite(ModelData data, boolean fancy) {
		final ILeafSpriteProvider leafSpriteProvider = getLeafSpriteProvider(data);
		final Boolean pollinated = data.get(POLLINATED);
		return leafSpriteProvider.getSprite(pollinated != null && pollinated, fancy);
	}

	@OnlyIn(Dist.CLIENT)
	private static ILeafSpriteProvider getLeafSpriteProvider(ModelData data) {
		final ILeafSpriteProvider leafSpriteProvider = data.get(SPRITE_PROVIDER);
		if (leafSpriteProvider != null) {
			return leafSpriteProvider;
		} else {
			IAlleleTreeSpecies oakSpecies = TreeDefinition.Oak.createIndividual().getGenome().getActiveAllele(TreeChromosomes.SPECIES);
			return oakSpecies.getLeafSpriteProvider();
		}
	}

	@Nullable
	public static ResourceLocation getFruitSprite(ModelData data) {
		return data.get(FRUIT_TEXTURE);
	}

	@Override
	public ModelData getModelData() {
        ModelData.Builder builder = ModelData.builder();
		builder.with(SPRITE_PROVIDER, getLeafSpriteProvider());
		builder.with(POLLINATED, isPollinatedState);
		builder.with(FRUIT_TEXTURE, fruitSprite);
		return builder.build();
	}

	public int getRipeningTime() {
		return ripeningTime;
	}

	/* IPOLLINATABLE */
	@Override
	public PlantType getPlantType() {
		ITree tree = getTree();
		if (tree == null) {
			return PlantType.PLAINS;
		}

		return tree.getGenome().getActiveAllele(TreeChromosomes.SPECIES).getPlantType();
	}

	@Override
	public boolean canMateWith(IIndividual individual) {
		if (individual instanceof ITree) {
			ITree tree = getTree();
			return tree != null && tree.getMate() == null && (ModuleApiculture.doSelfPollination || !tree.isGeneticEqual(individual));
		}
		return false;
	}

	@Override
	public void mateWith(IIndividual individual) {
		if (individual instanceof ITree) {
			ITree tree = getTree();
			if (tree == null || level == null) {
				return;
			}

			tree.setMate(individual.getGenome());
			if (!level.isClientSide) {
				sendNetworkUpdate();
			}
		}
	}

	@Override
	public ITree getPollen() {
		return getTree();
	}

	/* NETWORK */
	private void sendNetworkUpdate() {
		NetworkUtil.sendNetworkPacket(new PacketTileStream(this), worldPosition, level);
	}

	private void sendNetworkUpdateRipening() {
		if (isRemoved()) {
			return;
		}
		int newColourFruits = determineFruitColour();
		if (newColourFruits == colourFruits) {
			return;
		}
		colourFruits = newColourFruits;

		PacketRipeningUpdate ripeningUpdate = new PacketRipeningUpdate(this);
		NetworkUtil.sendNetworkPacket(ripeningUpdate, worldPosition, level);
	}

	private static final short hasFruitFlag = 1;
	private static final short isPollinatedFlag = 1 << 1;

	@Override
	public void writeData(FriendlyByteBuf data) {
		super.writeData(data);

		byte leafState = 0;
		boolean hasFruit = hasFruit();

		if (isPollinated()) {
			leafState |= isPollinatedFlag;
		}

		if (hasFruit) {
			leafState |= hasFruitFlag;
		}

		data.writeByte(leafState);

		if (hasFruit) {
			String fruitAlleleUID = getTree().getGenome().getActiveAllele(TreeChromosomes.FRUITS).alleleId().toString();
			int colourFruits = getFruitColour();

			data.writeUtf(fruitAlleleUID);
			data.writeInt(colourFruits);
		}
	}

	@Override
	public void readData(FriendlyByteBuf data) {

		String speciesUID = data.readUtf(); // this is called instead of super.readData, be careful!

		byte leafState = data.readByte();
		isFruitLeaf = (leafState & hasFruitFlag) > 0;
		isPollinatedState = (leafState & isPollinatedFlag) > 0;
		String fruitAlleleUID = null;

		if (isFruitLeaf) {
			fruitAlleleUID = data.readUtf();
			colourFruits = data.readInt();
		}

		IAllele[] treeTemplate = SpeciesUtil.TREE_TYPE.get().getTemplates().getTemplate(speciesUID);
		if (treeTemplate != null) {
			if (fruitAlleleUID != null) {
				AlleleUtils.actOn(new ResourceLocation(fruitAlleleUID), IFruit.class, fruitAllele -> treeTemplate[TreeChromosomes.FRUITS.ordinal()] = fruitAllele);
			}

			ITree tree = SpeciesUtil.TREE_TYPE.get().templateAsIndividual(treeTemplate);
			if (isPollinatedState) {
				tree.setMate(tree.getGenome());
			}

			setTree(tree);

			RenderUtil.markForUpdate(worldPosition);
			//world.markBlockRangeForRenderUpdate(getPos(), getPos());
		}
	}

	@Override
	public void fromRipeningPacket(int newColourFruits) {
		if (newColourFruits == colourFruits) {
			return;
		}
		colourFruits = newColourFruits;
		RenderUtil.markForUpdate(worldPosition);
	}

	/* IFRUITBEARER */
	@Override
	public List<ItemStack> pickFruit(ItemStack tool) {
		ITree tree = getTree();
		if (tree == null || !hasFruit()) {
			return NonNullList.create();
		}

		List<ItemStack> produceStacks = tree.produceStacks(level, getBlockPos(), getRipeningTime());
		ripeningTime = 0;
		sendNetworkUpdateRipening();
		return produceStacks;
	}

	@Override
	public float getRipeness() {
		if (ripeningPeriod == 0) {
			return 1.0f;
		}
		if (getTree() == null) {
			return 0f;
		}
		return (float) getRipeningTime() / ripeningPeriod;
	}

	@Override
	public boolean hasFruit() {
		return this.isFruitLeaf && !isDestroyed(getTree(), damage);
	}

	@Override
	public void addRipeness(float add) {
		if (getTree() == null || !this.isFruitLeaf || getRipeningTime() >= this.ripeningPeriod) {
			return;
		}
		this.ripeningTime += this.ripeningPeriod * add;
		sendNetworkUpdateRipening();
	}

	@Nullable
	public String getSpeciesUID() {
		if (species == null) {
			return null;
		}
		return species.getId().toString();
	}

	/* IBUTTERFLYNURSERY */

	private void matureCaterpillar() {
		if (caterpillar == null) {
			return;
		}
		maturationTime++;

		ITree tree = getTree();
		boolean wasDestroyed = isDestroyed(tree, damage);
		damage += caterpillar.getGenome().getActiveValue(ButterflyChromosomes.METABOLISM);

		IGenome caterpillarGenome = caterpillar.getGenome();
		int caterpillarMatureTime = Math.round((float) caterpillarGenome.getActiveValue(ButterflyChromosomes.LIFESPAN) / (caterpillarGenome.getActiveValue(ButterflyChromosomes.FERTILITY) * 2));

		if (maturationTime >= caterpillarMatureTime) {
			SpeciesUtil.BUTTERFLY_TYPE.get().plantCocoon(level, worldPosition, caterpillar, getOwnerHandler().getOwner(), 0, false);
			setCaterpillar(null);
		} else if (!wasDestroyed && isDestroyed(tree, damage)) {
			sendNetworkUpdate();
		}
	}

	@Override
	public BlockPos getCoordinates() {
		return getBlockPos();
	}

	@Override
	@Nullable
	public IButterfly getCaterpillar() {
		return caterpillar;
	}

	@Override
	public IIndividual getNanny() {
		return getTree();
	}

	@Override
	public void setCaterpillar(@Nullable IButterfly caterpillar) {
		maturationTime = 0;
		this.caterpillar = caterpillar;
		sendNetworkUpdate();
	}

	@Override
	public boolean canNurse(IButterfly caterpillar) {
		ITree tree = getTree();
		return !isDestroyed(tree, damage) && this.caterpillar == null;
	}

	@Override
	public Holder<Biome> getBiome() {
		Level level = Objects.requireNonNull(this.level);
		return level.getBiome(worldPosition).value();
	}

	@Override
	public TemperatureType temperature() {
		return TemperatureType.getFromBiome(getBiome(), worldPosition);
	}

	@Override
	public HumidityType humidity() {
		return HumidityType.getFromValue(getBiome().getDownfall());
	}

	@Override
	public Level getWorldObj() {
		return level;
	}
}
