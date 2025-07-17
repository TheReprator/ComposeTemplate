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

```kotlin
is NewsListAction.NewsClicked -> {
    previousState.copy(selectedNews = action.newsModel) to NavigateToDetail(action.newsModel)
}
```

### 🌐 5. **Middleware**
- Handles **asynchronous operations**
  - Network calls
  - Database access
  - Logging
- Never updates state directly — only **dispatches actions** back to the delegate

```kotlin
launch(dispatchers.io) {
    val result = newsUseCase()
    mviDispatcher.await()(NewsListAction.UpdateNews(result.data))
}
```

## ✅ Key Features

| Concern | Implementation |
|--------|----------------|
| Async Handling | `CoroutineScope + Middleware` |
| Lifecycle-aware | `collectAsStateWithLifecycle`, `flowWithLifecycle` |
| Testability | Reducer & Middleware testable in isolation |
| DI Friendly | Uses `@Inject`, `@Assisted`, `Set<Middleware>` |
| Navigation | Triggered via `SideEffect.NavigateToDetail(...)` |
| Error Handling | `AppError`, `CoroutineExceptionHandler` inside Middleware |
| Threading | `AppCoroutineDispatchers` abstracts IO/Main/etc. |
| Scalability | Middleware is a `Set` = easily extendable |

## 📌 Example Action Flow

```
[User taps Retry button]
       ▼
[Composable] → onAction(RetryFetchNews)
       ▼
[ViewModel] delegates to MVI
       ▼
[Reducer] sets loading=true
       ▼
[Middleware] triggers API call
       ▼
[Result Success] → dispatch(UpdateNews)
       ▼
[Reducer] updates newsList
```

## 📚 Suggested Naming Convention

| Component | Suggested Prefix |
|----------|------------------|
| State     | `XyzState`       |
| Action    | `XyzAction`      |
| Effect    | `XyzEffect`      |
| Reducer   | `XyzReducer`     |
| Middleware| `XyzMiddleware`  |
| ViewModel | `XyzViewModel`   |