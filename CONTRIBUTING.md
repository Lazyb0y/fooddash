# Contributing

FoodDash is run like a real product, with one honest exception: it has a **single maintainer**.

## Before you start

- Report bugs and suggest features with the [issue templates](https://github.com/Lazyb0y/fooddash/issues/new/choose). Work is planned on the public **FoodDash** project board: an idea becomes an issue, moves through Backlog, Evaluating and Approved, and is then built.
- Report security problems privately, as described in [SECURITY.md](SECURITY.md), never in a public issue.
- Students: course help goes through a help-request issue in **your own team repository**, and drill pull requests go to **your own fork**, not here.

## Making a change

1. **Issue first.** Anything bigger than a small fix starts from an issue on the board.
2. **Branch from `main`**, named `<type>/<topic>`, for example `feat/rain-surcharge` or `fix/fee-rounding`.
3. **Commit small.** Each commit is one logical change that builds. Messages follow [Conventional Commits](https://www.conventionalcommits.org/): `<type>(<scope>): <summary>`, for example `feat(order): add a rain surcharge to the delivery fee`.
   - Types: `feat`, `fix`, `docs`, `test`, `refactor`, `build`, `ci`, `chore`, `perf`, `style`, `revert`.
   - Scope: the feature package (`order`, `menu`, `restaurant`, `rider`, `payment`) or the area (`db`, `api`, `readme`, `drills`).
   - Summary: imperative and lowercase, 72 characters or fewer: "add", "fix", not "added". A body explains *why* when it isn't obvious.
4. **Open a pull request** whose title follows the same style; it becomes the commit on `main`. Fill in the template, with `Closes #<issue>` when there is an issue, and how you tested the change.
5. **Checks must be green**: the pull-request title, the secret scan, and the build (formatting and tests).
6. **Self-review.** Team repositories require an approving review from another member. FoodDash has one maintainer, so instead of a second reviewer the template has a self-review checklist, and `main` requires the checks. That is a limitation, stated here rather than hidden.
7. **Squash and merge.** The pull request becomes one commit on `main`, and the branch is deleted automatically.

`main` is protected: nobody pushes to it directly.

## Code

- Google Java Style, enforced by Spotless: run `./mvnw spotless:apply` before committing.
- Packages by feature; other modules call a module's service, never its repository.
- Money is `BigDecimal` and `DECIMAL`; time is stored in UTC.
- Database changes only through new Flyway migrations; never edit one that has been applied.
- SQL only through JPA or parameterised queries; never build SQL from strings.
- No secrets in the repository: configuration comes from environment variables, listed in `.env.example`.

## Class demos

Live class demos work on throwaway `live/class-NN-<section>` branches and are cleaned up after each class. The tags `class-NN-start` and `class-NN-end` are fixed snapshots used in class; they never move.
