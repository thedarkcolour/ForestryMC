package forestry.core.circuits;

import forestry.api.circuits.ICircuit;
import forestry.factory.circuits.CircuitSpeedUpgrade;

public class Circuits {
	// Managed Farm
	public static ICircuit farmArborealManaged;
	public static ICircuit farmOrchardManaged;
	public static ICircuit farmShroomManaged;
	public static ICircuit farmCropsManaged;
	public static ICircuit farmPeatManaged;
	public static ICircuit farmSucculentManaged;
	public static ICircuit farmPoalesManaged;
	public static ICircuit farmInfernalManaged;
	public static ICircuit farmEnderManaged;
	public static ICircuit farmGourdManaged;
	public static ICircuit farmCocoaManaged;

	// Manual Farm
	public static ICircuit farmArborealManual;
	public static ICircuit farmOrchardManual;
	public static ICircuit farmShroomManual;
	public static ICircuit farmCropsManual;
	public static ICircuit farmPeatManual;
	public static ICircuit farmSucculentManual;
	public static ICircuit farmPoalesManual;
	public static ICircuit farmInfernalManual;
	public static ICircuit farmEnderManual;
	public static ICircuit farmGourdManual;
	public static ICircuit farmCocoaManual;

	// Machine Upgrade
	public static final ICircuit machineSpeedUpgrade1 = new CircuitSpeedUpgrade("machine.speed.boost.1", 0.125f, 0.05f);
	public static final ICircuit machineSpeedUpgrade2 = new CircuitSpeedUpgrade("machine.speed.boost.2", 0.250f, 0.10f);
	public static final ICircuit machineEfficiencyUpgrade1 = new CircuitSpeedUpgrade("machine.efficiency.1", 0, -0.10f);
}
