package szewek.fl

import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.SoundCategory
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent
import org.apache.logging.log4j.Logger
import szewek.fl.energy.Battery
import szewek.fl.energy.EnergyNBTStorage
import szewek.fl.energy.IEnergy
import szewek.fl.kotlin.plusAssign
import szewek.fl.network.FLCloud
import szewek.fl.taste.ILikesTaste
import szewek.fl.taste.Taste
import szewek.fl.test.EventCounter
import szewek.fl.test.NamedCounters
import szewek.fl.util.CapStorage

@Mod(modid = R.FL_ID, version = R.FL_VERSION)
class FL {

	@Mod.EventHandler
	fun preInit(e: FMLPreInitializationEvent) {
		L = e.modLog
		Thread(FLCloud.Companion::checkVersions, "FL Updates check").start()
		val cm = CapabilityManager.INSTANCE
		cm.register<IEnergy>(IEnergy::class.java, EnergyNBTStorage(), ::Battery)
		cm.register<ILikesTaste>(ILikesTaste::class.java, CapStorage.getCustom<ILikesTaste>(), Taste::Storage)
		if (R.FL_DEBUG) {
			val ec = EventCounter()
			MinecraftForge.EVENT_BUS += ec
			val th = Thread(ec, "FL Event Counter")
			th.isDaemon = true
			th.start()
		}
	}

	@Mod.EventHandler
	fun init(e: FMLInitializationEvent) {
		FLX.PROXY!!.init()
	}

	@Mod.EventHandler
	fun serverStopped(e: FMLServerStoppingEvent) {
		NamedCounters.checkAndResetAll()
	}

	companion object {
		@JvmField
		@CapabilityInject(IEnergy::class)
		var ENERGY_CAP: Capability<IEnergy>? = null
		@JvmField
		@CapabilityInject(ILikesTaste::class)
		var TASTE_CAP: Capability<ILikesTaste>? = null

		/**
		 * Checks ItemStack emptiness (`null` indicates that ItemStack is empty)
		 * @param stk Checked item stack
		 * @return `true` if ItemStack is empty
		 */
		@Deprecated("Not used in current version", ReplaceWith("stk == null || stk.isEmpty"))
		@JvmStatic
		fun isItemEmpty(stk: ItemStack?) = stk == null || stk.isEmpty

		@JvmStatic
		fun formatMB(n: Int, c: Int) = n.toString() + " / " + c + " mB"

		@Deprecated("Already available in Forge's ItemHandlerHelper")
		@JvmStatic
		fun giveItemToPlayer(stk: ItemStack, p: EntityPlayer) {
			val f = p.inventory.addItemStackToInventory(stk)
			val ei: EntityItem?
			if (f) {
				val r = p.rng
				p.world.playSound(null, p.posX, p.posY, p.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((r.nextFloat() - r.nextFloat()) * 0.7f + 1.0f) * 2.0f)
				p.inventoryContainer.detectAndSendChanges()
			}
			if (!f || !stk.isEmpty) {
				ei = p.dropItem(stk, false)
				if (ei != null) {
					ei.setNoPickupDelay()
					ei.owner = p.name
				}
			}
		}

		// All stuff below is not a part of FL API
		private val FLC = FLCloud.getAPI("fl", R.FL_KEY)
		var L: Logger? = null
	}
}
