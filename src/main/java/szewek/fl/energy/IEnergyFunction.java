package szewek.fl.energy;

/**
 * Experimental functional interface.
 */
@FunctionalInterface
public interface IEnergyFunction {
	/**
	 * Gives amount of energy depending on amount and mode parameters.
	 * <br />
	 * Modes:<ul>
	 * <li>0 - output</li>
	 * <li>1 - input</li>
	 * <li>2 - available for output (omits amount parameter)</li>
	 * <li>3 - available for input (omits amount parameter)</li>
	 * <li>4 - simulated output</li>
	 * <li>5 - simulated input</li>
	 * <li>6 - stored energy (omits amount parameter)</li>
	 * <li>7 - energy capacity (omits amount parameter)</li>
	 * </ul>
	 *
	 * @param amount Amount of energy (positive number)
	 * @param mode Mode
	 * @return Amount of energy
	 */
	long moveEnergy(long amount, byte mode);

	byte
			MODE_OUT = 0,
			MODE_IN = 1,
			MODE_CAN_OUT = 2,
			MODE_CAN_IN = 3,
			MODE_SIM_OUT = 4,
			MODE_SIM_IN = 5,
			MODE_ENERGY = 6,
			MODE_CAP = 7;
}
