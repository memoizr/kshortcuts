package shortcuts

import Binding
import Shortcuts
import ShortCutHandler.*
import alt
import bind
import buttonHandler
import com.sun.jna.platform.unix.X11
import cursorHandler
import keyHandler
import keys
import meta
import press
import passthrough
import print
import released
import shortcuts.Direction.*
import kotlin.system.measureTimeMillis

val shortCuts: Shortcuts = {
    passthrough {
        bind keys meta + A to moveCursorLeft
        bind keys meta + S to moveCursorDown
        bind keys meta + D to moveCursorUp
        bind keys meta + F to moveCursorRight

        bind keys meta + N to slowCursor

        bind keys meta + Space to leftMouseClick
        bind keys meta + P to middleMouseClick
        bind keys meta + Escape to rightMouseClick

        press keys meta + J to scrollDown
        press keys meta + K to scrollUp
        press keys meta + H to scrollLeft
        press keys meta + L to scrollRight
    }

    bind keys meta + Y to moveCursorLeft
    bind keys meta + U to moveCursorDown
    bind keys meta + I to moveCursorUp
    bind keys meta + O to moveCursorRight

    bind keys meta + alt + O to pressEnterKey
}

val scrollLeft: Binding = { buttonHandler.scrollLeftPressed() }
val scrollRight: Binding = { buttonHandler.scrollRightPressed() }
val scrollDown: Binding = { buttonHandler.scrollDownPressed() }
val scrollUp: Binding = { buttonHandler.scrollUpPressed() }

val pressEnterKey: Binding = {
    released {  }
    println("=========")
//    keyHandler.pressEnter()
//    if (it) keyHandler.pressEnter() else keyHandler.releaseEnter()
    measureTimeMillis {
        if (it) Runtime.getRuntime().exec("""xvkbd -xsendevent -text \r""")
    }.print("time to execute")
}

val middleMouseClick: Binding = {
    if (it) {
        buttonHandler.middleButtonPressed()
    }
    if (!it) {
        buttonHandler.middleButtonReleased()
    }
}

val leftMouseClick: Binding = {
    if (it) {
        buttonHandler.leftButtonPressed()
    }
    if (!it) {
        buttonHandler.leftButtonReleased()
    }
}

val rightMouseClick: Binding = {
    if (it) {
        buttonHandler.rightButtonPressed()
    }
    if (!it) {
        buttonHandler.rightButtonReleased()
    }
}
val moveCursorLeft: Binding = moveCursor(LEFT)
val moveCursorRight: Binding = moveCursor(RIGHT)
val moveCursorUp: Binding = moveCursor(UP)
val moveCursorDown: Binding = moveCursor(DOWN)


val slowCursor: Binding = { cursorHandler.slowDown(it) }

fun moveCursor(direction: Direction): Binding = {
    if (it) {
        cursorHandler.keyPress(direction)
    }
    if (!it) {
        cursorHandler.keyRelease(direction)
    }
}

//val moveCursorLeft: Binding = {
//    val direction = LEFT
//    pressed { cursorHandler.keyPress(direction) }
//    released { cursorHandler.keyRelease(direction) }
//}
//
//val moveCursorRight: Binding = {
//    val direction = RIGHT
//    pressed { cursorHandler.keyPress(direction) }
//    released { cursorHandler.keyRelease(direction) }
//}
//val moveCursorDown: Binding = {
//    val direction = DOWN
//    pressed { cursorHandler.keyPress(direction) }
//    released { cursorHandler.keyRelease(direction) }
//}
//
//val moveCursorUp: Binding = {
//    val direction = UP
//    pressed { cursorHandler.keyPress(direction) }
//    released { cursorHandler.keyRelease(direction) }
//}
