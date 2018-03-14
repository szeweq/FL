package szewek.fl.proxy

import szewek.fl.util.PreRegister


class FLProxyDummy : FLProxy() {
    override fun addPreRegister(pr: PreRegister) {
        // DUMMY
    }

    override fun init() {
        // DUMMY
    }
}