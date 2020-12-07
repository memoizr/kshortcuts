import com.sun.jna.platform.unix.X11
import org.junit.Test
import java.awt.Event
import java.awt.event.InputEvent.META_DOWN_MASK
import java.awt.event.InputEvent.*
import java.lang.Math.max
import java.lang.Math.ceil


class YoTest {

    @Test
    fun df() {

        fun yo(i: Double) = ceil(max(1.0, Math.abs(i) * 1.3) / (Math.abs(i) /200 + 1))

        println((1..100)
            .map { 0.0 }
            .scan(yo(0.0)) { acc, _ -> yo(acc) })

    }
}