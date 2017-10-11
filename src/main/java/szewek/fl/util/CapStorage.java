package szewek.fl.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public final class CapStorage {
	private static final Capability.IStorage<?> EMPTY = new Capability.IStorage<Object>() {
		@Override
		public NBTBase writeNBT(Capability<Object> cap, Object t, EnumFacing side) {
			return null;
		}

		@Override
		public void readNBT(Capability<Object> cap, Object t, EnumFacing side, NBTBase nbt) {
		}
	};

	private static final Capability.IStorage<INBTSerializable<NBTBase>> NBT_STORAGE = new Capability.IStorage<INBTSerializable<NBTBase>>() {
		@Nullable
		@Override
		public NBTBase writeNBT(Capability<INBTSerializable<NBTBase>> cap, INBTSerializable<NBTBase> instance, EnumFacing side) {
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<INBTSerializable<NBTBase>> cap, INBTSerializable<NBTBase> instance, EnumFacing side, NBTBase nbt) {
			instance.deserializeNBT(nbt);
		}
	};

	private static final Capability.IStorage<?> CUSTOM = new Capability.IStorage<Object>() {
		@Nullable
		@Override
		public NBTBase writeNBT(Capability<Object> capability, Object t, EnumFacing side) {
			return t instanceof INBTSerializable ? ((INBTSerializable) t).serializeNBT() : null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void readNBT(Capability<Object> capability, Object t, EnumFacing side, NBTBase nbt) {
			if (t instanceof INBTSerializable)
				((INBTSerializable<NBTBase>) t).deserializeNBT(nbt);
		}
	};

	@SuppressWarnings("unchecked")
	public static <T> Capability.IStorage<T> getEmpty() {
		return (Capability.IStorage<T>) EMPTY;
	}

	@SuppressWarnings("unchecked")
	public static <T extends INBTSerializable<NBTBase>> Capability.IStorage<T> getNBTStorage() {
		return (Capability.IStorage<T>) NBT_STORAGE;
	}

	@SuppressWarnings("unchecked")
	public static <T> Capability.IStorage<T> getCustom() {
		return (Capability.IStorage<T>) CUSTOM;
	}

	private CapStorage() {
	}
}
