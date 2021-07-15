import react.*
import react.dom.*
import styled.*
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSubmitFunction

external interface AppState : RState {
	var messages: List<Message>
}

class App : RComponent<RProps, AppState>() {
	override fun AppState.init() {
		messages = listOf(
			Message(0, "Bonjour"),
			Message(1, "Bonjour, ça va ?"),
			Message(0, "Oui ça va\nEt toi ?")
		)
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
				css {
					flexGrow = 1.0
					overflowY = Overflow.scroll
				}
			}
			styledDiv { // input area
				styledInput(type = InputType.text, name = "message") {
					css {
						width = LinearDimension.inherit
					}
					attrs {
						onSubmitFunction = {
							console.log("Button clicked")
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
						onClickFunction = {
							console.log("Button clicked")
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

