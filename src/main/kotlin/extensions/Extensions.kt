package extensions

import HotKey
import models.*
import javax.swing.KeyStroke


infix fun Set<PressOrRelease>.keys(modifier: Modifier): Set<Modifier> = this + modifier
infix fun Set<PressOrRelease>.keys(keyEvent: KeyEvent) = keyEvent.copy(modifiers = keyEvent.modifiers + this)
infix fun PressOrRelease.keys(keyEvent: KeyEvent) = keyEvent.copy(modifiers = keyEvent.modifiers + this)

val bind = press or release
val otherModifiers = setOf(meta, control, alt, shift)

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


fun KeyEvent.toPress() = copy(modifiers = modifiers - release + press)
fun KeyEvent.toRelease() = copy(modifiers = modifiers + release - press)

fun execute(command: String) = Runtime.getRuntime().exec(command)