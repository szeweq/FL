package szewek.fl.util;

import net.minecraftforge.fml.common.discovery.ASMDataTable;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.BiConsumer;

public final class JavaUtils {

	@Nullable
	public static Class<?> getClassSafely(String name) {
		Class<?> c = null;
		try {
			c = Class.forName(name);
		} catch (ClassNotFoundException ignored) {}
		return c;
	}

	@Nullable
	public static Method getMethodSafely(Class<?> c, String name, Class<?>... cargs) {
		Method m = null;
		try {
			m = c.getDeclaredMethod(name, cargs);
		} catch (NoSuchMethodException ignored) {}
		return m;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] makeFilledArray(T[] t, T fill) {
		for (int i = 0, l = t.length; i < l; i++)
			t[i] = fill;
		return t;
	}

	public static <A extends Annotation> void eachAnnotatedClasses(ASMDataTable asdt, Class<A> ac, BiConsumer<A, Class<?>> eachfn) {
		final Set<ASMDataTable.ASMData> aset = asdt.getAll(ac.getCanonicalName());
		for (ASMDataTable.ASMData data : aset) {
			final String cn = data.getClassName();
			if (!cn.equals(data.getObjectName())) continue;
			final Class<?> c = getClassSafely(cn);
			if (c == null) continue;
			final A a = c.getAnnotation(ac);
			eachfn.accept(a, c);
		}
	}

	private JavaUtils() {}
}
