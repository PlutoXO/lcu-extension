import internal.authenticate
import internal.request

fun main() {
  val credentials = authenticate()
  val response = request(credentials, "/lol-summoner/v1/current-summoner", "GET")
  println(response)
}