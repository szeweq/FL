package szewek.fl;

import net.minecraftforge.fml.common.SidedProxy;
import szewek.fl.proxy.FLProxy;

public final class FLX {

	@SidedProxy(modId = R.FL_ID, serverSide = R.FL_PROXY_PKG, clientSide = R.FL_PROXY_PKG + "Client")
	public static FLProxy PROXY = null;
}
