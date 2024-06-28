package forestry.core.utils;

import java.util.Optional;
import java.util.function.Supplier;

import net.minecraft.network.chat.Component;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

public class Translator {
	private Translator() {}

	public static boolean canTranslateToLocal(String key) {
		return Language.getInstance().has(key);
	}

	public static MutableComponent tryTranslate(String optionalKey, String defaultKey) {
		return tryTranslate(optionalKey, () -> Component.translatable(defaultKey));
	}

	public static MutableComponent tryTranslate(String optionalKey, Supplier<MutableComponent> defaultKey) {
		TranslatableContents contents = new TranslatableContents(optionalKey);
		boolean translationFailed = contents.visit(s -> Optional.of(optionalKey.equals(s))).orElse(false);

		if (translationFailed) {
			return defaultKey.get();
		} else {
			return MutableComponent.create(contents);
		}
	}
}
