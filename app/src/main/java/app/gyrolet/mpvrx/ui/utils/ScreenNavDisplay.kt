/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package app.gyrolet.mpvrx.ui.utils

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import app.gyrolet.mpvrx.presentation.Screen

/**
 * Displays the current screen based on the given back stack.
 *
 * Each [Screen] implements its own `Content()` composable, which is invoked
 * here for the topmost entry in the stack.
 */
@Composable
fun ScreenNavDisplay(
    backStack: NavBackStack<Screen>,
    modifier: Modifier = Modifier,
    opaqueBackground: Boolean = false,
    onBack: () -> Unit = { },
) {
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = onBack,
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300),
            ) + fadeIn(animationSpec = tween(300)) togetherWith
                slideOutHorizontally(
                    targetOffsetX = { -it / 3 },
                    animationSpec = tween(300),
                ) + fadeOut(animationSpec = tween(300))
        },
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(300),
            ) + fadeIn(animationSpec = tween(300)) togetherWith
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300),
                ) + fadeOut(animationSpec = tween(300))
        },
    ) { screen ->
        screen.Content()
    }
}