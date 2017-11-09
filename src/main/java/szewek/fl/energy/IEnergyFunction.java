package szewek.fl.energy;

/**
 * Experimental functional interface.
 */
@FunctionalInterface
public interface IEnergyFunction {
	/**
	 * Gives amount of energy depending on amount and alt parameter.
	 * <br />
	 * Value options:<ul>
	 * <li>{@code (0, false)} - energy available for output</li>
	 * <li>{@code (0, true)} - energy available for input</li>
	 * <li>{@code (Long.MIN_VALUE, false)} - stored energy</li>
	 * <li>{@code (Long.MIN_VALUE, true)} - energy capacity</li>
	 * <li>{@code (any negative, false)} - output</li>
	 * <li>{@code (any negative, true)} - simulated output</li>
	 * <li>{@code (any positive, false)} - input</li>
	 * <li>{@code (any positive, true)} - simulated input</li>
	 * </ul>
	 * @param amount Amount for transfer (0 and {@link java.lang.Long#MIN_VALUE} are special)
	 * @param alt Alternative option switch
	 * @return An amount of energy
	 */
	long transferEnergy(long amount, boolean alt);
}
