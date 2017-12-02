package szewek.fl.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class FakeSlot extends Slot {
	private final boolean input;

	public FakeSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, boolean in) {
		super(inventoryIn, index, xPosition, yPosition);
		input = in;
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		return false;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return input;
	}

	@Nonnull @Override
	public ItemStack decrStackSize(int amount) {
		return ItemStack.EMPTY;
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return input ? 1 : 64;
	}

	@Override
	public int getSlotStackLimit() {
		return input ? 0 : 64;
	}

	@Override
	public void putStack(ItemStack stack) {
		if (input && !stack.isEmpty()) {
			stack.setCount(1);
		}
		super.putStack(stack);
	}
}
