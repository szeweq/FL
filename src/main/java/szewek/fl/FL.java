package szewek.fl;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.RecipeSorter;
import szewek.fl.recipes.BuiltShapedRecipe;

import java.util.Random;

@Mod(modid = R.FL_ID, version = R.FL_VERSION, dependencies = R.FL_DEPS)
public class FL {
	/**
	 * Checks ItemStack emptiness ({@code null} indicates that ItemStack is empty)
	 * @param is Checked item stack
	 * @return {@code true} if ItemStack is empty
	 */
	public static boolean isItemEmpty(final ItemStack is) {
		return is == null || is.isEmpty();
	}

	public static String formatMB(int n, int c) {
		return n + " / " + c + " mB";
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

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		RecipeSorter.register("fl:builtRecipe", BuiltShapedRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
		//new RecipeBuilder().shape(new R9[] {A, A, A, A}, 2, 2).with(A, new RecipeItem(Blocks.DIRT)).result(Items.DIAMOND).deploy();
	}
}
