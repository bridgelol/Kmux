package io.github.bridgelol.kmux

import java.io.InputStreamReader

/**
 * Class representing a tmux session.
 *
 * @see <a href="https://github.com/tmux/tmux/wiki">https://github.com/tmux/tmux/wiki</a>
 * @author Bridge
 * @since 1.0
 * @param name Name of the tmux session.
 */
class Session(val name: String) {

    /**
     * Boolean representing whether the session exists.
     */
    val exists: Boolean
        get() =
            InputStreamReader(Runtime.getRuntime().exec("tmux ls").inputStream)
                .readLines().any { it.split(":")[0] == name }

    /**
     * Boolean representing whether the tmux was created on initialization of this class.
     */
    val wasCreated = exists

    val windows: Int
        get() {
            val sessionLine = InputStreamReader(Runtime.getRuntime().exec("tmux ls").inputStream)
                .readLines().firstOrNull { it.split(":")[0] == name } ?: return -1

            return sessionLine
                .split(":")[1]
                .replace(" ", "")
                .split("windows")[0]
                .toInt()
        }

    init {
        if (!wasCreated)
            Runtime.getRuntime().exec("tmux new -d -s $name")
    }

    fun kill() {
        if (!exists)
            throw IllegalStateException("Tmux session $name does not exist!")

        Runtime.getRuntime().exec("tmux kill-ses -t $name")
    }

    fun sendCommand(command: String) {
        Runtime.getRuntime().exec(
            "tmux send-keys -t " +
                    "$name ${command.replace(" ", " Space ")} C-m"
        )
    }
}