import extensions.toPress
import extensions.toRelease
import models.KeyEvent
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.NativeInputEvent.*
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import java.util.logging.Level
import java.util.logging.Logger

class KeyLogger : NativeKeyListener {
    private val pressed: MutableMap<Int, Pair<KeyEvent, KeyEvent.() -> Unit>> = mutableMapOf()
    private val released: MutableMap<Int, Pair<KeyEvent, KeyEvent.() -> Unit>> = mutableMapOf()
    private val spacer = 10000

    fun start(): KeyLogger {
        val logger = Logger.getLogger(GlobalScreen::class.java.getPackage().getName())
        logger.level = Level.WARNING
        logger.useParentHandlers = false

        try {
            GlobalScreen.setEventDispatcher(VoidDispatchService());
            GlobalScreen.registerNativeHook()
            GlobalScreen.addNativeKeyListener(this)
        } catch (e: NativeHookException) {
            e.printStackTrace()
            System.exit(-1)
        }

        return this
    }

    fun register(keyEvent: KeyEvent, action: KeyEvent.() -> Unit) {
        val keyCode = keyEvent.key.code
        val modifiers = keyEvent.modifer

        val key = keyCode * spacer + modifiers

        if (keyEvent.isPress) {
            pressed.put(key, keyEvent.toPress() to action)
        }
        if (keyEvent.isRelease) {
            released.put(key, keyEvent.toRelease() to action)
        }
    }

    override fun nativeKeyPressed(e: NativeKeyEvent) {
        val keyCode = e.keyCode
        val modifiers = e.modifiers

        val i = keyCode * spacer + modifiers
        pressed[i]?.invoke()
        pressed[i - BUTTON1_MASK]?.invoke()
        pressed[i - BUTTON2_MASK]?.invoke()
        pressed[i - BUTTON3_MASK]?.invoke()
    }

    override fun nativeKeyReleased(e: NativeKeyEvent) {
        val keyCode = e.keyCode
        val modifiers = e.modifiers
        val i = keyCode * spacer + modifiers

        released[i]?.invoke()
        released[keyCode * spacer + (modifiers - BUTTON1_MASK)]?.invoke()
        released[keyCode * spacer + (modifiers - BUTTON2_MASK)]?.invoke()
        released[keyCode * spacer + (modifiers - BUTTON3_MASK)]?.invoke()
    }

    override fun nativeKeyTyped(e: NativeKeyEvent) {
    }

    fun Pair<KeyEvent, KeyEvent.() -> Unit>.invoke() = second.invoke(first)
}