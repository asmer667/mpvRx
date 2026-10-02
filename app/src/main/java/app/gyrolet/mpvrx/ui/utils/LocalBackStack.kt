/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package app.gyrolet.mpvrx.ui.utils

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation3.runtime.NavBackStack
import app.gyrolet.mpvrx.presentation.Screen

/**
 * Provides the current [NavBackStack] of [Screen] entries to any composable
 * in the tree, so screens can navigate without threading the stack through
 * every layer.
 */
val LocalBackStack = compositionLocalOf<NavBackStack<Screen>> {
    error("LocalBackStack was accessed before being provided")
}