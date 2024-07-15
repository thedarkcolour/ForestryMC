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
package forestry.apiculture;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.Forestry;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IApiaristTracker;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeHousingInventory;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeekeepingLogic;
import forestry.api.apiculture.genetics.BeeLifeStage;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.apiculture.genetics.IBeeSpeciesType;
import forestry.api.core.IErrorLogic;
import forestry.api.core.IError;
import forestry.api.genetics.IEffectData;
import forestry.apiculture.features.ApicultureItems;
import forestry.apiculture.network.packets.PacketBeeLogicActive;
import forestry.core.config.Constants;
import forestry.api.core.ForestryError;
import forestry.core.utils.NetworkUtil;

import forestry.api.genetics.IGenome;

import genetics.api.GeneticHelper;
import genetics.api.individual.IIndividual;
import forestry.api.genetics.ILifeStage;
import genetics.organism.OrganismHandler;

public class BeekeepingLogic implements IBeekeepingLogic {
	private static final int totalBreedingTime = Constants.APIARY_BREEDING_TIME;

	private final IBeeHousing housing;
	private final IBeeModifier beeModifier;
	private final IBeeListener beeListener;

	private int beeProgress;
	private int beeProgressMax;

	private int queenWorkCycleThrottle;
	private IEffectData[] effectData = new IEffectData[2];

	private final Stack<ItemStack> spawn = new Stack<>();

	private final HasFlowersCache hasFlowersCache = new HasFlowersCache();
	private final QueenCanWorkCache queenCanWorkCache = new QueenCanWorkCache();
	private final PollenHandler pollenHandler = new PollenHandler();

	// Client
	private boolean active;
	@Nullable
	private IBee queen;
	private ItemStack queenStack = ItemStack.EMPTY; // used to detect server changes and sync clientQueen

	public BeekeepingLogic(IBeeHousing housing) {
		this.housing = housing;
		this.beeModifier = BeeManager.beeRoot.createBeeHousingModifier(housing);
		this.beeListener = BeeManager.beeRoot.createBeeHousingListener(housing);
	}

	// / SAVING & LOADING
	@Override
	public void read(CompoundTag compoundNBT) {
		beeProgress = compoundNBT.getInt("BreedingTime");
		queenWorkCycleThrottle = compoundNBT.getInt("Throttle");

		if (compoundNBT.contains("queen")) {
			CompoundTag queenNBT = compoundNBT.getCompound("queen");
			queenStack = ItemStack.of(queenNBT);
			queen = BeeManager.beeRoot.create(queenStack);
		}

		setActive(compoundNBT.getBoolean("Active"));

		hasFlowersCache.read(compoundNBT);

		ListTag nbttaglist = compoundNBT.getList("Offspring", 10);
		for (int i = 0; i < nbttaglist.size(); i++) {
			spawn.add(ItemStack.of(nbttaglist.getCompound(i)));
		}
	}

	@Override
	public CompoundTag write(CompoundTag compoundNBT) {
		compoundNBT.putInt("BreedingTime", beeProgress);
		compoundNBT.putInt("Throttle", queenWorkCycleThrottle);

		if (!queenStack.isEmpty()) {
			CompoundTag queenNBT = new CompoundTag();
			queenStack.save(queenNBT);
			compoundNBT.put("queen", queenNBT);
		}

		compoundNBT.putBoolean("Active", active);

		hasFlowersCache.write(compoundNBT);

		Stack<ItemStack> spawnCopy = new Stack<>();
		spawnCopy.addAll(spawn);
		ListTag nbttaglist = new ListTag();
		while (!spawnCopy.isEmpty()) {
			CompoundTag compoundNBT1 = new CompoundTag();
			spawnCopy.pop().save(compoundNBT1);
			nbttaglist.add(compoundNBT1);
		}
		compoundNBT.put("Offspring", nbttaglist);
		return compoundNBT;
	}

	@Override
	public void writeData(FriendlyByteBuf data) {
		data.writeBoolean(active);
		if (active) {
			data.writeItem(queenStack);
			hasFlowersCache.writeData(data);
		}
	}

	@Override
	public void readData(FriendlyByteBuf data) {
		boolean active = data.readBoolean();
		setActive(active);
		if (active) {
			queenStack = data.readItem();
			queen = BeeManager.beeRoot.create(queenStack);
			hasFlowersCache.readData(data);
		}
	}

	/* Activatable */
	private void setActive(boolean active) {
		if (this.active == active) {
			return;
		}
		this.active = active;

		syncToClient();
	}

	/* UPDATING */

	@Override
	public boolean canWork() {

		IErrorLogic errorLogic = housing.getErrorLogic();
		errorLogic.clearErrors();

		IBeeHousingInventory beeInventory = housing.getBeeInventory();

		boolean hasSpace = addPendingProducts(beeInventory, spawn);
		errorLogic.setCondition(!hasSpace, ForestryError.NO_SPACE_INVENTORY);

		ItemStack queenStack = beeInventory.getQueen();
		ILifeStage beeType = BeeManager.beeRoot.getTypes().getType(queenStack);
		// check if we're breeding
		if (beeType == BeeLifeStage.PRINCESS) {
			boolean hasDrone = BeeManager.beeRoot.isDrone(beeInventory.getDrone());
			errorLogic.setCondition(!hasDrone, ForestryError.NO_DRONE);

			setActive(false); // not active (no bee FX) when we are breeding
			return !errorLogic.hasErrors();
		}
		if (beeType == BeeLifeStage.QUEEN) {
			if (!isQueenAlive(queenStack)) {
				IBee dyingQueen = BeeManager.beeRoot.create(queenStack);
				if (dyingQueen != null) {
					Collection<ItemStack> spawned = killQueen(dyingQueen, housing, beeListener);
					spawn.addAll(spawned);
				}
				queenStack = ItemStack.EMPTY;
			}
		} else {
			queenStack = ItemStack.EMPTY;
		}

		if (this.queenStack != queenStack) {
			if (!queenStack.isEmpty()) {
				this.queen = BeeManager.beeRoot.create(queenStack);
				if (this.queen != null) {
					hasFlowersCache.onNewQueen(queen, housing);
				}
			} else {
				this.queen = null;
			}
			this.queenStack = queenStack;
			queenCanWorkCache.clear();
		}

		if (errorLogic.setCondition(queen == null, ForestryError.NO_QUEEN)) {
			setActive(false);
			beeProgress = 0;
			return false;
		}

		Set<IError> queenErrors = queenCanWorkCache.queenCanWork(queen, housing);
		for (IError errorState : queenErrors) {
			errorLogic.setCondition(true, errorState);
		}

		hasFlowersCache.update(queen, housing);

		boolean hasFlowers = hasFlowersCache.hasFlowers();
		boolean flowerCacheNeedsSync = hasFlowersCache.needsSync();
		errorLogic.setCondition(!hasFlowers, ForestryError.NO_FLOWER);

		boolean canWork = !errorLogic.hasErrors();
		if (active != canWork) {
			setActive(canWork);
		} else if (flowerCacheNeedsSync) {
			syncToClient();
		}
		return canWork;
	}

	@Override
	public void doWork() {
		IBeeHousingInventory beeInventory = housing.getBeeInventory();
		ItemStack queenStack = beeInventory.getQueen();
		ILifeStage beeType = BeeManager.beeRoot.getTypes().getType(queenStack);
		if (beeType != null) {
			if (beeType == BeeLifeStage.PRINCESS) {
				tickBreed();
			} else if (beeType == BeeLifeStage.QUEEN) {
				queenWorkTick(queen, queenStack);
			}
		}
	}

	@Override
	public void clearCachedValues() {
		if (!housing.getWorldObj().isClientSide) {
			queenCanWorkCache.clear();
			canWork();
			if (queen != null) {
				hasFlowersCache.forceLookForFlowers(queen, housing);
			}
		}
	}

	private void queenWorkTick(@Nullable IBee queen, ItemStack queenStack) {
		if (queen == null) {
			beeProgress = 0;
			beeProgressMax = 0;
			return;
		}

		// Effects only fire when queen can work.
		effectData = queen.doEffect(effectData, housing);

		// Work cycles are throttled, rather than occurring every game tick.
		queenWorkCycleThrottle++;
		if (queenWorkCycleThrottle >= ModuleApiculture.ticksPerBeeWorkCycle) {
			queenWorkCycleThrottle = 0;

			doProduction(queen, housing, beeListener);
			Level world = housing.getWorldObj();
			List<BlockState> flowers = hasFlowersCache.getFlowers(world);
			if (flowers.size() < ModuleApiculture.maxFlowersSpawnedPerHive) {
				BlockPos blockPos = queen.plantFlowerRandom(housing, flowers);
				if (blockPos != null) {
					hasFlowersCache.addFlowerPos(blockPos);
				}
			}
			pollenHandler.doPollination(queen, housing, beeListener);

			// Age the queen
			IGenome mate = queen.getMate();
			float lifespanModifier = beeModifier.getLifespanModifier(queen.getGenome(), mate, 1.0f);
			queen.age(world, lifespanModifier);

			// Write the changed queen back into the item stack.
			GeneticHelper.setIndividual(queenStack, queen);
			housing.getBeeInventory().setQueen(queenStack);
		}

		beeProgress = queen.getHealth();
		beeProgressMax = queen.getMaxHealth();
	}

	private static void doProduction(IBee queen, IBeeHousing beeHousing, IBeeListener beeListener) {
		// Produce and add stacks
		List<ItemStack> products = queen.produceStacks(beeHousing);
		beeListener.wearOutEquipment(1);

		IBeeHousingInventory beeInventory = beeHousing.getBeeInventory();

		for (ItemStack stack : products) {
			beeInventory.addProduct(stack, false);
		}
	}

	private static boolean addPendingProducts(IBeeHousingInventory beeInventory, Stack<ItemStack> spawn) {
		boolean housingHasSpace = true;

		while (!spawn.isEmpty()) {
			ItemStack next = spawn.peek();
			if (beeInventory.addProduct(next, true)) {
				spawn.pop();
			} else {
				housingHasSpace = false;
				break;
			}
		}

		return housingHasSpace;
	}

	/**
	 * Checks if a queen is alive. Much faster than reading the whole bee nbt
	 */
	private static boolean isQueenAlive(ItemStack queenStack) {
		if (queenStack.isEmpty()) {
			return false;
		}
		CompoundTag compound = queenStack.getTag();
		if (compound == null) {
			return false;
		}
		CompoundTag individualTag = compound.getCompound(OrganismHandler.INDIVIDUAL_KEY);
		if (individualTag.isEmpty()) {
			return false;
		}
		int health = individualTag.getInt("Health");
		return health > 0;
	}

	// / BREEDING
	private void tickBreed() {
		beeProgressMax = totalBreedingTime;

		IBeeHousingInventory beeInventory = housing.getBeeInventory();

		ItemStack droneStack = beeInventory.getDrone();
		ItemStack princessStack = beeInventory.getQueen();

		IBeeSpeciesType root = BeeManager.beeRoot;
		ILifeStage droneType = root.getTypes().getType(droneStack);
		ILifeStage princessType = root.getTypes().getType(princessStack);
		if (droneType != BeeLifeStage.DRONE || princessType != BeeLifeStage.PRINCESS) {
			beeProgress = 0;
			return;
		}

		if (beeProgress < totalBreedingTime) {
			beeProgress++;
		}
		if (beeProgress < totalBreedingTime) {
			return;
		}

		// Mate and replace princess with queen
		IBee princess = BeeManager.beeRoot.create(princessStack);
		IBee drone = BeeManager.beeRoot.create(droneStack);
		princess.mate(drone.getGenome());

		queenStack = ApicultureItems.BEE_QUEEN.stack();
		princess.copyTo(queenStack);

		beeInventory.setQueen(queenStack);

		// Register the new queen with the breeding tracker
		BeeManager.beeRoot.getBreedingTracker(housing.getWorldObj(), housing.getOwner()).registerQueen(princess);

		// Remove drone
		droneStack.shrink(1);
		if (droneStack.isEmpty()) {
			beeInventory.setDrone(ItemStack.EMPTY);
		}

		// Reset breeding time
		queen = princess;
		beeProgress = princess.getHealth();
		beeProgressMax = princess.getMaxHealth();
	}

	private static Collection<ItemStack> killQueen(IBee queen, IBeeHousing beeHousing, IBeeListener beeListener) {
		IBeeHousingInventory beeInventory = beeHousing.getBeeInventory();

		Collection<ItemStack> spawn;

		if (queen.canSpawn()) {
			spawn = spawnOffspring(queen, beeHousing);
			beeListener.onQueenDeath();
			beeInventory.getQueen().setCount(0);
		} else {
			Forestry.LOGGER.warn("Tried to spawn offspring off an unmated queen. Devolving her to a princess.");

			ItemStack convert = ApicultureItems.BEE_PRINCESS.stack();
			GeneticHelper.setIndividual(convert, queen);

			spawn = Collections.singleton(convert);
		}
		beeInventory.setQueen(ItemStack.EMPTY);

		return spawn;
	}

	/**
	 * Creates the succeeding princess and between one and three drones.
	 */
	private static Collection<ItemStack> spawnOffspring(IBee queen, IBeeHousing beeHousing) {

		Level world = beeHousing.getWorldObj();

		Stack<ItemStack> offspring = new Stack<>();
		IApiaristTracker breedingTracker = BeeManager.beeRoot.getBreedingTracker(world, beeHousing.getOwner());

		// Princess
		boolean secondPrincess = world.random.nextInt(10000) < ModuleApiculture.getSecondPrincessChance() * 100;
		int count = secondPrincess ? 2 : 1;
		while (count > 0) {
			count--;
			IBee heiress = queen.spawnPrincess(beeHousing);
			if (heiress != null) {
				ItemStack princess = BeeManager.beeRoot.getTypes().createStack(heiress, BeeLifeStage.PRINCESS);
				breedingTracker.registerPrincess(heiress);
				offspring.push(princess);
			}
		}

		// Drones
		List<IBee> drones = queen.spawnDrones(beeHousing);
		for (IBee drone : drones) {
			ItemStack droneStack = BeeManager.beeRoot.getTypes().createStack(drone, BeeLifeStage.DRONE);
			breedingTracker.registerDrone(drone);
			offspring.push(droneStack);
		}

		IBeeHousingInventory beeInventory = beeHousing.getBeeInventory();

		Collection<ItemStack> spawn = new ArrayList<>();

		while (!offspring.isEmpty()) {
			ItemStack spawned = offspring.pop();
			if (!beeInventory.addProduct(spawned, true)) {
				spawn.add(spawned);
			}
		}

		return spawn;
	}

	/* CLIENT */

	@Override
	public void syncToClient() {
		Level level = housing.getWorldObj();
		if (level != null && !level.isClientSide) {
			NetworkUtil.sendNetworkPacket(new PacketBeeLogicActive(housing), housing.getCoordinates(), level);
		}
	}

	@Override
	public void syncToClient(ServerPlayer player) {
		Level level = housing.getWorldObj();
		if (level != null && !level.isClientSide) {
			NetworkUtil.sendToPlayer(new PacketBeeLogicActive(housing), player);
		}
	}

	@Override
	public int getBeeProgressPercent() {
		if (beeProgressMax == 0) {
			return 0;
		}

		return Math.round(beeProgress * 100f / beeProgressMax);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canDoBeeFX() {
		return !Minecraft.getInstance().isPaused() && active;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doBeeFX() {
		if (queen != null) {
			queen.doFX(effectData, housing);
		}
	}

	@Override
	public List<BlockPos> getFlowerPositions() {
		return hasFlowersCache.getFlowerCoords();
	}

	private static class QueenCanWorkCache {
		private static final int ticksPerCheckQueenCanWork = 10;

		private Set<IError> queenCanWorkCached = Collections.emptySet();
		private int queenCanWorkCooldown = 0;

		public Set<IError> queenCanWork(IBee queen, IBeeHousing beeHousing) {
			if (queenCanWorkCooldown <= 0) {
				queenCanWorkCached = queen.getCanWork(beeHousing);
				queenCanWorkCooldown = ticksPerCheckQueenCanWork;
			} else {
				queenCanWorkCooldown--;
			}

			return queenCanWorkCached;
		}

		public void clear() {
			queenCanWorkCached.clear();
			queenCanWorkCooldown = 0;
		}
	}

	private static class PollenHandler {
		private static final int MAX_POLLINATION_ATTEMPTS = 20;

		@Nullable
		private IIndividual pollen;
		private int attemptedPollinations = 0;

		public void doPollination(IBee queen, IBeeHousing beeHousing, IBeeListener beeListener) {
			// Get pollen if none available yet
			if (pollen == null) {
				attemptedPollinations = 0;
				pollen = queen.retrievePollen(beeHousing);
				if (pollen != null) {
					if (beeListener.onPollenRetrieved(pollen)) {
						pollen = null;
					}
				}
			}

			if (pollen != null) {
				attemptedPollinations++;
				if (queen.pollinateRandom(beeHousing, pollen) || attemptedPollinations >= MAX_POLLINATION_ATTEMPTS) {
					pollen = null;
				}
			}
		}
	}

}
