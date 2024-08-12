package forestry.modules.features;

import net.minecraft.resources.ResourceLocation;

public abstract class ModFeature implements IModFeature {
    protected final ResourceLocation moduleId;
    protected final String name;

    protected ModFeature(ResourceLocation moduleId, String name) {
        this.moduleId = moduleId;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ResourceLocation getModuleId() {
        return this.moduleId;
    }
}
