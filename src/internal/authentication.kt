package internal

import internal.properties.match

data class authentication_result(var port: Int, var pid: Int, var password: String, var success: Boolean)

fun authenticate(sync_connection: Boolean = true, expiration: Long = 5000): authentication_result = (sync_connection.takeIf { it }
    ?.let { process.exec(properties.client_ux_cl, true, expiration) }

    ?: process.exec(properties.client_ux_cl, false)
        .ifBlank { throw Exception("league client process could not be located.") })

    .let { authentication_result(properties.string_exclude(it.match("--app-port=([0-9])+")), properties.string_exclude(it.match("--app-pid=([0-9]+)")),
        it.match("--remoting-auth-token=([\\w-_]+)").split("=")[1], true)
    }