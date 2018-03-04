package szewek.fl

import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.LoaderState
import szewek.fl.energy.IEnergy

object FLU {
    fun loadedMods(vararg names: String): Boolean {
        val ldr = Loader.instance()
        val mm = ldr.indexedModList
        for (n in names) {
            if (mm.containsKey(n) && ldr.getModState(mm[n]) != LoaderState.ModState.DISABLED)
                return true
        }
        return false
    }

    fun getEnergySafely(icp: ICapabilityProvider, f: EnumFacing): IEnergy? {
        try {
            return if (icp is IEnergy) {
                icp
            } else icp.getCapability(FL.ENERGY_CAP!!, f)
        } catch (ignored: Exception) {
        }

        return null
    }
}
