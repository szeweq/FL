package szewek.fl.util;

import kotlin.jvm.internal.Intrinsics;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

public final class JavaUtils {
	public static final JavaUtils INSTANCE;

	@Nullable
	public final Class getClassSafely(@Nonnull String name) {
		Class var2;
		try {
			var2 = Class.forName(name);
		} catch (ClassNotFoundException var4) {
			var2 = null;
		}

		return var2;
	}

	@Nullable
	public final Method getMethodSafely(@Nonnull Class c, @Nonnull String name, @Nonnull Class... cargs) {
		Method var4;
		try {
			var4 = c.getDeclaredMethod(name, (Class[]) Arrays.copyOf(cargs, cargs.length));
		} catch (NoSuchMethodException var6) {
			var4 = null;
		}

		return var4;
	}

	public final Object[] makeFilledArray(@Nonnull Object[] t, Object fill) {
		int l = t.length - 1;
		int i = 0;
		int var5 = l;
		if (i <= l) {
			while (true) {
				t[i] = fill;
				if (i == var5) {
					break;
				}

				++i;
			}
		}

		return t;
	}

	public final void eachAnnotatedClasses(@Nonnull ASMDataTable asdt, @Nonnull Class<Annotation> ac, @Nonnull BiConsumer<Annotation, Class> eachfn) {
		Set<ASMDataTable.ASMData> aset = asdt.getAll(ac.getName());

		for (ASMDataTable.ASMData data : aset) {
			String cn = data.getClassName();
			if (Objects.equals(cn, data.getObjectName())) {
				Intrinsics.checkExpressionValueIsNotNull(cn, "cn");
				Class c = this.getClassSafely(cn);
				if (c != null) {
					Annotation a = c.getAnnotation(ac);
					eachfn.accept(a, c);
				}
			}
		}

	}

	private JavaUtils() {}

	static {
		INSTANCE = new JavaUtils();
	}
}
