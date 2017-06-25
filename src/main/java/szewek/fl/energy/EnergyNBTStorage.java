package szewek.fl.energy;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class EnergyNBTStorage implements Capability.IStorage<IEnergy> {

	@Nullable
	@Override
	public NBTBase writeNBT(Capability<IEnergy> capability, IEnergy ie, EnumFacing side) {
		return ie instanceof INBTSerializable ? ((INBTSerializable) ie).serializeNBT() : new NBTTagLong(ie.getEnergy());
	}

	@Override
	@SuppressWarnings("unchecked")
	public void readNBT(Capability<IEnergy> capability, IEnergy ie, EnumFacing side, NBTBase nbt) {
		if (ie instanceof INBTSerializable)
			((INBTSerializable) ie).deserializeNBT(nbt);
		else if (nbt instanceof NBTPrimitive)
			ie.setEnergy(((NBTPrimitive) nbt).getLong());
	}
}
