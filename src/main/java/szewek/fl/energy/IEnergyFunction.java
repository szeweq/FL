package szewek.fl.energy;

import javax.annotation.Nonnull;

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
	long queryEnergy(long amount, boolean alt);

	default long to(@Nonnull IEnergyFunction ief, final long amount) {
		if (amount > 0 && queryEnergy(0, false) > 0 && ief.queryEnergy(0, true) > 0) {
			long r = ief.queryEnergy(queryEnergy(amount, true), true);
			if (r > 0)
				return ief.queryEnergy(queryEnergy(amount, false), false);
		}
		return 0;
	}
}
