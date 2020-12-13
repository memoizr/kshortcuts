@file:Repository("https://jitpack.io")
@file:DependsOn("com.github.memoizr:kshortcuts:-SNAPSHOT")

import aliases.Action
import extensions.bind
import extensions.keys
import extensions.plus
import handlers.Direction
import handlers.Direction.*
import models.*

shortcuts {
    passthrough {
        bind keys meta + A toAction moveCursorLeft
        bind keys meta + S toAction moveCursorDown
        bind keys meta + D toAction moveCursorUp
        bind keys meta + F toAction moveCursorRight

        bind keys meta + N toAction slowCursor

        bind keys meta + Space toAction leftMouseClick
        bind keys meta + P toAction middleMouseClick
        bind keys meta + Escape toAction rightMouseClick

        press keys meta + J toAction scrollDown
        press keys meta + K toAction scrollUp
        press keys meta + H toAction scrollLeft
        press keys meta + L toAction scrollRight
    }

    bind keys meta + Y toAction moveCursorLeft
    bind keys meta + U toAction moveCursorDown
    bind keys meta + I toAction moveCursorUp
    bind keys meta + O toAction moveCursorRight

    press keys meta + alt_r + O toAction {
    }
}

val scrollLeft: Action = { buttonHandler.scrollLeftPressed() }
val scrollRight: Action = { buttonHandler.scrollRightPressed() }
val scrollDown: Action = { buttonHandler.scrollDownPressed() }
val scrollUp: Action = { buttonHandler.scrollUpPressed() }

val pressEnterKey = onPress {
//    execute("""xvkbd -xsendevent -text \r""")
}


val middleMouseClick =
    onPress { buttonHandler.middleButtonPressed() }
        .onRelease { buttonHandler.middleButtonReleased() }

val leftMouseClick =
    onPress { buttonHandler.leftButtonPressed() }
        .onRelease { buttonHandler.leftButtonReleased() }

val rightMouseClick =
    onPress { buttonHandler.rightButtonPressed() }
        .onRelease { buttonHandler.rightButtonReleased() }

val moveCursorLeft = moveCursor(LEFT)
val moveCursorRight = moveCursor(RIGHT)
val moveCursorUp = moveCursor(UP)
val moveCursorDown = moveCursor(DOWN)


val slowCursor: Action = { cursorHandler.slowDown(isPress) }

fun moveCursor(direction: Direction) =
    onPress { cursorHandler.keyPress(direction) }
        .onRelease { cursorHandler.keyRelease(direction) }
