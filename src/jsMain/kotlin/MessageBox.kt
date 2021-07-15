import react.*
import styled.*
import kotlinx.css.*

external interface MessageBoxProps : RProps {
	var message: Message
}

class MessageBox : RComponent<MessageBoxProps, RState>() {
	override fun RBuilder.render() {
		styledDiv {
			+props.message.text
			css {
				width = 80.pct
				margin = "10px 0"
				padding = "20px"
				borderRadius = 5.px
				backgroundColor = if (props.message.send) Color.turquoise else Color.aquamarine
				alignSelf = if (props.message.send) Align.flexEnd else Align.flexStart
			}
		}
	}
}

fun RBuilder.messageBox(handler: MessageBoxProps.() -> Unit): ReactElement {
	return child(MessageBox::class) {
		this.attrs(handler)
	}
}