# Korfball Referee Watch Link

A Wear OS smart watch application built natively with Kotlin and Jetpack Compose, designed to act as a real-time digital assistant for referees. It is closely integrated with the **KorfStat Pro** system, acting as a real-time synchronized extension of the official jury table.

## Features

- **Real-Time Game Timers:** Accurately syncs and tracks the overarching game time (e.g., 25 minutes) alongside a precision 25-second shot clock.
- **Score Tracking:** Displays dynamic scoring between the Home and Away teams, updating instantly.
- **Interactive Touch Controls:** 
  - Tap the center of the screen to smoothly pause or play the game time.
  - Quick, intuitive controls to reset the shot clock.
- **Dedicated Notification Popups:** 
  - **Substitutions:** Full-screen alerts showcasing who is subbing out and who is subbing in, complete with player numbers and names.
  - **Timeouts:** Full-screen overlay alerting the referee when a specific team requests a time-out.
- **Control Modes:**
  - **Write Mode:** Grants the referee full control over the timers on their wrist.
  - **Read-Only Mode:** Locks the watch controls, meaning the referee is only viewing actions strictly dictated by the official jury table.
- **Haptic Feedback:** Important match events trigger distinct vibration patterns on the wrist, keeping the referee informed without needing to look at the screen (e.g. shot clock expiring, timeouts).

## Architecture & Technology Stack

- **Platform:** Wear OS
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose for Wear OS
- **Architecture:** MVVM (Model-View-ViewModel) with `StateFlow`
- **Synchronization:** The app utilizes intent-based Data Mapping and currently syncs via an ADB Broadcast Bridge connected to the KorfStat Pro local development server.

## Local Development & Sync Setup

During development, the KorfStat Pro React web application (running locally) uses a custom Vite plugin to broadcast updates over ADB directly to the running Wear OS emulator.

1. Launch your **Wear OS Emulator** via Android Studio.
2. Install and run this application on the emulator.
3. In the KorfStat Pro repository, start the frontend and socket server:
   ```bash
   npm run dev:multi
   ```
4. Adjust the match states, goals, substitutes, timeouts, and watch Control Modes from the web app Command Center, and they will be beamed instantly to the wrist!

## License

This project is intended for personal and team use within KorfStat workflows. All rights reserved.
