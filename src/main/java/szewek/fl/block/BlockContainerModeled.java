package szewek.fl.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;

import javax.annotation.Nonnull;

public abstract class BlockContainerModeled extends BlockContainer {
	protected BlockContainerModeled() {
		super(Material.PISTON);
		setSoundType(SoundType.METAL);
	}

	@Nonnull @Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
}
