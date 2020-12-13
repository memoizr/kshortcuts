package models

import org.jnativehook.keyboard.NativeKeyEvent.*
import javax.swing.KeyStroke

sealed class PressOrRelease : Modifier

interface Modifier {
    val name: String
    val mask: Int
    val code: Int
}

object meta : Modifier {
    override val name = "meta"
    override val mask = META_L_MASK
    override val code = VC_META
}

object release : PressOrRelease() {
    override val name = "released"
    override val mask = 0
    override val code = 0

    infix fun or(press: press) = setOf(this, press)
}

object press : PressOrRelease() {
    override val name = "pressed"
    override val mask = 0
    override val code = 0

    infix fun or(release: release) = setOf(this, release)
}


object control : Modifier {
    override val name = "control"
    override val mask = CTRL_L_MASK
    override val code = VC_CONTROL
}

object shift : Modifier {
    override val name = "shift"
    override val mask = SHIFT_L_MASK
    override val code = VC_SHIFT
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

object alt : Modifier {
    override val name by lazy { "alt" }
    override val mask = ALT_L_MASK
    override val code = VC_ALT
}

object alt_r : Modifier {
    override val name by lazy { "alt" }
    override val mask = ALT_R_MASK
    override val code = VC_ALT
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
