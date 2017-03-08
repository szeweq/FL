package szewek.fl.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public enum FLGui {
	;

	public static void drawHGradientRect(int left, int top, int right, int bottom, float z, int color1, int color2) {
		int ya = color1 >> 24 & 255, yr = color1 >> 16 & 255, yg = color1 >> 8 & 255, yb = color1 & 255;
		int za = color2 >> 24 & 255, zr = color2 >> 16 & 255, zg = color2 >> 8 & 255, zb = color2 & 255;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tes = Tessellator.getInstance();
		VertexBuffer vb = tes.getBuffer();
		vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
		vb.pos(right, top, z).color(yr, yg, yb, ya).endVertex();
		vb.pos(left, top, z).color(zr, zg, zb, za).endVertex();
		vb.pos(left, bottom, z).color(zr, zg, zb, za).endVertex();
		vb.pos(right, bottom, z).color(yr, yg, yb, ya).endVertex();
		tes.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

	public static void drawFluidStack(Minecraft mc, GuiRect rect, float z, @Nullable FluidStack fs, int cap) {
		if (fs == null)
			return;
		Fluid fl = fs.getFluid();
		if (fl == null)
			return;
		TextureMap tm = mc.getTextureMapBlocks();
		ResourceLocation frl = fl.getStill();
		TextureAtlasSprite tas = tm.getTextureExtry(frl.toString());
		if (tas == null)
			tas = tm.getMissingSprite();
		int flc = fl.getColor(fs);
		int sa = (fs.amount * rect.height) / cap;
		if (sa < 1 && fs.amount > 0)
			sa = 1;
		if (sa > rect.height)
			sa = rect.height;
		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		setGLColor(flc);
		Tessellator tes = Tessellator.getInstance();
		VertexBuffer vb = tes.getBuffer();
		final int xc = rect.width / 16, xr = rect.width % 16, yc = sa / 16, yr = sa % 16, ys = rect.y2;
		for (int xt = 0; xt <= xc; xt++) {
			for (int yt = 0; yt <= yc; yt++) {
				int w = xt == xc ? xr : 16;
				int h = yt == yc ? yr : 16;
				int x = rect.x + xt * 16;
				int y = ys - (yt + 1) * 16;
				if (w > 0 && h > 0) {
					int mt = 16 - h;
					int mr = 16 - w;
					double umin = tas.getMinU();
					double umax = tas.getMaxU();
					double vmin = tas.getMinV();
					double vmax = tas.getMaxV();
					umax -= mr / 16.0 * (umax - umin);
					vmax -= mt / 16.0 * (vmax - vmin);
					vb.begin(7, DefaultVertexFormats.POSITION_TEX);
					vb.pos(x, y + 16, z).tex(umin, vmax).endVertex();
					vb.pos(x + 16 - mr, y + 16, z).tex(umax, vmax).endVertex();
					vb.pos(x + 16 - mr, y + mt, z).tex(umax, vmin).endVertex();
					vb.pos(x, y + mt, z).tex(umin, vmin).endVertex();
					tes.draw();
				}
			}
		}
	}

	private static void setGLColor(int color) {
		float r = (color >> 16 & 255) / 255.0F;
		float g = (color >> 8 & 255) / 255.0F;
		float b = (color & 255) / 255.0F;
		GlStateManager.color(r, g, b, 1F);
	}
}