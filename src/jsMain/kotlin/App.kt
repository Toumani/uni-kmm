import react.*

external interface AppState : RState {
	var user: User?
	var recipient: User?
}

class App : RComponent<RProps, AppState>() {

	override fun RBuilder.render() {
		if (state.user == null || state.recipient == null)
			login {
				onLogin = { u, r ->
					run {
						setState {
							user = u
							recipient = r
						}
					}
				}
			}
		else
			tchat {
				user = state.user!!
				recipient = state.recipient!!
			}
	}
}

