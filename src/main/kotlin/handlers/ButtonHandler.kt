package handlers

import java.awt.Robot
import java.awt.event.InputEvent

class ButtonHandler {
    private val robot = Robot()
    fun leftButtonPressed() = robot.mousePress(InputEvent.BUTTON1_DOWN_MASK)
    fun middleButtonPressed() = robot.mousePress(InputEvent.BUTTON2_DOWN_MASK)
    fun rightButtonPressed() = robot.mousePress(InputEvent.BUTTON3_DOWN_MASK)
    fun scrollDownPressed() = Runtime.getRuntime().exec("xdotool click 5")
    fun scrollUpPressed() = Runtime.getRuntime().exec("xdotool click 4")
    fun scrollLeftPressed() = Runtime.getRuntime().exec("xdotool click 6")
    fun scrollRightPressed() = Runtime.getRuntime().exec("xdotool click 7")

    fun leftButtonReleased() = robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
    fun rightButtonReleased() = robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK)
    fun middleButtonReleased() = robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK)
}
