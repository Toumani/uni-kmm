import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import styled.styledDiv

external interface MessageBoxProps : RProps {
	var message: Message
}

class MessageBox : RComponent<MessageBoxProps, RState>() {
	override fun RBuilder.render() {
		styledDiv {
			+props.message.text
		}
	}
}

fun RBuilder.messageBox(handler: MessageBoxProps.() -> Unit): ReactElement {
	return child(MessageBox::class) {
		this.attrs(handler)
	}
}