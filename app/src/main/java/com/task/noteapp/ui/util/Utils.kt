package com.task.noteapp.ui.util

import android.util.Patterns

/**
 * Created by Muhammad AKKAD on 11/14/21.
 */
 object Utils {
     fun isValidUrl(string: String): Boolean = Patterns.WEB_URL.matcher(string).matches()
}