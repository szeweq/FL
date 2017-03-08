package szewek.fl.gui;

public class GuiRect {
	public final int x, y, width, height, x2, y2, hash;

	public GuiRect(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		x2 = x + width;
		y2 = y + height;
		hash = 31 * (31 * ((31 * x) + y) + width) + height;
	}

	public boolean contains(int px, int py) {
		return !(px < x || py < y) && !(x2 < px || y2 < py);
	}

	@Override public int hashCode() {
		return hash;
	}

	@Override public boolean equals(Object o) {
		return o != null && (o == this || (o instanceof GuiRect && ((GuiRect) o).hash == hash));
	}
}
