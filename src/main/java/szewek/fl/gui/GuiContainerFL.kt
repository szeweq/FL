package szewek.fl.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraft.util.math.MathHelper
import net.minecraftforge.fluids.FluidStack
import szewek.fl.FL
import java.util.*

abstract class GuiContainerFL(inventorySlotsIn: Container) : GuiContainer(inventorySlotsIn) {
    protected var guiX: Int = 0
    protected var guiY: Int = 0

    override fun setWorldAndResolution(mc: Minecraft, width: Int, height: Int) {
        super.setWorldAndResolution(mc, width, height)
        guiX = (width - xSize) / 2
        guiY = (height - ySize) / 2
    }

    protected fun drawGuiBar(rect: GuiRect, fill: Float, c1: Int, c2: Int, horiz: Boolean, reverse: Boolean) {
        FLGui.switchGuiMode(true)
        FLGui.drawRectBatchOnly(rect.x, rect.y, rect.x2, rect.y2, 0f, BAR_BORDER)
        var x = rect.x + 1
        var y = rect.y + 1
        var x2 = rect.x2 - 1
        var y2 = rect.y2 - 1
        val f: Int
        if (fill > 0) {
            if (horiz) {
                FLGui.drawHGradientRect(x, y, x2, y2, zLevel, c1, c2)
                f = MathHelper.ceil((rect.width - 2) * fill)
                if (reverse)
                    x += f
                else
                    x2 -= f
            } else {
                FLGui.drawGradientRectBatchOnly(x, y, x2, y2, zLevel, c1, c2)
                f = MathHelper.ceil((rect.height - 2) * fill)
                if (reverse)
                    y += f
                else
                    y2 -= f
            }
        }
        FLGui.drawRectBatchOnly(x, y, x2, y2, 0f, BAR_BG)
        Gui.drawRect(x, y, x2, y2, BAR_BG)
        FLGui.switchGuiMode(false)
    }

    protected fun displayFluidInfo(rect: GuiRect, fs: FluidStack?, cap: Int, mx: Int, my: Int) {
        if (fs != null && rect.contains(mx, my))
            drawHoveringText(Arrays.asList(fs.localizedName, FL.formatMB(fs.amount, cap)), mx, my, fontRenderer)
    }

    companion object {
        private const val BAR_BORDER = -0xcdcdce
        private const val BAR_BG = -0xeaeaeb
    }
}
