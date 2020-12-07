import com.sun.jna.platform.unix.X11
import org.jnativehook.keyboard.NativeKeyEvent.VC_A
import org.jnativehook.keyboard.NativeKeyEvent.VC_B
import org.junit.Test
import javax.swing.KeyStroke
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


class KeyMappingTest {
    val shift = 1 shl 0     //0001
    val control = 1 shl 1   //0010
    val meta = 1 shl 2      //0100
    val alt = 1 shl 3       //1000

    @Test
    fun mapsKeysToActions() {
        val ks1 = KeyStroke.getKeyStroke(VC_A, control or shift)

        assertEquals(Woo(ks1.keyCode, ks1.modifiers), Woo(VC_A, shift or control))
        assertNotEquals(Woo(ks1.keyCode, ks1.modifiers), Woo(VC_A, control))
        assertNotEquals(Woo(ks1.keyCode, ks1.modifiers), Woo(VC_A, shift))
        assertNotEquals(Woo(ks1.keyCode, ks1.modifiers), Woo(VC_A, alt))
        assertNotEquals(Woo(ks1.keyCode, ks1.modifiers), Woo(VC_B, shift or control))
    }
}

data class Woo(val keyCode: Int, val modifiers: Int) {
    override fun equals(other: Any?): Boolean {
        if (other !is Woo) return false
        return this.keyCode == other.keyCode && this.modifiers xor modifiers == 0
    }

    override fun hashCode(): Int {
        var hash = 17;
        hash = hash * 31 + keyCode.hashCode();
        hash = hash * 31 + modifiers.hashCode();
        return hash;
    }
}

class KeyMapper() {
    fun put(keyEvent: Woo, action: () -> Unit) {

    }

    fun get(keyEvent: Woo): () -> Unit {
        return {}
    }
}

private fun correctModifiers(modifiers: Int, flags: Int): Int {
    var ret = modifiers
    if (flags and 1 != 0) ret = ret or X11.LockMask
    if (flags and 2 != 0) ret = ret or X11.Mod2Mask
    if (flags and 4 != 0) ret = ret or X11.Mod3Mask
    if (flags and 8 != 0) ret = ret or X11.Mod5Mask
    return ret
}
