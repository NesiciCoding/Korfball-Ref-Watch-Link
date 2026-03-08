package com.wmeetsma.korfballrefwatch.repository

import com.wmeetsma.korfballrefwatch.models.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Singleton repository to share GameState across the entire Wear app.
 * This ensures that updates received from DataLayerService (background)
 * are instantly reflected in the MainViewModel (foreground UI).
 */
object GameStateRepository {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    // Control mode setting
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
     * the real Wearable Data Layer API.
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
            val newTimeoutTeam = if (timeoutTeamData == "" || timeoutTeamData == "NONE") null else (timeoutTeamData ?: current.timeoutRequestedTeam)
            
            val newSubId = data["latestSubId"] as? String ?: current.latestSubEventId
            val showPopup = if (newSubId != current.latestSubEventId && newSubId != null && newSubId != "") true else current.showSubPopup

            current.copy(
                homeScore = (data["homeScore"] as? Int) ?: current.homeScore,
                awayScore = (data["awayScore"] as? Int) ?: current.awayScore,
                isGameTimeRunning = (data["isGameTimeRunning"] as? Boolean) ?: current.isGameTimeRunning,
                isShotClockRunning = (data["isShotClockRunning"] as? Boolean) ?: current.isShotClockRunning,
                gameTimeRemainingMillis = (data["gameTime"] as? Long) ?: current.gameTimeRemainingMillis,
                shotClockRemainingMillis = (data["shotClock"] as? Long) ?: current.shotClockRemainingMillis,
                currentPeriod = (data["period"] as? Int) ?: current.currentPeriod,
                substitutionPending = (data["subPending"] as? Boolean) ?: current.substitutionPending,
                timeoutRequestedTeam = newTimeoutTeam,
                latestSubEventId = newSubId,
                subOutInfo = data["subOut"] as? String ?: current.subOutInfo,
                subInInfo = data["subIn"] as? String ?: current.subInInfo,
                showSubPopup = showPopup
            )
        }
    }
}
