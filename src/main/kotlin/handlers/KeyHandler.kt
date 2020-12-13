package handlers

import java.awt.Rectangle
import java.awt.Robot
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.VK_ENTER
import java.io.File
import javax.imageio.ImageIO

class KeyHandler {
    private val robot = Robot()

    fun pressEnter() {
        println("pressing")
        robot.keyPress('\r'.toInt())
    }

    fun releaseEnter() {
        println("releasing")
        robot.keyRelease('\r'.toInt())
    }

}
