package szewek.fl.taste;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

public abstract class Taste {
	public final String name;
	public final NBTTagCompound data;

	public Taste(String name, NBTTagCompound data) {
		this.name = name;
		this.data = data;
	}

	public abstract long getAmount();

	public boolean areSameTaste(Taste t) {
		return !(name == null || t.name == null) && name.equals(t.name) && ((data == null && t.data == null) || (data != null && data.equals(t.data)));
	}

	public Immutable makeImmutable(long amount) {
		return new Immutable(name, data, amount);
	}

	public Mutable makeMutable(long amount) {
		return new Mutable(name, data, amount);
	}

	public NBTTagCompound toNBT() {
		final NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("name", name);
		nbt.setLong("amount", getAmount());
		if (data != null) {
			nbt.setTag("data", data);
		}
		return nbt;
	}

	public static <T extends Taste> T fromNBT(@Nonnull NBTTagCompound nbt, @Nonnull Factory<T> factory) {
		final String name = nbt.getString("name");
		final long amount = nbt.getLong("amount");
		NBTTagCompound data = null;
		if (nbt.hasKey("data", Constants.NBT.TAG_COMPOUND))
			data = nbt.getCompoundTag("data");
		return factory.create(name, data, amount);
	}

	public static final class Immutable extends Taste {
		private final long amount;

		public Immutable(String name, NBTTagCompound data, long amount) {
			super(name, data);
			this.amount = amount;
		}

		@Override
		public long getAmount() {
			return amount;
		}
	}

	public static class Mutable extends Taste {
		protected long amount;

		public Mutable(String name, NBTTagCompound data, long amount) {
			super(name, data);
			this.amount = amount;
		}

		@Override
		public long getAmount() {
			return amount;
		}

		public void setAmount(long amount) {
			this.amount = amount;
		}
	}

	public static class Storage extends Mutable implements ILikesTaste {
		private final long capacity;
		private final Taste accepted;

		public Storage() {
			this("", null, 50000L);
		}

		public Storage(String name, NBTTagCompound data, long cap) {
			super(name, data, 0);
			capacity = cap;
			accepted = makeImmutable(0);
		}

		@Override
		public boolean canInputTaste(Taste t) {
			return areSameTaste(t);
		}

		@Override
		public boolean canOutputTaste(Taste t) {
			return areSameTaste(t);
		}

		@Override
		public long inputTaste(Taste t, boolean sim) {
			if (!areSameTaste(t)) return 0;
			final long a = t.getAmount();
			long r = capacity - amount;
			if (a < r)
				r = a;
			if (!sim)
				amount += r;
			return r;
		}

		@Override
		public long outputTaste(Taste t, boolean sim) {
			if (!areSameTaste(t)) return 0;
			final long a = t.getAmount();
			long r = amount;
			if (a < r)
				r = a;
			if (!sim)
				amount -= r;
			return r;
		}

		@Override
		public Taste outputAnyTaste(long amount, boolean sim) {
			if (this.amount > 0) {
				long r = amount < this.amount ? amount : this.amount;
				if (!sim)
					this.amount -= r;
				return new Immutable(name, data.copy(), r);
			}
			return null;
		}

		@Override
		public long getTasteAmount(Taste t) {
			return areSameTaste(t) ? amount : 0;
		}

		@Override
		public long getTasteCapacity(Taste t) {
			return areSameTaste(t) ? capacity : 0;
		}

		@Override
		public Taste[] allTastesContained() {
			return new Taste[]{accepted};
		}

		@Override
		public Taste[] allTastesAcceptable() {
			return new Taste[]{accepted};
		}
	}

	@FunctionalInterface
	public interface Factory<T extends Taste> {
		T create(String name, NBTTagCompound data, long amount);
	}
}
