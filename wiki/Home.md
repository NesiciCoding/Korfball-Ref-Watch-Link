# Welcome to the Korfball Ref Watch Wiki ⌚

**Korfball Ref Watch** is a **Wear OS smartwatch application** built for Korfball referees. It works as a real-time digital assistant on your wrist, keeping you informed of the game clock, shot clock, score, and important events — all without needing to look at a scoreboard or consult the jury table.

The watch integrates directly with the **KorfStat Pro** system, receiving live updates automatically over Wi-Fi.

---

## 📚 Table of Contents

### Getting Started
- [Requirements & Installation](Requirements-and-Installation)
- [Connecting to KorfStat Pro](Connecting-to-KorfStat-Pro)

### Using the Watch
- [The Dashboard Screen](Dashboard-Screen)
- [Control Modes (Read-Only vs Write)](Control-Modes)
- [Notifications & Haptic Feedback](Notifications-and-Haptic-Feedback)

### Technical Reference
- [Architecture & Technology](Architecture-and-Technology)
- [Data Synchronization](Data-Synchronization)

---

## 🎯 What Does the Watch Do?

At a glance, the Korfball Ref Watch provides the referee with:

| Feature | Details |
|---|---|
| ⏱️ Game Clock | Countdown from the configured half duration |
| ⏱️ Shot Clock | 25-second countdown, turns red under 5 seconds |
| 🏆 Score | Live Home – Away score |
| 📋 Period | Shows "Period 1" or "Period 2" |
| 🔔 Substitution Alert | Full-screen popup with player numbers and names |
| 🔔 Timeout Alert | Full-screen popup showing which team called the timeout |
| 📳 Haptic Feedback | Vibration patterns for key events — no screen needed |

---

## 🔗 Ecosystem

The watch app is part of the full **KorfStat Pro ecosystem**:

```
KorfStat Pro (Web App)
        ↓ (Sync over network / ADB)
Korfball Ref Watch (Wear OS)
```

Everything displayed on the watch is driven by events recorded in KorfStat Pro.

---

*Built with ❤️ for the Korfball community.*
