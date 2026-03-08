# The Dashboard Screen

The Dashboard Screen is the **main display** of the Korfball Ref Watch — it is what the referee sees on their wrist during the match.

---

## Layout Overview

The watch screen is divided into clear zones:

```
┌─────────────────────────┐
│      12:34 (WatchOS)    │  ← System time (top edge)
│       [ READ-ONLY ]     │  ← Mode indicator (if Read-Only)
│        Period 1         │  ← Current period
│       3  —  2           │  ← Live score (Home – Away)
│         18:42           │  ← Game clock (big, color-coded)
│           12            │  ← Shot clock
│  SUBSTITUTION PENDING   │  ← Event alert (if active)
└─────────────────────────┘
```

---

## Elements Explained

### System Time
The standard Wear OS time display at the top edge of the screen — always visible so the referee always knows the real time.

### Mode Indicator
If the watch is in **Read-Only** mode (see [Control Modes](Control-Modes)), the label `READ-ONLY` appears in grey at the top. This reminds the referee they cannot control the clocks from their wrist.

### Current Period
Shows `Period 1` or `Period 2` (or whatever period number the match is in). This helps the referee quickly confirm which half they are in.

### Live Score
The Home and Away team scores are shown in large, bold white text in the center:
```
3  —  2
```

The score updates instantly whenever a goal is recorded in KorfStat Pro.

### Game Clock
The game clock is shown as a large countdown timer (`MM:SS` format):
- **Green** when the clock is **running**.
- **Red** when the clock is **stopped** (paused).

In Write Mode, tapping the game clock will toggle it on/off.

### Shot Clock
The 25-second shot clock is shown below the game clock as a single large number:
- **Cyan/White** when there is plenty of time.
- **Red** when **5 seconds or fewer** remain.

In Write Mode, double-tapping the shot clock will reset it to 25 seconds.

### Substitution Pending
If a substitution has been logged in KorfStat Pro but not yet dismissed on the watch, the label **"SUBSTITUTION PENDING"** appears in yellow at the bottom of the main screen.

---

## Popup Dialogs

For substitutions and timeouts, the watch displays a **full-screen popup** that blocks the normal dashboard. This ensures the referee cannot miss these important events.

### Substitution Popup
```
┌──────────────────────────┐
│       SUBSTITUTION       │
│                          │
│  OUT: #7 van der Berg    │
│  IN:  #11 de Vries       │
│                          │
│       [  Dismiss  ]      │
└──────────────────────────┘
```
Shows the player going **out** (in red) and the player coming **in** (in green), with jersey numbers and names.

### Timeout Popup
```
┌──────────────────────────┐
│         Home Team        │
│          TIMEOUT         │
│                          │
│       [  Dismiss  ]      │
└──────────────────────────┘
```
Shows which team requested the timeout in large, yellow text.

---

## Dismissing Popups

Press the **"Dismiss"** button on the popup to close it and return to the regular dashboard view. This does not affect the match state in any way — it is only a local acknowledgment on the watch.
