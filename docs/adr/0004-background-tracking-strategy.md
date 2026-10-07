# 0004. Background tracking: location foreground service

- Status: Accepted
- Date: 2026-10-07
- Brief requirements: R2, R4, R5

## Context
R2 asks tracking to continue in foreground and background "as long as possible". Android limits background
location heavily (Doze, background start limits, FGS types on 14+, OEM battery killers).

## Options
1. **FGS + battery optimization exemption**: `foregroundServiceType="location"`, ongoing notification with
   Stop, `START_STICKY`, ask to ignore battery optimizations. Started while the app is visible, so
   `ACCESS_BACKGROUND_LOCATION` isn't required.
2. **Option 1 + background permission + boot restart**: tracking resumes after reboot; extra permission step,
   stricter Play policy review.
3. **WorkManager periodic**: 15 min minimum interval, breaks the 100 m rule.

## Decision
Option 1. The "tracking enabled" flag is persisted (DataStore); if the process is killed and not restarted
by the system, opening the app resumes the service.

## Consequences
- Easier: simple permission flow (fine location + notifications), reliable while the notification is shown.
- Harder: service lifecycle and notification channel to manage; must stop itself on Stop (R4).
- Accepted limitations: tracking does not restart after a device reboot until the app is opened again.
- Review notes (`location-reviewer`):
  - The system's `START_STICKY` restart is best effort. On Android 12+/14+ a location FGS may not start
    from the background; the service then stops quietly, keeps the flag on, and resumes on next app open.
  - The service is started only from visible UI (user tap or screen `STARTED`), never from `init` or
    `Application.onCreate`. A failed start returns `false` and leaves the flag off.
  - Precise (fine) location is required; coarse fixes can't pass the 50 m accuracy filter.
  - Before starting, the UI asks for location, then notifications (13+), then the battery exemption, and
    checks that device location is on.
