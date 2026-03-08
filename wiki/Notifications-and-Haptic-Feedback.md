# Notifications & Haptic Feedback

The Korfball Ref Watch is designed to keep the referee informed **without them needing to look at their watch**. It achieves this through vibration (haptic feedback) patterns for key events.

---

## Haptic Feedback Events

### Shot Clock Expiry 🔴
When the shot clock counts down to **0 seconds**, the watch vibrates with a single, firm buzz:
- Pattern: **Long single vibration (500ms)**
- Purpose: Alert the referee that the shot clock has expired and a free pass should be awarded.

### Substitution Pending 🔄
When a substitution is logged in KorfStat Pro, the watch vibrates with a distinctive double pattern:
- Pattern: **Buzz — pause — Buzz** (200ms on, 100ms off, 200ms on)
- Purpose: Get the referee's attention so they can acknowledge the substitution.

### Timeout Requested ⏸️
When a team calls a timeout, the watch vibrates with the same double pattern as substitutions:
- Pattern: **Buzz — pause — Buzz** (200ms on, 100ms off, 200ms on)
- Purpose: Alert the referee immediately when a team signals for a timeout.

---

## On-Screen Notifications (Popup Dialogs)

In addition to vibration, certain events trigger **full-screen popup dialogs** on the watch that require the referee to dismiss them.

### Substitution Popup
When a substitution is pending, a full-screen dialog appears:
- **Header:** "SUBSTITUTION" (in cyan)
- **OUT:** The departing player's name and jersey number (in red)
- **IN:** The incoming player's name and jersey number (in green)
- **Dismiss button** to confirm and close the popup.

### Timeout Popup
When a team requests a timeout, a full-screen dialog appears:
- **Team name** and **"TIMEOUT"** in large yellow text.
- **Dismiss button** to close the popup.

---

## Why Both Vibration AND Visual?

The combination ensures the referee is always informed:
- **Vibration** catches attention immediately, even when the referee's arm is down and the watch face is not visible.
- **Visual popup** provides full context (who, what kind of event) so the referee has all the information they need without consulting anyone else.

---

## Device Compatibility Note

The haptic feedback uses the Android `VibrationEffect` API. This is supported on all modern Wear OS devices (Wear OS 2+). The implementation automatically selects the correct API based on the Android SDK version, ensuring compatibility with both older and newer watches.
