import react.*
import react.dom.*
import kotlinx.html.js.onClickFunction

external interface LoginProps : RProps {
	var onLogin: (User, User) -> Unit
}

class Login : RComponent<LoginProps, RState>() {
	override fun RBuilder.render() {
		form {
			button {
				+"Login as Toum"
				attrs {
					onClickFunction = {
						props.onLogin(User("Toum"), User("Trez"))
					}
				}
			}
			button {
				+"Login as Trez"
				attrs {
					onClickFunction = {
						props.onLogin(User("Trez"), User("Toum"))
					}
				}
			}
		}
	}
}

fun RBuilder.login(handler: LoginProps.() -> Unit): ReactElement {
	return child(Login::class) {
		this.attrs(handler)
	}
}