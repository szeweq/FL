package szewek.fl.energy

inline operator fun IEnergy.plus(a: Long) = this.inputEnergy(a, false)
inline operator fun IEnergy.minus(a: Long) = this.outputEnergy(a, false)

inline operator fun IEnergy.plusAssign(a: Long) {
    this.inputEnergy(a, true)
}
inline operator fun IEnergy.minusAssign(a: Long) {
    this.outputEnergy(a, true)
}