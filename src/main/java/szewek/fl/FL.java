package szewek.fl;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.apache.logging.log4j.Logger;
import szewek.fl.energy.Battery;
import szewek.fl.energy.EnergyNBTStorage;
import szewek.fl.energy.IEnergy;
import szewek.fl.network.FLCloud;
import szewek.fl.taste.ILikesTaste;
import szewek.fl.taste.Taste;
import szewek.fl.test.EventCounter;
import szewek.fl.test.NamedCounters;
import szewek.fl.util.CapStorage;

import java.util.Random;

@Mod(modid = R.FL_ID, version = R.FL_VERSION)
public final class FL {
	@CapabilityInject(IEnergy.class) public static Capability<IEnergy> ENERGY_CAP = null;
	@CapabilityInject(ILikesTaste.class) public static Capability<ILikesTaste> TASTE_CAP = null;

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

	// All stuff below is not a part of FL API
	private static FLCloud FLC = FLCloud.getAPI("fl", R.FL_KEY);
	public static Logger L = null;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		L = e.getModLog();
		new Thread(FLCloud::checkVersions, "FL Updates check").start();
		final CapabilityManager cm = CapabilityManager.INSTANCE;
		cm.register(IEnergy.class, new EnergyNBTStorage(), Battery::new);
		cm.register(ILikesTaste.class, CapStorage.getCustom(), Taste.Storage::new);
		if (R.FL_DEBUG) {
			EventCounter ec = new EventCounter();
			MinecraftForge.EVENT_BUS.register(ec);
			Thread th = new Thread(ec, "FL Event Counter");
			th.setDaemon(true);
			th.start();
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {}

	@Mod.EventHandler
	public void serverStopped(FMLServerStoppingEvent e) {
		NamedCounters.checkAndResetAll();
	}
}
