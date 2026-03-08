# Architecture & Technology

This page describes the technical architecture and technology stack used to build the Korfball Ref Watch application.

---

## Platform

- **Platform:** Wear OS
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose for Wear OS
- **Architecture Pattern:** MVVM (Model-View-ViewModel)

---

## Project Structure

```
app/src/main/java/com/wmeetsma/korfballrefwatch/
│
├── MainActivity.kt              ← App entry point, sets up Compose UI
├── models/
│   └── GameState.kt             ← Data model for the game state
├── repository/
│   └── GameStateRepository.kt   ← Stores and provides game state data
├── service/
│   └── DataLayerService.kt      ← Background service for receiving WearOS data messages
├── ui/
│   └── DashboardScreen.kt       ← The main watch UI (Composable)
└── viewmodels/
    └── MainViewModel.kt         ← Business logic and state management
```

---

## Key Components

### `GameState.kt` — The Data Model
A simple Kotlin data class that holds all the information the watch needs to display:

```kotlin
data class GameState(
    val homeScore: Int,
    val awayScore: Int,
    val gameTimeRemainingMillis: Long,    // Game clock in ms
    val isGameTimeRunning: Boolean,
    val shotClockRemainingMillis: Long,   // Shot clock in ms
    val isShotClockRunning: Boolean,
    val currentPeriod: Int,
    val homeTimeoutsRemaining: Int,
    val awayTimeoutsRemaining: Int,
    val timeoutRequestedTeam: String?,    // "Home" or "Away"
    val substitutionPending: Boolean,
    val subOutInfo: String?,              // Player going out
    val subInInfo: String?,               // Player coming in
    val showSubPopup: Boolean
)
```

### `DataLayerService.kt` — Receiving Live Data
A `WearableListenerService` that runs in the background and listens for incoming data from KorfStat Pro via the Wearable Data Layer. When new data arrives on the `/korfball_game_state` path, it extracts all the relevant fields and stores them in the `GameStateRepository`.

### `GameStateRepository.kt` — Single Source of Truth
A singleton repository (accessible from any component) that holds the current `GameState` as a `StateFlow`. Any update from the `DataLayerService` flows through here and immediately triggers a UI refresh.

### `MainViewModel.kt` — Business Logic
Uses `StateFlow` to expose the game state to the UI. Also handles user interactions (toggle game clock, reset shot clock, toggle control mode, dismiss popups) and maps them to state updates.

### `DashboardScreen.kt` — The UI
A Jetpack Compose screen that reactively displays all elements of the GameState. Uses `LaunchedEffect` to trigger haptic feedback patterns in response to state changes (shot clock expiry, substitution, timeout).

---

## State Management

The app uses a **reactive data flow**:

```
DataLayerService (receives data)
        ↓
GameStateRepository (StateFlow)
        ↓
MainViewModel (exposes state)
        ↓
DashboardScreen (collects state, renders UI)
```

Because `StateFlow` is used throughout, no manual refresh or polling is needed — the UI updates automatically whenever a new game state arrives.

---

## UI Technology: Jetpack Compose for Wear OS

The watch UI is built entirely with **Jetpack Compose for Wear OS**, Google's modern declarative UI framework for Wear OS. This provides:
- A modern, concise way to write UI in Kotlin.
- Built-in Wear OS components (like `TimeText` for the clock arc at the top).
- Easy reactive UI that automatically re-renders when state changes.
