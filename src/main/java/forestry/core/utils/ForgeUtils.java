package forestry.core.utils;

import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ForgeUtils {
	private static final Lazy<IEventBus> MOD_BUS = Lazy.of(() -> FMLJavaModLoadingContext.get().getModEventBus());

	private ForgeUtils() {
	}

	public static void postEvent(Event event) {
		modBus().post(event);
	}

	public static IEventBus modBus() {
		return MOD_BUS.get();
	}
}
