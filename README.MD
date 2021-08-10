# Kmux
Kotlin wrapper for tmux.

### Example
```kotlin
fun main() {
    val exampleSession = Session("example")
    exampleSession.sendCommand("cd /home/myuser/myfolder")
    
    Window(exampleSession, "examplewindow").sendCommand("cd /home/myuser/otherfolder")
}
```
