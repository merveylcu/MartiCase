# 0008. Coroutine bindings live in core:common

- Status: Accepted
- Date: 2026-10-07
- Brief requirements: R7

## Context
`core:common` defined the dispatcher and `ApplicationScope` qualifiers, but the Hilt module that provided
them sat in `:app`. `core:datastore` and `feature:tracking:data` relied on bindings from a module they
don't depend on, so they couldn't build a Hilt graph without the app.

## Options
1. **Keep `CoroutinesModule` in `:app`**: app stays the only wiring point; qualifier and binding stay apart.
2. **Move it to `core:common/di`**: `core:common` becomes an Android + Hilt module; definition and binding
   live together and the app gets thinner.

## Decision
Option 2, chosen by the user. Domain doesn't depend on `core:common`, so it stays pure Kotlin.

## Consequences
- Easier: any module that depends on `core:common` gets the bindings with it.
- Harder: `core:common` is no longer a plain JVM module.
- Accepted limitations: none.
