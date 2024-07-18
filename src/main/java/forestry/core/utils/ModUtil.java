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
package forestry.core.utils;

import javax.annotation.Nullable;
import java.util.Optional;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.VersionRange;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

import forestry.api.ForestryConstants;


public abstract class ModUtil {
	public static boolean isModLoaded(String modname) {
		return ModList.get().isLoaded(modname);
	}

	public static boolean isModLoaded(String modname, @Nullable String versionRangeString) {
		if (!isModLoaded(modname)) {
			return false;
		}

		if (versionRangeString != null) {
			Optional<? extends ModContainer> cont = ModList.get().getModContainerById(modname);
			if (cont.isPresent()) {
				ModContainer modContainer = cont.get();

				ArtifactVersion modVersion = modContainer.getModInfo().getVersion();

				VersionRange range = VersionRange.createFromVersion(versionRangeString);
				DefaultArtifactVersion requiredVersion = new DefaultArtifactVersion(versionRangeString);    //TODO - check

				return requiredVersion.compareTo(modVersion) > 0; //TODO - this comparison is incorrect
			}
		}

		return true;
	}

	// Creates a Forestry resource location
	public static ResourceLocation modLoc(String path) {
		return ForestryConstants.forestry(path);
	}

	public static ResourceLocation getRegistryName(Fluid o) {
		return ForgeRegistries.FLUIDS.getKey(o);
	}

	public static ResourceLocation getRegistryName(Block o) {
		return ForgeRegistries.BLOCKS.getKey(o);
	}

	public static ResourceLocation getRegistryName(Item o) {
		return ForgeRegistries.ITEMS.getKey(o);
	}

	public static ResourceLocation getRegistryName(ParticleType<?> o) {
		return ForgeRegistries.PARTICLE_TYPES.getKey(o);
	}
}
