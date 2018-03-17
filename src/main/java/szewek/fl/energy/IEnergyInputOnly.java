package szewek.fl.energy;

import javax.annotation.Nonnull;

public interface IEnergyInputOnly extends IEnergy {

	@Override
	default boolean canOutputEnergy() {
		return false;
	}

	@Override
	default long outputEnergy(long amount, boolean sim) {
		return 0;
	}

	@Override
	default long to(@Nonnull IEnergy ie, final long amount) {
		return 0;
	}
}
