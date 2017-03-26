package szewek.fl.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import szewek.fl.FL;

import javax.annotation.Nullable;
import java.util.Arrays;

public abstract class GuiContainerFL extends GuiContainer {
	private static final int BAR_BORDER = 0xFF323232, BAR_BG = 0xFF151515;
	protected int guiX, guiY;

	public GuiContainerFL(Container inventorySlotsIn) {
		super(inventorySlotsIn);
	}

	@Override public void setWorldAndResolution(Minecraft mc, int width, int height) {
		super.setWorldAndResolution(mc, width, height);
		guiX = (width - xSize) / 2;
		guiY = (height - ySize) / 2;
	}

	protected void drawGuiBar(GuiRect rect, float fill, int c1, int c2, boolean horiz, boolean reverse) {
		FLGui.switchGuiMode(true);
		FLGui.drawRectBatchOnly(rect.x, rect.y, rect.x2, rect.y2, 0, BAR_BORDER);
		int x = rect.x + 1, y = rect.y + 1, x2 = rect.x2 - 1, y2 = rect.y2 - 1, f;
		if (fill > 0) {
			if (horiz) {
				FLGui.drawHGradientRect(x, y, x2, y2, zLevel, c1, c2);
				f = MathHelper.ceil((rect.width - 2) * fill);
				if (reverse)
					x += f;
				else
					x2 -= f;
			} else {
				FLGui.drawGradientRectBatchOnly(x, y, x2, y2, zLevel, c1, c2);
				f = MathHelper.ceil((rect.height - 2) * fill);
				if (reverse)
					y += f;
				else
					y2 -= f;
			}
		}
		FLGui.drawRectBatchOnly(x, y, x2, y2, 0, BAR_BG);
		drawRect(x, y, x2, y2, BAR_BG);
		FLGui.switchGuiMode(false);
	}

	protected void displayFluidInfo(GuiRect rect, @Nullable FluidStack fs, int cap, int mx, int my) {
		if (fs != null && rect.contains(mx, my))
			drawHoveringText(Arrays.asList(fs.getLocalizedName(), FL.formatMB(fs.amount, cap)), mx, my, fontRenderer);
	}
}
