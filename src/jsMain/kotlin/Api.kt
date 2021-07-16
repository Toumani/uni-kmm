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

suspend fun getShoppingList(): List<ShoppingListItem> {
	return jsonClient.get(endpoint + ShoppingListItem.path)
}

suspend fun addShoopingListItem(shoppingListItem: ShoppingListItem) {
	jsonClient.post<Unit>(endpoint + ShoppingListItem.path) {
		contentType(ContentType.Application.Json)
		body = shoppingListItem
	}
}

suspend fun deleteShoppingListItem(shoppingListItem: ShoppingListItem) {
	jsonClient.delete<Unit>("$endpoint${ShoppingListItem.path}/${shoppingListItem.id}")
}
