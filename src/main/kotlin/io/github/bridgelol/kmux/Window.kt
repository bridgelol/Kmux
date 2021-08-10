package io.github.bridgelol.kmux

import java.io.InputStreamReader

class Window {

    val session: Session
    var name: String
        set(newName) {
            if (!exists)
                throw IllegalStateException("Tmux window $name does not exist!")

            Runtime.getRuntime().exec(
                "tmux rename-window -t \"${fullName()}\" " +
                        "\"$newName\""
            )
            field = newName
        }

    constructor(session: Session, name: String) {
        this.session = session
        this.name = name
        this.wasCreated = exists
        if (!wasCreated)
            Runtime.getRuntime().exec("tmux new-window -t \"${fullName()}\"")
    }

    /**
     * Boolean representing whether the window exists.
     */
    val exists: Boolean
        get() {
            if (!session.exists)
                return false

            val windowExists = InputStreamReader(
                Runtime.getRuntime().exec(
                    "tmux rename-window -t \"${session.name}:$name\" " +
                            "\"kmuxexiststest\""
                )
                    .inputStream
            )
                .readLines().any { it.contains("can't find window") }

            if (windowExists) // Make sure to rename back
                Runtime.getRuntime().exec(
                    "tmux rename-window -t \"${fullName()}\" " +
                            "\"$name\""
                )


            return windowExists
        }

    /**
     * Boolean representing whether the window was created on initialization of this class.
     */
    val wasCreated: Boolean

    fun kill() {
        if (!exists)
            throw IllegalStateException("Tmux window $name does not exist!")

        Runtime.getRuntime().exec("tmux kill-window -t \"${fullName()}\"")
    }

    fun sendCommand(command: String) {
        Runtime.getRuntime().exec(
            "tmux send-keys -t " +
                    "\"${fullName()}\" ${command.replace(" ", " Space ")} C-m"
        )
    }

    private fun fullName() = "${session.name}:$name"
}