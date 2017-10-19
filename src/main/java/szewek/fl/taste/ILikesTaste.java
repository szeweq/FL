package szewek.fl.taste;

public interface ILikesTaste {
	/**
	 * Checks if object can accept Taste input.
	 *
	 * @param t Taste object (amount is ignored).
	 * @return {@code true} if accepted, otherwise {@code false}.
	 */
	boolean canInputTaste(Taste t);

	/**
	 * Checks if object can accept Taste output.
	 *
	 * @param t Taste object (amount is ignored).
	 * @return {@code true} if accepted, otherwise {@code false}.
	 */
	boolean canOutputTaste(Taste t);

	/**
	 * Taste input method
	 *
	 * @param t Taste object with specified amount available to input.
	 * @param sim Simulation switch. Set to {@code false} only when you need to check actual energy input value.
	 * @return Amount of Flavor Energy sent to object.
	 */
	long inputTaste(Taste t, boolean sim);

	/**
	 * Taste output method (if energy data is known).
	 *
	 * @param t Taste object with specified maximum amount to be output.
	 * @param sim Simulation switch. Set to {@code false} only when you need to check actual energy input value.
	 * @return Amount of Flavor Energy received from object.
	 */
	long outputTaste(Taste t, boolean sim);

	/**
	 * Taste output method (if energy data is unknown).
	 *
	 * @param amount Taste amount
	 * @param sim    Simulation switch. Set to {@code false} only when you need to check actual energy input value.
	 * @return A taste.
	 */
	Taste outputAnyTaste(long amount, boolean sim);

	/**
	 * Getter for Taste amount specified by data.
	 *
	 * @param t Taste object (amount is ignored).
	 * @return Stored Taste amount.
	 */
	long getTasteAmount(Taste t);

	/**
	 * Getter for Taste capacity specified by data.
	 *
	 * @param t Taste object (amount is ignored).
	 * @return Amount of Taste capacity.
	 */
	long getTasteCapacity(Taste t);

	/**
	 * Lists all tastes that object currently contains.
	 * @return Array of Taste objects (amount is ignored).
	 */
	Taste[] allTastesContained();

	/**
	 * Lists all tastes that object can accept.
	 * @return Array of Taste objects (amount is ignored).
	 */
	Taste[] allTastesAcceptable();

	default long to(ILikesTaste ilt, Taste t, final long amount) {
		if (ilt != null && ilt.canInputTaste(t) && canOutputTaste(t)) {
			final long r = ilt.inputTaste(t.makeImmutable(outputTaste(t.makeImmutable(amount), true)), true);
			if (r > 0)
				return ilt.inputTaste(t.makeImmutable(outputTaste(t.makeImmutable(amount), false)), false);
		}
		return 0;
	}
}
