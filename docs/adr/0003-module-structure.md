# 0003. Module structure: one feature + core modules

- Status: Accepted
- Date: 2026-10-07
- Brief requirements: R1-R5, R7

## Context
The user asked for "multi-module but basic", following the structure of their earlier projects. The app has one screen.

## Options
1. **One feature + core**: `feature:tracking:{domain,data,presentation}` + `core:{common,designsystem,database,testing}`.
2. **+ separate `core:location` and `platform:tracking-service`**: cleaner split, more modules.
3. **+ `:navigation` with Navigation 3**: ready for more screens, unused for one screen.

## Decision
Option 1.

```
:app                            Application, MainActivity, manifest wiring
:build-logic                    marticase.* convention plugins
:core:common                    dispatcher qualifiers, Result/error types
:core:designsystem              theme, Marti* components
:core:database                  Room: route points
:core:datastore                 DataStore: tracking state (ADR 0006)
:core:testing                   test helpers, MainDispatcherRule
:feature:tracking:domain        RoutePoint model, repository interfaces, use cases (pure Kotlin)
:feature:tracking:data          repositories, Fused Location, Geocoder, foreground service
:feature:tracking:presentation  map screen, ViewModel, permission UI
```

## Consequences
- Easier: few modules, clear layer boundaries, same convention plugins as the reference projects.
- Harder: `feature:tracking:data` holds both the service and the repositories; split it if it grows.
- Accepted limitations: no navigation module; add one when a second screen appears.
