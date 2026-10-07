# 0007. Permission handling in core:permission

- Status: Accepted
- Date: 2026-10-07
- Brief requirements: R2, R4

## Context
The permission chain (precise location, notifications, battery exemption, device location on) lived in
`feature:tracking:presentation`. None of it is specific to the tracking screen.

## Options
1. **Keep it in the feature**: fewer modules, but any new screen that needs location repeats it.
2. **Move it to `core:permission`**: one place for permission checks and request flows, reusable by features.

## Decision
Option 2, requested by the user. `core:permission` holds the permission checks, the
`rememberLocationTrackingPermissionFlow` step chain, `PermissionBlocker`, and the battery optimization
permission in its manifest. The feature only reacts to `onReady` / `onBlock`.

## Consequences
- Easier: features stay free of Android permission APIs; the chain is defined once.
- Harder: one more module (extends ADR 0003).
- Accepted limitations: `feature:tracking:data` keeps its own fine-location check before requesting updates,
  so the data layer doesn't depend on a Compose module.
