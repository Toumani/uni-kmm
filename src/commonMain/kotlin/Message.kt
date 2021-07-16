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
	}
}