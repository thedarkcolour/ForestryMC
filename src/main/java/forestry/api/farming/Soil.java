package forestry.api.farming;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;

public record Soil(ItemStack resource, BlockState soilState) {
}