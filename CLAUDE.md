# MartiCase: Claude Context

Android case study for Martı. Brief: [`doc/Android-Marti-Case.pdf`](doc/Android-Marti-Case.pdf).

## The brief in one screen

| # | Requirement |
|---|---|
| R1 | Track the user's location; add a marker on the map for every 100 m of movement |
| R2 | Tracking keeps running in foreground and background for as long as possible |
| R3 | Tapping a marker shows the address of that location |
| R4 | User can start and stop tracking |
| R5 | User can reset the route; until reset, reopening the app shows the current route |
| R6 | Any map provider is allowed |
| R7 | Careful git usage, delivered as a GitHub repo |
| R8 | Design is up to the candidate |
| R9 | All AI helper files must be committed to the repo |

Every feature, ADR and test should be traceable to one of these IDs.

## How we work

The architecture is decided **together with the user**, one decision at a time. Claude never picks a
significant architectural option alone.

1. **Decide**: the `android-architect` agent lays out 2–3 options with trade-offs. The user chooses.
2. **Record**: the `adr` skill writes the decision to `docs/adr/NNNN-*.md`.
3. **Build**: the `android-implementer` agent (or the main session) implements one small, scoped step.
4. **Verify**: the `android-verifier` agent builds, tests and checks the rules below. FAIL blocks the commit.
5. **Ship**: the `ship` skill writes a conventional commit and pushes.

Keep each commit small and focused on one thing. Never batch unrelated changes.

## Reference projects

Architecture follows the user's own earlier projects (paths in the gitignored `CLAUDE.local.md`;
never name them in tracked files or commits):

- `build-logic` convention plugins (`marticase.android.library`, `marticase.android.hilt`, ...)
- version catalog in `gradle/libs.versions.toml`
- `feature:<name>:{domain,data,presentation}` + `core:*` modules
- Hilt, Coroutines/Flow, Room, Compose Material 3, MVVM + use cases
- Detekt, Spotless (ktlint), Android Lint, GitHub Actions CI

Take patterns from them; do **not** copy features that the brief doesn't need. Keep it basic.

## Architecture rules

### Layers
- `domain` is pure Kotlin: models, repository interfaces, use cases. No Android imports.
- `data` implements the repository interfaces. Room, location and geocoder APIs live here only.
- `presentation` depends on `domain` only. No repository or DAO access from composables.

### MVVM / Compose
- One immutable `UiState` data class per screen, exposed as `StateFlow`.
- One-off events as `Channel`/`SharedFlow` effects. No event-wrapper hacks.
- Collect flows with `collectAsStateWithLifecycle` only.
- No business logic or state mutation inside composables. Side effects via `LaunchedEffect`/`DisposableEffect`.
- Use `kotlinx.collections.immutable` for lists in UI state. Stable keys in lazy lists.
- Don't pass ViewModels down the tree; screens take state + lambdas.

### Concurrency
- Structured concurrency only. No `GlobalScope`.
- Dispatchers are injected (qualifier in `core:common`), never hardcoded.
- No blocking calls on the main thread (Geocoder is blocking on older APIs; wrap it).

### DI
- Hilt only. `@HiltViewModel` for every ViewModel. No manual singletons or service locators.

### Android platform
- No deprecated APIs. Target the latest stable SDK.
- Runtime permissions follow current Android behavior (fine/coarse, background, notifications on 13+).
- Foreground services declare their type (`location`) in the manifest.

### Testing
- At least one ViewModel test and one repository/use-case test per feature.
- JUnit 4, MockK, Turbine, Truth, `kotlinx-coroutines-test`.

## Commands

```bash
./gradlew assembleDebug
./gradlew testDebugUnitTest
./gradlew spotlessCheck detekt lintDebug
```

## Commits

Conventional commits: `feat(scope):`, `fix:`, `refactor:`, `build:`, `test:`, `docs:`, `chore:`, `ci:`.
Push to `origin main` after each verified step. GitHub account: `merveylcu`.
