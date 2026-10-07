---
name: feature-module
description: Scaffold a new module (feature:<name>:domain|data|presentation or core:<name>) wired into settings.gradle.kts with the right marticase convention plugins. Use when an ADR adds a module.
---

# Scaffold a module

Package root: `com.merveylcu.marticase`.

| Module kind | Plugins | May depend on |
|---|---|---|
| `feature:x:domain` | `marticase.kotlin.library` | `core:common` |
| `feature:x:data` | `marticase.android.library`, `marticase.android.hilt` | its domain, `core:*` |
| `feature:x:presentation` | `marticase.android.library.compose`, `marticase.android.hilt` | its domain, `core:designsystem`, `core:common` |
| `core:x` | `marticase.android.library` (+ `.compose` / `.hilt` as needed) | other `core:*` only |

Steps:
1. Create `<path>/build.gradle.kts` with the plugins above and `namespace = "com.merveylcu.marticase.<dotted.path>"`.
2. Create `src/main/kotlin/com/merveylcu/marticase/<dotted/path>/` and `src/test/kotlin/...`.
3. Add `include(":<path>")` to `settings.gradle.kts`, grouped with its siblings.
4. Use type-safe accessors in dependents: `implementation(projects.feature.x.domain)`.
5. `./gradlew :<path>:assemble` (or `:build` for JVM) must pass.
6. Never let `presentation` depend on `data`. Only `:app` wires data into the graph.
