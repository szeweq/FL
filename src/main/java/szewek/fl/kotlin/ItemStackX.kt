package szewek.fl.kotlin

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTBase

inline operator fun ItemStack.plusAssign(count: Int) {
	this.count += count
}
inline operator fun ItemStack.minusAssign(count: Int) {
	this.count -= count
}
inline operator fun ItemStack.get(s: String) = this.tagCompound?.getTag(s)
inline operator fun ItemStack.set(s: String, tag: NBTBase) = this.setTagInfo(s, tag)

inline infix fun ItemStack.split(a: Int) = this.splitStack(a)