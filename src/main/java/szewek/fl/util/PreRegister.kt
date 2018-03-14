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
    private val items = HashSet<Item>()
    private val blocks = HashSet<Block>()

    init {
        FL.PROXY.addPreRegister(this)
    }

    fun item(name: String, i: Item): PreRegister {
        val fn = "$domain:$name"
        i.setUnlocalizedName(fn).setCreativeTab(tab).setRegistryName(fn)
        items.add(i)
        return this
    }

    fun block(name: String, b: Block): PreRegister {
        val fn = "$domain:$name"
        b.setUnlocalizedName(fn).setCreativeTab(tab).setRegistryName(fn)
        blocks.add(b)
        return this
    }

    fun tileEntityClasses(tecs: Array<Class<out TileEntity>>) {
        for (tec in tecs) {
            val nr = tec.getAnnotation(NamedResource::class.java) ?: continue
            GameRegistry.registerTileEntity(tec, nr.value)
        }
    }

    fun registerItems(ifr: IForgeRegistry<Item>) {
        for (i in items) {
            ifr.register(i)
        }
    }

    fun registerBlocks(ifr: IForgeRegistry<Block>) {
        for (b in blocks) {
            ifr.register(b)
        }
    }

    fun getItems(): Set<Item> {
        return Collections.unmodifiableSet(items)
    }

    fun getBlocks(): Set<Block> {
        return Collections.unmodifiableSet(blocks)
    }
}
