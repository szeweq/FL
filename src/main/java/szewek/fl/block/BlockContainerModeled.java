package szewek.fl.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;

public abstract class BlockContainerModeled extends BlockContainer {
	protected BlockContainerModeled() {
		super(Material.PISTON);
		this.setSoundType(SoundType.METAL);
	}

	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
}
