# FoodDash

[![CI](https://github.com/Lazyb0y/fooddash/actions/workflows/ci.yml/badge.svg)](https://github.com/Lazyb0y/fooddash/actions/workflows/ci.yml)

A food-delivery app "like foodpanda", with **invented internals**: the reference project for **UIU CSE 3422 Software Engineering Laboratory**. Every class demonstrates one practice on FoodDash, and each class starts from a tagged snapshot.

| | |
|---|---|
| **Stack** | Java 21, Spring Boot 4, MySQL 8.4, Flyway, Thymeleaf, springdoc-openapi, JUnit 5 |
| **Maintainer** | Md. Foysal Ahamed (@Lazyb0y) |
| **Students** | Read and fork it; your own project lives in your team repository |

## Run it locally

You need **Java 21 or newer**, **MySQL 8.4** (easiest with Docker) and a terminal. Maven isn't needed: the wrapper (`mvnw`) downloads it.

| | Windows | macOS | Linux |
|---|---|---|---|
| **Java 21+** | Eclipse Temurin (adoptium.net) | Temurin, or `brew install --cask temurin` | `sudo apt install openjdk-21-jdk` (or `dnf`), or Temurin |
| **Docker** | Docker Desktop | Docker Desktop (Apple silicon or Intel) | Docker Engine |
| **The wrapper** | `./mvnw` in Git Bash; `.\mvnw.cmd` in PowerShell; `mvnw.cmd` in Command Prompt | `./mvnw` | `./mvnw` |

The commands below use `./mvnw`; on Windows outside Git Bash, use the wrapper from the table.

1. Start MySQL. With Docker (one line, so it works in every shell):

   ```bash
   docker run --name fooddash-mysql -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=change-me -e MYSQL_DATABASE=fooddash -e MYSQL_USER=fooddash -e MYSQL_PASSWORD=fooddash mysql:8.4
   ```

   Next time, `docker start fooddash-mysql` is enough. Without Docker, create a database `fooddash` and a user `fooddash` (password `fooddash`) in your own MySQL 8.4.
2. Nothing to configure for a local run: the defaults in `application.yml` match the MySQL command above and are for local development only. To change one, set the environment variable named in `.env.example` before starting the app: `export DATABASE_PASSWORD=…` (macOS, Linux, Git Bash), `$env:DATABASE_PASSWORD="…"` (PowerShell) or `set DATABASE_PASSWORD=…` (Command Prompt). FoodDash doesn't read a `.env` file.
3. Run the app (Flyway creates the tables and the demo data on the first start):

   ```bash
   ./mvnw spring-boot:run
   ```

4. Open <http://localhost:8080> for the pages and <http://localhost:8080/swagger-ui.html> for the API.

To see every SQL statement the app sends, start it with the `sql` profile: `./mvnw spring-boot:run "-Dspring-boot.run.profiles=sql"`.

**No MySQL at hand?** Run on an in-memory database instead: `./mvnw spring-boot:test-run "-Dspring-boot.run.profiles=h2"` (keep the quotes: PowerShell needs them). Data disappears when the app stops.

## Run the tests

```bash
./mvnw verify
```

This compiles the code, checks the formatting (Google Java Style, via Spotless; fix it with `./mvnw spotless:apply`) and runs the tests. The API tests use an in-memory database; Class 7 moves them to a real MySQL in Testcontainers.

## What's inside

```text
com.fooddash
├── customer/     Customer, CurrentCustomer (a demo stub until sign-in arrives in Class 10)
├── restaurant/   RestaurantController, RestaurantService, RestaurantRepository, Restaurant
├── menu/         MenuController, MenuService, MenuItem
├── order/        OrderController, RestaurantOrderController, OrderService, OrderRepository, Order
├── rider/        RiderController, AssignmentService, Rider
├── payment/      PaymentGateway (interface), MobileWalletGateway, FakePaymentGateway
└── shared/       Problem Details error handling, configuration
```

- **A modular monolith**, packaged by feature: other modules call a module's service, never its repository.
- **Errors** use one shape for the whole API: RFC 9457 Problem Details.
- **Money** is `BigDecimal` in Java, `DECIMAL` in MySQL and a string such as `"250.00"` in JSON.
- **Time** is stored in UTC.
- **URLs** use random public IDs (UUIDs); placing an order needs an `Idempotency-Key` header.
- **Payments** go through `PaymentGateway`; local runs and tests use `FakePaymentGateway`, so no key is needed.

## Demo data

The seed data (`src/main/resources/db/migration/V2__seed_demo_data.sql`) has fixed IDs, so demos and the Swagger examples always work.

| What | ID |
|---|---|
| Dhanmondi Kacchi House (open) | `d0000000-0000-4000-8000-000000000001` |
| Old Town Biryani (closed) | `d0000000-0000-4000-8000-000000000002` |
| Kacchi Biryani at Dhanmondi Kacchi House | `e0000000-0000-4000-8000-000000000001` |
| An order you can cancel (placed) | `11111111-1111-4111-8111-111111111111` |
| An order already picked up | `22222222-2222-4222-8222-222222222222` |
| A paid order with no rider | `33333333-3333-4333-8333-333333333333` |
| Riders | `aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa`, `bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbbb` |

## Lab database (Class 5)

`db/lab/` holds a bigger dataset for practising SQL, query plans and transactions: 60 restaurants, 20,002 customers, 302 riders and 300,004 orders from January to October, the same on every laptop. Loading it takes about a minute.

- **For SQL practice** (no app needed), load `db/lab/fooddash-lab.sql`. It **deletes the FoodDash tables** in the database and builds them again with the demo data and the lab data. With the Docker container from step 1, run these two lines in the repository folder (password `fooddash`):

  ```bash
  docker cp db/lab/fooddash-lab.sql fooddash-mysql:/tmp/lab.sql
  docker exec -it fooddash-mysql mysql -u fooddash -p fooddash -e "source /tmp/lab.sql"
  ```

  Without Docker: `mysql -u fooddash -p fooddash -e "source db/lab/fooddash-lab.sql"`.
- **For the app**, start it once on an empty database (Flyway creates the tables), then load `db/lab/bulk-data.sql` the same way: it only adds the lab data. The app won't start on a database loaded from `fooddash-lab.sql`, because Flyway didn't create it; start again from an empty database (`docker rm -f fooddash-mysql`, then step 1).

`fooddash-lab.sql` is generated from the two migrations and `bulk-data.sql`; don't edit it by hand.

## Class snapshots

| Tag | What it holds |
|---|---|
| `class-03-start` | Restaurants, menus, orders with idempotency keys, rider assignment, Problem Details, OpenAPI |
| `class-03-end` | Adds cancelling an order (`POST /api/v1/orders/{id}/cancel`) |
| `class-05-start` | Adds restaurant order history (`GET /api/v1/restaurants/{id}/orders`), the `sql` profile and the lab database |
| `class-05-end` | Order history with an index, in two queries instead of 22; riders' acceptance locks the order row |

`git switch --detach class-03-start` gives you the state at the start of that class (`git switch main` takes you back). Live demos happen on throwaway `live/class-NN-*` branches; `main` only moves forward.

## Drills

`drills/` holds files for the in-class and homework drills (Class 3: the second type-along, then the merge-conflict drill). Fork the repository for the drills and open pull requests **in your fork**, never against this repository.

## Contributing and security

See [CONTRIBUTING.md](CONTRIBUTING.md) and [SECURITY.md](SECURITY.md). Course help goes through a help-request issue in **your team repository**, not here.

## AI use

No AI-generated code is merged without the maintainer reading and being able to explain every line. AI coding agents working here follow [AGENTS.md](AGENTS.md).

## Licence

[MIT](LICENSE).
