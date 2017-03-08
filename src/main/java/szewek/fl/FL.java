package szewek.fl;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod(modid = R.FL_ID, version = R.FL_VERSION)
public class FL {
	public static boolean isItemEmpty(ItemStack is) {
		return is == null || is.isEmpty();
	}

	public static void giveItemToPlayer(final ItemStack is, EntityPlayer p) {
		boolean f = p.inventory.addItemStackToInventory(is);
		EntityItem ei;
		if (f) {
			Random r = p.getRNG();
			p.world.playSound(null, p.posX, p.posY, p.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((r.nextFloat() - r.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			p.inventoryContainer.detectAndSendChanges();
		}
		if (!f || !is.isEmpty()) {
			ei = p.dropItem(is, false);
			if (ei != null) {
				ei.setNoPickupDelay();
				ei.setOwner(p.getName());
			}
		}
	}
}
