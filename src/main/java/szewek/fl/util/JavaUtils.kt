package szewek.fl.util

import net.minecraftforge.fml.common.discovery.ASMDataTable
import java.lang.reflect.Method
import java.util.function.BiConsumer

object JavaUtils {

    fun getClassSafely(name: String): Class<*>? {
        var c: Class<*>? = null
        try {
            c = Class.forName(name)
        } catch (ignored: ClassNotFoundException) {
        }

        return c
    }

    fun getMethodSafely(c: Class<*>, name: String, vararg cargs: Class<*>): Method? {
        var m: Method? = null
        try {
            m = c.getDeclaredMethod(name, *cargs)
        } catch (ignored: NoSuchMethodException) {
        }

        return m
    }

    fun <T> makeFilledArray(t: Array<T>, fill: T): Array<T> {
        var i = 0
        val l = t.size
        while (i < l) {
            t[i] = fill
            i++
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
