package forestry.core.commands;

import forestry.apiculture.commands.CommandBeeGive;
import forestry.arboriculture.commands.CommandTreeSpawn;
import forestry.core.config.Constants;
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
    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = DeferredRegister.create(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY, Constants.MOD_ID);

    private static final RegistryObject<ArgumentTypeInfo<?, ?>> MODULE = ARGUMENT_TYPES.register("module", () -> ArgumentTypeInfos.registerByClass(CommandModules.CommandPluginsInfo.ModuleArgument.class, SingletonArgumentInfo.contextFree(CommandModules.CommandPluginsInfo.ModuleArgument::modules)));
    private static final RegistryObject<ArgumentTypeInfo<?, ?>> BEE = ARGUMENT_TYPES.register("bee", () -> ArgumentTypeInfos.registerByClass(CommandBeeGive.BeeArgument.class, SingletonArgumentInfo.contextFree(CommandBeeGive.BeeArgument::beeArgument)));
    private static final RegistryObject<ArgumentTypeInfo<?, ?>> TREE = ARGUMENT_TYPES.register("tree", () -> ArgumentTypeInfos.registerByClass(CommandTreeSpawn.TreeArugment.class, SingletonArgumentInfo.contextFree(CommandTreeSpawn.TreeArugment::treeArgument)));

    static {
        ARGUMENT_TYPES.register(ForgeUtils.modBus());
    }
}
