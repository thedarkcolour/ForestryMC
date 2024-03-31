package genetics;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import genetics.api.GeneticsAPI;
import genetics.api.IGeneTemplate;
import genetics.api.organism.IOrganism;
import genetics.api.root.IRootDefinition;
import genetics.api.root.components.DefaultStage;
import genetics.commands.CommandListAlleles;
import genetics.plugins.PluginManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.RegisterEvent;

public class Genetics {
	/**
	 * Capability for {@link IOrganism}.
	 */
	public static Capability<IOrganism<?>> ORGANISM = CapabilityManager.get(new CapabilityToken<>() {
	});
	public static Capability<IGeneTemplate> GENE_TEMPLATE = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static void initGenetics(IEventBus modBus) {
		GeneticsAPI.apiInstance = ApiInstance.INSTANCE;
        modBus.addListener(Genetics::registerCapabilities);
        modBus.addListener(EventPriority.HIGHEST, Genetics::registerBlocks);
		modBus.addListener(Genetics::setupCommon);
		modBus.addListener(Genetics::loadComplete);
		MinecraftForge.EVENT_BUS.addListener(Genetics::registerCommands);

        // Should be as early as possible since plugins are detected at the annotation level
        PluginManager.scanPlugins();
        PluginManager.initPlugins();
    }

	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(IOrganism.class);
		event.register(IGeneTemplate.class);
	}

	public static void registerBlocks(RegisterEvent event) {
		if (Registry.ITEM_REGISTRY.equals(event.getRegistryKey())) {
			for (IRootDefinition<?> definition : GeneticsAPI.apiInstance.getRoots().values()) {
				if (!definition.isPresent()) {
					continue;
				}
				definition.get().getComponentContainer().onStage(DefaultStage.REGISTRATION);
			}
		}
	}

	private static void setupCommon(FMLCommonSetupEvent event) {
		for (IRootDefinition<?> definition : GeneticsAPI.apiInstance.getRoots().values()) {
			if (!definition.isPresent()) {
				continue;
			}
			definition.get().getComponentContainer().onStage(DefaultStage.SETUP);
		}
	}

	private static void loadComplete(FMLLoadCompleteEvent event) {
		for (IRootDefinition<?> definition : GeneticsAPI.apiInstance.getRoots().values()) {
			if (!definition.isPresent()) {
				continue;
			}
			definition.get().getComponentContainer().onStage(DefaultStage.COMPLETION);
		}
	}

	private static void registerCommands(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
		LiteralArgumentBuilder<CommandSourceStack> rootCommand = LiteralArgumentBuilder.literal("genetics");
		rootCommand.then(CommandListAlleles.register());
		dispatcher.register(rootCommand);
	}
}
