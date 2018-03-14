package szewek.fl.util

import net.minecraftforge.fml.common.discovery.ASMDataTable
import java.lang.reflect.Method
import java.util.function.BiConsumer

object JavaUtils {

    fun getClassSafely(name: String): Class<*>? = try {
        Class.forName(name)
    } catch (ignored: ClassNotFoundException) {
        null
    }

    fun getMethodSafely(c: Class<*>, name: String, vararg cargs: Class<*>): Method? = try {
        c.getDeclaredMethod(name, *cargs)
    } catch (ignored: NoSuchMethodException) {
        null
    }

    fun <T> makeFilledArray(t: Array<T>, fill: T): Array<T> {
        val l = t.size - 1
        for (i in 0..l) {
            t[i] = fill
        }
        return t
    }

    fun <A : Annotation> eachAnnotatedClasses(asdt: ASMDataTable, ac: Class<A>, eachfn: BiConsumer<A, Class<*>>) {
        val aset = asdt.getAll(ac.name)
        for (data in aset) {
            val cn = data.className
            if (cn != data.objectName) continue
            val c = getClassSafely(cn) ?: continue
            val a = c.getAnnotation(ac)
            eachfn.accept(a, c)
        }
    }
}
