package forestry.core.commands;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;

import net.minecraftforge.registries.DeferredRegister;

import forestry.api.modules.ForestryModuleIds;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class CommandArgumentTypes {
	static {
		DeferredRegister<ArgumentTypeInfo<?, ?>> registry = ModFeatureRegistry.get(ForestryModuleIds.CORE).getRegistry(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY);

		registry.register("species", () -> ArgumentTypeInfos.registerByClass(SpeciesArgument.class, new ISpeciesArgumentType.Serializer<>(SpeciesArgument::new)));
		registry.register("life_stage", () -> ArgumentTypeInfos.registerByClass(LifeStageArgument.class, new LifeStageArgument.Serializer<>(LifeStageArgument::new)));
		registry.register("chromosome", () -> ArgumentTypeInfos.registerByClass(ChromosomeArgument.class, new ChromosomeArgument.Serializer<>(ChromosomeArgument::new)));
		registry.register("allele", () -> ArgumentTypeInfos.registerByClass(AlleleArgument.class, new AlleleArgument.Serializer<>(AlleleArgument::new)));
	}
}
