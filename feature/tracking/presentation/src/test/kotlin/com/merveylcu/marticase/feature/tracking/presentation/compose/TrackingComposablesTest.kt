package com.merveylcu.marticase.feature.tracking.presentation.compose

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.merveylcu.marticase.core.designsystem.theme.MartiCaseTheme
import com.merveylcu.marticase.feature.tracking.domain.model.Coordinate
import com.merveylcu.marticase.feature.tracking.domain.model.RoutePoint
import com.merveylcu.marticase.feature.tracking.presentation.R
import com.merveylcu.marticase.feature.tracking.presentation.model.AddressState
import com.merveylcu.marticase.feature.tracking.presentation.model.SelectedPoint
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrackingComposablesTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun controls_whenNotTracking_showStartAndCallStart() {
        var started = false
        setControls(isTracking = false, canReset = true, onStart = { started = true })

        composeRule.onNodeWithText(context.getString(R.string.tracking_start)).performClick()

        assertThat(started).isTrue()
        composeRule.onNodeWithText(context.getString(R.string.tracking_stop)).assertDoesNotExist()
    }

    @Test
    fun controls_whenTracking_showStopAndCallStop() {
        var stopped = false
        setControls(isTracking = true, canReset = true, onStop = { stopped = true })

        composeRule.onNodeWithText(context.getString(R.string.tracking_stop)).performClick()

        assertThat(stopped).isTrue()
    }

    @Test
    fun controls_resetIsDisabledForEmptyRoute() {
        setControls(isTracking = false, canReset = false)

        composeRule.onNodeWithContentDescription(context.getString(R.string.tracking_reset))
            .assertIsNotEnabled()
    }

    @Test
    fun controls_resetCallsReset() {
        var resetClicked = false
        setControls(isTracking = false, canReset = true, onReset = { resetClicked = true })

        composeRule.onNodeWithContentDescription(context.getString(R.string.tracking_reset))
            .assertIsEnabled()
            .performClick()

        assertThat(resetClicked).isTrue()
    }

    @Test
    fun statusChip_showsStateAndMarkerCount() {
        composeRule.setContent {
            MartiCaseTheme { TrackingStatusChip(isTracking = true, markerCount = 3) }
        }

        composeRule.onNodeWithText(
            context.getString(R.string.tracking_status_active),
        ).assertIsDisplayed()
        composeRule.onNodeWithText("3 markers").assertIsDisplayed()
    }

    @Test
    fun pointDetail_showsLoadedAddress() {
        setPointDetail(AddressState.Loaded("Moda, Kadıköy"))

        composeRule.onNodeWithText(context.getString(R.string.point_title, 2)).assertIsDisplayed()
        composeRule.onNodeWithText("Moda, Kadıköy").assertIsDisplayed()
    }

    @Test
    fun pointDetail_showsUnavailableAddress() {
        setPointDetail(AddressState.Unavailable)

        composeRule.onNodeWithText(context.getString(R.string.point_address_unavailable))
            .assertIsDisplayed()
    }

    @Test
    fun pointDetail_whileLoading_showsNoAddressText() {
        setPointDetail(AddressState.Loading)

        composeRule.onNodeWithText(context.getString(R.string.point_title, 2)).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.point_address_unavailable))
            .assertDoesNotExist()
    }

    @Test
    fun resetDialog_confirmAndDismissCallBack() {
        var confirmed = false
        var dismissed = false
        composeRule.setContent {
            MartiCaseTheme {
                ResetRouteDialog(onConfirm = { confirmed = true }, onDismiss = { dismissed = true })
            }
        }

        composeRule.onNodeWithText(context.getString(R.string.reset_dialog_confirm)).performClick()
        composeRule.onNodeWithText(context.getString(R.string.reset_dialog_dismiss)).performClick()

        assertThat(confirmed).isTrue()
        assertThat(dismissed).isTrue()
    }

    private fun setControls(
        isTracking: Boolean,
        canReset: Boolean,
        onStart: () -> Unit = {},
        onStop: () -> Unit = {},
        onReset: () -> Unit = {},
    ) {
        composeRule.setContent {
            MartiCaseTheme {
                TrackingControls(
                    isTracking = isTracking,
                    canReset = canReset,
                    onStartClick = onStart,
                    onStopClick = onStop,
                    onResetClick = onReset,
                )
            }
        }
    }

    private fun setPointDetail(address: AddressState) {
        composeRule.setContent {
            MartiCaseTheme {
                PointDetailContent(
                    selected = SelectedPoint(
                        point = RoutePoint(2L, Coordinate(40.98712, 29.02561), 4f, 0L, null),
                        order = 2,
                        address = address,
                    ),
                )
            }
        }
    }
}
