package szewek.fl.util

import net.minecraft.nbt.NBTBase
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.INBTSerializable

@Suppress("UNCHECKED_CAST")
object CapStorage {
	private val EMPTY = object : Capability.IStorage<Any> {
		override fun writeNBT(cap: Capability<Any>, t: Any, side: EnumFacing): NBTBase? {
			return null
		}

		override fun readNBT(cap: Capability<Any>, t: Any, side: EnumFacing, nbt: NBTBase) {}
	}

	private val NBT_STORAGE = object : Capability.IStorage<INBTSerializable<NBTBase>> {
		override fun writeNBT(cap: Capability<INBTSerializable<NBTBase>>, instance: INBTSerializable<NBTBase>, side: EnumFacing): NBTBase? {
			return instance.serializeNBT()
		}

		override fun readNBT(cap: Capability<INBTSerializable<NBTBase>>, instance: INBTSerializable<NBTBase>, side: EnumFacing, nbt: NBTBase) {
			instance.deserializeNBT(nbt)
		}
	}

	private val CUSTOM = object : Capability.IStorage<Any> {
		override fun writeNBT(capability: Capability<Any>, t: Any, side: EnumFacing): NBTBase? {
			return (t as? INBTSerializable<*>)?.serializeNBT()
		}

		override fun readNBT(capability: Capability<Any>, t: Any, side: EnumFacing, nbt: NBTBase) {
			if (t is INBTSerializable<*>)
				(t as INBTSerializable<NBTBase>).deserializeNBT(nbt)
		}
	}

	@JvmStatic
	fun <T> getEmpty(): Capability.IStorage<T> {
		return EMPTY as Capability.IStorage<T>
	}

	@JvmStatic
	fun <T : INBTSerializable<NBTBase>> getNBTStorage(): Capability.IStorage<T> {
		return NBT_STORAGE as Capability.IStorage<T>
	}

	@JvmStatic
	fun <T> getCustom(): Capability.IStorage<T> {
		return CUSTOM as Capability.IStorage<T>
	}
}
