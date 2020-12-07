import com.tulskiy.keymaster.common.Provider
import org.jnativehook.keyboard.NativeKeyEvent.*
import shortcuts.ButtonHandler
import shortcuts.CursorHandler
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

    object A : Key {
        override val name = "A"
        override val code = VC_A
    }

    object Y : Key {
        override val name = "Y"
        override val code = VC_Y
    }

    object U : Key {
        override val name = "U"
        override val code = VC_U
    }

    object I : Key {
        override val name = "I"
        override val code = VC_I
    }

    object O : Key {
        override val name = "O"
        override val code = VC_O
    }

    object S : Key {
        override val name = "S"
        override val code = VC_S
    }

    object D : Key {
        override val name = "D"
        override val code = VC_D
    }

    object F : Key {
        override val name = "F"
        override val code = VC_F
    }

    object J : Key {
        override val name = "J"
        override val code = VC_J
    }

    object K : Key {
        override val name = "K"
        override val code = VC_K
    }

    object H : Key {
        override val name = "H"
        override val code = VC_H
    }

    object L : Key {
        override val name = "L"
        override val code = VC_L
    }

    object N : Key {
        override val name = "N"
        override val code = VC_N
    }

    object P : Key {
        override val name = "P"
        override val code = VC_P
    }

    object Escape : Key {
        override val name = "ESCAPE"
        override val code = VC_ESCAPE
    }

    object Space : Key {
        override val name = "SPACE"
        override val code = VC_SPACE
    }

    private fun register(event: KeyEvent, action: KeyEvent.(Boolean) -> Unit) {
        logger.register(event, {
            event.action(it)
        })
        if (event.isPress) {
//            val ks = KeyStroke.getKeyStroke(event.key.code, event.keyStroke.modifiers, true)
            val ks = KeyStroke.getKeyStroke(
                "${
                    event.modifiers.filterNot { it is release }
                        .map { it.name }
                        .joinToString(" ")
                } ${event.key.name}")
//            provider.register(event.keyStroke) { }
            provider.register(ks) { }
        }

        if (event.isRelease) {
            val ks = KeyStroke.getKeyStroke(
                "${
                    event.modifiers.filterNot { it is press }
                        .map { it.name }
                        .joinToString(" ")
                } ${event.key.name}")
//            val ks = KeyStroke.getKeyStroke(event.key.code, event.keyStroke.modifiers, false)
//            provider.register(event.keyStroke) { }
            provider.register(ks) { }
        }
    }

    infix operator fun Modifier.plus(other: Modifier) = setOf(this, other)
    infix operator fun Set<Modifier>.plus(key: Key): KeyEvent = KeyEvent(
//        KeyStroke.getKeyStroke(key.code, this.map { it.mask }.reduce { acc, mask -> acc or mask }),
        KeyStroke.getKeyStroke("${map { it.name }.joinToString(" ")} ${key.name}"),
        this,
        key
    )

    infix operator fun Modifier.plus(key: Key): KeyEvent =
//        KeyEvent(KeyStroke.getKeyStroke(key.code, mask), setOf(this), key)
        KeyEvent(KeyStroke.getKeyStroke("${this.name} ${key.name}"), setOf(this), key)

    //    infix fun KeyEvent.to(invocation: KeyEvent.() -> Unit): Unit = register(this, invocation)
    infix fun KeyEvent.to(invocation: KeyEvent.(Boolean) -> Unit): HotKey {
//        register(this, invocation)
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