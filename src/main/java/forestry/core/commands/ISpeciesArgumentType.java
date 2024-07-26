package forestry.core.commands;

import com.google.gson.JsonObject;

import java.util.function.Function;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

import com.mojang.brigadier.arguments.ArgumentType;

import forestry.api.IForestryApi;
import forestry.api.genetics.ISpeciesType;

// Abstracts away the common serialization code between SpeciesArgument and LifeStageArgument
public interface ISpeciesArgumentType<A> extends ArgumentType<A> {
	ISpeciesType<?, ?> getType();

	// Serializes and deserializes using only a ISpeciesType
	class Serializer<A extends ISpeciesArgumentType<?>> implements ArgumentTypeInfo<A, Serializer<A>.Template> {
		private final Function<ISpeciesType<?, ?>, A> factory;

		public Serializer(Function<ISpeciesType<?, ?>, A> factory) {
			this.factory = factory;
		}

		@Override
		public void serializeToNetwork(Serializer.Template template, FriendlyByteBuf buffer) {
			buffer.writeResourceLocation(template.type.id());
		}

		@Override
		public Serializer<A>.Template deserializeFromNetwork(FriendlyByteBuf buffer) {
			return new Template(IForestryApi.INSTANCE.getGeneticManager().getSpeciesType(buffer.readResourceLocation()));
		}

		@Override
		public void serializeToJson(Serializer.Template template, JsonObject json) {
			json.addProperty("species_type", template.type.id().toString());
		}

		@Override
		public Serializer<A>.Template unpack(A argument) {
			return new Template(argument.getType());
		}

		public class Template implements ArgumentTypeInfo.Template<A> {
			private final ISpeciesType<?, ?> type;

			public Template(ISpeciesType<?, ?> type) {
				this.type = type;
			}

			@Override
			public A instantiate(CommandBuildContext context) {
				return Serializer.this.factory.apply(this.type);
			}

			@Override
			public ArgumentTypeInfo<A, Serializer<A>.Template> type() {
				return Serializer.this;
			}
		}
	}
}
