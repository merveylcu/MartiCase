---
name: adr
description: Record an architecture decision the user just made as docs/adr/NNNN-title.md. Use right after the user picks an option offered by the android-architect agent, or when they say "record this decision" / "ADR yaz".
---

# Write an ADR

1. Find the next number: highest `docs/adr/NNNN-*.md` + 1, zero-padded to 4 digits.
2. File name: `docs/adr/NNNN-kebab-case-title.md`.
3. Fill in `docs/adr/0000-template.md`. Rules:
   - **Context**: which brief requirements (R1-R9 in `CLAUDE.md`) drive this.
   - **Options**: every option that was on the table, one line each, with the main trade-off.
   - **Decision**: what the user chose, in their words if they gave a reason.
   - **Consequences**: what becomes easier, what becomes harder, what we accept as a limitation.
   - Status `Accepted`, today's date.
4. If it replaces an older ADR, set the old one to `Superseded by NNNN` and link both ways.
5. Add one row to the table in `docs/adr/README.md`.
6. Keep it under ~40 lines. Don't commit here; `ship` does that.
