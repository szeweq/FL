package szewek.fl.energy;

import net.minecraftforge.energy.IEnergyStorage;

import static java.lang.Integer.MAX_VALUE;

public final class ForgeEnergyWrapper implements IEnergy {
	private final IEnergyStorage ies;

	public ForgeEnergyWrapper(IEnergyStorage ies) {
		this.ies = ies;
	}

	@Override
	public boolean canInputEnergy() {
		return ies.canReceive();
	}

	@Override
	public boolean canOutputEnergy() {
		return ies.canExtract();
	}

	@Override
	public long inputEnergy(long amount, boolean sim) {
		return ies.receiveEnergy(amount > MAX_VALUE ? MAX_VALUE : (int) amount, sim);
	}

	@Override
	public long outputEnergy(long amount, boolean sim) {
		return ies.extractEnergy(amount > MAX_VALUE ? MAX_VALUE : (int) amount, sim);
	}

	@Override
	public long getEnergy() {
		return ies.getEnergyStored();
	}

	@Override
	public long getEnergyCapacity() {
		return ies.getMaxEnergyStored();
	}

	@Override
	public boolean hasNoEnergy() {
		return ies.getEnergyStored() == 0;
	}

	@Override
	public boolean hasFullEnergy() {
		return ies.getEnergyStored() == ies.getMaxEnergyStored();
	}
}
