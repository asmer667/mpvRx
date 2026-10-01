package app.gyrolet.mpvrx.presentation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
interface Screen : NavKey {
  @Composable
  fun Content()
}