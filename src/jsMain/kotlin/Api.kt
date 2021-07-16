import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

import kotlinx.browser.window
import org.w3c.dom.WebSocket

val endpoint = window.location.origin

val ws = WebSocket("ws://localhost:9090/ws/send") // TODO replace this with env var

val jsonClient = HttpClient {
	install(JsonFeature) { serializer = KotlinxSerializer() }
}

suspend fun getMessages(): List<Message> {
	return jsonClient.get(endpoint + Message.path)
}

suspend fun sendMessage(message: Message) {
	jsonClient.post<Unit>(endpoint + Message.path) {
		contentType(ContentType.Application.Json)
		body = message
	}
}
