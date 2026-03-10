package com.wmeetsma.korfballrefwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.MaterialTheme
import com.wmeetsma.korfballrefwatch.network.SocketManager
import com.wmeetsma.korfballrefwatch.ui.DashboardScreen
import com.wmeetsma.korfballrefwatch.ui.SetupScreen
import com.wmeetsma.korfballrefwatch.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by lazy {
        androidx.lifecycle.ViewModelProvider(this)[MainViewModel::class.java]
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("MockSync", "Received broadcast: ${intent?.action}")
            if (intent?.action == "com.korfstat.UPDATE_STATE") {
                val data = mutableMapOf<String, Any>()
                
                Log.d("MockSync", "Extras: ${intent.extras}")
                
                if (intent.hasExtra("homeScore")) data["homeScore"] = intent.getIntExtra("homeScore", 0)
                if (intent.hasExtra("awayScore")) data["awayScore"] = intent.getIntExtra("awayScore", 0)
                if (intent.hasExtra("isGameTimeRunning")) data["isGameTimeRunning"] = intent.getBooleanExtra("isGameTimeRunning", false)
                if (intent.hasExtra("isShotClockRunning")) data["isShotClockRunning"] = intent.getBooleanExtra("isShotClockRunning", false)
                if (intent.hasExtra("gameTime")) data["gameTime"] = intent.getLongExtra("gameTime", 0L)
                if (intent.hasExtra("shotClock")) data["shotClock"] = intent.getLongExtra("shotClock", 0L)
                if (intent.hasExtra("period")) data["period"] = intent.getIntExtra("period", 1)
                if (intent.hasExtra("subPending")) data["subPending"] = intent.getBooleanExtra("subPending", false)
                if (intent.hasExtra("latestSubId")) data["latestSubId"] = intent.getStringExtra("latestSubId") ?: ""
                if (intent.hasExtra("subOut")) data["subOut"] = intent.getStringExtra("subOut") ?: ""
                if (intent.hasExtra("subIn")) data["subIn"] = intent.getStringExtra("subIn") ?: ""
                if (intent.hasExtra("isReadOnly")) data["isReadOnly"] = intent.getBooleanExtra("isReadOnly", true)
                if (intent.hasExtra("timeoutTeam")) {
                    val tTeam = intent.getStringExtra("timeoutTeam") ?: ""
                    data["timeoutTeam"] = if (tTeam == "NONE") "" else tTeam
                }
                if (intent.hasExtra("hapticSignal")) data["hapticSignal"] = intent.getStringExtra("hapticSignal") ?: ""
                if (intent.hasExtra("hapticSignalId")) data["hapticSignalId"] = intent.getStringExtra("hapticSignalId") ?: ""

                Log.d("MockSync", "Parsed Data: $data")
                viewModel.updateFromMap(data)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Register local broadcast receiver for ADB dev testing
        val filter = IntentFilter("com.korfstat.UPDATE_STATE")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(receiver, filter)
        }

        setContent {
            WearApp(viewModel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        SocketManager.disconnect()
    }
}

@Composable
fun WearApp(viewModel: MainViewModel) {
    var screen by remember { mutableStateOf("setup") }
    
    MaterialTheme {
        when (screen) {
            "setup" -> SetupScreen(
                onConfigured = { config ->
                    if (config != null) {
                        SocketManager.connect(config.ip, config.port)
                    }
                    screen = "dashboard"
                }
            )
            "dashboard" -> DashboardScreen(viewModel)
        }
    }
}
