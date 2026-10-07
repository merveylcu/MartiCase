package com.merveylcu.marticase.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object MartiIcons {
    val Add: ImageVector = Icons.Filled.Add
    val Close: ImageVector = Icons.Filled.Close
    val Delete: ImageVector = Icons.Filled.Delete
    val Location: ImageVector = Icons.Filled.LocationOn
    val Play: ImageVector = Icons.Filled.PlayArrow

    val Remove: ImageVector by lazy {
        ImageVector
            .Builder(
                name = "Remove",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).path(fill = SolidColor(Color.Black)) {
                moveTo(19f, 13f)
                horizontalLineTo(5f)
                verticalLineToRelative(-2f)
                horizontalLineToRelative(14f)
                close()
            }.build()
    }
}
