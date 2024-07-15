package forestry.core.climate;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import net.minecraft.util.Mth;

import forestry.api.IForestryApi;
import forestry.api.climate.ClimateState;
import forestry.api.climate.ClimateType;
import forestry.api.climate.IClimateManipulator;
import forestry.api.climate.IClimatised;

public class ClimateManipulator implements IClimateManipulator {
	@Nullable
	private final ClimateState targetedState;
	private final ClimateState defaultState;
	private final ClimateState currentState;
	private final ClimateState startState;
	private final BiFunction<ClimateType, IClimateManipulator, Float> changeSupplier;
	private final Consumer<ClimateState> onFinish;
	private final boolean backwards;
	private final ClimateType type;

	public ClimateManipulator(@Nullable ClimateState targetedState, @Nullable ClimateState defaultState, @Nullable ClimateState currentState, BiFunction<ClimateType, IClimateManipulator, Float> changeSupplier, Consumer<ClimateState> onFinish, boolean backwards, ClimateType type) {
		ClimateState current = currentState != null ? currentState : defaultState;
		this.targetedState = targetedState;
		this.defaultState = defaultState;
		this.currentState = current;
		this.startState = current;
		this.changeSupplier = changeSupplier;
		this.onFinish = onFinish;
		this.backwards = backwards;
		this.type = type;
	}

	@Override
	public IClimatised getCurrent() {
		return currentState;
	}

	@Override
	public IClimatised getStart() {
		return startState;
	}

	@Override
	public IClimatised getTarget() {
		return targetedState;
	}

	@Override
	public boolean allowsBackwards() {
		return backwards;
	}

	@Override
	public IClimatised getDefault() {
		return defaultState;
	}

	@Override
	public ClimateType getType() {
		return type;
	}

	@Override
	public void finish() {
		if (!currentState.equals(startState)) {
			onFinish.accept(currentState);
		}
	}

	@Override
	public IClimatised addChange(boolean simulated) {
		return applyChange(true, simulated);
	}

	@Override
	public IClimatised removeChange(boolean simulated) {
		return applyChange(false, simulated);
	}

	@Override
	public boolean canAdd() {
		return false;
		//Difference between the targeted state of this method and the current state.
		/*IClimatised difference = targetedState.subtract(startState);
		if (IForestryApi.INSTANCE.getClimateManager().isZero(type, difference)) {
			return true;
		}
		float change = changeSupplier.apply(type, this);
		boolean rightDirection = difference.getClimate(type) > 0.0F && change > 0.0F || difference.getClimate(type) < 0.0F && change < 0.0F;
		if (!rightDirection) {
			IClimatised diffToDefault = startState.subtract(defaultState);
			return backwards || (diffToDefault.getClimate(type) > 0.0F && change > 0.0F || diffToDefault.getClimate(type) < 0.0F && change < 0.0F);
		}
		return true;*/
	}

	/**
	 * Applies the current return value of 'change' to the current climate value of the
	 * given type. Negates the change if the machine had not enough resources to work
	 *
	 * @param worked    If the machine had enough resources to work and can hold the current climate state.
	 * @param simulated If the action should only been simulated.
	 * @return The change that the method would have or has applied to the current state.
	 */
	private IClimatised applyChange(boolean worked, boolean simulated) {
		/*IClimatised target = worked ? targetedState : defaultState;
		//Difference between the targeted state of this method and the current state.
		IClimatised difference = target.subtract(startState);
		//Do nothing if the current state already equals the targeted state of this method.
		if (ClimateStateHelper.isZero(type, difference)) {
			return ClimateStateHelper.ZERO_STATE;
		}
		float change = changeSupplier.apply(type, this);
		//Create a mutable state that contains the current change.
		IClimatised changeState = ClimateStateHelper.INSTANCE.create(type, change).toMutable();
		boolean rightDirection = difference.getClimate(type) > 0.0F && change > 0.0F || difference.getClimate(type) < 0.0F && change < 0.0F;
		if (!rightDirection) {
			IClimatised diffToDefault = startState.subtract(defaultState);
			//Check if 'bothDirections' is true or if the difference to the default state has the same direction like the change state.
			//The Second one allows to go back to the default state if the current target is above, if the change is negative, or below, if the change is positive, the last targeted state.
			if (!worked || backwards || (diffToDefault.getClimate(type) > 0.0F && change > 0.0F || diffToDefault.getClimate(type) < 0.0F && change < 0.0F)) {
				//If so negate the current change so we can go back the targeted state.
				changeState.multiply(-1.0F);
			} else {
				//If not change nothing because we are not allowed to.
				return ClimateStateHelper.ZERO_STATE;
			}
		}
		IClimatised newState = startState.add(changeState);
		IClimatised newDifference = target.subtract(newState);
		float diff = newDifference.getClimate(type);
		//Round up or down to the targeted state if possible
		if (canRound(diff)) {
			changeState.add(type, diff);
		}
		//Add the change state to the current state if this isn't simulated
		if (!simulated) {
			currentState.add(changeState);
		}
		return changeState;*/
		return null;
	}
}
