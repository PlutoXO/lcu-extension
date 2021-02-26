package internal

import java.io.BufferedReader

fun request(credentials: authentication_result, url: String, method: String, body: String = ""): String =
    ssl_connection.open_connection("https://127.0.0.1:${credentials.port}$url")
        .apply {
            hostnameVerifier = ssl_connection.VERIFIER
            sslSocketFactory = ssl_connection.socket_factory("TLSv1.2")
            requestMethod = method
            doOutput = true
        }.let { connection ->
            mapOf(
                "Accept" to "application/json",
                "Content-type" to "application/json",
                "Authorization" to "Basic " + properties.base64("riot:${credentials.password}")
            ).forEach { (key, value) -> connection.addRequestProperty(key, value) }

            connection.takeIf { method == "GET" && body.isBlank() }?: run {
                connection.addRequestProperty("Content-Lenght", body.toByteArray().size.toString())
                connection.outputStream.use { os -> body.toByteArray().let { os.write(it, 0, it.size) } }
            }

            connection.inputStream.bufferedReader().use(BufferedReader::readText)
        }
