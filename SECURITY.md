# Security policy

## Reporting a vulnerability

Please **don't open a public issue** for a security problem. Report it privately through **Security → Report a vulnerability** on this repository. The maintainer replies within a week.

## What FoodDash does

- Secrets live in environment variables and GitHub secrets, never in the repository; `.env.example` lists the names without values.
- Dependabot alerts and weekly version updates; secret scanning with push protection.
- The seed data contains no real personal data.
- FoodDash is a teaching app: some later class snapshots contain **deliberately insecure code** (Class 10's legacy module), clearly marked. Don't deploy it as a real service.
