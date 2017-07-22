package szewek.fl.network;

import com.google.common.io.ByteStreams;
import net.minecraftforge.fml.common.Loader;
import szewek.fl.FL;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FLCloud {
	private static Map<String, FLCloud> apis = Collections.synchronizedMap(new HashMap<>());

	private final String name, key;
	private String cachedVersion = "";

	private FLCloud(String n, String k) {
		name = n;
		key = k;
	}

	@Override
	public int hashCode() {
		return 31 * name.hashCode() + key.hashCode();
	}

	private HttpURLConnection connect(String path) {
		HttpURLConnection huc = null;
		try {
			huc = (HttpURLConnection) new URL("https", "flcloud.herokuapp.com", 443, path, null).openConnection();
			huc.addRequestProperty("Authorization", "Bearer " + key);
		} catch (Exception x) {
			x.printStackTrace();
		}
		return huc;
	}

	public static FLCloud getAPI(String n, String k) {
		if (apis.containsKey(n)) {
			return apis.get(n);
		}
		FLCloud api = new FLCloud(n, k);
		apis.put(n, api);
		return api;
	}

	public static void checkVersions() {
		HttpURLConnection huc;
		for (FLCloud api : apis.values()) {
			huc = api.connect("/api/version/" + Loader.MC_VERSION);
			if (huc == null) {
				continue;
			}
			try {
				api.cachedVersion = new String(ByteStreams.toByteArray(huc.getInputStream()), "UTF-8");
				FL.L.info("Cached version for {}: {}", api.name, api.cachedVersion);
			} catch (Exception x) {
				String es = "";
				try {
					es = new String(ByteStreams.toByteArray(huc.getErrorStream()), "UTF-8");
				} catch (Exception x2) {
					x2.printStackTrace();
				}
				FL.L.error(es, x);
			}
		}
	}
}
