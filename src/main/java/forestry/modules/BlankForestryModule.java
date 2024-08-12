package forestry.modules;

import java.util.List;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import forestry.api.modules.ForestryModuleIds;
import forestry.api.modules.IForestryModule;

public abstract class BlankForestryModule implements IForestryModule {
	@Override
	public List<ResourceLocation> getModuleDependencies() {
		// todo is this necessary? core module overriding isCore should be sufficient
		return List.of(ForestryModuleIds.CORE);
	}

	@Override
	public String toString() {
		return getId().toString();
	}

	// Called by Forestry's ModuleCore
	public void addToRootCommand(LiteralArgumentBuilder<CommandSourceStack> command) {
	}
}
