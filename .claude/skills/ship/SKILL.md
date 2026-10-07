---
name: ship
description: Commit and push one verified step. Use after android-verifier returns PASS, or when the user says "commit", "push", "ship it".
---

# Ship a step

1. Only proceed if the last `android-verifier` run was PASS (or the change is docs/config only).
2. `git status` + `git diff --stat`. If unrelated changes are mixed in, split them into separate commits.
3. Check nothing from `CLAUDE.local.md` (reference project names/paths) is in the diff or message.
4. Commit message: conventional commit (`feat(tracking): ...`, `build: ...`, `docs(adr): ...`).
   Subject <= 72 chars, imperative, lowercase. Body: why, not what. End with the attribution trailer
   required by the session.
5. Push: `git push origin main`. If the remote isn't set up yet, say so and stop; don't create repos.
