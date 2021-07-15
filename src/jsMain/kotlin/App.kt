import kotlinx.browser.document
import react.*
import react.dom.*
import styled.*
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyPressFunction
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.KeyboardEvent

external interface AppState : RState {
	var messages: List<Message>
	var message: String
}

class App : RComponent<RProps, AppState>() {
	override fun AppState.init() {
		messages = listOf(
			Message(0, "Bonjour", false),
			Message(1, "Bonjour, ça va ?", true),
			Message(0, "Oui ça va", false),
			Message(0, "Et toi ?", false),
			Message(0, "Ça va je me plains pas", true),
			Message(0, "Alors quoi de neuf ?", true),
			Message(0, "Que du vieux", false),
			Message(0, "Et toi ?", false),
			Message(0, "Idem", true),
			Message(0, "On a quand même là la discussion la plus ennuyeuse au monde tu crois pas ?", true),
		)
		message = ""
	}

	fun sendMessage() {
		if (state.message.isNotEmpty()) {
			setState(state.apply {
				messages = state.messages.plus(Message(0, state.message, true))
				message = ""
			}) {
				val messagesArea = document.getElementById("messages-area") as HTMLDivElement
				messagesArea.scrollTo(0.0, messagesArea.scrollHeight.toDouble())
			}
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

