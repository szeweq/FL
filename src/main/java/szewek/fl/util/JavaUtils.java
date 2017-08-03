package szewek.fl.util;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

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

	private JavaUtils() {}
}
