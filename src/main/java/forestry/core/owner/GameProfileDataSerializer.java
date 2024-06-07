package forestry.core.owner;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;

import com.mojang.authlib.GameProfile;

public enum GameProfileDataSerializer implements EntityDataSerializer<Optional<GameProfile>> {
	INSTANCE;

	@Override
	public void write(FriendlyByteBuf buf, Optional<GameProfile> value) {
		if (!value.isPresent()) {
			buf.writeBoolean(false);
		} else {
			buf.writeBoolean(true);
			GameProfile gameProfile = value.get();
			buf.writeUUID(gameProfile.getId());
			buf.writeUtf(gameProfile.getName());
		}
	}

	@Override
	public Optional<GameProfile> read(FriendlyByteBuf buf) {
		if (buf.readBoolean()) {
			UUID uuid = buf.readUUID();
			String name = buf.readUtf(1024);
			GameProfile gameProfile = new GameProfile(uuid, name);
			return Optional.of(gameProfile);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public EntityDataAccessor<Optional<GameProfile>> createAccessor(int id) {
		return new EntityDataAccessor<>(id, this);
	}

	@Override
	public Optional<GameProfile> copy(Optional<GameProfile> value) {
		return value;
	}
}
