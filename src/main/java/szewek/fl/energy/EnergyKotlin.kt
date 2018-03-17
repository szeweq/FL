package szewek.fl.energy

inline val IEnergy.inputsEnergy get() = canInputEnergy()
inline val IEnergy.outputsEnergy get() = canOutputEnergy()
inline val IEnergy.isEnergyFull get() = hasFullEnergy()
inline val IEnergy.isEnergyEmpty get() = hasNoEnergy()

inline val IEnergyFunction.energyForInput get() = queryEnergy(0, true)
inline val IEnergyFunction.energyForOutput get() = queryEnergy(0, false)
inline val IEnergyFunction.energy get() = queryEnergy(Long.MIN_VALUE, false)
inline val IEnergyFunction.maxEnergy get() = queryEnergy(Long.MIN_VALUE, false)