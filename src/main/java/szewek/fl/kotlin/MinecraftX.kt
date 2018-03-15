package szewek.fl.kotlin

import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.EventBus

inline operator fun EventBus.plusAssign(o: Any) = register(o)
inline operator fun EventBus.minusAssign(o: Any) = unregister(o)
inline operator fun EventBus.invoke(e: Event) = post(e)
