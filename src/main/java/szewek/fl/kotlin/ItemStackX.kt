package szewek.fl.kotlin

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTBase
import net.minecraftforge.items.ItemHandlerHelper

inline operator fun ItemStack.plusAssign(count: Int) {
	this.count += count
}
inline operator fun ItemStack.minusAssign(count: Int) {
	this.count -= count
}
inline operator fun ItemStack.get(s: String) = tagCompound?.getTag(s)
inline operator fun ItemStack.set(s: String, tag: NBTBase) = setTagInfo(s, tag)

inline infix fun ItemStack.split(a: Int) = splitStack(a)
inline infix fun ItemStack.withSize(a: Int) = ItemHandlerHelper.copyStackWithSize(this, a)