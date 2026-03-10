package com.wmeetsma.korfballrefwatch.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wmeetsma.korfballrefwatch.models.GameState
import com.wmeetsma.korfballrefwatch.network.SocketManager
import com.wmeetsma.korfballrefwatch.repository.GameStateRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val gameState: StateFlow<GameState> = GameStateRepository.gameState
    val isReadOnlyMode: StateFlow<Boolean> = GameStateRepository.isReadOnlyMode

    init {
        startTimerLoops()
    }
    
    private fun startTimerLoops() {
        viewModelScope.launch {
            while (isActive) {
                delay(100)
                val current = gameState.value
                
                var newGameTime = current.gameTimeRemainingMillis
                var newShotClock = current.shotClockRemainingMillis
                
                if (current.isGameTimeRunning && newGameTime > 0) {
                    newGameTime -= 100
                    if (newGameTime < 0) newGameTime = 0
                }
                
                if (current.isShotClockRunning && newShotClock > 0 && current.isGameTimeRunning) {
                    newShotClock -= 100
                    if (newShotClock < 0) newShotClock = 0
                }
                
                if (newGameTime != current.gameTimeRemainingMillis || newShotClock != current.shotClockRemainingMillis) {
                    GameStateRepository.updateGameState {
                        it.copy(
                            gameTimeRemainingMillis = newGameTime,
                            shotClockRemainingMillis = newShotClock
                        )
                    }
                }
            }
        }
    }

    fun updateGameState(newState: GameState) {
        GameStateRepository.updateGameState { newState }
    }
    
    fun toggleControlMode() {
        GameStateRepository.toggleControlMode()
    }

    fun resetShotClock() {
        if (!isReadOnlyMode.value) {
            GameStateRepository.updateGameState { it.copy(shotClockRemainingMillis = 25000L) }
            // Emit action back to web app via Socket.IO when in write mode
            SocketManager.emitAction("RESET_SHOT_CLOCK")
        }
    }
    
    fun toggleGameTime() {
        if (!isReadOnlyMode.value) {
            GameStateRepository.updateGameState { 
                it.copy(
                    isGameTimeRunning = !it.isGameTimeRunning,
                    isShotClockRunning = !it.isGameTimeRunning
                ) 
            }
            // Emit action back to web app via Socket.IO when in write mode
            SocketManager.emitAction("TOGGLE_GAME_TIME")
        }
    }
    
    fun dismissPopups() {
        GameStateRepository.updateGameState {
            it.copy(substitutionPending = false, timeoutRequestedTeam = null, showSubPopup = false)
        }
    }
    
    fun updateFromMap(data: Map<String, Any>) {
        GameStateRepository.updateFromMap(data)
    }
}
