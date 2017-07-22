package szewek.fl.test;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import szewek.fl.FL;

public final class EventCounter implements Runnable {
	private static final Object o = new Object();
	private static long icount = 0;

	@SubscribeEvent
	public void count1(AttachCapabilitiesEvent<ItemStack> e) {
		synchronized (o) {
			icount++;
		}
	}

	private static long getIcount() {
		synchronized (o) {
			return icount;
		}
	}

	@Override
	public void run() {
		boolean running = true;
		while (running) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				running = false;
			}
			FL.L.info("ItemStack count: " + getIcount());
		}
	}
}