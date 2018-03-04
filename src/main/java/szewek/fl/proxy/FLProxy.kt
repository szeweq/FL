package szewek.fl.proxy

import szewek.fl.network.FLNetUtil
import szewek.fl.network.FLNetUtilServer
import szewek.fl.util.PreRegister

open class FLProxy {
    open val netUtil: FLNetUtil
        get() = FLNetUtilServer.THIS

    open fun addPreRegister(pr: PreRegister) {}
    open fun init() {}
}
