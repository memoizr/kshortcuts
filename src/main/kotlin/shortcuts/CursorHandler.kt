package shortcuts

import com.sun.jna.platform.KeyboardUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import shortcuts.Direction.*
import java.awt.MouseInfo
import java.awt.Robot
import java.awt.event.KeyEvent.VK_META
import java.lang.Math.*

class CursorHandler {
    private val robot = Robot()

    private var vert: Double = 0.0
    private var horiz: Double = 0.0
    private var left = false
    private var down = false
    private var up = false
    private var right = false
    private var running = false
    private var slower = false

    private val decelerationFactor = 0.85
    private val slowDownFactor = 3
    private val updateIntervalMs = 20L

    private inline fun accelerate(i: Double) = ceil(max(1.0, abs(i) * 1.40) / (abs(i) / 200 + 1))
    private inline fun decelerate(i: Double) = i * decelerationFactor


    fun Double.adjusted() = (if (slower) this / slowDownFactor else this).toInt()

    private fun loop() {
        GlobalScope.launch {
            running = true
            while (left || down || up || right) {
                val location = MouseInfo.getPointerInfo().location
                when {
                    left && right -> horiz = decelerate(horiz)
                    left -> horiz = accelerate(horiz) * -1
                    right -> horiz = accelerate(horiz)
                }

                when {
                    up && down -> vert = decelerate(vert)
                    up -> vert = accelerate(vert) * -1
                    down -> vert = accelerate(vert)
                }

                if (!right && !left) horiz = 0.0
                if (!up && !down) vert = 0.0
                robot.mouseMove(location.x + horiz.adjusted(), location.y + vert.adjusted())
                delay(updateIntervalMs)
            }
            running = false
            horiz = 0.0
            vert = 0.0
        }
    }

    fun slowDown(boolean: Boolean) {
        slower = boolean
    }

    fun keyPress(direction: Direction) {
        when (direction) {
            LEFT -> {
                left = true
            }
            DOWN -> {
                down = true
            }
            UP -> {
                up = true
            }
            RIGHT -> {
                right = true
            }
        }
        if (!running) {
            loop()
        }
    }

    fun keyRelease(direction: Direction) {
        when (direction) {
            UP -> {
                up = false
                vert = max(0.0, vert)
            }
            LEFT -> {
                left = false
                horiz = max(0.0, horiz)
            }
            DOWN -> {
                down = false
                vert = min(0.0, vert)
            }
            RIGHT -> {
                right = false
                horiz = min(0.0, horiz)
            }
        }
    }
}

enum class Direction {
    LEFT, DOWN, UP, RIGHT
}