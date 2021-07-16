import dev.fritz2.binding.watch
import dev.fritz2.remote.body
import dev.fritz2.remote.websocket
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyPressFunction
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.KeyboardEvent
import react.*
import react.dom.h1
import styled.css
import styled.styledButton
import styled.styledDiv
import styled.styledHeader
import styled.styledInput

external interface TchatProps : RProps {
	var user: User
	var recipient: User
}

external interface TchatState : RState {
	var messages: List<Message>
	var message: String
}

private val scope = MainScope()

val session = websocket("ws://${window.location.host}/ws/send").connect()
class Tchat : RComponent<TchatProps, TchatState>() {

	override fun TchatState.init() {
		messages = listOf()
		message = ""
	}

	override fun componentDidMount() {
		session.messages.body.onEach {
			console.log("Received $it")
		}.watch() // watch is needed because the message is not bound to html
	}

	private fun sendMessage() {
		if (state.message.isNotEmpty()) {
			val messageDto = Message(state.message, props.user, props.recipient)
			setState { message = "" }
			scope.launch {
				session.send(messageDto.text)
				sendMessage(messageDto)
				fetchMessages()
			}
		}
	}

	private suspend fun fetchMessages() {
		val fetchedMessages = getMessages()
		if (fetchedMessages.size != state.messages.size) // i.e if new messages were found
			setState(state.apply { messages = fetchedMessages }) {
				val messagesArea = document.getElementById("messages-area") as HTMLDivElement
				messagesArea.scrollTo(0.0, messagesArea.scrollHeight.toDouble())
			}
	}

	override fun RBuilder.render() {
		styledHeader {
			h1 { +"Uni" }
			css {
				position = Position.relative
				width = 100.pct
				backgroundColor = Color.cornflowerBlue
			}
		}
		styledDiv { // Main app body
			styledDiv {  // Messages area
				for (m in state.messages) {
					messageBox {
						message = m
						user = props.user
					}
				}
				attrs {
					id = "messages-area"
				}
				css {
					display = Display.flex
					flexDirection = FlexDirection.column
					flexGrow = 1.0
					put("height", "calc(100vh - 140px)")
					overflowY = Overflow.scroll
					padding = "0 10px"
				}
			}
			styledDiv { // input area
				styledInput(type = InputType.text, name = "message") {
					css {
						width = LinearDimension.inherit
					}
					attrs {
						value = state.message
						onChangeFunction = {
							val target = it.target as HTMLInputElement
							setState {
								message = target.value
							}
						}
						onKeyPressFunction = {
							val keyboardEvent = it.asDynamic().nativeEvent as KeyboardEvent
							if (keyboardEvent.key == "Enter")
								sendMessage()
						}
					}
				}
				styledButton {
					+"Send"
					css {
						width = 50.px
						height = 50.px
						borderRadius = 25.px
					}
					attrs {
						disabled = state.message.isEmpty()
						onClickFunction = {
							sendMessage()
						}
					}
				}
				css {
					display = Display.flex
					position = Position.relative
					bottom = 0.px
					width = 100.pct
					height = 50.px
				}
			}
			css {
				display = Display.flex
				flexDirection = FlexDirection.column
				justifyContent = JustifyContent.flexEnd
				width = 100.pct
				flexGrow = 1.0
			}
		}
		styledDiv {
			css {
				width = 100.pct
				height = 10.px
				backgroundColor = Color.cornflowerBlue
			}
		}
	}
}

fun RBuilder.tchat(handler: TchatProps.() -> Unit): ReactElement {
	return child(Tchat::class) {
		this.attrs(handler)
	}
}