package szewek.fl.gui

data class GuiRect(@JvmField val x: Int, @JvmField val y: Int, @JvmField val width: Int, @JvmField val height: Int) {
    @JvmField val x2: Int = x + width
    @JvmField val y2: Int = y + height

    fun contains(px: Int, py: Int) = !(px < x || py < y) && !(x2 < px || y2 < py)
}
