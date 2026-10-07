# 0006. Route persistence: Room for points, DataStore for tracking state

- Status: Accepted
- Date: 2026-10-07
- Brief requirements: R4, R5

## Context
R5: until the route is reset, reopening the app shows the current route. R4: tracking can be started and
stopped, and per ADR 0004 the app resumes the service if tracking was on when the process died.

## Options
1. **Room for points + DataStore for the tracking flag**: each store does what it is good at; both expose Flow.
2. **Room only** (flag in a settings table): one store, but a key-value flag in SQL is awkward.
3. **DataStore only** (points serialized): simple, but rewrites the whole route on every marker.

## Decision
Option 1.
- `core:database`: `route_points` table (id, latitude, longitude, accuracy, timestamp, address nullable).
  The address is cached after the first geocode so it isn't fetched twice.
- `core:datastore`: Preferences DataStore with `isTracking`.
- Reset deletes all points; it does not change the tracking state.

## Consequences
- Easier: the map observes a `Flow<List<RoutePoint>>`; the service and the UI share one source of truth.
- Harder: one extra core module (`core:datastore`), added to ADR 0003's module list.
- Accepted limitations: a single route at a time; no route history.
