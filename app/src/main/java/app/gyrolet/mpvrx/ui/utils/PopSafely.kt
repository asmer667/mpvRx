/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package app.gyrolet.mpvrx.ui.utils

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

/**
 * Pops the top entry from the back stack, but only if there is more than one
 * entry remaining. Returns true if a pop occurred, false otherwise.
 *
 * This prevents the app from ending up with an empty back stack, which would
 * crash the navigation framework.
 */
fun <T : NavKey> NavBackStack<T>.popSafely(): Boolean {
    if (size <= 1) return false
    removeLastOrNull() ?: return false
    return true
}