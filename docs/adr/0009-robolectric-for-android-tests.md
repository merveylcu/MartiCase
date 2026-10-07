# 0009. Robolectric for Android tests on the JVM

- Status: Accepted
- Date: 2026-10-08
- Brief requirements: R1, R2, R4, R5

## Context
A test-gap review showed the riskiest code had no tests: `TrackingService` branches (stop action,
permission loss, sticky restart, location errors), the stale/unknown-accuracy fix filter, the service
controller and the UI. The DAO test existed but only ran on a device, so CI never ran it.

## Options
1. **Robolectric**: Android framework on the JVM; service, Room, DataStore and Compose UI tests run in
   `testDebugUnitTest` and in CI. One extra test dependency.
2. **Instrumented tests only**: real device behavior, but CI needs an emulator and runs get slow.
3. **No new tooling**: only pure-Kotlin tests; the service and UI stay untested.

## Decision
Option 1, chosen by the user. A `marticase.android.robolectric` convention plugin adds Robolectric,
AndroidX Test and Compose UI test dependencies and includes Android resources in unit tests. Tests run on
SDK 34 (Android 14, which already has FGS types and background start limits); Robolectric needs Java 21
for SDK 35+, while the project toolchain and CI use Java 17. The service is tested with Hilt testing, replacing repositories,
the location client and dispatchers with fakes.

## Consequences
- Easier: every test runs in CI with `./gradlew testDebugUnitTest`; no emulator needed.
- Harder: Robolectric tests are slower than plain JVM tests.
- Accepted limitations: Google Maps can't render under Robolectric, so the map itself isn't UI-tested;
  the screen's other composables are. The FGS start-failure path (Android 12+/14+ background limits) can't
  be reproduced and stays covered by manual device testing.
