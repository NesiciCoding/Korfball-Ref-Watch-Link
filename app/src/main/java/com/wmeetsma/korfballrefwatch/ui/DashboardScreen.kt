package com.wmeetsma.korfballrefwatch.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.dialog.Dialog
import com.wmeetsma.korfballrefwatch.network.SocketManager
import com.wmeetsma.korfballrefwatch.viewmodels.MainViewModel

@Composable
fun DashboardScreen(viewModel: MainViewModel) {
    val state by viewModel.gameState.collectAsState()

    val isReadOnly by viewModel.isReadOnlyMode.collectAsState()
    
    val context = LocalContext.current

    LaunchedEffect(state.shotClockRemainingMillis) {
        if (state.shotClockRemainingMillis == 0L && state.isShotClockRunning) {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
    
    LaunchedEffect(state.substitutionPending, state.timeoutRequestedTeam) {
        if (state.substitutionPending || state.timeoutRequestedTeam != null) {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
            val pattern = longArrayOf(0, 200, 100, 200) // wait, vibrate, wait, vibrate
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
        }
    }

    LaunchedEffect(state.hapticSignalId) {
        if (state.hapticSignalId != null && state.hapticSignal != null) {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
            
            val effect = when (state.hapticSignal) {
                "TIMEOUT_PING" -> VibrationEffect.createWaveform(longArrayOf(0, 150, 100, 150, 100, 150), -1) // 3 quick
                "SUB_PING" -> VibrationEffect.createOneShot(800, VibrationEffect.DEFAULT_AMPLITUDE) // 1 long
                "SHOT_CLOCK_PING" -> VibrationEffect.createWaveform(longArrayOf(0, 300, 150, 300), -1) // 2 medium
                "GAME_CLOCK_PING" -> VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE) // 1 short tap
                else -> null
            }
            
            effect?.let { vibrator.vibrate(it) }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { viewModel.toggleControlMode() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        TimeText() // Default Wear OS time at top edge
        
        // Wi-Fi Connection Status Dot (top-left)
        val connState by SocketManager.connectionState.collectAsState()
        val dotColor = when (connState) {
            SocketManager.ConnectionState.Connected    -> Color(0xFF4CAF50) // green
            SocketManager.ConnectionState.Connecting  -> Color(0xFFFFC107) // amber
            SocketManager.ConnectionState.Error       -> Color(0xFFF44336) // red
            SocketManager.ConnectionState.Disconnected -> Color.Transparent
        }
        val dotLabel = when (connState) {
            SocketManager.ConnectionState.Connected    -> "WiFi"
            SocketManager.ConnectionState.Connecting  -> "..."
            SocketManager.ConnectionState.Error       -> "ERR"
            SocketManager.ConnectionState.Disconnected -> ""
        }
        if (connState != SocketManager.ConnectionState.Disconnected) {
            Row(
                modifier = Modifier.align(Alignment.TopStart).padding(start = 8.dp, top = 28.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(dotColor, shape = androidx.compose.foundation.shape.CircleShape)
                )
                Text(dotLabel, color = dotColor, fontSize = 8.sp)
            }
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Read-Only Indicator
            if (isReadOnly) {
                Text(
                    text = "READ-ONLY",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Header: Period
            Text(
                text = "Period ${state.currentPeriod}",
                color = MaterialTheme.colors.secondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(4.dp))
            
            // Scores (Home - Away)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${state.homeScore}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = " - ",
                    fontSize = 24.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${state.awayScore}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Game Timer
            val minutes = (state.gameTimeRemainingMillis / 1000) / 60
            val seconds = (state.gameTimeRemainingMillis / 1000) % 60
            val timeString = String.format("%02d:%02d", minutes, seconds)
            
            Text(
                text = timeString,
                fontSize = 32.sp,
                color = if (state.isGameTimeRunning) Color.Green else Color.Red,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { viewModel.toggleGameTime() }
                    )
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Shot Clock Timer
            val shotClockSeconds = state.shotClockRemainingMillis / 1000
            Text(
                text = "$shotClockSeconds",
                fontSize = 28.sp,
                color = if (shotClockSeconds <= 5) Color.Red else Color.Cyan,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { viewModel.resetShotClock() }
                    )
                }
            )
            
            // Footer: Indicator
            if (state.substitutionPending && state.timeoutRequestedTeam == null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "SUBSTITUTION PENDING",
                    color = Color.Yellow,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        // Notification Dialog
        val showDialog = state.timeoutRequestedTeam != null || state.showSubPopup
        Dialog(
            showDialog = showDialog,
            onDismissRequest = { viewModel.dismissPopups() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (state.timeoutRequestedTeam != null) {
                    Text(
                        text = "${state.timeoutRequestedTeam}\nTIMEOUT",
                        color = Color.Yellow,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                } else if (state.showSubPopup) {
                    Text(
                        text = "SUBSTITUTION",
                        color = Color.Cyan,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "OUT: ${state.subOutInfo ?: "?"}", color = Color.Red, fontSize = 14.sp)
                    Text(text = "IN: ${state.subInInfo ?: "?"}", color = Color.Green, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = { viewModel.dismissPopups() }) {
                    Text("Dismiss", fontSize = 12.sp)
                }
            }
        }
    }
}
