package szewek.fl;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.ModContainer;
import szewek.fl.energy.IEnergy;

import java.util.Map;

public final class FLU {
	public static boolean loadedMods(String... names) {
		final Loader ldr = Loader.instance();
		final Map<String, ModContainer> mm = ldr.getIndexedModList();
		for (String n : names) {
			if (mm.containsKey(n) && ldr.getModState(mm.get(n)) != LoaderState.ModState.DISABLED)
				return true;
		}
		return false;
	}

	public static IEnergy getEnergySafely(ICapabilityProvider icp, EnumFacing f) {
		try {
			if (icp instanceof IEnergy) {
				return (IEnergy) icp;
			}
			return icp.getCapability(FL.ENERGY_CAP, f);
		} catch (Exception ignored) {
		}
		return null;
	}

	private FLU() {}
}
