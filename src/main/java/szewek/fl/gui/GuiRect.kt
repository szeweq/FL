package szewek.fl.gui

class GuiRect(val x: Int, val y: Int, val width: Int, val height: Int) {
    val x2: Int
    val y2: Int
    val hash: Int

    init {
        x2 = x + width
        y2 = y + height
        hash = 31 * (31 * (31 * x + y) + width) + height
    }

    fun contains(px: Int, py: Int): Boolean {
        return !(px < x || py < y) && !(x2 < px || y2 < py)
    }

    override fun hashCode(): Int {
        return hash
    }

    override fun equals(other: Any?): Boolean {
        return other != null && (other === this || other is GuiRect && other.hash == hash)
    }
}
