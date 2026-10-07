package com.merveylcu.marticase.feature.tracking.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface AddressState {
    data object Loading : AddressState

    data class Loaded(val text: String) : AddressState

    data object Unavailable : AddressState
}
