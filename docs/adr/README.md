# Architecture Decision Records

Each decision is made together with the user (see the workflow in [`CLAUDE.md`](../../CLAUDE.md)),
proposed by the `android-architect` agent and recorded with the `adr` skill.

| # | Decision | Status |
|---|---|---|
| [0001](0001-ai-assisted-workflow.md) | AI-assisted workflow with agents, skills and ADRs | Accepted |
| [0002](0002-map-provider-google-maps-compose.md) | Google Maps Compose + Android Geocoder | Accepted |
| [0003](0003-module-structure.md) | One feature + core modules | Accepted |
| [0004](0004-background-tracking-strategy.md) | Location foreground service + battery optimization exemption | Accepted |
| [0005](0005-marker-distance-rule.md) | 100 m measured from the last marker, accuracy filtered | Accepted |
| [0006](0006-route-persistence.md) | Room for route points, DataStore for tracking state | Accepted |
| [0007](0007-core-permission-module.md) | Permission checks and request flow in core:permission | Accepted |
| [0008](0008-coroutines-module-in-core-common.md) | Coroutine dispatcher and scope bindings in core:common | Accepted |
| [0009](0009-robolectric-for-android-tests.md) | Robolectric for Android tests on the JVM | Accepted |
