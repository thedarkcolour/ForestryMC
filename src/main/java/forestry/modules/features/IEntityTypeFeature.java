package forestry.modules.features;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public interface IEntityTypeFeature<E extends Entity> extends IModFeature {
	AttributeSupplier.Builder createAttributes();

	EntityType<E> entityType();
}
