# 👉 ComposeTemplate

A clean, modular, and testable **Jetpack Compose + Kotlin Inject** template designed for building scalable Android apps using modern architectural patterns like **MVI**, **DI**, **Middleware**, and **Reducer** separation.

---

# 🧭 MVI Architecture Overview – NewsList Feature

## 📐 Architecture Diagram

```
        ┌─────────────┐
        │ Composable  │
        │ (UI Layer)  │◄───── Collects ─────┐
        └────┬────────┘                     │
             │                              │
             ▼                              │
        ┌───────────────┐       Emits       │
        │  ViewModel    │─────────────────► SideEffect (e.g., NavigateToDetail)
        │  (MVI entry)  │                   │
        └────┬──────────┘                   │
             │ dispatches                   ▼
             ▼                         ┌─────────────┐
        ┌───────────────┐              │NavController│
        │   Delegate    │              └─────────────┘
        │ (MVI Engine)  │
        └────┬──────┬───┘
             │      │
             │      │
             ▼      ▼
        ┌────────┐ ┌────────────────────────────┐
        │Reducer │ │        Middleware          │
        │(Sync)  │ │(Async: API, Storage, etc.) │
        └────────┘ └────────────────────────────┘
```

## 🧱 Layer Responsibilities

### 🧩 1. **Composable (UI Layer)**
- Collects state with `collectAsStateWithLifecycle`
- Collects side effects via `flowWithLifecycle`
- Sends user events to `ViewModel.onAction(...)`

### 🧠 2. **ViewModel (Entry Point)**
- Delegates to `MVI` engine (`MVIDelegate`)
- Triggers initial actions like `FetchNews`
- Exposes state (`StateFlow`) and side-effects (`Flow`)
- DI-friendly using `@Inject`, `@Assisted`, etc.

### ⚙️ 3. **MVIDelegate (Engine)**
- Central coordinator
- Calls reducer first
- Then calls middleware(s) after state update
- Emits side-effects (e.g., navigation or toasts)

### 🔁 4. **Reducer**
- Pure, synchronous state transition
- No side-effects beyond `Effect?` return
- Fully testable and predictable

### 🌐 5. **Middleware**
- Handles **asynchronous operations**
    - Network calls
    - Database access
    - Logging
- Never updates state directly — only **dispatches actions** back to the delegate


## ✅ Key Features

| Concern           | Implementation                                            |
|-------------------|-----------------------------------------------------------|
| Async Handling    | `CoroutineScope + Middleware`                             |
| Lifecycle-aware   | `collectAsStateWithLifecycle`, `flowWithLifecycle`        |
| Testability       | Reducer & Middleware testable in isolation                |
| DI Friendly       | Uses `@Inject`, `@Assisted`, `Set<Middleware>`            |
| Navigation        | Triggered via `SideEffect.NavigateToDetail(...)`          |
| Error Handling    | `AppError`, `CoroutineExceptionHandler` inside Middleware |
| Threading         | `AppCoroutineDispatchers` abstracts IO/Main/etc.          |
| Scalability       | Middleware is a `Set` = easily extendable                 |


## ✅ Completed Features

| Category                | Feature                                                  | Status      |
| ----------------------- |----------------------------------------------------------| ----------- |
| 🔌 Dependency Injection | Kotlin Inject with `@Component`, `@Provides`, `@IntoSet` | ✅ Completed |
| 🎨 UI                   | Jetpack Compose Screens (`NewsList`, `NewsDetail`)       | ✅ Completed |
| 🧱 Navigation           | Navigation with `AppRouteFactory` injection per feature  | ✅ Completed |
| 🧠 State Management     | MVI pattern (Reducer + Middleware + Delegate)            | ✅ Completed |
| 🌐 Networking           | Ktor-based `HttpClient` integration                      | ✅ Completed |
| 🧺 UI Testing           | Compose UI + Navigation tests with custom `TestApp`      | ✅ Completed |
| 🧲 DI for Testing       | Custom `TestApplicationComponent`                        | ✅ Completed |
| ⚡ Coroutine Dispatchers | `AppCoroutineDispatchers` abstraction                    | ✅ Completed |
| 🩵 Logging              | Kermit Logging via `AppInitializers`                     | ✅ Completed |
| 🛠 Environment Info     | Dynamic `ApplicationInfo` (debug, flavor, version)       | ✅ Completed |

---

## 🔧 Pending / Upcoming Features

| Feature                      | Priority  | Notes                                             |
| ---------------------------- | --------- | ------------------------------------------------- |
| 🔐 Authentication Module     | 🔜 Medium | Needs route factory, middleware, reducer setup    |
| 💃 Room Database Integration | 🔜 Medium | For local caching, data source abstraction        |
| 🌙 Dark Theme Support        | 🔜 Low    | Basic theming with system override                |
| 📀 Pull-to-Refresh           | 🔜 Low    | Add to `NewsListScreen` for refresh API           |
| 🩵 Paging                    | 🔜 Medium | Lazy load support for large news lists            |
| 🥐 Unit Test Coverage        | 🔜 High   | Reducer, Middleware, UseCases                     |
| 📊 Analytics Module          | 🔜 Low    | Plug-in via middleware pipeline                   |
| 🔄 Multi-Module Structure    | 🔜 High   | Split `:core`, `:features`, `:test-support`, etc. |

---

## 🧲 UI Testing Strategy

* **Test Harness:** Custom `TestApp` bootstrapped with `TestApplicationComponent`
* **Compose Rule:** Uses `createAndroidComposeRule<MainActivity>()`
* **DI Swapping:** Inject Dispatchers and Dependencies to be changed
* **Navigation Verification:** End-to-end test across route boundaries

---

## 📦 Tech Stack

* Jetpack Compose
* Kotlin 1.9+
* Kotlin Inject (by @evant)
* Ktor for networking
* MVI (Middleware + Reducer pattern)
* Coroutine Dispatchers abstraction
* Kermit Logging
* JUnit + Compose UI Test
* Gradle Kotlin DSL

---

## 🧱 Architecture

```
App
│
├── di/
│   ├── AndroidApplicationComponent
│   ├── AndroidActivityComponent
│   └── CommonAppComponent
│
├── features/
│   └── newsList/
│       ├── presentation/
│       │   ├── ui/                <-- Composables
│       │   ├── viewmodel/         <-- Reducer + Middleware
│       └── domain/
│
├── util/
│   └── AppCoroutineDispatchers.kt
│   └── ApplicationInfo.kt
│
└── e2e/
    ├── TestApp.kt
    └── NewsListScreenE2ETest.kt
```

---

## 📢 How to Run

```bash
# Run the app
./gradlew installDebug

# Run UI tests
./gradlew connectedAndroidTest (make allTest)
```

---

## 🤝 Contributions Welcome

If you'd like to contribute or test-drive a feature like Authentication, Paging, or DB caching — feel free to fork and PR. Open to feedback and pairing on key improvements.

---

## 🔗 Links

* 🔗 Heavily inspired by Tivi(https://github.com/chrisbanes/tivi)
* 🔗 [Kotlin Inject Docs](https://github.com/evant/kotlin-inject)
* 🔗 [Jetpack Compose Testing](https://developer.android.com/jetpack/compose/testing)

---
