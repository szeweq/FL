package szewek.fl.kotlin

import net.minecraft.nbt.*

inline operator fun NBTTagCompound.get(s: String): NBTBase = getTag(s)
inline operator fun NBTTagCompound.set(s: String, tag: NBTBase) = setTag(s, tag)
inline operator fun NBTTagCompound.contains(s: String) = hasKey(s)

inline val NBTBase.asBoolean
	get() = if (this is NBTPrimitive) this.byte != (0).toByte() else false
inline val NBTBase.asByte
	get() = (this as? NBTPrimitive)?.byte ?: (0).toByte()
inline val NBTBase.asShort
	get() = (this as? NBTPrimitive)?.short ?: (0).toShort()
inline val NBTBase.asInt
	get() = (this as? NBTPrimitive)?.int ?: 0
inline val NBTBase.asLong
	get() = (this as? NBTPrimitive)?.long ?: 0L
inline val NBTBase.asFloat
	get() = (this as? NBTPrimitive)?.float ?: 0F
inline val NBTBase.asDouble
	get() = (this as? NBTPrimitive)?.double ?: 0.0
inline val NBTBase.asString
	get() = (this as? NBTTagString)?.string ?: ""
inline val NBTBase.asByteArray
	get() = (this as? NBTTagByteArray)?.byteArray
inline val NBTBase.asIntArray
	get() = (this as? NBTTagIntArray)?.intArray
inline val NBTBase.asCompound
	get() = this as? NBTTagCompound
inline val NBTBase.asList
	get() = this as? NBTTagList