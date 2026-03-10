package com.wmeetsma.korfballrefwatch.repository

import com.wmeetsma.korfballrefwatch.models.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object GameStateRepository {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _isReadOnlyMode = MutableStateFlow(true)
    val isReadOnlyMode: StateFlow<Boolean> = _isReadOnlyMode.asStateFlow()

    fun updateGameState(updater: (GameState) -> GameState) {
        _gameState.update(updater)
    }

    fun toggleControlMode() {
        _isReadOnlyMode.update { !it }
    }

    /**
     * Helper to parse data maps from either ADB Broadcast intents or
     * Socket.IO JSON payloads. JSON values come as Int/Double/Boolean, not Long,
     * so we coerce Number types explicitly to avoid silent cast failures.
     */
    fun updateFromMap(data: Map<String, Any>) {
        if (data.containsKey("isReadOnly")) {
            val isReadOnlyData = data["isReadOnly"] as? Boolean
            if (isReadOnlyData != null) {
                _isReadOnlyMode.value = isReadOnlyData
            }
        }

        _gameState.update { current ->
            val timeoutTeamData = data["timeoutTeam"] as? String
            val newTimeoutTeam = if (timeoutTeamData == "" || timeoutTeamData == "NONE") null
                                 else (timeoutTeamData ?: current.timeoutRequestedTeam)

            val newSubId = data["latestSubId"] as? String ?: current.latestSubEventId
            val showPopup = if (newSubId != current.latestSubEventId && newSubId != null && newSubId != "")
                true else current.showSubPopup

            // JSON numbers from Socket.IO arrive as Int or Double, not Long.
            // Coerce via Number.toLong() to handle both ADB (Long) and Socket.IO (Int/Double).
            fun toLongSafe(key: String, default: Long): Long {
                return (data[key] as? Number)?.toLong() ?: default
            }

            current.copy(
                homeScore          = (data["homeScore"] as? Number)?.toInt() ?: current.homeScore,
                awayScore          = (data["awayScore"] as? Number)?.toInt() ?: current.awayScore,
                isGameTimeRunning  = (data["isGameTimeRunning"] as? Boolean) ?: current.isGameTimeRunning,
                isShotClockRunning = (data["isShotClockRunning"] as? Boolean) ?: current.isShotClockRunning,
                gameTimeRemainingMillis  = toLongSafe("gameTime", current.gameTimeRemainingMillis),
                shotClockRemainingMillis = toLongSafe("shotClock", current.shotClockRemainingMillis),
                currentPeriod      = (data["period"] as? Number)?.toInt() ?: current.currentPeriod,
                substitutionPending = (data["subPending"] as? Boolean) ?: current.substitutionPending,
                timeoutRequestedTeam = newTimeoutTeam,
                latestSubEventId   = newSubId,
                subOutInfo         = data["subOut"] as? String ?: current.subOutInfo,
                subInInfo          = data["subIn"] as? String ?: current.subInInfo,
                showSubPopup       = showPopup,
                hapticSignal       = data["hapticSignal"] as? String,
                hapticSignalId     = data["hapticSignalId"] as? String
            )
        }
    }
}
