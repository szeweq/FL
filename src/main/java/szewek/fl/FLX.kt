package szewek.fl

import net.minecraft.item.ItemStack


operator fun ItemStack.plusAssign(count: Int) {
    this.count += count
}
operator fun ItemStack.minusAssign(count: Int) {
    this.count -= count
}