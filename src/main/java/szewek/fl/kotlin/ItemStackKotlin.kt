package szewek.fl.kotlin

import net.minecraft.item.ItemStack

object ItemStackKotlin {
	inline operator fun ItemStack.plusAssign(count: Int) {
		this.count += count
	}

	inline operator fun ItemStack.minusAssign(count: Int) {
		this.count -= count
	}
}