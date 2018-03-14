package szewek.fl.gui

data class GuiRect(val x: Int, val y: Int, val width: Int, val height: Int) {
    val x2: Int = x + width
    val y2: Int = y + height

    fun contains(px: Int, py: Int) = !(px < x || py < y) && !(x2 < px || y2 < py)
}
