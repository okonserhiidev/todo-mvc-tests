# ROADMAP — Test coverage strategy for TodoMVC (React)

> Companion to `README.md`. Documents **what** we tested, **why exactly that**,
> and the decisions worth remembering. The verbose discovery log lives in git
> history; this file stays focused.

---

## 1. Application under test

Target: `https://todomvc.com/examples/react/dist/`

Manually walked through every interactive element in Chrome DevTools before
writing any code. Key facts that shaped the suite:

| # | Finding | Test impact |
| --- | --- | --- |
| 1 | No persistence — `localStorage` / `sessionStorage` / IndexedDB stay empty; reload wipes state. | Persistence test dropped. |
| 2 | `main` and `footer` always exist in DOM, hidden via `display:none` when empty. | Use `shouldBe(hidden)`, never `shouldNotExist()`. |
| 3 | `data-testid="text-input"` shared by the new-todo input AND the edit input. | Scope by parent: `[data-testid='header'] ...` for new, `.edit` for edit. |
| 4 | `toggle-all` is a real checkbox visually hidden under the chevron label. | Click via `ClickOptions.usingJavaScript()` on `.toggle-all-label`. |
| 5 | `clear-completed` uses HTML `hidden` attribute, not `display:none`. | `shouldBe(hidden)` works; the element stays in DOM. |
| 6 | Counter text uses pluralisation **and** a `!`: `"1 item left!"` / `"2 items left!"`. | Assert exact text including the punctuation. |
| 7 | React escapes HTML by default (`<script>` survives as plain text). | XSS regression-guard test included. |

---

## 2. Functional areas mapped to tests

9 functional areas, 27 test methods, 9 classes. Each area gets at least one
test, edge cases sit in the most regression-prone places.

| Area | Behaviour covered | Tests |
| --- | --- | --- |
| Create todo | Happy path, input clear, multi-add order, trim, whitespace-only rejection, unicode + emoji, 300-char title | 7 in `CreateTodoTest` |
| Toggle item | On / off, completed class, counter update | 2 in `ToggleTodoTest` |
| Toggle all | Mark all completed / clear all | 1 in `ToggleAllTest` |
| Edit mode | Enter edit, save with Enter, save on blur, edit-to-empty deletes, edit-trigger guards on toggle/destroy double-click | 6 in `EditTodoTest` |
| Delete | Destroy button, main/footer hide when empty | 1 in `DeleteTodoTest` |
| Counter | Pluralisation (`1 item` / `2 items` / `0 items`) | 1 in `CounterTest` |
| Filter routing | Active filter, Completed filter, reactive removal when toggling under filter | 3 in `FilterTodoTest` |
| Clear completed | Removes completed only, hides button when none | 1 in `ClearCompletedTest` |
| XSS escape | 5 payloads (`<script>`, `<img onerror>`, `javascript:`, `<svg onload>`, broken-out attribute) — JUnit 4 `@Parameterized` | 5 in `XssEscapingTest` |

---

## 3. Tests deliberately not written

| Skipped | Reason |
| --- | --- |
| Reload persistence | This React build does not persist state; nothing to assert. |
| Cross-tab sync | No shared state. |
| Visual regression | Out of scope — separate tool category (Percy / Applitools). |
| Mobile / responsive | TodoMVC has no responsive breakpoints. |
| Accessibility audit | Worth adding later via `axe-core`; not part of this task. |
| Performance / CWV | Different class of tooling (Lighthouse / k6). |
| Cross-browser, remote / Selenoid | Not in the brief; easy to bolt on through Maven profiles. |

---

## 4. Selector strategy

1. **🟢 Primary:** `[data-testid="..."]` — added by developers for testing,
   survives refactors.
2. **🟡 Secondary:** `[aria-label="..."]` — almost as stable; accessibility
   contract.
3. **🔵 State classes** (`.completed`, `.editing`, `.selected`) — used only to
   read visual state, never to locate primary elements.
4. **🔴 Avoided:** XPath by position, text matching that breaks under i18n,
   generated React ids.

The full selector map is inside each component class as private fields.

---

## 5. Notable design decisions

| Decision | Why |
| --- | --- |
| `selenide.fastSetValue=true` (in `pom.xml`) | React controlled inputs ignore plain `WebElement.clear()` + sendKeys (the official `selenide-examples/todomvc` only exercises an empty input, so they leave `fastSetValue=false`). With our edit-mode tests, the default sequence triggers an intermediate empty state that deletes the item under edit. JS native-setter avoids it — same recipe react-testing-library uses. |
| `forkCount=1, threadCount=3, perCoreThreadCount=false` (CI bumps to 4) | Three workers, each keeping its own Selenide `ThreadLocal` driver so Chrome launches once per worker, not once per class. The macOS Sequoia renderer-disconnect race is neutralised by `--disable-features=MacAppCodeSignClone` ([Chromium #379125944](https://issues.chromium.org/issues/379125944)); verified 10/10 clean runs locally. Linux CI runs `-Dthreads=4`. |
| Driver lives for the JVM (no `@After closeWebDriver`) | Same pattern as `selenide-examples/todomvc`. Tests are isolated by `clearBrowserCookies + clearBrowserLocalStorage` in `@Before`, not by tearing down the browser. Slashes Chrome startup count and removes the dominant flake surface. |
| Allure `@Step` on every POM action + `AllureSelenide` listener | Steps in the Allure report describe user intent ("Add todo: Buy milk") instead of raw element clicks. Failure auto-attaches a screenshot via the listener. |
| Native `-Dselenide.*` system properties | Selenide loads them straight into `Configuration` — no glue class needed. Defaults live in `pom.xml`. |

---

## 6. Risk register

| Risk | Probability | Mitigation |
| --- | --- | --- |
| Duplicate `data-testid="text-input"` lookups bleed into the wrong input | High | Scoped selectors (`[data-testid='header'] ...` vs `.edit`) verified by the edit-mode suite. |
| Toggle-all click intercepted by overlaying checkbox | Medium | `ClickOptions.usingJavaScript()` on the label. |
| AspectJ weaver vs Java 17 modules | Low | Pinned to 1.9.24, validated locally and in CI. |
| Chrome renderer disconnect on macOS Sequoia | Eliminated | `chromeoptions.args=--disable-features=MacAppCodeSignClone` ([Chromium #379125944](https://issues.chromium.org/issues/379125944)) + driver kept alive across tests. Verified 10/10 clean parallel runs at `threads=3`. Docker-isolated browser per test (Testcontainers) is the bigger-hammer fix, captured in *Future improvements*. |
| CSS transitions or animations causing flaky asserts | Low | Selenide auto-wait covers it; `Configuration.timeout = 4000` ms. |

---

## 7. Final state

- 27 tests · all green
- 3 parallel workers by default (`forkCount=1, threadCount=3, perCoreThreadCount=false`) — verified 10/10 clean local runs on macOS Sequoia + Apple Silicon
- ~5 s wall-clock locally; CI on Linux runs `-Dthreads=4`
- Allure report generated on every run; CI publishes it to GitHub Pages on `main`
- `mvn -B clean test` → BUILD SUCCESS

**How the macOS flake was killed:** Chromium #379125944 (`code_sign_clone`
Gatekeeper race on Sequoia 15+) surfaces as
`SessionNotCreatedException: unable to connect to renderer`. Default
`chromeoptions.args=--disable-features=MacAppCodeSignClone` neutralises it,
and the no-reopen-driver lifecycle in `BaseTest` keeps the number of Chrome
launches minimal. Docker-isolated browser per test (Testcontainers) remains the
bigger-hammer option in *Future improvements*.

See `README.md` for how to run, dependencies and troubleshooting.
