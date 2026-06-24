# TodoMVC UI Automation

UI tests for the React build of [TodoMVC](https://todomvc.com/examples/react/dist/) — Selenide + JUnit 4 + Maven with Allure reporting.

27 tests · all green · 3 parallel workers · ~5 s wall-clock · headless Chrome out of the box.

---

## How to run tests

Prerequisites: **JDK 17+** and **Google Chrome**. Maven not required — the wrapper downloads a pinned 3.9.13 on first run.

```bash
git clone <repo-url>
cd todo-mvc-tests
./mvnw test
```

| Command | What it does |
| --- | --- |
| `./mvnw test` | Run everything in headless Chrome (default) |
| `./mvnw test -Dselenide.headless=false` | Run with a visible browser |
| `./mvnw test -Dtest=CreateTodoTest` | Run a single test class |
| `./mvnw test -Dtest=CreateTodoTest#addTodo_singleItem_appearsInList` | Run a single test method |
| `./mvnw test -Dthreads=4` | Override the worker count (default 3 locally; CI uses 4) |
| `./mvnw test -Dselenide.timeout=8000` | Override Selenide element timeout in ms (default 4000) |
| `./mvnw allure:serve` | Build and open the Allure report in a browser |
| `./mvnw allure:report` | Generate static HTML into `target/site/allure-maven-plugin/` |
| `./mvnw clean test` | Wipe `target/` and run from scratch |

Replace `./mvnw` with `mvn` if you prefer the system install.

---

## Framework overview

```
src/test/
├── java/com/todomvc/
│   ├── core/
│   │   └── BaseTest.java            @Before lifecycle, Allure listener, SLF4J TestWatcher
│   ├── data/                        Test data layer
│   │   ├── Filter.java              ALL/ACTIVE/COMPLETED enum with hash + label
│   │   └── TodoTexts.java           Centralised payload constants (XSS, unicode, …)
│   ├── pages/                       Page Object Model
│   │   ├── TodoPage.java            Aggregator: header() + list() + footer()
│   │   └── components/
│   │       ├── HeaderComponent.java
│   │       ├── TodoListComponent.java
│   │       ├── TodoItemComponent.java
│   │       └── FooterComponent.java
│   └── tests/                       27 tests in 9 classes
│       ├── CreateTodoTest.java
│       ├── ToggleTodoTest.java
│       ├── DeleteTodoTest.java
│       ├── EditTodoTest.java
│       ├── ToggleAllTest.java
│       ├── FilterTodoTest.java
│       ├── ClearCompletedTest.java
│       ├── CounterTest.java
│       └── XssEscapingTest.java     @Parameterized — 5 XSS payloads
└── resources/
    ├── selenide.properties          Selenide defaults (browser, baseUrl, timeout, …)
    ├── allure.properties            Allure results directory
    └── logback-test.xml             SLF4J / Logback config
```

---

## Dependencies

All versions are pinned in `pom.xml`.

| Component | Version | Role |
| --- | --- | --- |
| Java | 17 (Temurin) | Build target |
| Maven | 3.9.13 (wrapped) | Build tool — pinned via `./mvnw` |
| Selenide | 7.16.2 | Browser API, auto-waits, screenshots, Selenium Manager |
| JUnit | 4.13.2 | Test runner |
| AssertJ Core | 3.27.7 | Fluent assertions |
| SLF4J API | 2.0.17 | Logging facade |
| Logback Classic | 1.5.34 | Logging implementation |
| Allure (BOM) | 2.34.0 | Report generation (`allure-junit4` + `allure-selenide`) |
| AspectJ Weaver | 1.9.24 | Runtime weaving for Allure `@Step` |
| Maven Compiler Plugin | 3.13.0 | Java 17 release target |
| Maven Surefire Plugin | 3.5.6 | Parallel test runner with AspectJ agent |
| Allure Maven Plugin | 2.13.0 | `./mvnw allure:serve` / `./mvnw allure:report` |

---

## Browser configuration

The default browser is **Google Chrome** in **headless** mode at **1366×900**.

Selenide defaults live in `src/test/resources/selenide.properties` (auto-loaded from the classpath).

| Property | Default | Notes |
| --- | --- | --- |
| `selenide.browser` | `chrome` | Chrome is the only browser explicitly covered |
| `selenide.headless` | `true` | Set to `false` to watch the browser locally |
| `selenide.baseUrl` | `https://todomvc.com/examples/react/dist/` | Full URL — `BaseTest` opens `""` and navigates here |
| `selenide.timeout` | `4000` | Element timeout in ms |
| `selenide.browserSize` | `1366x900` | Window size in `WxH` |
| `selenide.reportsFolder` | `target/selenide-reports` | Where Selenide drops failure screenshots |
| `selenide.screenshots` | `true` | Take a screenshot on failure |
| `selenide.savePageSource` | `false` | Skip dumping HTML — leaner artifacts |
| `selenide.fastSetValue` | `true` | JS-native value setter — required by the inline-edit tests on React controlled inputs |
| `threads` (in `pom.xml`) | `3` | Surefire `threadCount`. CI on Linux runs at `4`. |
| `chromeoptions.args` (in `pom.xml`) | `--disable-features=MacAppCodeSignClone` | Forwarded to `ChromeOptions`. |

Examples:

```bash
# Watch the browser locally:
./mvnw test -Dselenide.headless=false

# Saturate a beefy CI runner:
./mvnw test -Dthreads=4
```
