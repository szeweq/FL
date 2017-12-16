package szewek.fl.network;

import com.google.common.io.ByteStreams;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraftforge.fml.common.Loader;
import szewek.fl.FL;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.google.gson.internal.bind.TypeAdapters.JSON_ELEMENT;

public class FLCloud {
	private static Map<String, FLCloud> apis = Collections.synchronizedMap(new HashMap<>());

	private final String name, key;
	private String clikey = null, cachedVersion = "";

	private FLCloud(String n, String k) {
		name = n;
		key = k;
	}

	@Override
	public int hashCode() {
		return 31 * name.hashCode() + key.hashCode();
	}

	public APICall connect(String path) {
		try {
			final HttpURLConnection huc = (HttpURLConnection) new URL("https", "api-fl.1d35.starter-us-east-1.openshiftapps.com", 443, path, null).openConnection();
			huc.setRequestProperty("Authorization", "Bearer " + key);
			if (clikey != null)
				huc.setRequestProperty("X-Client-Key", clikey);
			return new APICall(huc);
		} catch (Exception x) {
			x.printStackTrace();
		}
		return null;
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
		APICall ac;
		for (FLCloud api : apis.values()) {
			ac = api.connect("/api/version/" + Loader.MC_VERSION);
			if (ac == null) {
				continue;
			}
			try {
				final String es = ac.responseText();
				if (ac.statusCode() == 200) {
					api.cachedVersion = es;
					FL.L.info("Cached version for {}: {}", api.name, api.cachedVersion);
				} else {
					FL.L.error("Got Error Response: {}", es);
				}

			} catch (Exception x) {
				FL.L.error("Exception thrown while getting a version for " + api.name, x);
			}
		}
	}

	public static class APICall {
		private final HttpURLConnection conn;

		private APICall(HttpURLConnection huc) {
			conn = huc;
		}

		public int statusCode() throws IOException {
			return conn.getResponseCode();
		}

		private InputStream getResponse() throws IOException {
			final int rc = conn.getResponseCode();
			return rc / 100 == 2 ? conn.getInputStream() : conn.getErrorStream();
		}

		public APICall postJSON(JsonElement o) throws IOException {
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			JsonWriter jw = new JsonWriter(new BufferedWriter(new OutputStreamWriter(conn.getOutputStream())));
			jw.setLenient(true);
			JSON_ELEMENT.write(jw, o);
			jw.close();
			conn.getOutputStream().close();
			return this;
		}

		public JsonElement responseJSON() throws IOException {
			conn.setRequestProperty("Accept", "application/json");
			boolean jsonCheck = false;
			try {
				JsonReader jr = new JsonReader(new InputStreamReader(getResponse()));
				jr.setLenient(true);
				jsonCheck = true;
				jr.peek();
				jsonCheck = false;
				return JSON_ELEMENT.read(jr);
			} catch (Exception x) {
				if (x instanceof EOFException && jsonCheck) {
					return JsonNull.INSTANCE;
				}
				throw x;
			}
		}

		public String responseText() throws IOException {
			conn.setRequestProperty("Accept", "text/plain; charset=UTF-8");
			InputStream in = getResponse();
			ByteArrayOutputStream bout = new ByteArrayOutputStream(Math.max(32, in.available()));
			ByteStreams.copy(in, bout);
			return bout.toString("UTF-8");
		}
	}
}
