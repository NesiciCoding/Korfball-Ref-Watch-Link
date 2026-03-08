# Requirements & Installation

This page explains what you need and how to install the Korfball Ref Watch app on your Wear OS device.

---

## Requirements

### Hardware
- A **Wear OS smartwatch** (any modern Wear OS 3+ device is recommended).
- Alternatively, the **Wear OS Emulator** in Android Studio can be used for development and testing.

### Software
- **Android Studio** — to build and install the app. Download from [developer.android.com/studio](https://developer.android.com/studio).
- **Java Development Kit (JDK)** — included with Android Studio.
- The **Korfball Ref Watch** source code (this repository).

### KorfStat Pro
The watch app is designed to work alongside **KorfStat Pro**. You need KorfStat Pro running on a computer on the same network (or connected via ADB) to receive live match data.

---

## Setting Up the Project

1. **Clone or download** this repository to your computer.
2. **Open Android Studio**.
3. Click **"Open"** and select the root folder of this repository.
4. Android Studio will automatically download the required Gradle dependencies. Wait for the "Gradle build" to finish.

---

## Installing on a Physical Watch

1. Enable **Developer Options** on your Wear OS watch:
   - Go to **Settings → About → Build Number** and tap it 7 times.
2. Enable **ADB Debugging** and **Wi-Fi Debugging** in the Developer Options.
3. In Android Studio, pair your watch (either via cable or Wi-Fi ADB).
4. With the watch selected as the target device, press **Run ▶** in Android Studio.
5. The app will be installed and launched on the watch.

---

## Running on the Emulator (For Developers)

1. In Android Studio, open **Device Manager** (View → Tool Windows → Device Manager).
2. Click **"Create Device"** and choose a **Wear OS** device profile (e.g. Wear OS Square or Wear OS Round).
3. Select a system image and complete the emulator setup.
4. Start the emulator and press **Run ▶** to install the app.

---

## Connecting to KorfStat Pro

See the [Connecting to KorfStat Pro](Connecting-to-KorfStat-Pro) page for the next steps.
