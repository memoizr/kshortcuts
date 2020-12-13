import aliases.Action
import com.tulskiy.keymaster.common.Provider
import extensions.otherModifiers
import handlers.KeyHandler
import handlers.ButtonHandler
import handlers.CursorHandler
import models.KeyEvent
import models.Modifier
import models.press
import models.release
import javax.swing.KeyStroke

val buttonHandler = ButtonHandler()
val cursorHandler = CursorHandler()
val keyHandler = KeyHandler()

fun ShortCutHandler.passthrough(config: ShortCutHandler.() -> Unit): Unit {
    val shortCutHandler = ShortCutHandler(provider, logger)
    shortCutHandler.config()
    shortCutHandler.hotkeys.forEach { it.ignoring(otherModifiers) }
    shortCutHandler.start()
}

class ShortCutHandler(val provider: Provider, val logger: KeyLogger) {
    val hotkeys: MutableSet<HotKey> = mutableSetOf()


    fun start() {
        hotkeys.forEach {
            register(it.keyEvent, it.action)
        }
    }

    private fun register(event: KeyEvent, action: KeyEvent.() -> Unit) {
        logger.register(event, action)
        if (event.isPress) {
            val ks = KeyStroke.getKeyStroke(
                "${
                    event.modifiers.filterNot { it is release }
                        .map { it.name }
                        .joinToString(" ")
                } ${event.key.name}")
            provider.register(ks) { }
        }

        if (event.isRelease) {
            val ks = KeyStroke.getKeyStroke(
                "${
                    event.modifiers.filterNot { it is press }
                        .map { it.name }
                        .joinToString(" ")
                } ${event.key.name}")
            provider.register(ks) { }
        }
    }

    infix fun KeyEvent.toAction(invocation: Action): HotKey {
        val hotKey = HotKey(this, invocation)
        hotkeys.add(hotKey)
        return hotKey
    }

    infix fun HotKey.ignoring(modifiers: Set<Modifier>) {
        modifiers.forEach {
            register(keyEvent.copy(modifiers = setOf(it) + keyEvent.modifiers), action)
        }
    }
}