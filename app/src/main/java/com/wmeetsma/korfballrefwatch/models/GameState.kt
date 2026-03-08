package com.wmeetsma.korfballrefwatch.models

data class GameState(
    val homeScore: Int = 0,
    val awayScore: Int = 0,
    val gameTimeRemainingMillis: Long = 1500000L, // 25 mins default
    val isGameTimeRunning: Boolean = false,
    val shotClockRemainingMillis: Long = 25000L, // 25 seconds default
    val isShotClockRunning: Boolean = false,
    val currentPeriod: Int = 1, // 1st half, 2nd half, etc.
    val homeTimeoutsRemaining: Int = 2,
    val awayTimeoutsRemaining: Int = 2,
    val activeTimeoutMillis: Long = 0L,
    val timeoutRequestedTeam: String? = null, // "Home" or "Away"
    val substitutionPending: Boolean = false,
    val latestSubEventId: String? = null,
    val subOutInfo: String? = null,
    val subInInfo: String? = null,
    val showSubPopup: Boolean = false
)
