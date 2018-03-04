package szewek.fl.block

import net.minecraft.block.BlockContainer
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumBlockRenderType

abstract class BlockContainerModeled protected constructor() : BlockContainer(Material.PISTON) {
    init {
        soundType = SoundType.METAL
    }

    override fun getRenderType(state: IBlockState?): EnumBlockRenderType {
        return EnumBlockRenderType.MODEL
    }
}
