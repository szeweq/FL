package szewek.fl.taste

inline operator fun ILikesTaste.plus(a: Taste) = this.inputTaste(a, false)
inline operator fun ILikesTaste.minus(a: Taste) = this.outputTaste(a, false)

inline operator fun ILikesTaste.plusAssign(a: Taste) {
    this.inputTaste(a, true)
}
inline operator fun ILikesTaste.minusAssign(a: Taste) {
    this.outputTaste(a, true)
}