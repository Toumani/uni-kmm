import kotlinx.serialization.Serializable

@Serializable
data class Message(
	val text: String,
	val sender: User,
	val recipient: User,
) {
	val id: Int = text.hashCode()

	companion object {
		const val path = "/messages"

		fun fromString(messageString: String): Message {
			val parts = messageString.split(":")
			return Message(parts[0], User(parts[1]), User(parts[2]))
		}
	}

	override fun toString(): String {
		return "$text:${sender.name}:${recipient.name}"
	}
}