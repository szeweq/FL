package szewek.fl.util

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

/**
 * RecipeItem is a lightweight alternative for an [ItemStack] for use in recipes
 */
class RecipeItem @JvmOverloads constructor(private val item: Item, private val meta: Int = 32767, private val tags: NBTTagCompound? = null) {
	private val cachedHash: Int

	@JvmOverloads constructor(b: Block, m: Int = 32767, nbt: NBTTagCompound? = null) : this(Item.getItemFromBlock(b), m, nbt) {}

	init {
		var h = (31 + item.hashCode()) * 31 + meta
		if (tags != null)
			h = 31 * h + tags.hashCode()
		cachedHash = h
	}

	fun makeItemStack(): ItemStack {
		val stk = ItemStack(item, 1, meta, null)
		if (tags != null)
			stk.tagCompound = tags.copy()
		return stk
	}

	fun matchesStack(stk: ItemStack) =
			!stk.isEmpty && stk.item === item && (meta == 32767 || stk.itemDamage == meta)

	inline infix fun matches(stk: ItemStack): Boolean = matchesStack(stk)

	override fun hashCode() = cachedHash

	override fun equals(other: Any?): Boolean {
		return other != null && other is RecipeItem && other.hashCode() == cachedHash
	}
}
