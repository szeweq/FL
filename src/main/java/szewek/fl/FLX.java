package szewek.fl;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.SidedProxy;
import szewek.fl.energy.IEnergy;
import szewek.fl.proxy.FLProxy;
import szewek.fl.taste.ILikesTaste;

public final class FLX {

	@SidedProxy(modId = R.FL_ID, serverSide = R.FL_PROXY_PKG, clientSide = R.FL_PROXY_PKG + "Client")
	public static FLProxy PROXY = null;

	@CapabilityInject(IEnergy.class)
	public static Capability<IEnergy> ENERGY_CAP = null;

	@CapabilityInject(ILikesTaste.class)
	public static Capability<ILikesTaste> TASTE_CAP = null;
}
