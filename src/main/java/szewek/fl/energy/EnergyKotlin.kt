package szewek.fl.energy

inline val IEnergy.inputsEnergy
	get() = canInputEnergy()

inline val IEnergy.outputsEnergy
	get() = canOutputEnergy()

inline val IEnergy.isEnergyFull
	get() = hasFullEnergy()

inline val IEnergy.isEnergyEmpty
	get() = hasNoEnergy()
