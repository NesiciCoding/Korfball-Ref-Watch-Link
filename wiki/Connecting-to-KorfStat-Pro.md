# Connecting to KorfStat Pro

The Korfball Ref Watch receives all its data from the **KorfStat Pro** web application. This page explains how to set up the connection.

---

## How the Connection Works

During development and typical usage, the connection is established via **ADB (Android Debug Bridge)** — a tool built into Android Studio that lets your computer communicate directly with a connected Android or Wear OS device (or emulator).

KorfStat Pro includes a special plugin that automatically **broadcasts match state updates** over ADB every time the game state changes.

```
KorfStat Pro (running on your PC)
         ↓
  Custom Vite Plugin
         ↓
  ADB Broadcast Bridge
         ↓
  Wear OS App (watch or emulator)
```

---

## Step-by-Step Setup

### 1. Start the Wear OS App

- Launch the **Wear OS Emulator** in Android Studio, or
- Connect your **physical Wear OS watch** via ADB.

Install and run the Korfball Ref Watch app (see [Requirements & Installation](Requirements-and-Installation)).

### 2. Start KorfStat Pro

On your computer, navigate to the KorfStat Pro folder and run:

```bash
npm run dev:multi
```

This starts both the frontend and the server. The custom Vite plugin included in KorfStat Pro will automatically begin broadcasting game state updates over ADB.

### 3. Start a Match

Go to the KorfStat Pro web interface (usually `http://localhost:3000`), set up a match, and start it. As soon as the match begins and events are recorded (goals, clock changes, substitutions, etc.), the watch will update in real time.

---

## Verifying the Connection

Once everything is running, the watch display should show:
- The current score (e.g. **0 – 0**)
- The game clock (counting down from the configured duration)
- The shot clock

If the watch shows zeros but does not update, double-check:
- That ADB is connected (`adb devices` in a terminal should list your watch/emulator).
- That KorfStat Pro is running (you should see "Local: http://localhost:3000" in the terminal).

---

## Future Connection Methods

Currently the connection relies on ADB, which is primarily a developer tool. Future versions of the Korfball Ref Watch may support direct Wi-Fi connection (using the Wearable Data Layer API over Bluetooth), making it easier to use in real match settings without needing a laptop running ADB.

Always check the repository for the latest connection options.
