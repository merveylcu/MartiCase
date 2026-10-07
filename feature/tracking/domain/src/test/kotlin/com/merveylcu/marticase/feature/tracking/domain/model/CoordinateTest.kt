package com.merveylcu.marticase.feature.tracking.domain.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CoordinateTest {
    @Test
    fun distanceTo_samePoint_isZero() {
        val point = Coordinate(41.0, 29.0)

        assertThat(point.distanceTo(point)).isEqualTo(0.0)
    }

    @Test
    fun distanceTo_oneThousandthDegreeLatitude_isAbout111Meters() {
        val from = Coordinate(41.000, 29.0)
        val to = Coordinate(41.001, 29.0)

        assertThat(from.distanceTo(to)).isWithin(0.5).of(111.2)
    }
}
