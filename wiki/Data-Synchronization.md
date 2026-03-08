# Data Synchronization

This page explains how match data flows from **KorfStat Pro** on a computer to the **Korfball Ref Watch** on a Wear OS device.

---

## Overview

```
KorfStat Pro Web App (running on laptop/PC)
              ↓
    Custom Vite Plugin (dev mode)
              ↓
    ADB Broadcast (USB or Wi-Fi)
              ↓
    Wear OS Data Layer
              ↓
    DataLayerService.kt (running on watch)
              ↓
    GameStateRepository (StateFlow)
              ↓
    Watch UI updates instantly
```

---

## The Data Path

### 1. KorfStat Pro Records an Event
When the jury official starts the clock, or a goal is recorded in the Match Tracker, or a substitution is logged — the match state in KorfStat Pro is updated.

### 2. The Custom Vite Plugin Broadcasts
Running alongside the KorfStat Pro development server, a **custom Vite plugin** monitors for match state changes. When a change is detected, it sends the updated state over **ADB** (Android Debug Bridge) as an Android broadcast message.

### 3. Wear OS Data Layer
The ADB broadcast writes data to the **Wear OS Data Layer** (`/korfball_game_state`), which is a low-latency data sharing mechanism built into Android for phone-to-watch communication.

### 4. `DataLayerService` Receives the Update
The watch app has a `WearableListenerService` running in the background 24/7 while the watch is connected. When new data arrives on the `/korfball_game_state` path, this service parses the fields and passes them to the `GameStateRepository`.

### 5. UI Updates
Because the `GameStateRepository` uses `StateFlow`, all parts of the UI that observe it automatically re-render with the new data. The dashboard updates within milliseconds.

---

## Data Fields Transmitted

The following fields are sent with every game state update:

| Field | Type | Description |
|---|---|---|
| `homeScore` | Int | Home team's current score |
| `awayScore` | Int | Away team's current score |
| `gameTime` | Long | Remaining game time in milliseconds |
| `shotClock` | Long | Remaining shot clock time in milliseconds |
| `isGameTimeRunning` | Boolean | Is the game clock currently running? |
| `isShotClockRunning` | Boolean | Is the shot clock currently running? |
| `period` | Int | Current period (1 = first half, 2 = second half) |
| `subPending` | Boolean | Is a substitution currently pending? |
| `latestSubId` | String | Unique ID to detect new vs duplicate sub events |
| `subOut` | String | Name/number of player coming off |
| `subIn` | String | Name/number of player coming on |
| `isReadOnly` | Boolean | Watch control mode (Read-Only vs Write) |
| `timeoutTeam` | String | Name of team that requested a timeout, or empty |

---

## Latency

The data path via ADB is extremely fast. In practice, updates appear on the watch within **less than 1 second** of being recorded in KorfStat Pro.

---

## Future: Direct Wi-Fi Connection

In the current implementation, ADB is used as the transport layer — which requires the watch or emulator to be connected via ADB to the same computer running KorfStat Pro. This is reliable in development and controlled environments.

A future version may use the **Wearable DataClient API** over Bluetooth, or direct **Wi-Fi socket communication**, to remove the dependency on ADB and make deployment at a match venue more practical.
