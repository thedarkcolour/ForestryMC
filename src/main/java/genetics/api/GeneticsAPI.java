package genetics.api;

public class GeneticsAPI {
	/**
	 * An object that contains getter-methods almost every registry of the genetics mod.
	 * Please call {@link IGeneticApiInstance#isModPresent()} before you use one of the other methods.
	 * The methods will also throw an {@link IllegalStateException} if you call them to early in the
	 * registration cycle.
	 */
	public static IGeneticApiInstance apiInstance;
}
