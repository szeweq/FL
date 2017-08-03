package szewek.fl.test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class NamedCounters {
	private static final Map<String, Counter> names = new HashMap<>();
	private static final Map<Counter, BiConsumer<String, Counter>> consumers = new HashMap<>();

	public static Counter getCounter(String n) {
		return names.computeIfAbsent(n, Counter::new);
	}

	public static void addConsumer(Counter c, BiConsumer<String, Counter> bic) {
		BiConsumer<String, Counter> obic = consumers.get(c);
		if (obic == null)
			consumers.put(c, bic);
		else
			consumers.put(c, obic.andThen(bic));
	}

	public static void checkAndResetAll() {
		for (Map.Entry<String, Counter> e : names.entrySet()) {
			Counter c = e.getValue();
			if (consumers.containsKey(c))
				consumers.get(c).accept(e.getKey(), c);
			c.count = 0;
		}
	}

	static final class Counter {
		private long count = 0;

		Counter(String x) {}

		public void add() {
			if (count < Long.MAX_VALUE)
				count++;
		}

		public boolean expect(long min, long max) {
			return count >= min && (min >= max || count <= max);
		}

		public long getCount() {
			return count;
		}
	}
}
