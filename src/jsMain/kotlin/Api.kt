import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

import kotlinx.browser.window

val endpoint = window.location.origin

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
