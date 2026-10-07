# 0005. 100 m marker rule: distance from the last marker

- Status: Accepted
- Date: 2026-10-07
- Brief requirements: R1

## Context
R1: a marker is added for every 100 m of movement. GPS fixes are noisy, especially indoors and at start.

## Options
1. **Distance from the last marker**: add a marker when the new fix is >= 100 m from the last saved one.
   Drift does not add up; markers are evenly spaced.
2. **Sum of distances between consecutive fixes**: follows curved paths, but GPS jitter accumulates and
   creates markers while standing still.
3. **Only `minUpdateDistanceMeters = 100`** in the location request: simplest, but the platform treats it
   as a hint and measures from the last *delivered* fix.

## Decision
Option 1, as a pure Kotlin use case in `feature:tracking:domain`:
- Fixes with horizontal accuracy worse than 50 m are ignored.
- The first accepted fix after an empty route becomes the first marker.
- The location request asks for frequent, high accuracy updates (`minUpdateDistanceMeters` ~ 20 m) so
  crossing 100 m is noticed promptly.

## Consequences
- Easier: deterministic, unit-testable rule with no Android dependency.
- Harder: we keep receiving fixes between markers (small battery cost).
- Accepted limitations: on a winding path markers are 100 m apart in a straight line, not along the path.
- Update (performance review): while the app is in the background, fixes are delivered in batches of up to
  60 s to cut wake-ups; every fix is still evaluated, so marker positions don't change. The stale-fix limit
  moved from 30 s to 2 min so batched fixes aren't dropped.
