package szewek.fl.test

import net.minecraft.item.ItemStack
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import szewek.fl.FL

class EventCounter : Runnable {

    @SubscribeEvent
    fun count1(e: AttachCapabilitiesEvent<ItemStack>) {
        synchronized(o) {
            icount.add()
        }
    }

    override fun run() {
        var running = true
        while (running) {
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                running = false
            }

            FL.L!!.info("ItemStack count: " + getIcount())
        }
    }

    companion object {
        private val o = Any()
        private val icount = NamedCounters.getCounter("Attaching ItemStack Capabilities")

        private fun getIcount(): Long {
            synchronized(o) {
                return icount.count
            }
        }
    }
}
