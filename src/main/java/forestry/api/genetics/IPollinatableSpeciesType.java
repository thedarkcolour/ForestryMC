package forestry.api.genetics;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import com.mojang.authlib.GameProfile;

/**
 * Indicates to Forestry bees and butterflies that this species may be pollinated.
 */
public interface IPollinatableSpeciesType {
	ICheckPollinatable createPollinatable(IIndividual individual);

	@Nullable
	IPollinatable tryConvertToPollinatable(@Nullable GameProfile owner, Level level, BlockPos pos, IIndividual pollen);
}
