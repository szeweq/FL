package szewek.fl.network

import com.google.common.io.ByteStreams
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.internal.bind.TypeAdapters.JSON_ELEMENT
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import net.minecraftforge.fml.common.Loader
import szewek.fl.FL
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class FLCloud private constructor(private val name: String, private val key: String) {
    private val clikey: String? = null
    private var cachedVersion = ""

    override fun hashCode(): Int {
        return 31 * name.hashCode() + key.hashCode()
    }

    fun connect(path: String): APICall? {
        try {
            val huc = URL("https", "api-fl.1d35.starter-us-east-1.openshiftapps.com", 443, path, null).openConnection() as HttpURLConnection
            huc.setRequestProperty("Authorization", "Bearer " + key)
            if (clikey != null)
                huc.setRequestProperty("X-Client-Key", clikey)
            return APICall(huc)
        } catch (x: Exception) {
            x.printStackTrace()
        }

        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FLCloud
        return name == other.name && key == other.key
    }

    class APICall internal constructor(private val conn: HttpURLConnection) {

        private val response: InputStream
            @Throws(IOException::class)
            get() {
                val rc = conn.responseCode
                return if (rc / 100 == 2) conn.inputStream else conn.errorStream
            }

        @Throws(IOException::class)
        fun statusCode(): Int {
            return conn.responseCode
        }

        @Throws(IOException::class)
        fun postJSON(o: JsonElement): APICall {
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            val jw = JsonWriter(BufferedWriter(OutputStreamWriter(conn.outputStream)))
            jw.isLenient = true
            JSON_ELEMENT.write(jw, o)
            jw.close()
            conn.outputStream.close()
            return this
        }

        @Throws(IOException::class)
        fun responseJSON(): JsonElement {
            var jsonCheck = false
            try {
                val jr = JsonReader(InputStreamReader(response))
                jr.isLenient = true
                jsonCheck = true
                jr.peek()
                jsonCheck = false
                return JSON_ELEMENT.read(jr)
            } catch (x: Exception) {
                if (x is EOFException && jsonCheck) {
                    return JsonNull.INSTANCE
                }
                throw x
            }

        }

        @Throws(IOException::class)
        fun responseText(): String {
            val `in` = response
            val bout = ByteArrayOutputStream(Math.max(32, `in`.available()))
            ByteStreams.copy(`in`, bout)
            return bout.toString("UTF-8")
        }
    }

    companion object {
        private val apis = Collections.synchronizedMap(HashMap<String, FLCloud>())

        fun getAPI(n: String, k: String): FLCloud {
            if (apis.containsKey(n)) {
                return apis[n]!!
            }
            val api = FLCloud(n, k)
            apis.put(n, api)
            return api
        }

        fun checkVersions() {
            var ac: APICall?
            for (api in apis.values) {
                ac = api.connect("/api/version/" + Loader.MC_VERSION)
                if (ac == null) {
                    continue
                }
                try {
                    val es = ac.responseText()
                    if (ac.statusCode() == 200) {
                        api.cachedVersion = es
                        FL.L!!.info("Cached version for {}: {}", api.name, api.cachedVersion)
                    } else {
                        FL.L!!.error("Got Error Response: {}", es)
                    }

                } catch (x: Exception) {
                    FL.L!!.error("Exception thrown while getting a version for " + api.name, x)
                }

            }
        }
    }
}
