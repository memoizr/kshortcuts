import aliases.Action
import com.tulskiy.keymaster.common.Provider
import models.KeyEvent
import shortcuts.shortCuts

fun main(args: Array<String>) {
    shortcuts(shortCuts)
}

fun shortcuts(config: ShortCutHandler.() -> Unit) {
    val provider: Provider = Provider.getCurrentProvider(true)
    val keyLogger = KeyLogger().start()
    val handler = ShortCutHandler(provider, keyLogger)
    handler.config()
    println(handler.hotkeys)
    handler.start()
}

data class HotKey(val keyEvent: KeyEvent, val action: Action)
data class CodeWithModifiers(val code: Int, val modifier: Int)

fun <T> T.print(message: String): T = also { println("$message $it") }


fun KeyEvent.onPress(action: KeyEvent.() -> Unit) {
    if (isPress) action()
}

fun KeyEvent.onRelease(action: KeyEvent.() -> Unit) {
    if (isRelease) action()
}

fun onPress(action: KeyEvent.() -> Unit): Action = {
    if (isPress) action()
}

fun onRelease(action: KeyEvent.() -> Unit): Action = {
    if (isRelease) action()
}

fun Action.onPress(action: KeyEvent.() -> Unit): Action = {
    this@onPress(this)
    if (isPress) action()
}

fun Action.onRelease(action: KeyEvent.() -> Unit): Action = {
    this@onRelease(this)
    if (isRelease) action()
}
