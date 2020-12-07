import com.tulskiy.keymaster.common.Provider
import org.jnativehook.keyboard.NativeKeyEvent.*
import shortcuts.shortCuts
import javax.swing.KeyStroke

val bind = press or release
val otherModifiers = setOf(meta, control, alt, shift)

fun main(args: Array<String>) {
    runShort(shortCuts)
}

infix fun Set<PressOrRelease>.keys(modifier: Modifier): Set<Modifier> = this + modifier
infix fun Set<PressOrRelease>.keys(keyEvent: KeyEvent) = keyEvent.copy(modifiers = keyEvent.modifiers + this)
infix fun PressOrRelease.keys(keyEvent: KeyEvent) = keyEvent.copy(modifiers = keyEvent.modifiers + this)

sealed class PressOrRelease : Modifier

interface Modifier {
    val name: String
    val mask: Int


}

object meta : Modifier {
    override val name = "meta"
    override val mask = META_L_MASK
}

object release : PressOrRelease() {
    override val name = "released"
    override val mask = 0

    infix fun or(press: press) = setOf(this, press)
}

object press : PressOrRelease() {
    override val name = "pressed"
    override val mask = 0
    infix fun or(release: release) = setOf(this, release)
}


object control : Modifier {
    override val name = "control"
    override val mask = CTRL_L_MASK
}

object shift : Modifier {
    override val name = "shift"
    override val mask = SHIFT_L_MASK
}

object alt : Modifier {
    override val name by lazy { "alt" }
    override val mask = ALT_L_MASK
}

interface Key {
    val name: String
    val code: Int
}


data class KeyEvent(val keyStroke: KeyStroke, val modifiers: Set<Modifier>, val key: Key) {
    val modifer = modifiers.map { it.mask }.reduce { acc, i -> acc or i }
    val isPress = press in modifiers
    val isRelease = release in modifiers
}

fun runShort(config: ShortCutHandler.() -> Unit) {
    val provider: Provider = Provider.getCurrentProvider(true)
    val keyLogger = KeyLogger().start()
    val handler = ShortCutHandler(provider, keyLogger)
    handler.config()
    println(handler.hotkeys)
    handler.start()
}


typealias Shortcuts = ShortCutHandler.() -> Unit
typealias Binding = KeyEvent.(Boolean) -> Unit


data class HotKey(val keyEvent: KeyEvent, val action: KeyEvent.(Boolean) -> Unit)
data class CodeWithModifiers(val code: Int, val modifier: Int)

fun <T> T.print(message: String): T = also { println("$message $it") }


fun KeyEvent.pressed(action: KeyEvent.() -> Unit) {
    if (isPress) action()
}

fun KeyEvent.released(action: KeyEvent.() -> Unit) {
    if (isRelease) action()
}

