package szewek.fl.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@SideOnly(Side.CLIENT)
object FLGui {
    private var guiMode = false
    private var guiBatch = false

    fun switchGuiMode(b: Boolean) {
        guiBatch = b
        if (b) start() else finish()
    }

    private fun start() {
        if (guiMode) return
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)
        GlStateManager.shadeModel(7425)
        guiMode = true
    }

    private fun finish() {
        if (!guiMode) return
        GlStateManager.shadeModel(7424)
        GlStateManager.disableBlend()
        GlStateManager.enableAlpha()
        GlStateManager.enableTexture2D()
        guiMode = false
    }

    fun drawHGradientRect(left: Int, top: Int, right: Int, bottom: Int, z: Float, color1: Int, color2: Int) {
        val ya = color1 shr 24 and 255
        val yr = color1 shr 16 and 255
        val yg = color1 shr 8 and 255
        val yb = color1 and 255
        val za = color2 shr 24 and 255
        val zr = color2 shr 16 and 255
        val zg = color2 shr 8 and 255
        val zb = color2 and 255
        if (!guiBatch) start()
        val tes = Tessellator.getInstance()
        val vb = tes.buffer
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR)
        vb.pos(right.toDouble(), top.toDouble(), z.toDouble()).color(yr, yg, yb, ya).endVertex()
        vb.pos(left.toDouble(), top.toDouble(), z.toDouble()).color(zr, zg, zb, za).endVertex()
        vb.pos(left.toDouble(), bottom.toDouble(), z.toDouble()).color(zr, zg, zb, za).endVertex()
        vb.pos(right.toDouble(), bottom.toDouble(), z.toDouble()).color(yr, yg, yb, ya).endVertex()
        tes.draw()
        if (!guiBatch) finish()
    }

    fun drawGradientRectBatchOnly(left: Int, top: Int, right: Int, bottom: Int, z: Float, color1: Int, color2: Int) {
        if (!guiBatch) return
        val ya = color1 shr 24 and 255
        val yr = color1 shr 16 and 255
        val yg = color1 shr 8 and 255
        val yb = color1 and 255
        val za = color2 shr 24 and 255
        val zr = color2 shr 16 and 255
        val zg = color2 shr 8 and 255
        val zb = color2 and 255
        val tes = Tessellator.getInstance()
        val vb = tes.buffer
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR)
        vb.pos(right.toDouble(), top.toDouble(), z.toDouble()).color(yr, yg, yb, ya).endVertex()
        vb.pos(left.toDouble(), top.toDouble(), z.toDouble()).color(yr, yg, yb, ya).endVertex()
        vb.pos(left.toDouble(), bottom.toDouble(), z.toDouble()).color(zr, zg, zb, za).endVertex()
        vb.pos(right.toDouble(), bottom.toDouble(), z.toDouble()).color(zr, zg, zb, za).endVertex()
        tes.draw()
    }

    fun drawRectBatchOnly(left: Int, top: Int, right: Int, bottom: Int, z: Float, c: Int) {
        var left = left
        var top = top
        var right = right
        var bottom = bottom
        if (!guiBatch) return
        var p: Int
        if (left < right) {
            p = left
            left = right
            right = p
        }
        if (top < bottom) {
            p = top
            top = bottom
            bottom = p
        }
        setGLColor(c)
        val tes = Tessellator.getInstance()
        val vb = tes.buffer
        vb.begin(7, DefaultVertexFormats.POSITION)
        vb.pos(left.toDouble(), bottom.toDouble(), z.toDouble()).endVertex()
        vb.pos(right.toDouble(), bottom.toDouble(), z.toDouble()).endVertex()
        vb.pos(right.toDouble(), top.toDouble(), z.toDouble()).endVertex()
        vb.pos(left.toDouble(), top.toDouble(), z.toDouble()).endVertex()
        tes.draw()
    }

    fun drawFluidStack(mc: Minecraft, rect: GuiRect, z: Float, fs: FluidStack?, cap: Int) {
        if (fs == null)
            return
        val fl = fs.fluid ?: return
        val tm = mc.textureMapBlocks
        val frl = fl.still
        var tas = tm.getTextureExtry(frl.toString())
        if (tas == null)
            tas = tm.missingSprite
        val flc = fl.getColor(fs)
        var sa = fs.amount * rect.height / cap
        if (sa < 1 && fs.amount > 0)
            sa = 1
        if (sa > rect.height)
            sa = rect.height
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
        setGLColor(flc)
        val tes = Tessellator.getInstance()
        val vb = tes.buffer
        val xc = rect.width / 16
        val xr = rect.width % 16
        val yc = sa / 16
        val yr = sa % 16
        val ys = rect.y2
        for (xt in 0..xc) {
            for (yt in 0..yc) {
                val w = if (xt == xc) xr else 16
                val h = if (yt == yc) yr else 16
                val x = rect.x + xt * 16
                val y = ys - (yt + 1) * 16
                if (w > 0 && h > 0) {
                    val mt = 16 - h
                    val mr = 16 - w
                    val umin = tas!!.minU.toDouble()
                    var umax = tas.maxU.toDouble()
                    val vmin = tas.minV.toDouble()
                    var vmax = tas.maxV.toDouble()
                    umax -= mr / 16.0 * (umax - umin)
                    vmax -= mt / 16.0 * (vmax - vmin)
                    vb.begin(7, DefaultVertexFormats.POSITION_TEX)
                    vb.pos(x.toDouble(), (y + 16).toDouble(), z.toDouble()).tex(umin, vmax).endVertex()
                    vb.pos((x + 16 - mr).toDouble(), (y + 16).toDouble(), z.toDouble()).tex(umax, vmax).endVertex()
                    vb.pos((x + 16 - mr).toDouble(), (y + mt).toDouble(), z.toDouble()).tex(umax, vmin).endVertex()
                    vb.pos(x.toDouble(), (y + mt).toDouble(), z.toDouble()).tex(umin, vmin).endVertex()
                    tes.draw()
                }
            }
        }
    }

    private fun setGLColor(c: Int) =
            GlStateManager.color((c shr 16 and 255) / 255f, (c shr 8 and 255) / 255f, (c and 255) / 255f, (c shr 24 and 255) / 255f)
}
