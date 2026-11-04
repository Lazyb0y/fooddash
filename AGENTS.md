# Agent instructions

Instructions for AI coding agents working in this repository. Read this file before doing anything.

## Approval and change control

These rules are mandatory.

- Don't create, modify, rename, move or delete any file unless the maintainer explicitly authorizes that specific change. Research, proposals and plan approval are not authorization, and permission for one task doesn't cover another. When unsure, ask.
- Work in small tasks: propose the next task, wait for authorization, implement only that task, validate it, report and stop.
- Never stage, commit, push, tag, publish releases or rewrite Git history without explicit permission. Never overwrite or discard existing changes without approval.
- Make no changes on GitHub (issues, labels, project items, settings, releases) unless the maintainer asks for that action. Read-only `gh` commands are fine.
- Discuss significant decisions first: present the alternatives and trade-offs, recommend one and let the maintainer decide.
- Never silently remove, contradict or weaken existing requirements, including when editing this file.
- Never add AI attribution, such as a `Co-Authored-By` trailer, to commits or pull requests.

After each task, report: a summary, the files changed, decisions and their reasons, validation results, steps to check by hand, remaining concerns, and a suggested commit message.

## Project

FoodDash is a food-delivery app "like foodpanda", with invented internals: the reference project for UIU CSE 3422 Software Engineering Laboratory. Every class demonstrates one practice on it, and students read, fork and run it. The maintainer is the course instructor.

Because students learn from it:
- Code, commit messages, pull requests and docs are examples of good practice. Keep them plain and easy to follow.
- **Some problems are deliberate.** `drills/` and the branch `specimen/review-01` contain planted mistakes for the drills; rider assignment isn't safe under concurrent requests (a later class's lost-update case); the API tests use H2 until a later class moves them to Testcontainers. Never fix these, or anything a future class is meant to fix, unless the maintainer asks.
- The tags `class-NN-start` and `class-NN-end` are fixed snapshots used in class. Never move or delete them, and never rewrite the history of `main`.

## Build and run

- Java 21 or newer; the Maven wrapper (`./mvnw`, or `mvnw.cmd` on Windows) downloads Maven.
- `./mvnw verify` checks the formatting (Google Java Style, via Spotless), compiles and runs the tests, as CI does; use it to validate every task. `./mvnw spotless:apply` fixes the formatting.
- `./mvnw spring-boot:run` needs MySQL 8.4 (see the README); `./mvnw spring-boot:test-run "-Dspring-boot.run.profiles=h2"` runs on an in-memory database instead.
- New dependencies need the maintainer's approval. Don't add a library when a few lines of our own will do.

## Architecture and code

- A modular monolith, packaged by feature (`restaurant`, `menu`, `order`, `rider`, `payment`, `customer`, `shared`). Other modules call a module's service, never its repository.
- Every API error is RFC 9457 Problem Details; stack traces stay in the server log.
- Money is `BigDecimal` in Java, `DECIMAL` in MySQL and a string such as `"250.00"` in JSON. Time is stored in UTC. URLs use random public IDs, and placing an order needs an `Idempotency-Key`.
- Database changes only through new Flyway migrations; never edit a migration that has been applied. The SQL must run on MySQL 8.4 and on H2 in MySQL mode.
- SQL only through JPA or parameterised queries; never build SQL from strings.
- Payments go through `PaymentGateway`; local runs and tests use `FakePaymentGateway`.
- Logging through SLF4J; no `System.out`.

## Security and privacy

- No secrets in the repository: configuration comes from environment variables, listed in `.env.example`. The defaults in `application.yml` are for local development only.
- Never log passwords, tokens or personal data such as phone numbers and addresses.
- Treat all input as untrusted and validate it at the API boundary.

## Testing

- Add or update tests with every change. Test names say the behaviour (`deliveryFee_isCapped_whenOrderExceedsThreshold`), and tests follow Given / When / Then.
- Report what was and wasn't tested. Never claim that a test passed if you didn't run it.

## Git and GitHub

- Commit messages follow [Conventional Commits](https://www.conventionalcommits.org/): `<type>(<scope>): <summary>`, imperative and lowercase, 72 characters or fewer, for example `feat(order): add a rain surcharge to the delivery fee`. The scope is the feature package or the area (`db`, `api`, `readme`, `drills`). Commit messages don't carry issue numbers.
- Each commit is one logical change that builds and passes `./mvnw verify`.
- Each task gets its own short branch from an up-to-date `main`, named `<type>/<topic>` (for example `fix/fee-rounding`). The maintainer commits, pushes and opens the pull request unless they ask the agent to.
- Pull-request titles follow the same style, because squash and merge makes the title the commit on `main`; the description says `Closes #<issue>` when there is an issue. `main` is protected and needs the `pr-title`, `secrets` and `build` checks.
- Work flows from an idea to an issue, then through the FoodDash project board (Backlog, Evaluating, Approved, In Progress, Done). The maintainer manages the board.
- See [CONTRIBUTING.md](CONTRIBUTING.md) for the full workflow.
