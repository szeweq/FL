package szewek.fl.energy;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagLong;
import net.minecraftforge.common.util.INBTSerializable;

public class Battery implements IEnergy, INBTSerializable<NBTBase> {
	protected long energy = 0;
	protected final long maxEnergy;

	public Battery() {
		this(50000);
	}

	public Battery(long max) {
		maxEnergy = max;
	}

	@Override
	public boolean canInputEnergy() {
		return true;
	}

	@Override
	public boolean canOutputEnergy() {
		return true;
	}

	@Override
	public long inputEnergy(long amount, boolean sim) {
		if (amount == 0)
			return 0;
		final long r = maxEnergy - energy;
		if (amount > r)
			amount = r;
		if (!sim)
			energy += amount;
		return amount;
	}

	@Override
	public long outputEnergy(long amount, boolean sim) {
		if (amount == 0)
			return 0;
		if (amount > energy)
			amount = energy;
		if (!sim)
			energy -= amount;
		return amount;
	}

	@Override
	public long getEnergy() {
		return energy;
	}

	@Override
	public long getEnergyCapacity() {
		return maxEnergy;
	}

	@Override
	public void setEnergy(long amount) {
		energy = amount > maxEnergy ? maxEnergy : amount;
	}

	@Override public boolean hasNoEnergy() {
		return energy == 0;
	}

	@Override public boolean hasFullEnergy() {
		return energy == maxEnergy;
	}

	@Override
	public long to(IEnergy ie, long amount) {
		if (amount > 0 && ie != null && ie.canInputEnergy()) {
			if (amount > energy)
				amount = energy;
			final long r = ie.inputEnergy(amount, true);
			if (r > 0) {
				energy -= r;
				return ie.inputEnergy(r, false);
			}
		}
		return 0;
	}

	@Override public NBTBase serializeNBT() {
		return new NBTTagLong(energy);
	}

	@Override public void deserializeNBT(NBTBase nbt) {
		if (!(nbt instanceof NBTTagLong))
			return;
		energy = ((NBTTagLong) nbt).getLong();
	}
}
