package szewek.fl.container

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

class FakeSlot(inventoryIn: IInventory, index: Int, xPosition: Int, yPosition: Int, private val input: Boolean) : Slot(inventoryIn, index, xPosition, yPosition) {

    override fun canTakeStack(playerIn: EntityPlayer?): Boolean {
        return false
    }

    override fun isItemValid(stack: ItemStack?): Boolean {
        return input
    }

    override fun decrStackSize(amount: Int): ItemStack {
        return ItemStack.EMPTY
    }

    override fun getItemStackLimit(stack: ItemStack?): Int {
        return if (input) 1 else 64
    }

    override fun getSlotStackLimit(): Int {
        return if (input) 0 else 64
    }

    override fun putStack(stack: ItemStack) {
        if (input && !stack.isEmpty) {
            stack.count = 1
        }
        super.putStack(stack)
    }
}
