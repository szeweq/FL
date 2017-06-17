package szewek.fl.recipes;

/**
 * Recipe item identifier. It is used on recipe shapes.
 */
public enum R9 {
	A, B, C, D, E, F, G, H, I;

	public final byte ord = (byte) ordinal();

	public static final R9[]
			STAR = {null, A, null, A, B, A, null, A, null},
			CROSS = {A, null, A, null, B, null, A, null, A};
}
