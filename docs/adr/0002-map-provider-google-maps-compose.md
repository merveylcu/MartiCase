# 0002. Map provider: Google Maps Compose + Android Geocoder

- Status: Accepted
- Date: 2026-10-07
- Brief requirements: R1, R3, R6

## Context
R6 lets us pick any map provider. Markers (R1) and tapping a marker to see its address (R3) are the core UI.

## Options
1. **Google Maps Compose (`maps-compose`)**: Compose-native `GoogleMap { Marker() }`, same Play Services as
   Fused Location; needs an API key.
2. **OpenStreetMap (osmdroid)**: no key, but `AndroidView` wrapper and imperative marker handling.
3. **MapLibre**: open source vector maps, keyless demo style; Compose support less mature.

## Decision
Option 1. Addresses come from the platform `Geocoder` (no key; async API on 33+, wrapped on a background
dispatcher below that).

## Consequences
- Easier: declarative markers and info windows in Compose; one Play Services dependency family.
- Harder: reviewers must add `MAPS_API_KEY` to `local.properties` (Secrets Gradle Plugin); without it the
  map renders blank but the app still runs and tracks.
- Accepted limitations: `Geocoder` needs network and may return no result; UI shows coordinates as a fallback.
