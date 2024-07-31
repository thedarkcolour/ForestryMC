package forestry.api.farming;

@FunctionalInterface
public interface IWaterConsumption {
	int get(IFarmHousing housing, float hydrationModifier);
}
