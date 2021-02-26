package internal

import java.io.BufferedReader
import java.lang.Exception
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*


object properties {
    val client_ux_cl = system_property("os.name").takeIf { it.contains("windows") }
        ?.let { "WMIC PROCESS WHERE name=\"LeagueClientUx.exe\" GET CommandLine" }
        ?: "ps x -o args | grep \"LeagueClientUx\""

    fun system_property(property_key: String) = System.getProperty(property_key).toLowerCase()
    val current_millis: Long get() = System.currentTimeMillis()

    fun base64(origin: String): String = String(Base64.getEncoder().encode(origin.toByteArray()))

    fun string_exclude(origin: String) = Regex("[0-9]+").find(origin)
        ?.value?.toInt() ?: throw Exception("Not found.")

    fun String.match(regex: String) = Regex(regex).find(this)
        ?.value ?: throw Exception("Not found.")
}

object process {
    fun exec(command: String, sync: Boolean = true, expiration: Long = 5000): String = sync.takeIf { it }
        ?.let { sync(command, expiration) }
        ?:Runtime.getRuntime().exec(command).inputStream.bufferedReader().use(BufferedReader::readText)

    fun sync(command: String, expiration: Long): String {
        val sync_expiration = properties.current_millis + expiration
        while (true) {
            Runtime.getRuntime().exec(command).inputStream.bufferedReader().use(BufferedReader::readText).takeIf { it.isNotBlank() }
                ?.also { return it }
                ?:if(properties.current_millis >= sync_expiration) throw Exception("sync process timeout.") else Thread.sleep(1000)
        }
    }
}

object ssl_connection {
    fun open_connection(url: String): HttpsURLConnection = URL(url).openConnection() as HttpsURLConnection

    val VERIFIER = HostnameVerifier{ hostname, session -> true }

    val trust_manager = arrayOf<TrustManager>(object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        override fun checkClientTrusted(certs: Array<X509Certificate>?, auth_type: String?) = Unit
        override fun checkServerTrusted(certs: Array<X509Certificate>?, auth_type: String?) = Unit
    })

    fun socket_factory(protocol: String) = SSLContext.getInstance(protocol).also { it.init(null, trust_manager, SecureRandom()) }.socketFactory
}