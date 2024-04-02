package forestry.modules.features;

public abstract class ModFeature implements IModFeature {
    protected final String moduleId;
    protected final String modId;
    protected final String identifier;

    protected ModFeature(String moduleId, String modId, String identifier) {
        this.moduleId = moduleId;
        this.modId = modId;
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public String getModId() {
        return this.modId;
    }

    @Override
    public String getModuleId() {
        return this.moduleId;
    }
}
