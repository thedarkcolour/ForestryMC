package forestry.core.commands;

import forestry.api.ForestryConstants;
import forestry.arboriculture.commands.CommandTreeSpawn;
import forestry.core.utils.ForgeUtils;
import forestry.modules.features.FeatureProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@FeatureProvider
public class CommandArgumentTypes {
    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = DeferredRegister.create(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY, ForestryConstants.MOD_ID);

    private static final RegistryObject<ArgumentTypeInfo<?, ?>> SPECIES = ARGUMENT_TYPES.register("species", () -> ArgumentTypeInfos.registerByClass(SpeciesArgument.class, new ISpeciesArgumentType.Serializer<>(SpeciesArgument::new)));
    private static final RegistryObject<ArgumentTypeInfo<?, ?>> LIFE_STAGE = ARGUMENT_TYPES.register("life_stage", () -> ArgumentTypeInfos.registerByClass(LifeStageArgument.class, new LifeStageArgument.Serializer<>(LifeStageArgument::new)));

    static {
        ARGUMENT_TYPES.register(ForgeUtils.modBus());
    }
}
