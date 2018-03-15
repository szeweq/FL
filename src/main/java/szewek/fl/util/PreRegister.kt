package szewek.fl.util

import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.registries.IForgeRegistry
import szewek.fl.FL
import szewek.fl.annotations.NamedResource
import java.util.*

class PreRegister(private val domain: String, private val tab: CreativeTabs) {
    private val iset = HashSet<Item>()
    private val bset = HashSet<Block>()

    init {
        FL.PROXY.addPreRegister(this)
    }

    fun item(name: String, i: Item): PreRegister {
        val fn = "$domain:$name"
        i.setUnlocalizedName(fn).setCreativeTab(tab).setRegistryName(fn)
        iset.add(i)
        return this
    }

    fun block(name: String, b: Block): PreRegister {
        val fn = "$domain:$name"
        b.setUnlocalizedName(fn).setCreativeTab(tab).setRegistryName(fn)
        bset.add(b)
        return this
    }

    fun tileEntityClasses(tecs: Array<Class<out TileEntity>>) {
        for (tec in tecs) {
            val nr = tec.getAnnotation(NamedResource::class.java) ?: continue
            GameRegistry.registerTileEntity(tec, nr.value)
        }
    }

    fun registerItems(ifr: IForgeRegistry<Item>) {
        for (i in iset) ifr.register(i)
    }

    fun registerBlocks(ifr: IForgeRegistry<Block>) {
        for (b in bset) ifr.register(b)
    }

    val items: Set<Item>
        get() = Collections.unmodifiableSet(iset)

    val blocks: Set<Block>
        get() = Collections.unmodifiableSet(bset)
}
