package szewek.fl.energy;

public interface IEnergyOutputOnly extends IEnergy {

	@Override
	default boolean canInputEnergy() {
		return false;
	}

	@Override
	default long inputEnergy(long amount, boolean sim) {
		return 0;
	}
}
