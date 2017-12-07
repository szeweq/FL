package szewek.fl.proxy;

import szewek.fl.network.FLNetUtil;
import szewek.fl.network.FLNetUtilServer;
import szewek.fl.util.PreRegister;

public class FLProxy {
	public void addPreRegister(PreRegister pr) {}
	public void init() {}
	public FLNetUtil getNetUtil() {
		return FLNetUtilServer.THIS;
	}
}
