# Control Modes

The Korfball Ref Watch supports two distinct **control modes** that determine whether the referee can interact with the clocks from their wrist, or simply observe them.

---

## What Are Control Modes?

Think of it this way:
- In most matches, the **jury table** (scorer's table) is the official authority for the game clock.
- But sometimes, the referee may act as their own timekeeper.

Control modes let you choose which setup is right for your situation.

---

## Read-Only Mode 🔒

**"Jury controls time"**

In this mode:
- The watch **displays** the game clock, shot clock, and score.
- The referee **cannot** start, stop, or reset any clock from the watch.
- All timing remains under the control of the jury official using KorfStat Pro.

This is the **recommended default** for official matches where a dedicated jury/timekeeper is operating.

The watch shows a small grey **"READ-ONLY"** label at the top of the screen as a reminder.

---

## Write Mode ✏️

**"Referee controls time"**

In this mode:
- The referee has **full clock control** from their wrist.
- **Tap the Game Clock** → starts or stops the game timer.
- **Double-tap the Shot Clock** → resets it to 25 seconds.
- **Long press anywhere** → toggles between Write and Read-Only mode.

This is useful for informal matches, training sessions, or situations where there is no dedicated timekeeper.

> **Warning:** In Write Mode, actions on the watch take priority over the jury table. Make sure both the referee and the jury operator know which mode is active to avoid conflicts.

---

## Switching Between Modes

### From the Watch
While in any mode, **long press** anywhere on the main dashboard screen to toggle between Read-Only and Write Mode. The mode label will update immediately.

### From KorfStat Pro
In KorfStat Pro, go to **Settings** → **Match Defaults** → **Wear OS Control Mode** and select the mode. This sets the default mode that the watch will use from startup.

The watch receives the control mode as part of the data sync — the `isReadOnly` field is sent with every game state update.

---

## Which Mode Should I Use?

| Situation | Recommended Mode |
|---|---|
| Official match with a jury table | **Read-Only** |
| Training session, no jury | **Write** |
| Friendly match, referee self-timing | **Write** |
| Exhibition with production team | **Read-Only** |
