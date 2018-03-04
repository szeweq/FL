package szewek.fl.proxy

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import szewek.fl.network.FLNetUtil
import szewek.fl.network.FLNetUtilClient
import szewek.fl.util.PreRegister
import java.util.*

@SideOnly(Side.CLIENT)
class FLProxyClient : FLProxy() {
    private val prl = ArrayList<PreRegister>()

    override val netUtil: FLNetUtil
        get() = if (FMLCommonHandler.instance().side == Side.CLIENT) FLNetUtilClient.THIS else super.netUtil

    override fun addPreRegister(pr: PreRegister) {
        prl.add(pr)
    }

    override fun init() {
        val imm = Minecraft.getMinecraft().renderItem.itemModelMesher
        for (pr in prl) {
            val iset = pr.getItems()
            for (i in iset) {
                val mrl = ModelResourceLocation(i.registryName!!, "inventory")
                imm.register(i, 0, mrl)
            }
        }
        prl.clear()
    }
}
