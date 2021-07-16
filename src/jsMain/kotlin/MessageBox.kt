import react.*
import styled.*
import kotlinx.css.*

external interface MessageBoxProps : RProps {
	var message: Message
	var user: User
}

class MessageBox : RComponent<MessageBoxProps, RState>() {
	override fun RBuilder.render() {
		val received = props.message.sender.name != props.user.name
		styledDiv {
			+props.message.text
			css {
				width = 80.pct
				margin = "10px 0"
				padding = "20px"
				borderRadius = 5.px
				backgroundColor = if (received) Color.aquamarine else Color.turquoise
				alignSelf = if (received) Align.flexStart else Align.flexEnd
			}
		}
	}
}

fun RBuilder.messageBox(handler: MessageBoxProps.() -> Unit): ReactElement {
	return child(MessageBox::class) {
		this.attrs(handler)
	}
}