package com.merveylcu.marticase.feature.tracking.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.merveylcu.marticase.core.designsystem.component.MartiBottomSheet
import com.merveylcu.marticase.core.designsystem.component.MartiIcon
import com.merveylcu.marticase.core.designsystem.component.MartiProgressIndicator
import com.merveylcu.marticase.core.designsystem.component.MartiText
import com.merveylcu.marticase.core.designsystem.icon.MartiIcons
import com.merveylcu.marticase.core.designsystem.theme.MartiCaseTheme
import com.merveylcu.marticase.feature.tracking.presentation.R
import com.merveylcu.marticase.feature.tracking.presentation.model.AddressState
import com.merveylcu.marticase.feature.tracking.presentation.model.SelectedPoint
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun PointDetailSheet(
    selected: SelectedPoint,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MartiBottomSheet(onDismiss = onDismiss, modifier = modifier) {
        PointDetailContent(selected = selected)
    }
}

@Composable
internal fun PointDetailContent(selected: SelectedPoint, modifier: Modifier = Modifier) {
    val spacing = MartiCaseTheme.spacing
    val time = remember(selected.point.recordedAtMillis) {
        DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(selected.point.recordedAtMillis))
    }
    val coordinates = remember(selected.point.coordinate) {
        String.format(
            Locale.US,
            "%.5f, %.5f",
            selected.point.coordinate.latitude,
            selected.point.coordinate.longitude,
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = spacing.lg, end = spacing.lg, bottom = spacing.xl),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.sm),
        ) {
            MartiIcon(
                imageVector = MartiIcons.Location,
                contentDescription = null,
                tint = MartiCaseTheme.colors.primary,
            )
            MartiText(
                text = stringResource(R.string.point_title, selected.order),
                style = MartiCaseTheme.typography.titleLarge,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(spacing.xs)) {
            MartiText(
                text = stringResource(R.string.point_address),
                style = MartiCaseTheme.typography.labelLarge,
                color = MartiCaseTheme.colors.onSurfaceVariant,
            )
            when (val address = selected.address) {
                AddressState.Loading -> MartiProgressIndicator()

                is AddressState.Loaded -> MartiText(
                    text = address.text,
                    style = MartiCaseTheme.typography.bodyLarge,
                )

                AddressState.Unavailable -> MartiText(
                    text = stringResource(R.string.point_address_unavailable),
                    style = MartiCaseTheme.typography.bodyLarge,
                    color = MartiCaseTheme.colors.onSurfaceVariant,
                )
            }
        }

        MartiText(
            text = stringResource(R.string.point_recorded_at, time) + "  ·  " + coordinates,
            style = MartiCaseTheme.typography.bodySmall,
            color = MartiCaseTheme.colors.onSurfaceVariant,
        )
    }
}
