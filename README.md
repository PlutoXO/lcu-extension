## 🚧 LCU - EXTENSION

> **Note**: This project is a **kotlin library** that extends **LCU** to simplify the use of it.

### 🔐 Authentication
The authentication result is taken from the LeagueClientUx process and used for all requests or connections from lcu. The authentication result is passed to a data class containing the port and password.

**Reference**
```kotlin
authenticate(sync_connection: Boolean = true, expiration: Long = 5000ms): authentication_result
```

**Default**
```kotlin
import internal.authenticate

val credentials: authentication_result = authenticate()
println(credentials)

//authentication_result(port=64656, pid=3236, password='', success=true)
```

> By default, the ```authenticate``` function runs until the client is found, and the default expiration is 5000ms.

**Reject**

```kotlin
import internal.authenticate

val credentials: authentication_result = authenticate(false)
println(credentials)

//authentication_result(port=64656, pid=3236, password='', success=true)
```
> If you want to throw an exception when the client process is not found, you can use ```sync_connection``` as false.

### 📬 Request
Send https requests to lcu endpoints using the ```request``` function and return the result as string.

**Reference**
```kotlin
request(credentials: authentication_result, url: String, method: String, body: String = ''): String
```

**Default**
```kotlin
import internal.authenticate
import internal.request

val credentials: authentication_result = authenticate()
val response: String = request(credentials, "/lol-summoner/v1/current-summoner", "GET")
println(response)
```
> If the request method is ```GET```, you do not need to specify the body.

### 📌 Exceptions
> league client process could not be located.

authentication fails and is rejected in the ```authenticate``` function.

> sync process timeout.

Occurs because the expiration time overs when the ```sync_connection``` of the ```authenticate``` function is true.

## 📋 License
Distributed under the MIT License. See ```LICENSE``` for more information.

**© 2021 PlutoXO, All rights reserved.**
