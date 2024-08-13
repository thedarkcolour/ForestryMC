package forestry.core.worldgen;

import java.util.List;

import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.data.worldgen.VillagePools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;

import forestry.api.ForestryConstants;

public class VillagerJigsaw {
	public static void init() {
		VillagePools.bootstrap();

		addVillagerHouse("plains", 15);
		addVillagerHouse("snowy", 15);
		addVillagerHouse("savanna", 15);
		addVillagerHouse("desert", 15);
		addVillagerHouse("taiga", 15);
	}

	private static void addVillagerHouse(String biome, int weight) {
		addToJigsawPattern(new ResourceLocation("village/" + biome + "/houses"), new ApiaristPoolElement(Either.left(ForestryConstants.forestry("village/apiarist_house_" + biome + "_1")), ProcessorLists.EMPTY), weight);
	}

	public static void addToJigsawPattern(ResourceLocation pool, StructurePoolElement newPiece, int weight) {
		StructureTemplatePool oldPool = BuiltinRegistries.TEMPLATE_POOL.get(pool);
		if (oldPool != null) {
			oldPool.rawTemplates.add(Pair.of(newPiece, weight));
			List<StructurePoolElement> jigsawPieces = oldPool.templates;

			for (int i = 0; i < weight; ++i) {
				jigsawPieces.add(newPiece);
			}
		}
	}
}
