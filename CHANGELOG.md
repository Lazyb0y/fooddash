# Changelog

All notable changes to FoodDash are documented here. The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and the project uses [semantic versioning](https://semver.org/).

## [Unreleased]

### Added
- Contribution rules, pull-request template, bug-report form and security policy.
- CI on every pull request (title check, secret scan, build) and weekly Dependabot updates.
- Google Java Style, checked by Spotless in every build.
- MySQL schema and demo data through Flyway migrations.
- One RFC 9457 Problem Details error shape for the whole API; OpenAPI documentation at `/swagger-ui.html`.
- List restaurants a page at a time with a cursor (`GET /api/v1/restaurants`), and a home page.
- A restaurant's menu (`GET /api/v1/restaurants/{id}/menu`), and a menu page.
