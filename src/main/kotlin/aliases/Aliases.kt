package aliases

import ShortCutHandler
import models.KeyEvent

typealias Shortcuts = ShortCutHandler.() -> Unit
typealias Action = KeyEvent.() -> Unit

