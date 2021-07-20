import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.*

fun main() {
	val connections = Collections.synchronizedSet<Connection>(LinkedHashSet())
//	val messages = mutableListOf(
//			Message("Bonjour", false),
//			Message("Bonjour, ça va ?", true),
//			Message("Oui ça va", false),
//			Message("Et toi ?", false),
//			Message("Ça va je me plains pas", true),
//			Message("Alors quoi de neuf ?", true),
//			Message("Que du vieux", false),
//			Message("Et toi ?", false),
//			Message("Idem", true),
//			Message("On a quand même là la discussion la plus ennuyeuse au monde tu crois pas ?", true),
//	)
	val messages = mutableListOf<Message>()

	val port = System.getenv("PORT")?.toInt() ?: 9090

	embeddedServer(Netty, port) {
		install(ContentNegotiation) {
			json()
		}
		install(CORS) {
			method(HttpMethod.Get)
			method(HttpMethod.Post)
			method(HttpMethod.Delete)
			anyHost()
		}
		install(Compression) {
			gzip()
		}
		install(WebSockets) {
			pingPeriod = Duration.ofSeconds(15)
			timeout = Duration.ofSeconds(15)
			maxFrameSize = Long.MAX_VALUE
			masking = false
		}

		launch {

		}

		routing {
			get("/") {
				call.respondText (
					this::class.java.classLoader.getResource("index.html")!!.readText(),
					ContentType.Text.Html
				)
			}
			static("/") {
				resources("")
			}
			route(Message.path) {
				get {
					call.respond(messages)
				}
				post {
					messages += call.receive<Message>()
					call.respond(HttpStatusCode.OK)
				}
			}
			webSocket("/ws/send") {
				val thisConnection = Connection(this)
				connections += thisConnection
				try {
					for (frame in incoming) {
						frame as? Frame.Text ?: continue
						val messageString = frame.readText()
						println("Received: $messageString")
						messages += Message.fromString(messageString)
						connections.forEach {
							if (it != thisConnection)
								it.session.send(messageString)
						}
					}
				} catch (e: Exception) {
					println(e.localizedMessage)
				} finally {
					connections -= thisConnection
				}
			}
		}
	}.start(true)
}