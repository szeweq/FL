package szewek.fl.energy;

import net.minecraftforge.energy.IEnergyStorage;

import static java.lang.Integer.MAX_VALUE;

public final class ForgeEnergyCompat implements IEnergyStorage {
	private final IEnergy ie;

	public ForgeEnergyCompat(IEnergy ie) {
		this.ie = ie;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean sim) {
		return (int) ie.inputEnergy(maxReceive, sim);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean sim) {
		return (int) ie.outputEnergy(maxExtract, sim);
	}

	@Override
	public int getEnergyStored() {
		long l = ie.getEnergy();
		return l > MAX_VALUE ? MAX_VALUE : (int) l;
	}

	@Override
	public int getMaxEnergyStored() {
		long l = ie.getEnergyCapacity();
		return l > MAX_VALUE ? MAX_VALUE : (int) l;
	}

	@Override
	public boolean canExtract() {
		return ie.canOutputEnergy();
	}

	@Override
	public boolean canReceive() {
		return ie.canInputEnergy();
	}
}
