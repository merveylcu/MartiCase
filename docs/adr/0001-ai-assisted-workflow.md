# 0001. AI-assisted workflow with agents, skills and ADRs

- Status: Accepted
- Date: 2026-10-07
- Brief requirements: R7, R9

## Context
The brief allows AI help and asks that all helper files are committed. The reviewers care about
*how* AI is used: how context is given, how skills and agents are defined, and how decisions are made.

## Options
1. **Single chat, no structure**: fast, but no record of decisions and context is re-explained every time.
2. **CLAUDE.md only**: shared context, but no role separation and no repeatable steps.
3. **CLAUDE.md + project agents + skills + ADRs**: more files, but each step is repeatable and recorded.

## Decision
Option 3.

- `CLAUDE.md`: brief as R1-R9, architecture rules, workflow. Loaded automatically every session, so
  context is given once instead of per prompt.
- `.claude/agents/`: `android-architect` (options, read-only), `android-implementer` (scoped code),
  `android-verifier` (build + rules, PASS/FAIL), `location-reviewer` (domain expert for R1/R2).
  Read-only agents can't drift into editing; the implementer can't make architecture choices.
- `.claude/skills/`: `adr`, `ship`, `feature-module` for the steps we repeat.
- Every architectural choice is offered as options and picked by the user, then recorded here.

## Consequences
- Easier: reviewers can follow every decision from prompt to commit; new sessions start with full context.
- Harder: a few extra files to keep up to date.
- Accepted limitations: prompts are not logged; the ADRs and commit history are the record.
